import 'package:flutter/material.dart';

class GradientButton extends StatelessWidget {
  final BorderRadiusGeometry? borderRadius;
  final double? width;
  final double height;
  final VoidCallback? onPressed;
  final Widget child;

  const GradientButton({
    super.key,
    this.borderRadius,
    this.width,
    required this.height,
    required this.onPressed,
    required this.child,
  });

  @override
  Widget build(BuildContext context) {
    final borderRadius = this.borderRadius ?? BorderRadius.circular(0);

    return Container(
      width: width,
      height: height,
      decoration: BoxDecoration(
        borderRadius: borderRadius,
        color: Colors.red,
        gradient: const LinearGradient(
          begin: Alignment(-1.0, -1.0),
          end: Alignment(1.0, 2.0),
          colors: [
            Color(0xff5c5ae4),
            Color(0xffDE4981),
          ],
        ),
      ),
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          shape: RoundedRectangleBorder(borderRadius: borderRadius),
          backgroundColor: Colors.transparent,
          shadowColor: Colors.transparent,
        ),
        child: child,
      ),
    );
  }
}
