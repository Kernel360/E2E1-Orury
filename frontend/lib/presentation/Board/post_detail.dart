import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/global/messages/board/comment_message.dart';
import 'package:orury/presentation/Board/comment.dart';
import 'package:orury/presentation/Board/post.dart';
import 'package:orury/presentation/Board/post_update.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../main/main_screen.dart';


// StatelessWidget 맨 밑 } 주석처리
// class PostDetail extends StatelessWidget {
//   final int id;
//
//   PostDetail(this.id, {super.key});

class PostDetail extends StatefulWidget {
  final int id;

  PostDetail(this.id, {Key? key}) : super(key: key);

  @override
  _PostDetailState createState() => _PostDetailState();
}

class _PostDetailState extends State<PostDetail> {
  Post? post;

  TextEditingController commentController = TextEditingController();

  // 게시글 상세 조회
  Future<Post> fetchPost() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('jwtToken');

    final response = await http.get(
      Uri.http(dotenv.env['API_URL']!, '/api/post/' + widget.id.toString()),
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

  // 사진 클릭 시 확대 기능
  void _showDialog(BuildContext context, String url) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        content: Image.network(url),
      ),
    );
  }

  // 게시글 삭제
  Future<void> deletePost() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('jwtToken');

    final response = await http.delete(
      Uri.http(dotenv.env['API_URL']!, '/api/post/' + widget.id.toString()),
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      // router.pop();
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => MainScreen()),
      );
    } else {
      throw Exception('Failed to delete post');
    }
  }

  // 댓글 작성
  Future<void> commentCreate() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('jwtToken');

    final response = await http.post(
      Uri.http(dotenv.env['API_URL']!, '/api/comment'),
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({      // 현재 user_id가 1로 들어가는 중
        "post_id": widget.id,
        "comment_content": commentController.text,
        "user_nickname": "test1" //코드 수정 필요
      }),
    );

    if (response.statusCode == 200) {
      // final jsonData = jsonDecode(utf8.decode(response.bodyBytes));
      // final post = Post.fromJson(jsonData);
      commentController.clear();
    } else {
      throw Exception('Failed to create comment');
    }
  }

  // 댓글 수정
  Future<void> commentUpdate(int comid) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('jwtToken');

    final response = await http.patch(
      Uri.http(dotenv.env['API_URL']!, '/api/comment'),
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode({      // 현재 user_id가 1로 들어가는 중
        "id": comid,
        "comment_content": commentController.text,
        "user_nickname": "test1" //코드 수정 필요
      }),
    );

    if (response.statusCode == 200) {
      // final jsonData = jsonDecode(utf8.decode(response.bodyBytes));
      // final post = Post.fromJson(jsonData);
      commentController.clear();
    } else {
      throw Exception('Failed to update comment');
    }
  }

  // 댓글 삭제
  Future<void> deleteComment(int commentId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('jwtToken');

    final response = await http.delete(
      Uri.http(dotenv.env['API_URL']!, '/api/comment/' + commentId.toString()),
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      // router.pop();
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => PostDetail(widget.id)),
      );
    } else {
      throw Exception(CommentMessage.commentFail);
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
              leading: IconButton(
                icon: Icon(Icons.arrow_back),
                onPressed: () {
                  // 여기에 뒤로 가기 버튼을 눌렀을 때 수행할 작업을 코딩합니다.
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(
                      builder: (context) => MainScreen(),  // 이전 화면으로 돌아갈 때 rebuild하려는 화면을 지정합니다.
                    ),
                  );
                },
              ),
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
                        if (post.imageList != null && post.imageList.isNotEmpty)
                          Container(
                            height: 200, // Adjust this as needed
                            child: ListView.builder(
                              scrollDirection: Axis.horizontal,
                              itemCount: post.imageList!.length,
                              itemBuilder: (context, i) {
                                return GestureDetector(
                                    onTap: () => _showDialog(context, post.imageList[i]),
                                    child: Padding(
                                      padding: const EdgeInsets.only(right: 8.0),
                                      child: Image.network(post.imageList[i]),
                                  ),
                                );
                              },
                            ),
                          ),
                        SizedBox(height: 16),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(post.postTitle,
                                style: TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.bold)), // 제목
                            PopupMenuButton<String>(
                              onSelected: (String result) {
                                if (result == 'edit') {
                                  // Navigator.push(
                                  //   context,
                                  //   MaterialPageRoute(
                                  //     builder: (context) =>
                                  //         PostUpdate(data: post),
                                  //   ),
                                  // );
                                  Navigator.pushReplacement(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) => PostUpdate(data: post),
                                    ),
                                  );

                                  // context.go(RoutePath.postUpdate, extra: {'postset': postset});
                                } else if (result == 'delete') {
                                  // 게시글 삭제 기능 구현
                                  deletePost();
                                }
                              },
                              itemBuilder: (BuildContext context) =>
                              <PopupMenuEntry<String>>[
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
                    Comment comment = post.commentList[post.commentList.length - index];
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
                                // final commentController = TextEditingController(); // 댓글 컨트롤러 생성

                                showDialog(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: Text('댓글 수정'),
                                      content: TextField(
                                        controller: commentController,
                                        autofocus: true,
                                        decoration: InputDecoration(
                                          labelText: "댓글을 입력하세요",
                                        ),
                                      ),
                                      actions: <Widget>[
                                        TextButton(
                                          child: Text('취소'),
                                          onPressed: () {
                                            Navigator.of(context).pop();
                                          },
                                        ),
                                        TextButton(
                                          child: Text('수정'),
                                          onPressed: () {
                                            // 댓글 수정 처리를 수행합니다.
                                            commentUpdate(comment.id);
                                            // 댓글 작성 및 저장이 완료되면, 게시글과 댓글을 다시 불러옵니다.
                                            fetchPost().then((post) {
                                              setState(() {
                                                // 새로 불러온 게시글과 댓글로 화면을 업데이트합니다.
                                                this.post = post;
                                              });
                                            });
                                            Navigator.of(context).pop();
                                          },
                                        ),
                                      ],
                                    );
                                  },
                                );
                              } else if (result == 'delete') {
                                // 삭제 확인 다이얼로그 보여주기
                                showDialog(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: Text('삭제'),
                                      content: Text('삭제하시겠습니까?'),
                                      actions: <Widget>[
                                        TextButton(
                                          child: Text('취소'),
                                          onPressed: () {
                                            Navigator.of(context).pop();
                                          },
                                        ),
                                        TextButton(
                                          child: Text('확인'),
                                          onPressed: () {
                                            // 댓글 삭제 기능 구현
                                            deleteComment(comment.id);
                                            // Navigator.of(context).pop();
                                          },
                                        ),
                                      ],
                                    );
                                  },
                                );
                              }
                            },
                            itemBuilder: (BuildContext context) =>
                            <PopupMenuEntry<String>>[
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
            floatingActionButton: ElevatedButton(
              child: Text('댓글 달기'),
              onPressed: () {
                // final commentController = TextEditingController(); // 댓글 컨트롤러 생성

                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return AlertDialog(
                      title: Text('댓글 작성'),
                      content: TextField(
                        controller: commentController,
                        autofocus: true,
                        decoration: InputDecoration(
                          labelText: "댓글을 입력하세요",
                        ),
                      ),
                      actions: <Widget>[
                        TextButton(
                          child: Text('취소'),
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                        ),
                        TextButton(
                          child: Text('작성'),
                          onPressed: () {
                            // 댓글 작성 처리를 수행합니다.
                            commentCreate();
                            // 댓글 작성 및 저장이 완료되면, 게시글과 댓글을 다시 불러옵니다.
                            fetchPost().then((post) {
                              setState(() {
                                // 새로 불러온 게시글과 댓글로 화면을 업데이트합니다.
                                this.post = post;
                              });
                            });
                            Navigator.of(context).pop();
                          },
                        ),
                      ],
                    );
                  },
                );
              },
            ),
          );
        }
      },
    );
  }
}
