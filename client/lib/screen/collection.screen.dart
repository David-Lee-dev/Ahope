import 'package:client/enum/errorCode.enum.dart';
import 'package:client/model/collection.model.dart';
import 'package:client/provider/collection.provider.dart';
import 'package:client/provider/member.provider.dart';
import 'package:client/util/HttpResponseException.util.dart';
import 'package:client/util/RequestManager.dart';
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

  void _refresh() {
    final mp = Provider.of<MemberProvider>(context, listen: false);
    final cp = Provider.of<CollectionProvider>(context, listen: false);

    if (cp.collection != null) return;

    RequestManager.requestCollection(mp.id)
        .then((collection) => cp.setCollection(collection))
        .catchError(
      (exception) {
        late Text alertTitle;
        late List<Text> alertContents;
        late List<TextButton> buttons;

        if (exception is HttpResponseException) {
          if (exception.code == ErrorCode.serverError) {
            alertTitle = const Text('새로고침에 실패했습니다.');
            alertContents = [
              const Text('서버와 통신할 수 없습니다.'),
              const Text('다시 시도해주세요.')
            ];
            buttons = [
              TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('확인'))
            ];
          } else {
            alertTitle = const Text('새로고침에 실패했습니다.');
            alertContents = [const Text('다시 시도해주세요.')];
            buttons = [
              TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('확인'))
            ];
          }
        } else {
          alertTitle = const Text('새로고침에 실패했습니다.');
          alertContents = [
            const Text('알 수 없는 오류가 발생했습니다.'),
            const Text('빠른 시일 내로 복구하겠습니다. 사용에 불편을 드려 죄송합니다.')
          ];
          buttons = [
            TextButton(
                onPressed: () => Navigator.pop(context),
                child: const Text('확인'))
          ];
        }

        WidgetsBinding.instance.addPostFrameCallback(
          (_) {
            showDialog(
              context: context,
              barrierDismissible: false,
              builder: (BuildContext context) {
                return AlertDialog(
                  title: alertTitle,
                  content: SingleChildScrollView(
                      child: ListBody(children: alertContents)),
                  actions: buttons,
                );
              },
            );
          },
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<CollectionProvider>(builder: (context, cp, child) {
      return Scaffold(
        body: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 15),
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const SizedBox(height: 40),
                Stack(children: [
                  const Center(
                    heightFactor: 1.25,
                    child: Text(
                      'Collection',
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        fontSize: 28,
                      ),
                    ),
                  ),
                  Positioned(
                    right: 0,
                    child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                            shape: const CircleBorder(),
                            backgroundColor: const Color(0xff3D4A5D)),
                        onPressed: _refresh,
                        child: const Icon(Icons.refresh)),
                  ),
                ]),
                const SizedBox(height: 30),
                if (cp.collection != null)
                  for (final data in cp.collection!)
                    CollectionProgressBar(
                      category: data.category.split('_')[1],
                      rate: _getCollectingRate(data),
                      onTap: () {
                        _selectCollection(data);
                        _showDrawer(context);
                      },
                    ),
              ],
            ),
          ),
        ),
      );
    });
  }

  Future<void> _showDrawer(BuildContext context) {
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
                  color: Color(0xF1364458),
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
                                    if (data.cards == null) return;

                                    showDialog(
                                      context: context,
                                      builder: (BuildContext context) => Dialog(
                                        insetPadding:
                                            const EdgeInsets.symmetric(
                                                horizontal: 0),
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
