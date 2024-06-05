import 'package:client/model/member.model.dart';
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

  void setAll(Member member) {
    _memberId = member.id;
    _email = member.email;
    _lastGachaTimestamp = member.lastGachaTimestamp;
    _remainTicket = member.remainTicket;

    notifyListeners();
  }

  void setId(String id) {
    _memberId = id;

    notifyListeners();
  }

  void setEmail(String email) {
    _email = email;

    notifyListeners();
  }

  void setLastGachaTimestamp(int? lastGachaTimestamp) {
    _lastGachaTimestamp = lastGachaTimestamp;

    notifyListeners();
  }

  void setRemainTicket(int remainTicket) {
    _remainTicket = remainTicket;

    notifyListeners();
  }

  void reset() {
    _memberId = null;
    _email = null;
    _lastGachaTimestamp = null;
    _remainTicket = null;

    notifyListeners();
  }
}
