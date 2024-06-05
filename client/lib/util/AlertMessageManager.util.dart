import 'dart:io';
import 'package:flutter/material.dart';

class AlertDialogManager {
  static AlertDialog getExitAlertByServerError() {
    const alertTitle = Text('심뽑을 종료합니다.');
    final alertContents = [
      const Text('서버와 통신할 수 없습니다.'),
      const Text('앱을 종료합니다.')
    ];
    final buttons = [
      TextButton(
        onPressed: () => exit(0),
        child: const Text('종료'),
      )
    ];

    return _getDialog(alertTitle, alertContents, buttons);
  }

  static AlertDialog getExitAlertByResourceError() {
    const alertTitle = Text('유저 정보를 불러오지 못했습니다.');
    final alertContents = [const Text('앱을 재실행 해주세요.')];
    final buttons = [
      TextButton(
        onPressed: () => exit(0),
        child: const Text('종료'),
      ),
    ];

    return _getDialog(alertTitle, alertContents, buttons);
  }

  static AlertDialog getExitAlertByUnknownError() {
    const alertTitle = Text('앱이 종료됩니다.');
    final alertContents = [
      const Text('알 수 없는 오류가 발생했습니다.'),
      const Text('빠른 시일 내로 복구하겠습니다. 사용에 불편을 드려 죄송합니다.')
    ];
    final buttons = [
      TextButton(
        onPressed: () => exit(0),
        child: const Text('종료'),
      )
    ];

    return _getDialog(alertTitle, alertContents, buttons);
  }

  static AlertDialog getNeedRefreshAlertByResourceError(BuildContext context) {
    const alertTitle = Text('컬렉션을 불러오지 못했습니다.');
    final alertContents = [const Text('컬렉션 탭에서 새로고침 해주세요.')];
    final buttons = [
      TextButton(
        onPressed: () => Navigator.pop(context),
        child: const Text('확인'),
      ),
    ];

    return _getDialog(alertTitle, alertContents, buttons);
  }

  static AlertDialog getCannotDrawAlertByServerError(BuildContext context) {
    const alertTitle = Text('카드 뽑기에 실패했습니다.');
    final alertContents = [const Text('서버와 통신할 수 없습니다.')];
    final buttons = [
      TextButton(
        onPressed: () => Navigator.pop(context),
        child: const Text('확인'),
      )
    ];

    return _getDialog(alertTitle, alertContents, buttons);
  }

  static AlertDialog getCannotDrawAlertByUnknwonError() {
    const alertTitle = Text('카드 뽑기에 실패했습니다.');
    final alertContents = [const Text('서버와 통신할 수 없습니다.')];
    final buttons = [
      TextButton(
        onPressed: () => exit(0),
        child: const Text('종료'),
      )
    ];

    return _getDialog(alertTitle, alertContents, buttons);
  }

  static AlertDialog getRetryToDrawAlertByServerError(BuildContext context) {
    const alertTitle = Text('카드 뽑기에 실패했습니다.');
    final alertContents = [const Text('다시 시도해주세요.')];
    final buttons = [
      TextButton(
        onPressed: () => Navigator.pop(context),
        child: const Text('확인'),
      )
    ];

    return _getDialog(alertTitle, alertContents, buttons);
  }

  static AlertDialog _getDialog(
      Text alertTitle, List<Text> alertContents, List<TextButton> buttons) {
    return AlertDialog(
      title: alertTitle,
      content: SingleChildScrollView(child: ListBody(children: alertContents)),
      actions: buttons,
    );
  }
}
