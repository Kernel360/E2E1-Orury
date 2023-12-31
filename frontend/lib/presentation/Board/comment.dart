// 댓글 데이터 모델
class Comment {
  final int id;
  final int? pId;
  final int userId;
  final int postId;
  final String commentContent;
  final String userNickname;
  final int likeCnt;

  bool isLike;

  Comment(
      {required this.id,
      required this.pId,
      required this.userId,
      required this.postId,
      required this.commentContent,
      required this.userNickname,
      required this.likeCnt,
      required this.isLike});

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      id: json['id'],
      pId: json['pid'],
      userId: json['user_id'],
      postId: json['post_id'],
      commentContent: json['comment_content'],
      userNickname: json['user_nickname'],
      likeCnt: json['like_cnt'],
      isLike: json['is_like']
    );
  }
}
