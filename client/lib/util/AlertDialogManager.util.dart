import 'dart:io';

import 'package:client/enum/errorCode.enum.dart';
import 'package:client/util/DiskStorageManager.util.dart';
import 'package:client/util/HttpResponseException.util.dart';
import 'package:flutter/material.dart';

enum DialogAction { exit, logout, retry, refresh }

class AlertDialogManager {
  static handleJoinError(
    HttpResponseException exception,
    BuildContext context,
  ) {
    const alertTitle = '로그인할 수 없습니다.';
    late AlertDialog dialog;

    switch (exception.code) {
      case ErrorCode.badRequest:
        dialog = AlertDialogManager._generateDialog(
          alertTitle,
          ['이메일와 비밀번호 모두 입력했는지 확인해주세요.'],
          [
            AlertDialogManager._generateButton(
              '확인',
              () => Navigator.pop(context),
            ),
          ],
        );
        break;
      case ErrorCode.conflict:
        dialog = AlertDialogManager._generateDialog(
          alertTitle,
          ['입력하신 이메일과 비밀번호에 해당하는 정보를 찾을 수 없습니다.'],
          [
            AlertDialogManager._generateButton(
              '확인',
              () => Navigator.pop(context),
            ),
          ],
        );
        break;
      default:
        dialog = AlertDialogManager._generateDefaultDialog(alertTitle, context);
        break;
    }

    AlertDialogManager._showDialog(context, dialog);
  }

  static handleFetchMember(
    HttpResponseException exception,
    BuildContext context,
  ) {
    const alertTitle = '사용자 정보를 불러오지 못했습니다.';
    late AlertDialog dialog;

    switch (exception.code) {
      case ErrorCode.notFound:
        dialog = AlertDialogManager._generateDialog(
          alertTitle,
          ['로그인을 다시 해주세요. 로그아웃됩니다.'],
          [
            AlertDialogManager._generateButton('확인', () {
              Navigator.pop(context);
              DiskStorageManager.removeMemberData();
            }),
          ],
        );
        break;
      default:
        dialog = AlertDialogManager._generateDefaultDialog(alertTitle, context);
        break;
    }

    AlertDialogManager._showDialog(context, dialog);
  }

  static handleFetchCollectionError(
    HttpResponseException exception,
    BuildContext context,
  ) {
    const alertTitle = '컬렉션 정보를 불러오지 못했습니다.';
    late AlertDialog dialog;

    switch (exception.code) {
      case ErrorCode.notFound:
        dialog = AlertDialogManager._generateDialog(
          alertTitle,
          ['로그인을 다시 해주세요. 로그아웃됩니다.'],
          [
            AlertDialogManager._generateButton('확인', () {
              Navigator.pop(context);
              DiskStorageManager.removeMemberData();
            }),
          ],
        );
        break;
      default:
        dialog = AlertDialogManager._generateDefaultDialog(alertTitle, context);
        break;
    }

    AlertDialogManager._showDialog(context, dialog);
  }

  static handleDrawCard(
    HttpResponseException exception,
    BuildContext context,
  ) {
    const alertTitle = '뽑기에 실패했습니다.';
    late AlertDialog dialog;

    switch (exception.code) {
      case ErrorCode.notFound:
        dialog = AlertDialogManager._generateDialog(
          alertTitle,
          ['로그인을 다시 해주세요. 로그아웃됩니다.'],
          [
            AlertDialogManager._generateButton('확인', () {
              Navigator.pop(context);
              DiskStorageManager.removeMemberData();
            }),
          ],
        );
        break;
      case ErrorCode.conflict:
        dialog = AlertDialogManager._generateDialog(
          alertTitle,
          ['남은 티켓이 부족합니다.'],
          [
            AlertDialogManager._generateButton(
              '확인',
              () => Navigator.pop(context),
            ),
          ],
        );
        break;
      default:
        dialog = AlertDialogManager._generateDefaultDialog(alertTitle, context);
        break;
    }

    AlertDialogManager._showDialog(context, dialog);
  }

  static TextButton _generateButton(String text, void Function()? action) {
    return TextButton(
      onPressed: action,
      child: Text(text),
    );
  }

  static AlertDialog _generateDialog(
    String alertTitle,
    List<String> alertContents,
    List<TextButton> buttons,
  ) {
    return AlertDialog(
      title: Text(alertTitle),
      content: SingleChildScrollView(
        child: ListBody(
          children: alertContents.map((content) => Text(content)).toList(),
        ),
      ),
      actions: buttons,
    );
  }

  static AlertDialog _generateDefaultDialog(
      String alertTitle, BuildContext context,
      [bool? needExit]) {
    action() {
      if (needExit == true) {
        exit(0);
      } else {
        Navigator.pop(context);
      }
    }

    return _generateDialog(
      alertTitle,
      ['서버와 통신할 수 없습니다.'],
      [_generateButton('확인', action)],
    );
  }

  static void _showDialog(
    BuildContext context,
    AlertDialog dialog,
  ) {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (BuildContext context) {
          return dialog;
        },
      );
    });
  }
}
