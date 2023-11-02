import 'package:orury/presentation/Board/post.dart';

class Board {
  final String boardTitle;
  final int id;
  final List<Post> postList;

  // Board(this.boardTitle, this.id, this.postDtoList);

  Board({required this.boardTitle, required this.id, required this.postList});
  //
  factory Board.fromJson(Map<String, dynamic> json) {
    var postList = json['post_list'] as List;
    List<Post> postObjects = postList.map((post) => Post.fromJson(post)).toList();

    return Board(
      id: json['id'],
      boardTitle: json['board_title'],
      postList: postObjects,
    );
  }
}