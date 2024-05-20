import 'package:client/model/collection.model.dart';
import 'package:flutter/material.dart';

class CollectionProvider extends ChangeNotifier {
  Collection? _collection;

  Collection? get collection => _collection;

  void setCollection(Collection collection) {
    _collection = collection;

    notifyListeners();
  }
}
