import 'package:go_router/go_router.dart';
import 'package:orury/presentation/Board/post_detail.dart';
import 'package:orury/presentation/main/main_screen.dart';
import 'package:orury/presentation/pages/user/login_page.dart';
import 'package:orury/presentation/routes/route_path.dart';

import '../Board/post.dart';
import '../Board/post_create.dart';
import '../Board/post_update.dart';
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
      path: RoutePath.postCreate,
      name: 'post_create',
      builder: (context, state) {
        final boardId = state.extra as int?;
        return PostCreate(boardId ?? 0);
      },
    ),

    GoRoute(
      path: RoutePath.postUpdate,
      name: 'post_update',
      builder: (context, state) {
        final post = state.extra as Post?;
        return PostUpdate(data: post);
      },
    ),

    GoRoute(
      path: RoutePath.registerPage,
      name: 'register_page',
      builder: (context, state) => const RegisterPage(),
    ),

    GoRoute(
      path: RoutePath.postDetail,
      name: 'post_detail',
      builder: (context, state) {
        final postId = state.extra as int?;
        return PostDetail(postId ?? 0);
      },
    ),

  ],
  initialLocation: '/splash',
);