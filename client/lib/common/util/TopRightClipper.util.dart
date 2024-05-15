import 'package:flutter/material.dart';

class TopRightClipper extends CustomClipper<Path> {
  final double clipSize;

  TopRightClipper({
    this.clipSize = 80,
  });

  @override
  Path getClip(Size size) {
    double w = size.width;
    double h = size.height;

    Path path = Path()
      ..moveTo(w - clipSize, 0)
      ..cubicTo(w - (clipSize / 2), 0, w - clipSize,
          (clipSize / 2) + (clipSize / 8), w - (clipSize / 2), clipSize * 0.75)
      ..lineTo(w - (clipSize / 4), clipSize * 0.75)
      ..quadraticBezierTo(w, clipSize * 0.75, w, clipSize)
      ..lineTo(w, clipSize)
      ..lineTo(w, h)
      ..lineTo(0, h)
      ..lineTo(0, 0)
      ..lineTo(w - clipSize, 0)
      ..close();
    return path;
  }

  @override
  bool shouldReclip(CustomClipper<Path> oldClipper) => false;
}
