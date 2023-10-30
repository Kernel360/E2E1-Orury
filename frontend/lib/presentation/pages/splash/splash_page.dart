import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:go_router/go_router.dart';
import 'package:orury/presentation/routes/route_path.dart';

// TODO 로그인 bloc
class SplashPage extends StatefulWidget {
  const SplashPage({super.key});

  @override
  State<SplashPage> createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {
  @override
  void initState(){
    super.initState();
    Timer(Duration(seconds: 2), () => context.go(RoutePath.home));
    }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // TODO 하드코딩된 값 변경할 것
      backgroundColor: const Color(0xFF737373),
      body: Center(
        child: Image(
          image: AssetImage('assets/svg/orury_splash.png'),
        ),
      ),
    );
  }
}
