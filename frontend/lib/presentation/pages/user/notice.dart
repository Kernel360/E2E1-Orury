class Notice {
  final int post_id;
  final String postTitle;
  final String postContent;

  Notice({required this.post_id, required this.postTitle, required this.postContent});

  factory Notice.fromJson(Map<String, dynamic> json) {
    return Notice(
      post_id: json['id'],
      postTitle: json['post_title'],
      postContent: json['post_content'],
    );
  }
}