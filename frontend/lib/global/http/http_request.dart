import 'dart:convert';

import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import '../../presentation/routes/route_path.dart';
import '../../presentation/routes/routes.dart';

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

  if (response.statusCode == 200) { // 응답의 상태 코드가 200인 경우
    return response; // 응답을 그대로 반환
  } else if (response.statusCode == 401) { // 응답의 상태 코드가 401인 경우
    if (jsonDecode(response.body)['code'] == 411) { // 응답 바디의 errorCode가 401인 경우
      final refreshToken = prefs.getString('refreshToken');
      final tokenResponse = await http.post(
        // Uri.http(dotenv.env['API_URL']!, '/api/auth/refreshToken'),
        Uri.http(dotenv.env['AWS_API_URL']!, '/api/auth/refreshToken'),
        headers: {
          "Content-Type": "application/json",
          'Authorization': 'Bearer $accessToken',
          'Refresh-Token': 'Bearer $refreshToken'
        },
      );

      if (tokenResponse.statusCode == 200) { // 새로운 토큰 요청의 응답 상태 코드가 200인 경우
        var data = jsonDecode(tokenResponse.body);
        String newAccessToken = data['accessToken'];
        await prefs.setString('accessToken', newAccessToken);
        print('재발급 완료');

        // 재귀 호출하여 새로운 토큰으로 다시 HTTP 요청을 보냅니다.
        return sendHttpRequest(method, uri, body: body, headers: headers);
      } else if (jsonDecode(tokenResponse.body)['code'] == 412) { // 응답 바디의 errorCode가 402인 경우
        Set<String> keys = prefs.getKeys();

        for (String key in keys) {
          await prefs.remove(key);
        }

        router.go(RoutePath.loginPage);

        print('리프레시 토큰 만료');

        // 예외 발생
        throw Exception('토큰이 만료되었습니다. 다시 로그인 해주세요');

      } else {
        throw Exception('알 수 없는 에러');
      }

    } else {
      throw Exception('HTTP request failed with status code: ${response.statusCode}');
    }
  } else { // 그 외의 상태 코드인 경우
    throw Exception('HTTP request failed with status code: ${response.statusCode}');
  }
}
