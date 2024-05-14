import 'package:flutter/material.dart';

class GachaCard extends StatelessWidget {
  final VoidCallback onClose;

  const GachaCard({
    super.key,
    required this.onClose,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          decoration: const BoxDecoration(
            color: Color(0xff303F55),
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(30),
              topRight: Radius.circular(30),
            ),
          ),
          child: Padding(
            padding: const EdgeInsets.only(
              top: 8,
              left: 12,
              right: 8,
              bottom: 6,
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text(
                  "No. 000",
                  style: TextStyle(
                    color: Colors.white70,
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Container(
                  width: 50,
                  height: 50,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(30),
                    color: const Color(0xff556072),
                  ),
                  child: ElevatedButton(
                    onPressed: () {},
                    style: ElevatedButton.styleFrom(
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30)),
                      backgroundColor: Colors.transparent,
                      shadowColor: Colors.transparent,
                      padding: EdgeInsets.zero,
                    ),
                    child: GestureDetector(
                      onTap: onClose,
                      child: const Icon(Icons.close),
                    ),
                  ),
                )
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
