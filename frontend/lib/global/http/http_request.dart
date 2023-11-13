import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

Future<http.Response> sendHttpRequest(String method, Uri uri, {String? body, Map<String, String>? headers}) async {
  SharedPreferences prefs = await SharedPreferences.getInstance();
  final accessToken = prefs.getString('accessToken');
  headers = headers ?? {};
  headers.addAll({
    "Content-Type": "application/json",
    'Authorization': 'Bearer $accessToken',
  });

  http.Response response;
  switch (method) {
    case 'GET':
      response = await http.get(uri, headers: headers);
      break;
    case 'POST':
      response = await http.post(uri, headers: headers, body: body);
      break;
    case 'DELETE':
      response = await http.delete(uri, headers: headers);
      break;
    case 'PATCH':
      response = await http.patch(uri, headers: headers, body: body);
      break;
    default:
      throw Exception('Unsupported HTTP method: $method');
  }

  if (response.statusCode == 205) {  // 임시 statusCode.
    final refreshToken = prefs.getString('refreshToken');
    final tokenResponse = await http.post(
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/auth/refreshToken'),
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer $accessToken',
        'Refresh-Token': '$refreshToken',
      },
    );

    if (tokenResponse.statusCode == 200) {
      String newAccessToken = response.headers['newAccessToken'] ?? '';  // 임시 key.
      await prefs.setString("accessToken", newAccessToken);

      // 재귀 호출하여 새로운 토큰으로 다시 HTTP 요청을 보냅니다.
      return sendHttpRequest(method, uri, body: body, headers: headers);

    } else {
      throw Exception('Failed to get newAccessToken');
    }
  } else if (response.statusCode != 200) { // 추가적인 예외 처리 필요할 수 도
    throw Exception('HTTP request failed with status code: ${response.statusCode}');
  }

  return response;
}
