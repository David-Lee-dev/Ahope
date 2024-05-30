import 'package:client/model/collection.model.dart';
import 'package:client/provider/collection.provider.dart';
import 'package:client/util/TopRightClipper.util.dart';
import 'package:client/widget/collectionCard.widget.dart';
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
                for (final data in cp.collection)
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
                  children: [
                    const SizedBox(height: 65),
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
                                CollcetionCard(data: data),
                            ]),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Container(
                  width: 55,
                  height: 55,
                  decoration: BoxDecoration(
                    color: const Color.fromRGBO(54, 68, 88, 0.7),
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: ElevatedButton(
                    onPressed: () {
                      _unselectCollection();
                      Navigator.pop(context);
                    },
                    style: ElevatedButton.styleFrom(
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(20)),
                      backgroundColor: Colors.transparent,
                      shadowColor: Colors.transparent,
                      padding: EdgeInsets.zero,
                    ),
                    child: const Icon(Icons.close),
                  ),
                ),
              ],
            ),
          ],
        );
      },
    );
  }
}
