import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;

import 'package:flutter/cupertino.dart';
import 'package:orury/presentation/routes/route_path.dart';
import 'package:orury/presentation/routes/routes.dart';

import 'notice.dart';

class MySlidingAppBar extends StatefulWidget {
  @override
  _MySlidingAppBarState createState() => _MySlidingAppBarState();
}

class _MySlidingAppBarState extends State<MySlidingAppBar> with AutomaticKeepAliveClientMixin {
  late PageController _pageController;
  late Timer _timer;
  late List<Notice> notice_list;
  int _noticeCount = 0;

  Future<List<Notice>> fetchData() async {
    final response = await http.get(
      // Uri.http(dotenv.env['API_URL']!, '/api/board/notice'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/board/notice'),
      headers: <String, String>{
        "Content-Type": "application/json",
      },
    );
    if (response.statusCode == 200) {
      // 응답이 성공적이면, 데이터를 반환합니다.
      final jsonData = jsonDecode(utf8.decode(response.bodyBytes)) as List;
      final notices = jsonData.map((boardJson) => Notice.fromJson(boardJson)).toList();

      _noticeCount = notices.length; // 공지사항의 수를 저장

      return notices;
    } else {
      // 응답이 실패하면, 에러를 던집니다.
      throw Exception('Failed to load notice data');
    }
  }

  // Future<void> loadNotices() async {
  //   notice_list = await fetchData();
  // }

  @override
  void initState() {
    super.initState();
    _pageController = PageController();
    _timer = Timer.periodic(Duration(seconds: 2), (Timer timer) {
      if (_pageController.page!.round() == _noticeCount - 1) {
        _pageController.animateToPage(
          0,
          duration: Duration(milliseconds: 800),
          curve: Curves.easeInOut,
        );
      } else {
        _pageController.nextPage(
          duration: Duration(milliseconds: 800),
          curve: Curves.easeInOut,
        );
      }
    });
    // loadNotices();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 50,
      child:
          FutureBuilder<List<Notice>>(
            future: fetchData(),
            builder: (BuildContext context, AsyncSnapshot<List<Notice>> snapshot) {
              if (snapshot.hasData) {
                return PageView.builder(
                    controller: _pageController,
                    itemCount: snapshot.data!.length,
                    itemBuilder: (context, index) {
                      return GestureDetector(
                        onTap: () {
                          router.push(
                            RoutePath.noticeDetail,
                            extra: {
                              'title': snapshot.data![index].postTitle,
                              'content': snapshot.data![index].postContent
                            }
                          );
                        },
                        child: Center(child: Text('${snapshot.data![index].postTitle}')),
                      );
                    }
                );
              } else if (snapshot.hasError) {
                return Center(child: Text('Error: ${snapshot.error}'));
              }
              // 기본적으로 로딩 스피너를 보여줍니다.
              return CircularProgressIndicator();
            },
          ),
    );
  }

  @override
  void dispose() {
    _timer.cancel();
    _pageController.dispose();
    super.dispose();
  }

  @override
  bool get wantKeepAlive => true;
}
