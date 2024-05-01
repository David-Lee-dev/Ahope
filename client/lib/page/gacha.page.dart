import 'package:flutter/material.dart';

class GachaPage extends StatefulWidget {
  final int lastGachaTimestampe;
  final int ticketCount;

  const GachaPage({
    super.key,
    required this.lastGachaTimestampe,
    required this.ticketCount,
  });

  @override
  State<GachaPage> createState() => _GachaPageState();
}

class _GachaPageState extends State<GachaPage> {
  final int _remainSecondsForNextTicket = 100;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text('시간 영역'),
          ElevatedButton(
            onPressed: () {},
            child: const Text('Gacha!!'),
          ),
          const Text('가능 횟수 1/3'),
        ],
      ),
    );
  }
}
