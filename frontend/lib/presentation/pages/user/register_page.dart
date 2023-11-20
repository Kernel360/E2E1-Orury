import 'package:flutter/material.dart';
import 'package:orury/components/app_text_form_field.dart';
import 'package:orury/core/theme/constant/app_colors.dart';
import 'package:orury/global/messages/user/user_message.dart';
import 'package:orury/utils/extensions.dart';
import 'package:orury/values/app_constants.dart';

import 'package:http/http.dart' as http;
import 'package:flutter_dotenv/flutter_dotenv.dart';

import '../../routes/route_path.dart';
import '../../routes/routes.dart';
import 'dart:convert';

class RegisterPage extends StatefulWidget {
  const RegisterPage({super.key});

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final _formKey = GlobalKey<FormState>();
  TextEditingController nameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController confirmPasswordController = TextEditingController();

  // FocusNode confirmFocusNode = FocusNode();

  bool isObscure = true;
  bool isConfirmPasswordObscure = true;

  void signup() async {
    final response = await http.post(
      // Uri.http(dotenv.env['API_URL']!, '/api/user/signup'),
      Uri.http(dotenv.env['AWS_API_URL']!, '/api/user/signup'),
      headers: <String, String>{
        "Content-Type": "application/json",
      },
      body: jsonEncode({
        'nickname' : nameController.text,
        'email_addr': emailController.text,
        'password': passwordController.text,
      }),
    );
    // 정상 회원가입 시 로그인 처리.
    if (response.statusCode == 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Registration Complete!'),
        ),
      );
      nameController.clear();
      emailController.clear();
      passwordController.clear();
      confirmPasswordController.clear();
      router.go(RoutePath.loginPage);
    } else {
      // HTTP 요청이 실패했다면,
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('회원가입에 실패하였습니다.'),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final size = context.mediaQuerySize;
    return Scaffold(
      body: Form(
        key: _formKey,
        child: ListView(
          children: [
            Container(
              height: size.height * 0.24,
              width: double.infinity,
              padding: const EdgeInsets.all(20),
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    AppColors.lightBlue,
                    AppColors.blue,
                    AppColors.darkBlue,
                  ],
                ),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(
                      top: 15,
                    ),
                    child: IconButton(
                      onPressed: () => router.go(RoutePath.loginPage),
                      icon: const Icon(
                        Icons.arrow_back_ios,
                        color: Colors.white,
                      ),
                    ),
                  ),
                  Column(
                    children: [
                      Text(
                        '가입하기',
                        style: Theme.of(context).textTheme.titleLarge,
                      ),
                      const SizedBox(
                        height: 6,
                      ),
                      Text(
                        '간단한 정보만 입력해주세요',
                        style: Theme.of(context).textTheme.bodySmall,
                      ),
                    ],
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(
                horizontal: 20,
                vertical: 30,
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  AppTextFormField(
                    labelText: '이메일(ID)',
                    keyboardType: TextInputType.emailAddress,
                    textInputAction: TextInputAction.next,
                    onChanged: (_) => _formKey.currentState?.validate(),
                    validator: (value) {
                      return value!.isEmpty
                          ? UserMessage.enterYourEmail
                          : AppConstants.emailRegex.hasMatch(value)
                              ? null
                              : UserMessage.invalidEmailForm;
                    },
                    controller: emailController,
                  ),
                  AppTextFormField(
                    labelText: '닉네임',
                    autofocus: true,
                    keyboardType: TextInputType.name,
                    textInputAction: TextInputAction.next,
                    onChanged: (value) => _formKey.currentState?.validate(),
                    validator: (value) {
                      return value!.isEmpty
                          ? UserMessage.enterYourNickname
                          : value.length < 2
                          ? UserMessage.invalidNicknameForm
                          : null;
                    },
                    controller: nameController,
                  ),
                  AppTextFormField(
                    labelText: '비밀번호',
                    keyboardType: TextInputType.visiblePassword,
                    textInputAction: TextInputAction.next,
                    onChanged: (_) => _formKey.currentState?.validate(),
                    validator: (value) {
                      return value!.isEmpty
                          ? UserMessage.enterYourPassword
                          : AppConstants.passwordRegex.hasMatch(value)
                            ? null
                            : UserMessage.invalidPasswordForm;
                    },
                    controller: passwordController,
                    obscureText: isObscure,
                    // onEditingComplete: () {
                    //   FocusScope.of(context).unfocus();
                    //   FocusScope.of(context).requestFocus(confirmFocusNode);
                    // },
                    suffixIcon: Padding(
                      padding: const EdgeInsets.only(right: 15),
                      child: Focus(
                        /// If false,
                        ///
                        /// disable focus for all of this node's descendants
                        descendantsAreFocusable: false,

                        /// If false,
                        ///
                        /// make this widget's descendants un-traversable.
                        // descendantsAreTraversable: false,
                        child: IconButton(
                          onPressed: () => setState(() {
                            isObscure = !isObscure;
                          }),
                          style: ButtonStyle(
                            minimumSize: MaterialStateProperty.all(
                              const Size(48, 48),
                            ),
                          ),
                          icon: Icon(
                            isObscure
                                ? Icons.visibility_off_outlined
                                : Icons.visibility_outlined,
                            color: Colors.black,
                          ),
                        ),
                      ),
                    ),
                  ),
                  AppTextFormField(
                    labelText: '비밀번호 확인',
                    keyboardType: TextInputType.visiblePassword,
                    textInputAction: TextInputAction.done,
                    // focusNode: confirmFocusNode,
                    onChanged: (value) {
                      _formKey.currentState?.validate();
                    },
                    validator: (value) {
                      return value!.isEmpty
                          ? UserMessage.reEnterYourPassword
                          : AppConstants.passwordRegex.hasMatch(value)
                              ? passwordController.text ==
                                      confirmPasswordController.text
                                  ? null
                                  : UserMessage.misMatchPassword
                              : UserMessage.invalidPasswordForm;
                    },
                    controller: confirmPasswordController,
                    obscureText: isConfirmPasswordObscure,
                    suffixIcon: Padding(
                      padding: const EdgeInsets.only(right: 15),
                      child: Focus(
                        /// If false,
                        ///
                        /// disable focus for all of this node's descendants.
                        descendantsAreFocusable: false,

                        /// If false,
                        ///
                        /// make this widget's descendants un-traversable.
                        // descendantsAreTraversable: false,
                        child: IconButton(
                          onPressed: () {
                            setState(() {
                              isConfirmPasswordObscure =
                                  !isConfirmPasswordObscure;
                            });
                          },
                          style: ButtonStyle(
                            minimumSize: MaterialStateProperty.all(
                              const Size(48, 48),
                            ),
                          ),
                          icon: Icon(
                            isConfirmPasswordObscure
                                ? Icons.visibility_off_outlined
                                : Icons.visibility_outlined,
                            color: Colors.black,
                          ),
                        ),
                      ),
                    ),
                  ),
                  FilledButton(
                    onPressed: _formKey.currentState?.validate() ?? false ? signup : null,
                    style: const ButtonStyle().copyWith(
                      backgroundColor: MaterialStateProperty.all(
                        _formKey.currentState?.validate() ?? false
                            ? null
                            : Colors.grey.shade300,
                      ),
                    ),
                    child: const Text('Register'),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 25),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    '이미 계정이 있으신가요?',
                    style: Theme.of(context)
                        .textTheme
                        .bodySmall
                        ?.copyWith(color: Colors.black),
                  ),
                  TextButton(
                    onPressed: () {
                      router.go(RoutePath.loginPage);
                    },
                    style: Theme.of(context).textButtonTheme.style,
                    child: Text(
                      '로그인',
                      style: Theme.of(context).textTheme.bodySmall?.copyWith(
                            color: AppColors.positive,
                            fontWeight: FontWeight.bold,
                          ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
