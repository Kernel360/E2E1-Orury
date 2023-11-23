import 'dart:convert';
import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/presentation/routes/route_path.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../global/http/http_request.dart';
import '../Board/board.dart';
import '../Board/boards.dart';
import '../Board/post.dart';
import '../routes/routes.dart';

import 'package:http/http.dart' as http;

class MainScreen extends StatefulWidget {
  MainScreen({Key? key}) : super(key: key);

  @override
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  final client = http.Client();

  late List<Boards> board_list;

  late SharedPreferences prefs;

  int _selectedCategory = 2;

  @override
  void initState() {
    super.initState();
    initSharedPreferences();
    connectToServer();
    loadBoards();
  }

  @override
  void dispose() {
    client.close();
    super.dispose();
  }

  Future<void> initSharedPreferences() async {
    prefs = await SharedPreferences.getInstance();
  }

  Future<void> connectToServer() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    // final response = await client.send(http.Request('GET', Uri.http(dotenv.env['API_URL']!, '/api/notify/subscribe/' + prefs.getInt('userId').toString())));
    final response = await client.send(http.Request(
        'GET',
        Uri.http(dotenv.env['AWS_API_URL']!,
            '/api/notify/subscribe/' + prefs.getInt('userId').toString())));
    if (response.statusCode == 200) {
      await for (final data in response.stream
          .transform(utf8.decoder)
          .transform(LineSplitter())) {
        // SSE 스트림 데이터 처리 로직 구현
        print('Received data: $data');
        if (data.startsWith('data:')) {
          Fluttertoast.showToast(
              msg: data.substring(6),
              backgroundColor: Colors.white,
              toastLength: Toast.LENGTH_SHORT,
              gravity: ToastGravity.TOP);
        }
      }
    }
  }

  // 게시판 id들 가져오기
  Future<List<Boards>> getBoards() async {
    final response = await sendHttpRequest(
      'GET',
      // Uri.http(dotenv.env['API_URL']!, '/api/board'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/board'),
    );

    final jsonData = jsonDecode(utf8.decode(response.bodyBytes)) as List;
    final boards =
        jsonData.map((boardJson) => Boards.fromJson(boardJson)).toList();
    return boards;
  }

  // getBoards()의 결과를 기다립니다.
  Future<void> loadBoards() async {
    board_list = await getBoards();
  }

  // 게시글 목록 조회
  Future<List<Post>> fetchPosts(int boardId) async {
    final response = await sendHttpRequest(
      'GET',
      // Uri.http(dotenv.env['API_URL']!, '/api/board/1'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/board/$boardId'),
    );

    final jsonData = jsonDecode(utf8.decode(response.bodyBytes));
    final board = Board.fromJson(jsonData);
    return board.postList;
  }

  Future<void> logout() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    Set<String> keys = prefs.getKeys();

    for (String key in keys) {
      await prefs.remove(key);
    }
    router.go(RoutePath.loginPage);
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: initSharedPreferences(),
        builder: (BuildContext context, AsyncSnapshot<void> prefssnapshot) {
          if (prefssnapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator()); // Loading indicator
          } else if (prefssnapshot.hasError) {
            return Text('Error: ${prefssnapshot.error}');
          } else {
            return FutureBuilder(
                future: loadBoards(),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return Center(child: CircularProgressIndicator()); // 데이터를 기다리는 동안 표시할 위젯
                  } else if (snapshot.hasError) {
                    return Text('Error: ${snapshot.error}'); // 에러 발생 시 표시할 위젯
                  } else {
                    return FutureBuilder<List<Post>>(
                        future:
                            fetchPosts(board_list[_selectedCategory - 1].id),
                        builder: (context, postsnapshot) {
                          if (postsnapshot.connectionState == ConnectionState.waiting) {
                            return Center(child: CircularProgressIndicator());
                          } else if (postsnapshot.hasError) {
                            return Center(
                                child: Text('Error: ${postsnapshot.error}'));
                            // } else if (!postsnapshot.hasData ||
                            //     postsnapshot.data!.isEmpty) {
                            //   return Center(child: Text('No data available.'));
                          } else {
                            return Scaffold(
                              appBar: AppBar(
                                  centerTitle: true,
                                  title: Text('Orury!'),
                                  actions: [
                                    TextButton(
                                      onPressed: () {
                                        logout();
                                      },
                                      style: Theme.of(context)
                                          .textButtonTheme
                                          .style,
                                      child: Text(
                                        '로그아웃',
                                        style: Theme.of(context)
                                            .textTheme
                                            .bodySmall
                                            ?.copyWith(
                                              color: AppColors.black,
                                              fontWeight: FontWeight.bold,
                                            ),
                                      ),
                                    ),
                                  ]),
                              body: Column(children: [
                                Container(
                                    height: 50.0,
                                    child: ListView.builder(
                                      scrollDirection: Axis.horizontal,
                                      // 가로 스크롤 설정
                                      itemCount: board_list.length,
                                      // 버튼 개수
                                      itemBuilder: (context, index) {
                                        return ElevatedButton(
                                          onPressed: () {
                                            setState(() {
                                              _selectedCategory = index + 1;
                                            });
                                          },
                                          child: Text(
                                              '${board_list[index].boardTitle}'),
                                        );
                                      },
                                    )),
                                SizedBox(height: 15),
                                Text(
                                    board_list[_selectedCategory - 1]
                                            .boardTitle +
                                        ' 게시판',
                                    style: TextStyle(
                                        fontSize: 30,
                                        fontWeight: FontWeight.bold)),
                                Expanded(
                                    child: ListView.builder(
                                  itemCount: postsnapshot.data?.length,
                                  itemBuilder: (context, index) {
                                    final post = postsnapshot.data![index];
                                    return ListTile(
                                      // tileColor: AppColors.background,
                                      selectedTileColor: AppColors.oruryMain,
                                      leading: post.thumbnailUrl != null
                                          ? Image.network(
                                              dotenv.env[
                                                      'IMGUR_GET_IMAGE_URL']! +
                                                  post.thumbnailUrl!,
                                              width: 80,
                                              height: 80,
                                              fit: BoxFit.cover,
                                              errorBuilder:
                                                  (BuildContext context,
                                                      Object exception,
                                                      StackTrace? stackTrace) {
                                                return const SizedBox
                                                    .shrink(); // 이미지 로드에 실패하면 아무것도 표시하지 않음
                                              },
                                            )
                                          : null,
                                      // null인 경우 leading 생략
                                      title: Text(post.postTitle),
                                      subtitle: Text(post.userNickname),
                                      onTap: () {
                                        // 게시물을 누르면 상세 페이지로 이동
                                        router.push(
                                          RoutePath.postDetail,
                                          extra: post.id,
                                        );
                                      },
                                      trailing: board_list[
                                                      _selectedCategory - 1]
                                                  .id !=
                                              1
                                          ? Row(
                                              mainAxisSize: MainAxisSize.min,
                                              children: [
                                                Icon(
                                                  Icons.favorite,
                                                  size: 15,
                                                  color: Colors.red,
                                                ),
                                                // 좋아요 아이콘
                                                Text(post.likeCnt.toString()),
                                                // 좋아요 수
                                                SizedBox(width: 20),
                                                // 간격 조절
                                                Icon(
                                                  Icons.comment,
                                                  size: 15,
                                                ),
                                                // 댓글 아이콘
                                                Text(post.commentCnt.toString()),
                                              ],
                                            )
                                          : null,
                                    );
                                  },
                                ))
                              ]),
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
                              floatingActionButton:
                                  (board_list[_selectedCategory - 1].id == 1 &&
                                              prefs.getString('role') ==
                                                  'ROLE_ADMIN') ||
                                          board_list[_selectedCategory - 1]
                                                  .id !=
                                              1
                                      ? FloatingActionButton(
                                          onPressed: () {
                                            router.push(RoutePath.postCreate,
                                                extra: board_list[
                                                        _selectedCategory - 1]
                                                    .id);
                                          },
                                          child: Icon(Icons.add),
                                        )
                                      : null,
                            );
                          }
                        });
                  }
                });
          }
        });
  }
}
