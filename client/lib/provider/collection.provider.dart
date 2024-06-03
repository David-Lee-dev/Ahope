import 'package:client/model/collection.model.dart';
import 'package:flutter/material.dart';

class CollectionProvider extends ChangeNotifier {
  List<Collection>? _collection;

  List<Collection>? get collection => _collection;

  void setCollection(List<Collection> collection) {
    _collection = collection;

    notifyListeners();
  }
}
