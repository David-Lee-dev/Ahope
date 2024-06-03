import 'package:client/util/TopRightClipper.util.dart';
import 'package:client/widget/greyscaleFilter.widget.dart';
import 'package:flutter/material.dart';

class CardFrame extends StatefulWidget {
  final Widget? title;
  final Widget content;
  final Widget? button;
  final double paddingTop;
  final double clipScale;
  final double borderRadius;
  final bool greyscale;

  const CardFrame({
    super.key,
    required this.title,
    required this.content,
    required this.button,
    required this.paddingTop,
    required this.clipScale,
    required this.borderRadius,
    this.greyscale = false,
  });

  @override
  State<CardFrame> createState() => _CardFrameState();
}

class _CardFrameState extends State<CardFrame> {
  @override
  Widget build(BuildContext context) {
    return widget.greyscale
        ? GreyscaleFliter(child: _buildCardContent())
        : _buildCardContent();
  }

  Stack _buildCardContent() {
    return Stack(
      children: [
        ClipPath(
          clipper: TopRightClipper(clipSize: widget.clipScale),
          child: Container(
            clipBehavior: Clip.hardEdge,
            decoration: BoxDecoration(
              gradient: const LinearGradient(
                begin: Alignment(-1.0, -1.0),
                end: Alignment(1.0, 1.0),
                colors: [Color(0xff5c5ae4), Color(0xffDE4981)],
              ),
              borderRadius: BorderRadius.circular(widget.borderRadius),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                SizedBox(
                  height: widget.paddingTop,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      widget.title!,
                    ],
                  ),
                ),
                AspectRatio(
                  aspectRatio: 1,
                  child: Container(
                    width: double.infinity,
                    clipBehavior: Clip.hardEdge,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(widget.borderRadius),
                    ),
                    child: widget.content,
                  ),
                ),
              ],
            ),
          ),
        ),
        if (widget.button != null)
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [widget.button!],
          ),
      ],
    );
  }
}
