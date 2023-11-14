import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/presentation/Board/comment.dart';
import 'package:orury/presentation/Board/post.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../global/http/http_request.dart';
import '../routes/route_path.dart';
import '../routes/routes.dart';


class PostDetail extends StatefulWidget {
  final int id;

  PostDetail(this.id, {Key? key}) : super(key: key);

  @override
  _PostDetailState createState() => _PostDetailState();
}

class _PostDetailState extends State<PostDetail> {
  Post? post;

  TextEditingController commentController = TextEditingController();

  late SharedPreferences prefs;

  @override
  void initState() {
    super.initState();
    _initSharedPreferences();
  }

  Future<void> _initSharedPreferences() async {
    prefs = await SharedPreferences.getInstance();
  }

  // 게시글 상세 조회
  Future<Post> fetchPost() async {
    final response = await sendHttpRequest(
      'GET',
      Uri.http(dotenv.env['API_URL']!, '/api/post/${widget.id}'),
      // Uri.http(dotenv.env['AWS_API_URL']!, '/api/post/${widget.id}'),
    );

    final jsonData = jsonDecode(utf8.decode(response.bodyBytes));
    final post = Post.fromJson(jsonData);
    return post;
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
    final response = await sendHttpRequest(
      'DELETE',
      Uri.http(dotenv.env['API_URL']!, '/api/post/${widget.id}'),
      // Uri.http(dotenv.env['AWS_API_URL']!, '/api/post/${widget.id}'),
    );

    router.pop();
  }

  // 댓글 작성
  Future<void> commentCreate() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    final nickname = prefs.getString('nickname');

    final response = await sendHttpRequest(
      'POST',
      Uri.http(dotenv.env['API_URL']!, '/api/comment'),
      // Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment'),
      body: jsonEncode({
        "user_id": userId,
        "post_id": widget.id,
        "comment_content": commentController.text,
        "user_nickname": nickname
      }),
    );

    commentController.clear();
  }

  // 댓글 수정
  Future<void> commentUpdate(int comId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    final nickname = prefs.getString('nickname');

    final response = await sendHttpRequest(
      'PATCH',
      Uri.http(dotenv.env['API_URL']!, '/api/comment'),
      // Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment'),
      body: jsonEncode({
        "id": comId,
        "user_id": userId,
        "comment_content": commentController.text,
        "user_nickname": nickname
      }),
    );

    commentController.clear();
  }

  // 댓글 삭제
  Future<void> deleteComment(int commentId) async {
    final response = await sendHttpRequest(
      'DELETE',
      Uri.http(dotenv.env['API_URL']!, '/api/comment/' + commentId.toString()),
      // Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment/' + commentId.toString()),
    );
  }

  // 대댓글 작성
  Future<void> replyCreate(int comId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    final nickname = prefs.getString('nickname');

    final response = await sendHttpRequest(
      'POST',
      Uri.http(dotenv.env['API_URL']!, '/api/comment'),
      // Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment'),
      body: jsonEncode({
        "id": comId,
        "user_id": userId,
        "post_id": widget.id,
        "comment_content": commentController.text,
        "user_nickname": nickname
      }),
    );

    commentController.clear();
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
                            (post.userId == prefs.getInt('userId') || 'ROLE_ADMIN' == prefs.getString('role')) ?
                            PopupMenuButton<String>(
                              onSelected: (String result) {
                                if (result == 'edit') {
                                  // 게시글 수정 기능 구현
                                  router.push(
                                    RoutePath.postUpdate,
                                    extra: post,
                                  );
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
                            )
                            : Container(),
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
                      contentPadding: comment.pId != null ? EdgeInsets.only(left: 50.0) : null,
                      dense: comment.pId != null ? true : null,
                      title: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Text(comment.commentContent),
                          ),
                          (comment.userId == prefs.getInt('userId') || 'ROLE_ADMIN' == prefs.getString('role')) ?
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
                                            router.pop();
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
                                            router.pop();
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
                                            router.pop();
                                          },
                                        ),
                                        TextButton(
                                          child: Text('확인'),
                                          onPressed: () {
                                            // 댓글 삭제 기능 구현
                                            deleteComment(comment.id);

                                            fetchPost().then((post) {
                                              setState(() {
                                                // 새로 불러온 게시글과 댓글로 화면을 업데이트합니다.
                                                this.post = post;
                                              });
                                            });

                                            router.pop();
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
                          )
                          : Container(),
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
                              if (comment.pId == null)
                                TextButton(
                                  onPressed: () {
                                    // 대댓글 작성 기능 구현
                                    showDialog(
                                      context: context,
                                      builder: (BuildContext context) {
                                        return AlertDialog(
                                          title: Text('답글 작성'),
                                          content: TextField(
                                            controller: commentController,
                                            autofocus: true,
                                            decoration: InputDecoration(
                                              labelText: "답글을 입력하세요",
                                            ),
                                          ),
                                          actions: <Widget>[
                                            TextButton(
                                              child: Text('취소'),
                                              onPressed: () {
                                                router.pop();
                                              },
                                            ),
                                            TextButton(
                                              child: Text('작성'),
                                              onPressed: () {
                                                // 댓글 작성 처리를 수행합니다.
                                                replyCreate(comment.id);
                                                // 댓글 작성 및 저장이 완료되면, 게시글과 댓글을 다시 불러옵니다.
                                                fetchPost().then((post) {
                                                  setState(() {
                                                    // 새로 불러온 게시글과 댓글로 화면을 업데이트합니다.
                                                    this.post = post;
                                                  });
                                                });
                                                router.pop();
                                              },
                                            ),
                                          ],
                                        );
                                      },
                                    );
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
                            router.pop();
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
                            router.pop();
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
