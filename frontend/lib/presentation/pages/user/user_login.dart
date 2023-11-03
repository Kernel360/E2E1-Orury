import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/presentation/routes/route_path.dart';
import 'package:orury/presentation/routes/routes.dart';

class UserLogin extends StatefulWidget {
  const UserLogin({super.key});

  @override
  State<UserLogin> createState() => _UserLoginState();
}

class _UserLoginState extends State<UserLogin> {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  String? emailError;
  String? passwordError;

  void validateForm() {
    final isValid = _formKey.currentState!.validate();
    if (isValid) {
      // 이메일 및 비밀번호가 유효한 경우 로그인 로직을 수행

      router.go(RoutePath.main);
    } else {
      // 유효성 검사에 실패한 경우 토스트 메시지 표시
      Fluttertoast.showToast(
        msg: "이메일 및 비밀번호를 입력해주세요.",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        backgroundColor: AppColors.oruryMain,
        textColor: AppColors.orurySub,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        backgroundColor: AppColors.orurySub,
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              'Orury!',
              style: TextStyle(
                  fontSize: 35,
                  color: AppColors.oruryMain,
                  fontWeight: FontWeight.bold),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 30),
              child: Form(

                key: _formKey,

                child: Column(
                  children: [

                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 15),
                      child: TextFormField(
                        // 로그인
                        controller: emailController,
                        keyboardType: TextInputType.emailAddress,
                        decoration: InputDecoration(
                          labelText: '이메일',
                          hintText: '이메일 주소(ID)를 입력해주세요.',
                          prefixIcon: Icon(Icons.email),
                          border: OutlineInputBorder(),
                        ),
                        onChanged: (String value) {},
                        validator: (value) {
                          return value!.isEmpty ? '이메일을 입력해주세요.' : null;
                        },
                      ),
                    ),

                    SizedBox(
                      height: 30,
                    ),

                    // 패스워드
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 15),
                      child: TextFormField(
                        keyboardType: TextInputType.visiblePassword,
                        obscureText: true,
                        decoration: InputDecoration(
                          labelText: '비밀번호',
                          hintText: '비밀번호를 입력해주세요.',
                          prefixIcon: Icon(Icons.password),
                          border: OutlineInputBorder(),
                        ),
                        onChanged: (String value) {},
                        validator: (value) {
                          return value!.isEmpty ? '비밀번호를 입력해주세요.' : null;
                        },
                      ),
                    ),

                    SizedBox(
                      height: 30,
                    ),

                    // 로그인 버튼
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 35),
                      child: MaterialButton(
                        onPressed: validateForm,
                        child: Text('로그인'),
                        color: AppColors.oruryMain,
                        textColor: AppColors.orurySub,
                      ),
                    ),

                    SizedBox(
                      height: 10,
                    ),

                    // 회원가입 버튼
                    TextButton(
                      onPressed: () {},
                      child: Text(
                        '회원가입',
                        style: TextStyle(color: AppColors.oruryMain),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
