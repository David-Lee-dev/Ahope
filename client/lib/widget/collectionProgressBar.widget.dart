import 'dart:math';

import 'package:flutter/material.dart';

class CollectionProgressBar extends StatefulWidget {
  final String category;
  final double rate;
  final VoidCallback onTap;

  const CollectionProgressBar({
    super.key,
    required this.category,
    required this.rate,
    required this.onTap,
  });

  @override
  State<CollectionProgressBar> createState() => _CollectionProgressBarState();
}

class _CollectionProgressBarState extends State<CollectionProgressBar>
    with TickerProviderStateMixin {
  late AnimationController controller;

  double roundDown(double value, int precision) {
    final isNegative = value.isNegative;
    final mod = pow(10.0, precision);
    final roundDown = (((value.abs() * mod).floor()) / mod);
    return isNegative ? -roundDown : roundDown;
  }

  @override
  void initState() {
    controller = AnimationController(
      vsync: this,
      duration: const Duration(seconds: 1),
    )..addListener(() {
        setState(() {});
      });
    controller.animateTo(roundDown(widget.rate, 2));
    super.initState();
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: widget.onTap,
      child: Padding(
        padding: const EdgeInsets.only(bottom: 10),
        child: Stack(
          children: [
            LinearProgressIndicator(
              minHeight: 44,
              backgroundColor: const Color.fromRGBO(61, 74, 93, 0.7),
              color: const Color.fromARGB(177, 110, 121, 136),
              value: controller.value,
              semanticsLabel: 'test progress',
              borderRadius: BorderRadius.circular(10),
            ),
            Container(
              padding: const EdgeInsets.only(left: 10),
              child: Text(
                widget.category,
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  height: 1.8,
                  color: Colors.white,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
