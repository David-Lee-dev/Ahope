import 'package:client/collection/widget/collectionCard.widget.dart';
import 'package:flutter/material.dart';

class CollectionPage extends StatelessWidget {
  final List<int> _dummyNum = <int>[
    1,
    2,
    3,
    4,
    5,
    6,
    6,
  ];

  CollectionPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 15),
        child: Column(
          children: [
            const SizedBox(height: 50),
            Expanded(
              child: GridView.count(
                crossAxisCount: 2,
                mainAxisSpacing: 5,
                crossAxisSpacing: 10,
                childAspectRatio: 0.8,
                children: List.generate(
                  _dummyNum.length,
                  (index) => CollcetionCard(
                    onClose: () {},
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
