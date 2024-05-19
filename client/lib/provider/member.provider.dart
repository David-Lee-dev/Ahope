import 'package:flutter/material.dart';

class MemberProvider extends ChangeNotifier {
  String? _memberId;

  String? get id => _memberId;

  void setId(String id) {
    _memberId = id;

    notifyListeners();
  }
}
