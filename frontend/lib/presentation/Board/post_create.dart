import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../routes/routes.dart';

class PostCreate extends StatefulWidget {
  const PostCreate({super.key});

  @override
  State<PostCreate> createState() => _PostCreateState();
}

class _PostCreateState extends State<PostCreate> {
  final _formKey = GlobalKey<FormState>();
  List<File> _images = [];
  final picker = ImagePicker();

  TextEditingController titleController = TextEditingController();
  TextEditingController contentController = TextEditingController();

  bool isObscure = true;

  Future getImage() async {
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

    setState(() {
      if (pickedFile != null && _images.length < 10) {
        _images.add(File(pickedFile.path));
      }
    });
  }

  Future getUrl() async {} // API 결과를 가져오기 위한 함수 작성 필요해 보임

  void post() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('jwtToken');

    final response = await http.post(
      Uri.http(dotenv.env['API_URL']!, '/api/post'),
      // Uri.parse(url),
      headers: <String, String>{
        "Content-Type": "application/json",
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({
        // 'user_id' : 1,   // 코드 수정 필요
        'board_id' : 1, // 코드 수정 필요
        'post_title': titleController.text,
        'user_nickname' : 'test1', // 코드 수정 필요
        'post_content': contentController.text,
        'post_image_list' : ["https://i.imgur.com/RD1Qemr.jpg", "https://i.imgur.com/3lPyhi2.jpg", "https://i.imgur.com/ggn3EMw.jpg"]
        // 코드 수정 필요
      }),
    );
    // 정상 회원가입 시 로그인 처리.
    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('작성 성공!'),
        ),
      );
      titleController.clear();
      contentController.clear();
      // _images는 안비워줘도 되나?
      router.pop();
    } else {
      // HTTP 요청이 실패했다면,
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('게시글 작성에 실패하였습니다.'),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Image Picker Demo'),
      ),
      body: Column(
        children: <Widget>[
          TextField(
            decoration: InputDecoration(
              labelText: '제목',
            ),
            controller: titleController,
          ),
          TextField(
            decoration: InputDecoration(
              labelText: '본문',
            ),
            maxLines: 5,
            controller: contentController,
          ),
          Wrap(
            spacing: 8.0, // gap between adjacent chips
            runSpacing: 4.0, // gap between lines
            children: _images
                .map((image) => Image.file(
              image,
              width: 100,
              height: 100,
              fit: BoxFit.cover,
            ))
                .toList(),
          ),
          ElevatedButton(
            onPressed: getImage,
            child: Text('이미지 선택'),
          ),
          FilledButton(
            onPressed: post,
            style: const ButtonStyle().copyWith(
              backgroundColor: MaterialStateProperty.all(
                _formKey.currentState?.validate() ?? false
                    ? null
                    : Colors.grey.shade300,
              ),
            ),
            child: const Text('등록하기'),
          ),
        ],
      ),
    );
  }
}