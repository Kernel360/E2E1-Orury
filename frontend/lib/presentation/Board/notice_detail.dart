import 'package:flutter/material.dart';
import 'package:orury/core/theme/constant/app_colors.dart';

class NoticeDetail extends StatelessWidget {
  final String title;
  final String content;

  NoticeDetail(this.title, this.content, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('공지'),
        centerTitle: true,
        backgroundColor: AppColors.error,
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: TextStyle(
                fontSize: 30,
                fontWeight: FontWeight.bold,
              ),
            ),
            Divider(),
            Text(
              content,
              style: TextStyle(
                fontSize: 20,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
