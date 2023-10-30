import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:orury/presentation/Board/post.dart';

class PostDetail extends StatelessWidget {
  final Post post;

  PostDetail(this.post);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('게시물 상세보기'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Image.network("https://economychosun.com/site/data/img_dir/2020/07/06/2020070600017_0.jpg"), // 썸네일 이미지
            SizedBox(height: 16),
            Text(post.postTitle, style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)), // 제목
            Text('작성자: ${post.userNickname}'), // 작성자
            Divider(),
            Text(post.postContent), // 본문
          ],
        ),
      ),
    );
  }
}