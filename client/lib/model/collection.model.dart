import 'package:client/util/alias.dart';

class MetaData {
  final List<CardUUID> cards;
  final String imageUrl;
  final int grade;
  final int count;
  final int weight;

  MetaData({
    required this.cards,
    required this.imageUrl,
    required this.grade,
    required this.count,
    required this.weight,
  });

  factory MetaData.fromJson(Map<String, dynamic> json) {
    return MetaData(
      cards: List<CardUUID>.from(json['cards']),
      imageUrl: json['imageUrl'],
      grade: json['grade'],
      count: json['count'],
      weight: json['weight'],
    );
  }
}

class Category {
  final Map<MetaDataUUID, MetaData>? items;

  Category({
    required this.items,
  });

  factory Category.fromJson(Map<String, dynamic> json) {
    Map<String, MetaData> items = {};

    json.forEach((key, value) {
      if (value != null) {
        items[key] = MetaData.fromJson(value);
      }
    });

    return Category(items: items);
  }
}

class Collection {
  final Map<CategoryName, Category> categories;

  Collection({
    required this.categories,
  });

  factory Collection.fromJson(Map<String, dynamic> json) {
    Map<String, Category> categories = {};

    json.forEach((key, value) {
      categories[key] = Category.fromJson(value);
    });

    return Collection(categories: categories);
  }
}
