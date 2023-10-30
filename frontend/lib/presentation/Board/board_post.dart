import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../routes/route_path.dart';
import '../routes/routes.dart';

class BoardPost extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        final goRouter = GoRouter.of(context);

        // Navigator의 상태를 확인
        final navigatorState = Navigator.of(context);
        if (!navigatorState.canPop()) {
          // Navigator 스택이 비어있는 경우 main_screen으로 돌아가기
          goRouter.go(RoutePath.main);
        } else {
          // Navigator 스택에 다른 화면이 있는 경우 뒤로가기
          navigatorState.pop();
        }
        return false; // false로 반환하여 뒤로가기 버튼을 처리
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text('게시글 작성'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('게시글 작성 화면'),
            ],
          ),
        ),
      ),
    );
  }
}