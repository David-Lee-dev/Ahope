import 'package:client/widget/gachaCard.widget.dart';
import 'package:client/widget/gredientButton.widget.dart';
import 'package:flutter/material.dart';

class GachaScreen extends StatefulWidget {
  final int lastGachaTimestampe;
  final int ticketCount;

  const GachaScreen({
    super.key,
    required this.lastGachaTimestampe,
    required this.ticketCount,
  });

  @override
  State<GachaScreen> createState() => _GachaScreenState();
}

class _GachaScreenState extends State<GachaScreen> {
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
          onPressed: () => showDialog(
            context: context,
            builder: (BuildContext context) => Dialog(
              backgroundColor: Colors.transparent,
              child: GachaCard(
                onClose: () {
                  Navigator.pop(context);
                },
              ),
            ),
          ),
          child: const Text(
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
