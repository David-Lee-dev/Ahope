class CardItem {
  final String id;
  final int seq;

  CardItem({
    required this.id,
    required this.seq,
  });

  factory CardItem.fromJson(Map<String, dynamic> json) {
    return CardItem(
      id: json['id'],
      seq: json['seq'],
    );
  }
}
