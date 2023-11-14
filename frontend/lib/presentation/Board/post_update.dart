import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/global/messages/board/post_message.dart';
import 'package:orury/presentation/Board/post_detail.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:orury/presentation/Board/post.dart';

import '../routes/route_path.dart';
import '../routes/routes.dart';

class PostUpdate extends StatefulWidget {

  final Post? data;

  PostUpdate({Key? key, this.data}) : super(key: key);

  @override
  State<PostUpdate> createState() => _PostUpdateState();
}

class _PostUpdateState extends State<PostUpdate> {
  final _formKey = GlobalKey<FormState>();
  final String? imgurClientId = dotenv.env['IMGUR_CLIENT_ID'];
  final picker = ImagePicker();

  TextEditingController titleController = TextEditingController();
  TextEditingController contentController = TextEditingController();

  bool isObscure = true;

  // 게시글 수정
  void update() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final accessToken = prefs.getString('accessToken');
    final refreshToken = prefs.getString('refreshToken');

    final response = await http.patch(
      Uri.http(dotenv.env['API_URL']!, '/api/post'),
      // Uri.parse(url),
      headers: <String, String>{
        "Content-Type": "application/json",
        'Authorization': 'Bearer $accessToken',
      },
      body: jsonEncode({
        'id' : widget.data?.id,
        'post_title': titleController.text,
        'user_nickname': widget.data?.userNickname,
        'post_content': contentController.text,
      }),
    );

    // 작성 성공
    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text(PostMessage.postSuccess),
        ),
      );
      titleController.clear();
      contentController.clear();
      // router.pop();
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => PostDetail(widget.data!.id)),
      );
      // router.go(RoutePath.main);
    } else {
      // HTTP 요청이 실패했다면,
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text(PostMessage.postFail),
        ),
      );
    }
  }

  // 컨트롤러 초기값
  @override
  void initState() {
    super.initState();
    titleController = TextEditingController(text: widget.data?.postTitle);
    contentController = TextEditingController(text: widget.data?.postContent);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('게시글 수정'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: <Widget>[
            Container(
              height: 200, // Adjust this as needed
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: widget.data!.imageList!.length,
                itemBuilder: (context, i) {
                  return Padding(
                    padding: const EdgeInsets.only(right: 8.0),
                    child: Image.network(widget.data!.imageList[i]),
                  );
                },
              ),
            ),
            SizedBox(height: 10),
            TextField(
              decoration: InputDecoration(
                labelText: '제목',
                border: OutlineInputBorder(),
                focusedBorder: OutlineInputBorder(
                  borderSide: BorderSide(color: Colors.deepPurple),
                ),
              ),
              controller: titleController,
            ),
            SizedBox(height: 10),
            TextField(
              decoration: InputDecoration(
                labelText: '본문',
                border: OutlineInputBorder(),
                focusedBorder: OutlineInputBorder(
                  borderSide: BorderSide(color: Colors.deepPurple),
                ),
              ),
              maxLines: 5,
              controller: contentController,
            ),
            SizedBox(height: 10),
            ElevatedButton(
              onPressed: update,
              child: const Text('수정하기'),
            ),
          ],
        ),
      ),
    );
  }
}