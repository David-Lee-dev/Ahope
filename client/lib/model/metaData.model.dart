import 'package:client/model/cardItem.model.dart';

class Metadata {
  List<CardItem>? cards;
  String imageUrl;
  int grade;
  int weight;
  String id;
  String category;

  Metadata({
    this.cards,
    required this.imageUrl,
    required this.grade,
    required this.weight,
    required this.id,
    required this.category,
  });

  factory Metadata.fromJson(Map<String, dynamic> json) {
    var cardsJson = json['cards'] as List?;
    List<CardItem>? cardsList =
        cardsJson?.map((card) => CardItem.fromJson(card)).toList();

    return Metadata(
      cards: cardsList,
      imageUrl: json['imageUrl'],
      grade: json['grade'],
      weight: json['weight'],
      id: json['id'],
      category: json['category'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'cards': cards?.map((card) => card.toJson()).toList(),
      'imageUrl': imageUrl,
      'grade': grade,
      'weight': weight,
      'id': id,
      'category': category,
    };
  }
}
