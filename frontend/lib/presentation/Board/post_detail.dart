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
  bool isLike = false; // 좋아요 상태를 나타내는 변수
  List<bool> isCommentLikedList = []; // 댓글 좋아요 상태를 나타내는 리스트

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
      // Uri.http(dotenv.env['API_URL']!, '/api/post/${widget.id}'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/post/${widget.id}'),
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
      // Uri.http(dotenv.env['API_URL']!, '/api/post/${widget.id}'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/post/${widget.id}'),
    );

    router.pop();
  }

  // 댓글 및 답글 작성 함수
  Future<void> commentCreate({int? parentCommentId}) async {
    final userId = prefs.getInt('userId');
    final nickname = prefs.getString('nickname');

    final response = await sendHttpRequest(
      'POST',
      // Uri.http(dotenv.env['API_URL']!, '/api/comment'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment'),
      body: jsonEncode({
        "id": parentCommentId,
        "user_id": userId,
        "post_id": widget.id,
        "comment_content": commentController.text,
        "user_nickname": nickname
      }),
    );

    commentController.clear();
  }

  // 댓글 및 답글 수정
  Future<void> commentUpdate(int comId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    final nickname = prefs.getString('nickname');

    final response = await sendHttpRequest(
      'PATCH',
      // Uri.http(dotenv.env['API_URL']!, '/api/comment'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment'),
      body: jsonEncode({
        "id": comId,
        "user_id": userId,
        "comment_content": commentController.text,
        "user_nickname": nickname
      }),
    );

    commentController.clear();
  }

  // 댓글 및 답글 삭제
  Future<void> deleteComment(int commentId) async {
    final response = await sendHttpRequest(
      'DELETE',
      // Uri.http(dotenv.env['API_URL']!, '/api/comment/$commentId'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment/$commentId'),
    );
  }

  // 댓글 및 대댓글 작성, 수정, 삭제 후 게시글 및 댓글 다시 불러오는 함수
  void refreshPostAfterCommentAction() {
    fetchPost().then((post) {
      setState(() {
        this.post = post;
      });
    });
  }

  // 작성, 수정 다이얼로그
  void showCommentDialog(
      {String? title,
      String? labelText,
      String? buttonText,
      bool showTextField = true,
      String? initialText,
      required Function onPressed}) {
    commentController.text = initialText ?? '';

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(title ?? '댓글 작성'),
          content: showTextField
              ? TextField(
                  controller: commentController,
                  autofocus: true,
                  decoration: InputDecoration(
                    labelText: labelText ?? "댓글을 입력하세요",
                  ),
                )
              : Text('삭제하시겠습니까?'),
          actions: <Widget>[
            TextButton(
              child: Text('취소'),
              onPressed: () {
                router.pop();
              },
            ),
            TextButton(
              child: Text(buttonText ?? '작성'),
              onPressed: () {
                onPressed();
                refreshPostAfterCommentAction();
                router.pop();
              },
            ),
          ],
        );
      },
    );
  }

  // 좋아요 상태 업데이트 api 호출
  void updateLikeStatus(bool isLiked) async {
    final response = await sendHttpRequest(
      'PATCH',
      // Uri.http(dotenv.env['API_URL']!, '/api/post/like'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/post/like'),
      body: jsonEncode({
        'user_id': prefs.getInt('userId'),
        'post_id': widget.id,
        'is_like': isLiked,
      }),
    );

    // // 추가: 좋아요 상태 업데이트 이후 게시글 다시 불러오기
    // fetchPost().then((post) {
    //   setState(() {
    //     this.post = post;
    //   });
    // });
  }

  // 좋아요 상태 업데이트 api 호출 (댓글용)
  void updateCommentLikeStatus(bool isLiked, int commentId) async {
    final response = await sendHttpRequest(
      'PATCH',
      // Uri.http(dotenv.env['API_URL']!, '/api/comment/like'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/comment/like'),
      body: jsonEncode({
        'user_id': prefs.getInt('userId'),
        'comment_id': commentId,
        'is_like': isLiked,
      }),
    );

    // 추가: 댓글 좋아요 상태 업데이트 이후 게시글 다시 불러오기
    // fetchPost().then((post) {
    //   setState(() {
    //     this.post = post;
    //     // 추가: 댓글 좋아요 상태 리스트 초기화
    //     isCommentLikedList = List.generate(
    //         post.commentList.length, (index) => post.commentList[index].isLike);
    //   });
    // });
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
          isLike = post.isLike; // 초기 좋아요 상태 설정
          final commentMap = post.commentMap;
          final mainComments = commentMap['0'] ?? []; // 본댓글 리스트
          int totalComments = mainComments.length; // 본댓글 개수를 먼저 셉니다.

          // 본댓글에 해당하는 대댓글 개수를 더합니다.
          mainComments.forEach((mainComment) {
            totalComments +=
                (commentMap[mainComment.id.toString()]?.length ?? 0);
          });

          mainComments.forEach((mainComment) {
            isCommentLikedList.add(mainComment.isLike);
            post.commentMap[mainComment.id.toString()]?.forEach((reply) {
              isCommentLikedList.add(reply.isLike);
            });
          });

          return Scaffold(
            appBar: AppBar(
              title: Text('게시물 상세보기'),
            ),
            body: Padding(
              padding: const EdgeInsets.all(16.0),
              child: ListView.builder(
                itemCount: totalComments + 1,
                itemBuilder: (context, index) {
                  if (index == 0) {
                    return Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        if (post.imageList.isNotEmpty)
                          Container(
                            height: 200, // Adjust this as needed
                            child: ListView.builder(
                              scrollDirection: Axis.horizontal,
                              itemCount: post.imageList.length,
                              itemBuilder: (context, i) {
                                return GestureDetector(
                                  onTap: () =>
                                      _showDialog(context, post.imageList[i]),
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
                            Expanded(
                              child: Text(
                                post.postTitle,
                                style: TextStyle(
                                    fontSize: 20, fontWeight: FontWeight.bold),
                              ),
                            ),
                            // 수정: 좋아요 토글 아이콘
                            post.boardId != 1 ? IconButton(
                              icon: Icon(
                                isLike ? Icons.favorite : Icons.favorite_border,
                                color: isLike ? Colors.red : Colors.grey,
                              ),
                              onPressed: () {
                                setState(() {
                                  isLike = !isLike; // 토글
                                });

                                // 추가: 좋아요 상태 업데이트 API 호출
                                updateLikeStatus(isLike);
                              },
                            ) : Container(),
                            (post.userId == prefs.getInt('userId') ||
                                    'ROLE_ADMIN' == prefs.getString('role'))
                                ? PopupMenuButton<String>(
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
                    Comment? comment;
                    int commentIndex = index - 1;
                    for (var mainComment in mainComments) {
                      if (commentIndex < 1 + (commentMap[mainComment.id.toString()]?.length ?? 0)) {
                        if (commentIndex == 0) {
                          comment = mainComment; // 본 댓글 출력
                        } else {
                          comment = commentMap[mainComment.id.toString()]![
                              commentIndex - 1]; // 대댓글 출력
                        }
                        break;
                      } else {
                        commentIndex -= 1 +
                            (commentMap[mainComment.id.toString()]?.length ??
                                0);
                      }
                    }

                    if (comment != null) {
                      return ListTile(
                        contentPadding: comment.pId != null
                            ? EdgeInsets.only(left: 50.0)
                            : null,
                        dense: comment.pId != null ? true : null,
                        title: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Expanded(
                              child: Text(comment.commentContent),
                            ),
                            (comment.userId == prefs.getInt('userId') ||
                                    'ROLE_ADMIN' == prefs.getString('role'))
                                ? PopupMenuButton<String>(
                                    onSelected: (String result) {
                                      if (result == 'edit') {
                                        // 댓글 수정 기능 구현
                                        comment?.pId == null
                                            ? showCommentDialog(
                                                title: '댓글 수정',
                                                initialText:
                                                    comment?.commentContent,
                                                buttonText: '수정',
                                                onPressed: () {
                                                  commentUpdate(comment!.id);
                                                })
                                            : showCommentDialog(
                                                title: '답글 수정',
                                                labelText: '답글을 입력하세요',
                                                initialText:
                                                    comment?.commentContent,
                                                buttonText: '수정',
                                                onPressed: () {
                                                  commentUpdate(comment!.id);
                                                });
                                      } else if (result == 'delete') {
                                        // 삭제 확인 다이얼로그 보여주기
                                        showCommentDialog(
                                            title: '삭제',
                                            buttonText: '삭제',
                                            showTextField: false,
                                            onPressed: () {
                                              deleteComment(comment!.id);
                                            });
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
                                IconButton(
                                  icon: Icon(
                                    isCommentLikedList[index - 1]
                                        ? Icons.favorite
                                        : Icons.favorite_border,
                                    color: isCommentLikedList[index - 1]
                                        ? Colors.red
                                        : Colors.grey,
                                  ),
                                  onPressed: () {
                                    setState(() {
                                      // 수정: 댓글 좋아요 상태 토글
                                      isCommentLikedList[index - 1] =
                                          !isCommentLikedList[index - 1];
                                    });

                                    // 추가: 댓글 좋아요 상태 업데이트 API 호출
                                    updateCommentLikeStatus(
                                        isCommentLikedList[index - 1],
                                        comment!.id);
                                  },
                                ),
                                Text(comment.likeCnt.toString()),
                                SizedBox(width: 16),
                                if (comment.pId == null)
                                  TextButton(
                                    onPressed: () {
                                      // 대댓글 작성 기능 구현
                                      showCommentDialog(
                                          title: '답글 작성',
                                          labelText: '답글을 입력하세요',
                                          onPressed: () {
                                            commentCreate(
                                                parentCommentId: comment!.id);
                                          });
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
                    } else {
                      return Container();
                    }
                  }
                },
              ),
            ),
            floatingActionButton: post.boardId != 1 ? ElevatedButton(
              child: Text('댓글 달기'),
              onPressed: () {
                showCommentDialog(onPressed: () {
                  commentCreate();
                });
              },
            ) : null,
          );
        }
      },
    );
  }
}
