// 게시물 데이터 모델
import 'dart:ffi';

import 'package:orury/presentation/Board/comment.dart';

class Post {
  final int id;
  final int boardId;
  final String postTitle;
  final String postContent;
  final String userNickname;
  final String? thumbnailUrl;
  final List<dynamic> imageList;
  final int viewCnt;
  final int likeCnt;
  final int commentCnt;
  final int userId;
  final Map<String, List<Comment>> commentMap;
  bool isLike; // 좋아요 상태를 나타내는 변수

  Post({
      required this.id,
      required this.boardId,
      required this.postTitle,
      required this.postContent,
      required this.userNickname,
      required this.thumbnailUrl,
      required this.imageList,
      required this.viewCnt,
      required this.likeCnt,
      required this.userId,
      required this.commentCnt,
      required this.commentMap,
      required this.isLike
  });

  //
  factory Post.fromJson(Map<String, dynamic> json) {

    final Map<String, List<Comment>> commentMap2 = {};
    var commentMapJson = json['comment_map'] as Map;

    for (var key in commentMapJson.keys) {
      var commentMapList = commentMapJson[key] as List;
      List<Comment> commentMapListObjects = commentMapList.map((comment) => Comment.fromJson(comment)).toList();
      commentMap2[key] = commentMapListObjects; // 여기서 Long은 Dart에 기본적으로 제공되는 타입이 아닙니다.
    }


    return Post(
        id: json['id'],
        boardId: json['board_id'],
        postTitle: json['post_title'],
        postContent: json['post_content'],
        userNickname: json['user_nickname'],
        thumbnailUrl: json['thumbnail_url'],
        imageList: json['image_list'],
        viewCnt: json['view_cnt'],
        likeCnt: json['like_cnt'],
        commentCnt: json['comment_cnt'],
        userId: json['user_id'],
        commentMap: commentMap2,
        isLike: json['is_like']
    );
  }
}
