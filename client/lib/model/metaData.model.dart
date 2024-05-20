import 'package:client/model/cardItem.model.dart';

class MetaData {
  final String id;
  final List<CardItem> cards;
  final String imageUrl;
  final int grade;
  final int weight;
  final String category;

  MetaData({
    required this.id,
    required this.cards,
    required this.imageUrl,
    required this.grade,
    required this.weight,
    required this.category,
  });

  factory MetaData.fromJson(Map<String, dynamic> json) {
    return MetaData(
      id: json['id'],
      cards: List<CardItem>.from(json['cards']),
      imageUrl: json['imageUrl'],
      grade: json['grade'],
      weight: json['weight'],
      category: json['category'],
    );
  }
}
