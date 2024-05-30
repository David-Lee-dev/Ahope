class CardItem {
  String id;
  int seq;

  CardItem({required this.id, required this.seq});

  factory CardItem.fromJson(Map<String, dynamic> json) {
    return CardItem(
      id: json['id'],
      seq: int.parse(json['seq']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'seq': seq,
    };
  }
}
