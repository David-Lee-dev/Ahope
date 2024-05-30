class Member {
  final String id;
  final String email;
  final int? lastGachaTimestamp;
  final int remainTicket;

  Member({
    required this.id,
    required this.email,
    required this.lastGachaTimestamp,
    required this.remainTicket,
  });

  factory Member.fromJson(Map<String, dynamic> json) {
    return Member(
      id: json['id'] as String,
      email: json['email'] as String,
      lastGachaTimestamp: json['lastGachaTimestamp'] == 'Null'
          ? int.parse(json['lastGachaTimestamp'])
          : null,
      remainTicket: json['remainTicket'] as int,
    );
  }
}
