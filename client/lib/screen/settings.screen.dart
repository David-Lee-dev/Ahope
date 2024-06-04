import 'package:client/enum/errorCode.enum.dart';
import 'package:client/enum/requestMethod.enum.dart';
import 'package:client/provider/member.provider.dart';
import 'package:client/util/DiskStorageManager.util.dart';
import 'package:client/util/HttpResponseException.util.dart';
import 'package:client/util/RequestManager.dart';
import 'package:client/widget/gredientButton.widget.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer<MemberProvider>(
      builder: (context, mp, child) {
        return Column(
          children: [
            const SizedBox(height: 100),
            GradientButton(
              borderRadius: BorderRadius.circular(10),
              width: 320,
              height: 50,
              onPressed: () {
                RequestManager.requestQuit(mp.id).then((_) {
                  DiskStorageManager.removeMemberData();
                  mp.reset();
                }).catchError((exception) {
                  late Text alertTitle;
                  late List<Text> alertContents;
                  late List<TextButton> buttons;

                  if (exception is HttpResponseException) {
                    if (exception.method == RequestMethod.post) {
                      if (exception.code == ErrorCode.serverError) {
                        alertTitle = const Text('데이터 삭제에 실패했습니다.');
                        alertContents = [const Text('서버와 통신할 수 없습니다.')];
                        buttons = [
                          TextButton(
                              onPressed: () => Navigator.pop(context),
                              child: const Text('확인'))
                        ];
                      } else {
                        alertTitle = const Text('데이터 삭제에 실패했습니다.');
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
                      TextButton(
                          onPressed: () => Navigator.pop(context),
                          child: const Text('확인'))
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
                });
              },
              startColor: const Color.fromARGB(255, 94, 105, 120),
              endColor: const Color(0xff28374D),
              child: const Text('Delete My data'),
            ),
            const SizedBox(height: 20),
            GradientButton(
              borderRadius: BorderRadius.circular(10),
              width: 320,
              height: 50,
              onPressed: () {
                DiskStorageManager.removeMemberData();
                mp.reset();
              },
              startColor: const Color.fromARGB(255, 94, 105, 120),
              endColor: const Color(0xff28374D),
              child: const Text('Sign out'),
            ),
          ],
        );
      },
    );
  }
}
