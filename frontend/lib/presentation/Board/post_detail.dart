import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/presentation/Board/comment.dart';
import 'package:orury/presentation/Board/post.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class PostDetail extends StatelessWidget {
  final int id;

  PostDetail(this.id, {super.key});

  // 게시글 조회
  Future<Post> fetchPost() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('jwtToken');

    final response = await http.get(
      Uri.http(dotenv.env['API_URL']!, '/api/post/' + id.toString()),
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final jsonData = jsonDecode(utf8.decode(response.bodyBytes));
      final post = Post.fromJson(jsonData);
      return post;
    } else {
      throw Exception('Failed to load post detail');
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Post>(
      future: fetchPost(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return Center(child: CircularProgressIndicator());
        } else if (snapshot.hasError) {
          return Text('Error: ${snapshot.error}');
        } else {
          final post = snapshot.data!;
          return Scaffold(
            appBar: AppBar(
              title: Text('게시물 상세보기'),
            ),
            body: Padding(
              padding: const EdgeInsets.all(16.0),
              child: ListView.builder(
                itemCount: post.commentList.length + 1,
                itemBuilder: (context, index) {
                  if (index == 0) {
                    return Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                      if (post.imageList != null && post.imageList!.isNotEmpty)
                          Container(
                            height: 200, // Adjust this as needed
                            child: ListView.builder(
                              scrollDirection: Axis.horizontal,
                              itemCount: post.imageList!.length,
                              itemBuilder: (context, i) {
                                return Padding(
                                  padding: const EdgeInsets.only(right: 8.0),
                                  child: Image.network(post.imageList![i]), // 썸네일 이미지
                                );
                              },
                            ),
                          ),
                        SizedBox(height: 16),
                        Text(post.postTitle,
                            style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold)), // 제목
                        Text(
                          post.userNickname.toString(),
                          style: TextStyle(
                            fontSize: 15,
                          ),
                        ), // 작성자
                        Divider(),
                        Text(post.postContent), // 본문
                      ],
                    );
                  } else {
                    Comment comment = post.commentList[index - 1];
                    return ListTile(
                      title: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(comment.commentContent),
                          ),
                          PopupMenuButton<String>(
                            onSelected: (String result) {
                              if (result == 'edit') {
                                // 댓글 수정 기능 구현
                              } else if (result == 'delete') {
                                // 댓글 삭제 기능 구현
                              }
                            },
                            itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
                              const PopupMenuItem<String>(
                                value: 'edit',
                                child: Text('수정'),
                              ),
                              const PopupMenuItem<String>(
                                value: 'delete',
                                child: Text('삭제'),
                              ),
                            ],
                          ),
                        ],
                      ),
                      subtitle: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(comment.userNickname),
                          Row(
                            children: [
                              Icon(Icons.favorite, color: Colors.red),
                              Text(comment.likeCnt.toString()),
                              SizedBox(width: 16),
                              TextButton(
                                onPressed: () {
                                  // 대댓글 작성 기능 구현
                                },
                                child: Text('답글 달기'),
                                style: TextButton.styleFrom(
                                  foregroundColor: AppColors.oruryMain,
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    );
                  }
                },
              ),
            ),
          );
        }
      },
    );
  }
}
