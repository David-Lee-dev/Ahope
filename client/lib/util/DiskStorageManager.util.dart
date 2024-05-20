import 'package:client/model/member.model.dart';
import 'package:shared_preferences/shared_preferences.dart';

class DiskStorageManager {
  static Future<void> setData(String key, dynamic value) async {
    final prefs = await _getPrefs();

    if (value is String) {
      prefs.setString(key, value);
    } else if (value is int) {
      prefs.setInt(key, value);
    } else if (value is double) {
      prefs.setDouble(key, value);
    } else if (value is bool) {
      prefs.setBool(key, value);
    } else if (value is List<String>) {
      prefs.setStringList(key, value);
    } else {
      throw ArgumentError("Unsupported type: ${value.runtimeType}");
    }
  }

  static Future<Member?> getMember() async {
    final String? id = await getMemberId();
    final String? email = await getMemberEmail();
    final int? lastGachaTimestamp = await getLastGachaTimestamp();
    final int? remainTicket = await getRemainTicket();

    if (id != null && email != null && remainTicket != null) {
      return Member(
        id: id,
        email: email,
        lastGachaTimestamp: lastGachaTimestamp,
        remainTicket: remainTicket,
      );
    } else {
      return null;
    }
  }

  static Future<String?> getMemberId() async {
    final prefs = await _getPrefs();
    return prefs.getString('id');
  }

  static Future<String?> getMemberEmail() async {
    final prefs = await _getPrefs();
    return prefs.getString('email');
  }

  static Future<int?> getLastGachaTimestamp() async {
    final prefs = await _getPrefs();
    return prefs.getInt('lastGachaTimestamp');
  }

  static Future<int?> getRemainTicket() async {
    final prefs = await _getPrefs();
    return prefs.getInt('remainTicket');
  }

  static Future<SharedPreferences> _getPrefs() async {
    return await SharedPreferences.getInstance();
  }
}
