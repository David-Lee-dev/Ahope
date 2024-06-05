import 'package:client/provider/member.provider.dart';
import 'package:client/util/DiskStorageManager.util.dart';
import 'package:client/util/RequestManager.dart';
import 'package:client/widget/gredientButton.widget.dart';
import 'package:flutter/material.dart';
import 'package:email_validator/email_validator.dart';
import 'package:provider/provider.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController = TextEditingController();

  void _login() {
    if (_formKey.currentState?.validate() ?? false) {
      RequestManager.requestJoin(_emailController.text).then(
        (member) {
          DiskStorageManager.setMemberData(member);
          Provider.of<MemberProvider>(context, listen: false).setAll(member);
        },
      ).catchError((error) {
        throw error;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              TextFormField(
                controller: _emailController,
                keyboardType: TextInputType.emailAddress,
                style: const TextStyle(color: Colors.white),
                decoration: const InputDecoration(
                  labelText: 'Email',
                  labelStyle: TextStyle(color: Colors.white),
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.white),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.white),
                  ),
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter your email';
                  } else if (!EmailValidator.validate(value)) {
                    return 'Please enter a valid email';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20),
              GradientButton(
                width: 80,
                height: 40,
                onPressed: _login,
                startColor: const Color(0xff5c5ae4),
                endColor: const Color(0xffDE4981),
                borderRadius: BorderRadius.circular(20),
                child: const Text('Join'),
              )
            ],
          ),
        ),
      ),
    );
  }
}
