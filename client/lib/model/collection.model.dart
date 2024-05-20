import 'package:client/model/metaData.model.dart';

class Collection {
  List<MetaData> metaDataList;
  String category;

  Collection({required this.metaDataList, required this.category});

  factory Collection.fromJson(Map<String, dynamic> json) {
    var metaDataListJson = json['metaDataList'] as List;
    List<MetaData> metaDataList =
        metaDataListJson.map((item) => MetaData.fromJson(item)).toList();

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
