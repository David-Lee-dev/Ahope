import 'package:client/provider/collection.provider.dart';
import 'package:client/provider/member.provider.dart';
import 'package:client/util/RequestManager.dart';
import 'package:client/widget/card/gachaCard.widget.dart';
import 'package:client/widget/gredientButton.widget.dart';
import 'package:flutter/material.dart';
import 'package:loading_animation_widget/loading_animation_widget.dart';
import 'package:provider/provider.dart';

class GachaScreen extends StatefulWidget {
  const GachaScreen({super.key});

  @override
  State<GachaScreen> createState() => _GachaScreenState();
}

class _GachaScreenState extends State<GachaScreen> {
  final bool _canDismiss = true;
  bool _isDrawing = false;

  @override
  void initState() {
    final mp = Provider.of<MemberProvider>(context, listen: false);
    final cp = Provider.of<CollectionProvider>(context, listen: false);

    RequestManager.requestCollection(mp.id).then((collection) {
      cp.setCollection(collection);
    });

    super.initState();
  }

  Future<dynamic> _drawCard() async {
    final mp = Provider.of<MemberProvider>(context, listen: false);
    final cp = Provider.of<CollectionProvider>(context, listen: false);

    final card = await RequestManager.requestDraw(mp.id);
    final collection = await RequestManager.requestCollection(mp.id);

    cp.setCollection(collection);

    return card;
  }

  void _turnOnDrawing() {
    setState(() {
      _isDrawing = true;
    });
  }

  void _turnOffDrawing() {
    setState(() {
      _isDrawing = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        GradientButton(
          borderRadius: BorderRadius.circular(35),
          width: 250,
          height: 70,
          startColor: const Color(0xff5c5ae4),
          endColor: const Color(0xffDE4981),
          onPressed: () {
            _turnOnDrawing();
            _drawCard()
                .then(
                  (card) {
                    showDialog(
                      context: context,
                      barrierDismissible: _canDismiss,
                      builder: (BuildContext context) => Dialog(
                        backgroundColor: Colors.transparent,
                        child: GachaCard(
                          onClose: () {},
                          imageUrl: card['imageUrl'],
                          seq: card['seq'],
                        ),
                      ),
                    );
                  },
                )
                .catchError((onError) {})
                .whenComplete(() {
                  _turnOffDrawing();
                });
          },
          child: _isDrawing
              ? LoadingAnimationWidget.waveDots(color: Colors.white70, size: 80)
              : const Text(
                  'Gacha',
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 32,
                  ),
                ),
        ),
      ],
    );
  }
}
