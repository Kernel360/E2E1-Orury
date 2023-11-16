import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:orury/core/theme/theme_data.dart';
import 'package:orury/presentation/pages/user/login_page.dart';
import 'package:orury/presentation/pages/user/register_page.dart';
import 'package:orury/presentation/routes/routes.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:orury/values/app_constants.dart';
import 'package:orury/values/app_routes.dart';
import 'package:orury/values/app_theme.dart';

void main() async{
  await dotenv.load(fileName: 'lib/global/config/.env');
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setSystemUIOverlayStyle(
    const SystemUiOverlayStyle(
      statusBarIconBrightness: Brightness.light,
    ),
  );
  SystemChrome.setPreferredOrientations(
    [DeviceOrientation.portraitUp],
  ).then(
        (_) => runApp(
      const MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});


  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    // return MaterialApp.router(
    //   routerConfig: router,
    // );
    Theme.of(context);
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
      title: 'Login and Register UI',
      theme: AppTheme.themeData,
      routerConfig: router,
      // initialRoute: AppRoutes.loginScreen,
      // navigatorKey: AppConstants.navigationKey,
      // routes: {
      //   AppRoutes.loginScreen: (context) => const LoginPage(),
      //   AppRoutes.registerScreen: (context) => const RegisterPage(),
      // },
    );
  }
}