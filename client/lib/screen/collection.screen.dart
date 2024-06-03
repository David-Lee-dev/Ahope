import 'package:client/model/collection.model.dart';
import 'package:client/provider/collection.provider.dart';
import 'package:client/util/TopRightClipper.util.dart';
import 'package:client/widget/card/collectionCard.widget.dart';
import 'package:client/widget/cardSlider.widget.dart';
import 'package:client/widget/collectionProgressBar.widget.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class CollectionScreen extends StatefulWidget {
  const CollectionScreen({super.key});

  @override
  State<CollectionScreen> createState() => _CollectionScreenState();
}

class _CollectionScreenState extends State<CollectionScreen> {
  Collection? _selectedCollection;

  void _selectCollection(Collection collection) {
    setState(() {
      _selectedCollection = collection;
    });
  }

  void _unselectCollection() {
    setState(() {
      _selectedCollection = null;
    });
  }

  double _getCollectingRate(Collection collection) {
    final totalCount = collection.metadataList.length;
    final collectedCount = collection.metadataList
        .where(
            (metadata) => metadata.cards != null && metadata.cards!.isNotEmpty)
        .length;

    return collectedCount / totalCount;
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<CollectionProvider>(builder: (context, cp, child) {
      return Scaffold(
        body: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 15),
          child: SingleChildScrollView(
            child: Column(
              children: [
                const SizedBox(height: 70),
                if (cp.collection != null)
                  for (final data in cp.collection!)
                    CollectionProgressBar(
                      category: data.category.split('_')[1],
                      rate: _getCollectingRate(data),
                      onTap: () {
                        _selectCollection(data);
                        showDrawer(context);
                      },
                    ),
              ],
            ),
          ),
        ),
      );
    });
  }

  Future<void> showDrawer(BuildContext context) {
    return showModalBottomSheet<void>(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (BuildContext context) {
        return Stack(
          children: [
            ClipPath(
              clipper: TopRightClipper(),
              child: Container(
                height: MediaQuery.of(context).size.height * 0.8,
                decoration: const BoxDecoration(
                  color: Color.fromRGBO(54, 68, 88, 0.95),
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(20),
                    topRight: Radius.circular(20),
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(
                      height: 65,
                      child: Padding(
                        padding: const EdgeInsets.only(top: 10, left: 15),
                        child: Text(
                          _selectedCollection!.category.split('_')[1],
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 28,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ),
                    Expanded(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 10,
                        ),
                        child: GridView.count(
                          crossAxisCount: 3,
                          mainAxisSpacing: 5,
                          crossAxisSpacing: 10,
                          childAspectRatio: 0.78,
                          children: [
                            for (final data
                                in _selectedCollection!.metadataList)
                              GestureDetector(
                                  onTap: () {
                                    showDialog(
                                      context: context,
                                      builder: (BuildContext context) => Dialog(
                                        insetPadding: EdgeInsets.zero,
                                        backgroundColor: Colors.transparent,
                                        child: CardSlider(
                                          cards: data.cards!,
                                          imageUrl: data.imageUrl,
                                        ),
                                      ),
                                    );
                                  },
                                  child: CollcetionCard(data: data)),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Positioned(
              right: 0,
              child: InkWell(
                onTap: () {
                  _unselectCollection();
                  Navigator.pop(context);
                },
                child: Container(
                  width: 55,
                  height: 55,
                  decoration: BoxDecoration(
                    color: const Color.fromRGBO(54, 68, 88, 1),
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: const Icon(Icons.close, color: Colors.white),
                ),
              ),
            ),
          ],
        );
      },
    );
  }
}
