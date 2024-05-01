import 'dart:io';

import 'package:client/widget/bundle.widget.dart';
import 'package:flutter/material.dart';

class CollectionPage extends StatelessWidget {
  final List<int> _dummyNum = <int>[
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    1,
    2,
    3,
    4,
    5,
    6,
    7
  ];

  CollectionPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GridView.count(
        crossAxisCount: 3,
        children: List.generate(_dummyNum.length, (index) => const Bundle()),
      ),
    );
  }
}
