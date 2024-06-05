import 'package:client/enum/errorCode.enum.dart';
import 'package:client/provider/member.provider.dart';
import 'package:client/util/AlertMessageManager.util.dart';
import 'package:client/util/HttpResponseException.util.dart';
import 'package:client/util/RequestManager.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'dart:async';

class TicketTimer extends StatefulWidget {
  const TicketTimer({super.key});

  @override
  State<TicketTimer> createState() => _TicketTimerState();
}

class _TicketTimerState extends State<TicketTimer> {
  int remainTimeToGetTicket = 0;
  int remainTicket = 30 * 60 * 1000;
  Timer? _timer;

  @override
  void initState() {
    super.initState();

    final mp = Provider.of<MemberProvider>(context, listen: false);

    RequestManager.requestMember(mp.id).then((member) {
      mp.setAll(member);

      final nowTimestamp = DateTime.now().millisecondsSinceEpoch;
      if (mp.lastGachaTimestamp != null && mp.remainTicket != null) {
        setState(() {
          remainTimeToGetTicket = mp.lastGachaTimestamp!;
          remainTicket = mp.remainTicket! - 1;

          while (nowTimestamp > remainTimeToGetTicket) {
            remainTicket++;
            remainTimeToGetTicket += 30 * 60 * 1000;
          }

          remainTimeToGetTicket -= nowTimestamp;
        });

        startTimer();
      }
    }).catchError((exception) {
      late AlertDialog dialog;

      if (exception is HttpResponseException) {
        if (exception.code == ErrorCode.serverError) {
          dialog = AlertDialogManager.getExitAlertByServerError();
        } else {
          dialog = AlertDialogManager.getExitAlertByResourceError();
        }
      } else {
        dialog = AlertDialogManager.getExitAlertByUnknownError();
      }

      WidgetsBinding.instance.addPostFrameCallback(
        (_) {
          showDialog(
            context: context,
            barrierDismissible: false,
            builder: (BuildContext context) {
              return dialog;
            },
          );
        },
      );
    });
  }

  void startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        if (remainTimeToGetTicket > 0) {
          remainTimeToGetTicket -= 1000;
        } else {
          updateTicketAndRestartTimer();
        }
      });
    });
  }

  void updateTicketAndRestartTimer() {
    setState(() {
      remainTicket++;
      remainTimeToGetTicket = 30 * 60 * 1000;
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  String formatDuration(int milliseconds) {
    final duration = Duration(milliseconds: milliseconds);
    final minutes = (duration.inMinutes % 60).toString().padLeft(2, '0');
    final seconds = (duration.inSeconds % 60).toString().padLeft(2, '0');
    return '$minutes:$seconds';
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 100),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Row(
            children: [
              const Icon(
                Icons.curtains_closed,
                color: Colors.white,
              ),
              const SizedBox(width: 10),
              Text(
                '$remainTicket',
                style: const TextStyle(color: Colors.white, fontSize: 24),
              ),
            ],
          ),
          const SizedBox(width: 20),
          Text(
            formatDuration(remainTimeToGetTicket),
            style: const TextStyle(color: Colors.white, fontSize: 24),
          ),
        ],
      ),
    );
  }
}
