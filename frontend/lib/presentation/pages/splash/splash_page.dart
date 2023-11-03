import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:go_router/go_router.dart';
import 'package:orury/core/theme/constant/app_icons.dart';
import 'package:orury/presentation/routes/route_path.dart';

// TODO 로그인 bloc
class SplashPage extends StatefulWidget {
  const SplashPage({super.key});

  @override
  State<SplashPage> createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {
  @override
  void initState() {
    super.initState();
    Timer(
      Duration(seconds: 2), () => context.go(RoutePath.loginPage),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF737373),
      body: Center(
        child: Image(
        image: AssetImage(AppIcons.mainLogo),
        ),
        //   child: SvgPicture.asset('assets/svg/orury_splash.svg'),
      ),
    );
  }
}
