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

  static Future<String?> getMemberId() async {
    final prefs = await _getPrefs();
    return prefs.getString('id');
  }

  static Future<SharedPreferences> _getPrefs() async {
    return await SharedPreferences.getInstance();
  }
}
