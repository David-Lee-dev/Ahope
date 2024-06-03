import 'package:flutter/material.dart';

class GreyscaleFliter extends StatelessWidget {
  final Widget child;

  const GreyscaleFliter({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    final filterMatrix = <double>[
      0.2126,
      0.7152,
      0.0722,
      0,
      0,
      0.2126,
      0.7152,
      0.0722,
      0,
      0,
      0.2126,
      0.7152,
      0.0722,
      0,
      0,
      0,
      0,
      0,
      1,
      0,
    ];

    return ColorFiltered(
      colorFilter: ColorFilter.matrix(filterMatrix),
      child: child,
    );
  }
}
