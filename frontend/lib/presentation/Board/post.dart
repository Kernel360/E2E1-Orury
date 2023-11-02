// 게시물 데이터 모델
import 'package:orury/presentation/Board/comment.dart';

class Post {
  final int id;
  final int boardId;
  final String postTitle;
  final String postContent;
  final String userNickname;
  final String? thumbnailUrl;
  final int viewCnt;
  final int likeCnt;
  final int userId;
  final List<Comment> commentList;

  // Post(this.id, this.boardId, this.postTitle, this.postContent,
  //     this.userNickname, this.viewCnt, this.likeCnt, this.userId);

  Post({required this.id,
      required this.boardId,
      required this.postTitle,
      required this.postContent,
      required this.userNickname,
      required this.thumbnailUrl,
      required this.viewCnt,
      required this.likeCnt,
      required this.userId,
      required this.commentList});

  //
  factory Post.fromJson(Map<String, dynamic> json) {
    var commentList = json['comment_list'] as List;
    List<Comment> commentObjects = commentList.map((comment) => Comment.fromJson(comment)).toList();

    return Post(
        id: json['id'],
        boardId: json['board_id'],
        postTitle: json['post_title'],
        postContent: json['post_content'],
        userNickname: json['user_nickname'],
        thumbnailUrl: json['thumbnail_url'],
        viewCnt: json['view_cnt'],
        likeCnt: json['like_cnt'],
        userId: json['user_id'],
        commentList: commentObjects);
  }
}
