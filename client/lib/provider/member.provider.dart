import 'package:flutter/material.dart';

class MemberProvider extends ChangeNotifier {
  String? _memberId;
  String? _email;
  int? _lastGachaTimestamp;
  int? _remainTicket;

  String? get id => _memberId;
  String? get email => _email;
  int? get lastGachaTimestamp => _lastGachaTimestamp;
  int? get remainTicket => _remainTicket;

  void setId(String id) {
    _memberId = id;

    notifyListeners();
  }

  void setEmail(String email) {
    _email = id;

    notifyListeners();
  }

  void setLastGachaTimestamp(int? lastGachaTimestamp) {
    _lastGachaTimestamp = lastGachaTimestamp;

    notifyListeners();
  }

  void setRemainTicket(int id) {
    _remainTicket = remainTicket;

    notifyListeners();
  }
}
