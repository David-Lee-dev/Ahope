import 'package:flutter/material.dart';

typedef Collection = Map<String, Map<String, Map<String, dynamic>>>;

class CollectionProvider extends ChangeNotifier {
  Collection? _collection;

  Collection? get collection => _collection;

  void setCollection(Collection collection) {
    _collection = collection;

    notifyListeners();
  }
}
