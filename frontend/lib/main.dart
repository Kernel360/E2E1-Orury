import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:orury/presentation/pages/home/home_page.dart';
import 'package:orury/presentation/pages/splash/splash_page.dart';
import 'package:orury/presentation/routes/routes.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: router,
    );
  }
}
