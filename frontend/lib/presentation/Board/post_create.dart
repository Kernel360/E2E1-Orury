import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/global/messages/board/post_message.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:orury/presentation/Board/post.dart';

import '../../global/http/http_request.dart';
import '../routes/routes.dart';

class PostCreate extends StatefulWidget {

  const PostCreate({super.key});


  @override
  State<PostCreate> createState() => _PostCreateState();
}

class _PostCreateState extends State<PostCreate> {
  final _formKey = GlobalKey<FormState>();
  final String? imgurClientId = dotenv.env['IMGUR_CLIENT_ID'];
  List<File> _images = [];
  final picker = ImagePicker();

  TextEditingController titleController = TextEditingController();
  TextEditingController contentController = TextEditingController();
  
  bool isObscure = true;

  Future getImage() async {
    ImageSource? source = await showDialog<ImageSource>(
      context: context,
      builder: (BuildContext context) {
        return SimpleDialog(
          title: Text('사진 추가'),
          children: <Widget>[
            SimpleDialogOption(
              onPressed: () {
                // Navigator.pop(context, ImageSource.camera);
                router.pop(ImageSource.camera);
              },
              child: const Text('카메라로 사진 찍기'),
            ),
            SimpleDialogOption(
              onPressed: () {
                // Navigator.pop(context, ImageSource.gallery);
                router.pop(ImageSource.gallery);
              },
              child: const Text('갤러리에서 사진 선택'),
            ),
          ],
        );
      },
    );

    if (source != null) {
      final pickedFile = await picker.pickImage(source: source);

      if (pickedFile != null && _images.length < 10) {
        setState(() {
          _images.add(File(pickedFile.path));
        });
      }
    }
  }

  // imgur API를 통한 이미지 업로드
  Future<List<String>> uploadImages() async {
    List<String> urls = [];

    for (var image in _images) {
      final request = http.MultipartRequest('POST', Uri.parse(dotenv.env['IMGUR_UPLOAD_URL']!));
      request.files.add(await http.MultipartFile.fromPath('image', image.path));
      request.headers.addAll({
        'Authorization': 'Client-ID $imgurClientId',
      });

      try {
        final response = await request.send();
        final respStr = await response.stream.bytesToString();
        if (response.statusCode == 200) {
          final data = jsonDecode(respStr);
          String str = data['data']['link'];
          // 도메인 절삭
          str = str.replaceFirst(dotenv.env['IMGUR_GET_IMAGE_URL']!, '');
          urls.add(str);
        } else {
          print('Failed to upload image: $respStr');
        }
      } catch (e) {
        print('Error occurred: $e');
      }
    }
    return urls;
  }


  // 게시글 작성
  void post() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');

    // 이미지 업로드하고 URL 리스트 받아오기
    final imageUrls = await uploadImages();

    final response = await sendHttpRequest(
      'POST',
      Uri.http(dotenv.env['API_URL']!, '/api/post'),
      // Uri.http(dotenv.env['AWS_API_URL']!, '/api/post'),
      body: jsonEncode({
        'user_id' : userId,
        'board_id': 1, // 코드 수정 필요
        'post_title': titleController.text,
        'post_content': contentController.text,
        'post_image_list': imageUrls
      }),
    );

    // 작성 성공
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text(PostMessage.postSuccess),
      ),
    );
    titleController.clear();
    contentController.clear();
    _images.clear();
    router.pop();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('게시글 작성'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: <Widget>[
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
            Wrap(
              spacing: 8.0, // gap between adjacent chips
              runSpacing: 4.0, // gap between lines
              children: _images
                  .asMap()
                  .entries
                  .map(
                    (entry) => Stack(
                  children: <Widget>[
                    Image.file(
                      entry.value,
                      width: 100,
                      height: 100,
                      fit: BoxFit.cover,
                    ),
                    Positioned(
                      right: 0,
                      child: IconButton(
                        icon: Icon(Icons.close, size: 20),
                        onPressed: () {
                          setState(() {
                            _images.removeAt(entry.key);
                          });
                        },
                      ),
                    ),
                  ],
                ),
              )
                  .toList(),
            ),
            SizedBox(height: 10),
            ElevatedButton(
              onPressed: () {
                // 사진은 최대 10장까지 첨부 가능
                if (_images.length < 10) {
                  getImage();
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text('사진은 최대 10장까지만 첨부 가능합니다.'),
                    ),
                  );
                }
              },
              child: Text('이미지 선택'),
              style: ElevatedButton.styleFrom(
              ),
            ),
            SizedBox(height: 10),
            ElevatedButton(
              onPressed: post,
              child: const Text('등록하기'),
            ),
          ],
        ),
      ),
    );
  }
}