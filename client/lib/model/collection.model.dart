class CardItem {
  String id;
  int seq;

  CardItem({required this.id, required this.seq});

  factory CardItem.fromJson(Map<String, dynamic> json) {
    return CardItem(
      id: json['id'],
      seq: json['seq'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'seq': seq,
    };
  }
}

class MetaDataList {
  List<CardItem>? cards;
  String imageUrl;
  int grade;
  int weight;
  String id;
  String category;

  MetaDataList({
    this.cards,
    required this.imageUrl,
    required this.grade,
    required this.weight,
    required this.id,
    required this.category,
  });

  factory MetaDataList.fromJson(Map<String, dynamic> json) {
    var cardsJson = json['cards'] as List?;
    List<CardItem>? cardsList =
        cardsJson?.map((card) => CardItem.fromJson(card)).toList();

    return MetaDataList(
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

class Collection {
  List<MetaDataList> metaDataList;
  String category;

  Collection({required this.metaDataList, required this.category});

  factory Collection.fromJson(Map<String, dynamic> json) {
    var metaDataListJson = json['metaDataList'] as List;
    List<MetaDataList> metaDataList =
        metaDataListJson.map((item) => MetaDataList.fromJson(item)).toList();

    return Collection(
      metaDataList: metaDataList,
      category: json['category'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'metaDataList': metaDataList.map((item) => item.toJson()).toList(),
      'category': category,
    };
  }
}
