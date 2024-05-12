import 'package:client/widget/gredientButton.widget.dart';
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
          GradientButton(
            borderRadius: BorderRadius.circular(25),
            height: 50,
            onPressed: () {},
            child: const Text(
              'Gacha',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 18,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
