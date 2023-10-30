import 'package:flutter/material.dart';

class UserPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('프로필 페이지')),
      body: Center(
        child: Text('이곳은 프로필 페이지입니다.'),
      ),
    );
  }
}