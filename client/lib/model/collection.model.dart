import 'package:client/model/metaData.model.dart';

class Collection {
  List<Metadata> metadataList;
  String category;

  Collection({required this.metadataList, required this.category});

  factory Collection.fromJson(Map<String, dynamic> json) {
    final metadataListJson = json['metadataList'] as List;
    List<Metadata> metadataList =
        metadataListJson.map((item) => Metadata.fromJson(item)).toList();

    return Collection(
      metadataList: metadataList,
      category: json['category'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'metadataList': metadataList.map((item) => item.toJson()).toList(),
      'category': category,
    };
  }
}
