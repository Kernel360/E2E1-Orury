import 'package:go_router/go_router.dart';
import 'package:orury/presentation/main/main_screen.dart';
import 'package:orury/presentation/pages/user/login_page.dart';
import 'package:orury/presentation/routes/route_path.dart';

import '../pages/splash/splash_page.dart';
import '../pages/user/register_page.dart';

final GoRouter router = GoRouter(
  routes: [
    GoRoute(
      path: RoutePath.splash,
      name: 'splash',
      builder: (context, state) => const SplashPage(),
    ),
    GoRoute(
      path: RoutePath.main,
      name: 'main',
      builder: (context, state) => MainScreen(),
    ),
    GoRoute(
      path: RoutePath.loginPage,
      name: 'login_page',
      builder: (context, state) => const LoginPage(),
    ),
    GoRoute(
      path: RoutePath.board_post,
      name: 'board_post',
      builder: (context, state) => BoardPost(),
    ),
    GoRoute(
      path: RoutePath.registerPage,
      name: 'register_page',
      builder: (context, state) => const RegisterPage(),
    ),
  ],
  initialLocation: '/splash',
);