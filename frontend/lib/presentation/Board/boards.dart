class Boards {
  final int id;
  final String boardTitle;

  Boards({required this.id, required this.boardTitle});

  factory Boards.fromJson(Map<String, dynamic> json) {
    return Boards(
      id: json['id'],
      boardTitle: json['board_title'],
    );
  }
}
