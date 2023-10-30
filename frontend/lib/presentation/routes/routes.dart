import 'package:go_router/go_router.dart';
import 'package:orury/presentation/Board/board_post.dart';
import 'package:orury/presentation/main/main_screen.dart';
import 'package:orury/presentation/pages/user/user_login.dart';
import 'package:orury/presentation/routes/route_path.dart';

import '../pages/splash/splash_page.dart';

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
      path: RoutePath.user_login,
      name: 'user_login',
      builder: (context, state) => const UserLogin(),
    ),
    GoRoute(
      path: RoutePath.board_post,
      name: 'board_post',
      builder: (context, state) => BoardPost(),
    ),
  ],
  initialLocation: '/splash',
);