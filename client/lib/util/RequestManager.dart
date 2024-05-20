import 'dart:convert';
import 'package:client/model/member.model.dart';
import 'package:client/model/collection.model.dart';
import 'package:http/http.dart' as http;

typedef Res = http.Response;

class RequestManager {
  static const String _baseUrl = "http://localhost:8080";

  static Future<Member> requestJoin(String email) async {
    Res response = await http.post(
      _getUrl('/api/member'),
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

  static Future<List<Collection>> requestCollection(String? id) async {
    if (id == null) {
      throw Error();
    }

    Res response = await http.get(
      _getUrl('/api/card', {'memberId': id}),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    return (jsonDecode(response.body) as List)
        .map((data) => Collection.fromJson(data))
        .toList();
  }

  static Uri _getUrl(String path, [Map<String, String>? queryParams]) {
    Uri uri = Uri.parse(_baseUrl).resolve(path);

    if (queryParams != null) {
      uri = uri.replace(queryParameters: queryParams);
    }

    return uri;
  }
}
