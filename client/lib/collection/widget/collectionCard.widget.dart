import 'package:flutter/material.dart';

class CollcetionCard extends StatelessWidget {
  final VoidCallback onClose;

  const CollcetionCard({
    super.key,
    required this.onClose,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Container(
          decoration: const BoxDecoration(
            color: Color(0xff303F55),
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(30),
              topRight: Radius.circular(30),
            ),
          ),
          child: const Padding(
            padding: EdgeInsets.only(
              top: 8,
              left: 12,
              bottom: 6,
            ),
            child: Row(
              children: [
                Text(
                  "No. 000",
                  style: TextStyle(
                    color: Colors.white70,
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),
        ),
        Container(
          decoration: const BoxDecoration(
            color: Color(0xff303F55),
            borderRadius: BorderRadius.only(
              bottomLeft: Radius.circular(30),
              bottomRight: Radius.circular(30),
            ),
          ),
          child: Container(
            clipBehavior: Clip.hardEdge,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(30),
            ),
            child: Image.asset(
              'assets/images/seat2.jpeg',
            ),
          ),
        ),
      ],
    );
  }
}
