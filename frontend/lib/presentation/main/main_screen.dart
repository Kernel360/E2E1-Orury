import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/presentation/routes/route_path.dart';
import 'package:http/http.dart' as http;

import '../Board/Board.dart';
import '../Board/post.dart';
import '../Board/post_detail.dart';
import '../routes/routes.dart';

class MainScreen extends StatelessWidget {
  MainScreen({super.key});

  Future<List<Post>> fetchPosts() async {
    final response = await http.get(
      Uri.http('127.0.0.1:8080', '/api/board/2'),
      headers: {
        "Content-Type": "application/json",
        // "X-CSRF-TOKEN": csrfToken,
      },
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      List<Post> posts = data.map((item) {
        return Post(
          item['id'],
          item['boardId'],
          item['postTitle'],
          item['postContent'],
          item['userNickname'],
          item['viewCnt'],
          item['likeCnt'],
          item['userId'],
        );
      }).toList();
      return posts;
    } else {
      throw Exception('Failed to load posts');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text('Orury!'),
      ),
      body: FutureBuilder<List<Post>>(
        future: fetchPosts(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(child: Text('No data available.'));
          } else {
            return ListView.builder(
              itemCount: snapshot.data?.length,
              itemBuilder: (context, index) {
                final post = snapshot.data![index];
                return ListTile(
                  leading: Image.network("https://economychosun.com/site/data/img_dir/2020/07/06/2020070600017_0.jpg"),
                  title: Text(post.postTitle),
                  subtitle: Text('작성자: ${post.userNickname}'),
                  onTap: () {
                    // 게시물을 누르면 상세 페이지로 이동
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => PostDetail(post),
                      ),
                    );
                  },
                );
              },
            );
          }
        },
      ),
      bottomNavigationBar: BottomNavigationBar(
        showUnselectedLabels: false,
        showSelectedLabels: false,
        items: [
          BottomNavigationBarItem(
            icon: Icon(
              Icons.people,
              color: Colors.blue, // 'board' 탭은 파란색 아이콘
            ),
            label: 'board',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              Icons.map,
              color: Colors.black, // 다른 탭들은 검은색 아이콘
            ),
            label: 'map',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              Icons.person,
              color: Colors.black,
            ),
            label: 'profile',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              Icons.settings,
              color: Colors.black,
            ),
            label: 'setting',
          ),
        ],
        backgroundColor: AppColors.oruryMain,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          router.go(RoutePath.board_post);
        },
        child: Icon(Icons.add),
      ),
    );
  }
}
