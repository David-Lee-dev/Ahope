import 'dart:async';

import 'package:client/enum/requestMethod.enum.dart';
import 'package:client/provider/collection.provider.dart';
import 'package:client/provider/member.provider.dart';
import 'package:client/util/AlertDialogManager.util.dart';
import 'package:client/util/HttpResponseException.util.dart';
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
  bool _isDrawing = false;
  int _remainTimeToGetTicket = 30 * 60 * 1000;
  int _remainTicket = 0;
  Timer? _timer;

  @override
  void initState() {
    super.initState();

    final mp = Provider.of<MemberProvider>(context, listen: false);
    final cp = Provider.of<CollectionProvider>(context, listen: false);

    RequestManager.requestMember(mp.id).then((member) {
      mp.setAll(member);

      final nowTimestamp = DateTime.now().millisecondsSinceEpoch;
      if (mp.lastGachaTimestamp != null && mp.remainTicket != null) {
        setState(() {
          _remainTimeToGetTicket = mp.lastGachaTimestamp!;
          _remainTicket = mp.remainTicket! - 1;

          while (nowTimestamp > _remainTimeToGetTicket) {
            _remainTicket++;
            _remainTimeToGetTicket += 30 * 60 * 1000;
          }

          _remainTimeToGetTicket -= nowTimestamp;
        });

        startTimer();
      }
    }).catchError((exception) =>
        AlertDialogManager.handleFetchMember(exception, context));

    if (cp.collection == null) {
      RequestManager.requestCollection(mp.id)
          .then((collection) => cp.setCollection(collection))
          .catchError((exception) =>
              AlertDialogManager.handleFetchCollectionError(
                  exception, context));
    }
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
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

  void startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        if (_remainTimeToGetTicket > 0) {
          _remainTimeToGetTicket -= 1000;
        } else {
          updateTicketAndRestartTimer();
        }
      });
    });
  }

  void updateTicketAndRestartTimer() {
    setState(() {
      _remainTicket++;
      _remainTimeToGetTicket = 30 * 60 * 1000;
    });
  }

  String formatDuration(int milliseconds) {
    final duration = Duration(milliseconds: milliseconds);
    final minutes = (duration.inMinutes % 60).toString().padLeft(2, '0');
    final seconds = (duration.inSeconds % 60).toString().padLeft(2, '0');
    return '$minutes:$seconds';
  }

  @override
  Widget build(BuildContext context) {
    Future<dynamic> drawCard() async {
      if (_remainTicket <= 0) return null;

      final mp = Provider.of<MemberProvider>(context, listen: false);
      final cp = Provider.of<CollectionProvider>(context, listen: false);

      final card = await RequestManager.requestDraw(mp.id);
      final collection = await RequestManager.requestCollection(mp.id);

      cp.setCollection(collection);

      setState(() {
        _remainTicket--;
      });

      return card;
    }

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
            drawCard().then(
              (card) {
                if (card != null) {
                  showDialog(
                    context: context,
                    barrierDismissible: false,
                    builder: (BuildContext context) => Dialog(
                      backgroundColor: Colors.transparent,
                      child: GachaCard(
                        imageUrl: card['imageUrl'],
                        seq: card['seq'],
                      ),
                    ),
                  );
                }
              },
            ).catchError((exception) {
              if (exception is HttpResponseException) {
                if (exception.method == RequestMethod.post) {
                  AlertDialogManager.handleDrawCard(exception, context);
                }

                if (exception.method == RequestMethod.get) {
                  AlertDialogManager.handleFetchMember(exception, context);
                }
              }
            }).whenComplete(() {
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
        const SizedBox(height: 10),
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 70),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                children: [
                  const Icon(
                    Icons.curtains_closed,
                    color: Colors.white,
                  ),
                  const SizedBox(width: 5),
                  Text(
                    '$_remainTicket',
                    style: const TextStyle(color: Colors.white, fontSize: 24),
                  ),
                ],
              ),
              const SizedBox(width: 20),
              Text(
                formatDuration(_remainTimeToGetTicket),
                style: const TextStyle(color: Colors.white, fontSize: 24),
              ),
            ],
          ),
        )
      ],
    );
  }
}
