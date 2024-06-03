import 'dart:convert';
import 'package:client/enum/requestMethod.enum.dart';
import 'package:client/model/member.model.dart';
import 'package:client/model/collection.model.dart';
import 'package:client/util/HttpResponseException.util.dart';
import 'package:http/http.dart' as http;

typedef Res = http.Response;

class RequestManager {
  static const String _baseUrl = "http://localhost:3000";

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

    if (response.statusCode == 201) {
      return Member.fromJson(jsonDecode(response.body));
    } else {
      throw HttpResponseException(RequestMethod.post, response.statusCode);
    }
  }

  static Future<List<Collection>> requestCollection(String? id) async {
    if (id == null) {
      throw Error();
    }

    Res response = await http.get(
      _getUrl('/api/member/$id/card'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if (response.statusCode == 200) {
      return (jsonDecode(response.body) as List)
          .map((data) => Collection.fromJson(data))
          .toList();
    } else {
      throw HttpResponseException(RequestMethod.get, response.statusCode);
    }
  }

  static Future<dynamic> requestDraw(String? id) async {
    if (id == null) {
      throw Error();
    }

    final Res response = await http.post(
      _getUrl('/api/member/$id/card'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if (response.statusCode == 201) {
      final data = jsonDecode(response.body);

      return {
        'seq': data['seq'],
        'imageUrl': data['metadata']['imageUrl'],
      };
    } else {
      throw HttpResponseException(RequestMethod.post, response.statusCode);
    }
  }

  static Future<Member> requestMember(String? id) async {
    if (id == null) {
      throw Error();
    }

    Res response = await http.get(
      _getUrl('/api/member/$id'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if (response.statusCode == 200) {
      return Member.fromJson(jsonDecode(response.body));
    } else {
      throw HttpResponseException(RequestMethod.post, response.statusCode);
    }
  }

  static Uri _getUrl(String path, [Map<String, String>? queryParams]) {
    Uri uri = Uri.parse(_baseUrl).resolve(path);

    if (queryParams != null) {
      uri = uri.replace(queryParameters: queryParams);
    }

    return uri;
  }
}
