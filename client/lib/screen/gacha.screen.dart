import 'dart:async';
import 'dart:io';

import 'package:client/enum/errorCode.enum.dart';
import 'package:client/enum/requestMethod.enum.dart';
import 'package:client/provider/collection.provider.dart';
import 'package:client/provider/member.provider.dart';
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

  @override
  void initState() {
    super.initState();

    final mp = Provider.of<MemberProvider>(context, listen: false);
    final cp = Provider.of<CollectionProvider>(context, listen: false);

    if (mp.lastGachaTimestamp == null) {
      RequestManager.requestMember(mp.id).then((member) {
        mp.setLastGachaTimestamp(member.lastGachaTimestamp);
      }).catchError((exception) {
        late Text alertTitle;
        late List<Text> alertContents;
        late List<TextButton> buttons;

        if (exception is HttpResponseException) {
          if (exception.code == ErrorCode.serverError) {
            alertTitle = const Text('심뽑을 종료합니다.');
            alertContents = [
              const Text('서버와 통신할 수 없습니다.'),
              const Text('앱을 종료합니다.')
            ];
            buttons = [
              TextButton(onPressed: () => exit(0), child: const Text('종료'))
            ];
          } else {
            alertTitle = const Text('유저 정보를 불러오지 못했습니다.');
            alertContents = [const Text('앱을 재실행 해주세요.')];
            buttons = [
              TextButton(
                onPressed: () => exit(0),
                child: const Text('종료'),
              ),
            ];
          }
        } else {
          alertTitle = const Text('앱이 종료됩니다.');
          alertContents = [
            const Text('알 수 없는 오류가 발생했습니다.'),
            const Text('빠른 시일 내로 복구하겠습니다. 사용에 불편을 드려 죄송합니다.')
          ];
          buttons = [
            TextButton(onPressed: () => exit(0), child: const Text('종료'))
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
      });
    }

    if (cp.collection == null) {
      RequestManager.requestCollection(mp.id)
          .then((collection) => cp.setCollection(collection))
          .catchError(
        (exception) {
          late Text alertTitle;
          late List<Text> alertContents;
          late List<TextButton> buttons;

          if (exception is HttpResponseException) {
            if (exception.code == ErrorCode.serverError) {
              alertTitle = const Text('심뽑을 종료합니다.');
              alertContents = [
                const Text('서버와 통신할 수 없습니다.'),
                const Text('앱을 종료합니다.')
              ];
              buttons = [
                TextButton(onPressed: () => exit(0), child: const Text('종료'))
              ];
            } else {
              alertTitle = const Text('컬렉션을 불러오지 못했습니다.');
              alertContents = [const Text('컬렉션 탭에서 새로고침 해주세요.')];
              buttons = [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('확인'),
                ),
              ];
            }
          } else {
            alertTitle = const Text('앱이 종료됩니다.');
            alertContents = [
              const Text('알 수 없는 오류가 발생했습니다.'),
              const Text('빠른 시일 내로 복구하겠습니다. 사용에 불편을 드려 죄송합니다.')
            ];
            buttons = [
              TextButton(onPressed: () => exit(0), child: const Text('종료'))
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
  }

  @override
  Widget build(BuildContext context) {
    Future<dynamic> drawCard() async {
      final mp = Provider.of<MemberProvider>(context, listen: false);
      final cp = Provider.of<CollectionProvider>(context, listen: false);

      final card = await RequestManager.requestDraw(mp.id);
      final collection = await RequestManager.requestCollection(mp.id);

      cp.setCollection(collection);

      return card;
    }

    void turnOnDrawing() {
      setState(() {
        _isDrawing = true;
      });
    }

    void turnOffDrawing() {
      setState(() {
        _isDrawing = false;
      });
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
            turnOnDrawing();
            drawCard().then(
              (card) {
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
              },
            ).catchError((exception) {
              late Text alertTitle;
              late List<Text> alertContents;
              late List<TextButton> buttons;

              if (exception is HttpResponseException) {
                if (exception.method == RequestMethod.post) {
                  if (exception.code == ErrorCode.serverError) {
                    alertTitle = const Text('카드 뽑기에 실패했습니다.');
                    alertContents = [const Text('서버와 통신할 수 없습니다.')];
                    buttons = [
                      TextButton(
                          onPressed: () => Navigator.pop(context),
                          child: const Text('확인'))
                    ];
                  } else {
                    alertTitle = const Text('카드 뽑기에 실패했습니다.');
                    alertContents = [const Text('다시 시도해주세요.')];
                    buttons = [
                      TextButton(
                          onPressed: () => Navigator.pop(context),
                          child: const Text('확인'))
                    ];
                  }
                }

                if (exception.method == RequestMethod.get) {
                  alertTitle = const Text('컬렉션 최신화에 실패했습니다.');
                  alertContents = [const Text('컬렉션 탭에서 새로고침 해주세요.')];
                  buttons = [
                    TextButton(
                        onPressed: () => Navigator.pop(context),
                        child: const Text('확인'))
                  ];
                }
              } else {
                alertTitle = const Text('카드 뽑기에 실패했습니다.');
                alertContents = [
                  const Text('알 수 없는 오류가 발생했습니다.'),
                  const Text('빠른 시일 내로 복구하겠습니다. 사용에 불편을 드려 죄송합니다.')
                ];
                buttons = [
                  TextButton(onPressed: () => exit(0), child: const Text('종료'))
                ];
              }

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
            }).whenComplete(() {
              turnOffDrawing();
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
