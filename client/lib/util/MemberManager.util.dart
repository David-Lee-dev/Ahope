import 'dart:convert';

import 'package:client/model/member.model.dart';
import 'package:http/http.dart' as http;

typedef Res = http.Response;

class MemberManager {
  static const String _baseUrl = "http://localhost:8080/api";

  static Future<Member> requestJoin(String email) async {
    Res response = await http.post(
      Uri.parse(_getUrl('/member')),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        <String, String>{
          'email': email,
        },
      ),
    );

    return Member.fromJson(jsonDecode(response.body));
  }

  static _getUrl(String path) {
    return _baseUrl + path;
  }
}
