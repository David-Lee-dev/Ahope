import 'package:client/util/TopRightClipper.util.dart';
import 'package:flutter/material.dart';
import 'package:random_text_reveal/random_text_reveal.dart';
import 'package:shimmer/shimmer.dart';

class GachaCard extends StatefulWidget {
  final String imageUrl;
  final int seq;
  final VoidCallback onClose;

  const GachaCard({
    super.key,
    required this.onClose,
    required this.imageUrl,
    required this.seq,
  });

  @override
  State<GachaCard> createState() => _GachaCardState();
}

class _GachaCardState extends State<GachaCard> {
  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        ClipPath(
          clipper: TopRightClipper(),
          child: Container(
            clipBehavior: Clip.hardEdge,
            decoration: BoxDecoration(
              gradient: const LinearGradient(
                begin: Alignment(-1.0, -1.0),
                end: Alignment(1.0, 1.0),
                colors: [Color(0xff5c5ae4), Color(0xffDE4981)],
              ),
              borderRadius: BorderRadius.circular(20),
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                SizedBox(
                  height: 60,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      const SizedBox(width: 8),
                      RandomTextReveal(
                        text: 'No. ${widget.seq}',
                        duration: const Duration(seconds: 1),
                        style: const TextStyle(
                          fontSize: 24,
                          color: Colors.white70,
                          fontWeight: FontWeight.bold,
                        ),
                        curve: Curves.easeIn,
                      ),
                    ],
                  ),
                ),
                AspectRatio(
                  aspectRatio: 1,
                  child: Container(
                    width: double.infinity,
                    clipBehavior: Clip.hardEdge,
                    decoration: BoxDecoration(
                      color: const Color(0xff364458),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Image.network(
                      widget.imageUrl,
                      loadingBuilder: (context, child, loadingProgress) {
                        if (loadingProgress == null) return child;

                        return Shimmer.fromColors(
                          baseColor: const Color(0xff5c5ae4),
                          highlightColor: const Color(0xffDE4981),
                          period: const Duration(milliseconds: 500),
                          child: Container(
                            decoration:
                                const BoxDecoration(color: Colors.black),
                            child: const Text(''),
                          ),
                        );
                      },
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Container(
              width: 55,
              height: 55,
              decoration: BoxDecoration(
                color: const Color(0xff364458),
                borderRadius: BorderRadius.circular(20),
              ),
              child: ElevatedButton(
                onPressed: widget.onClose,
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(20),
                  ),
                  backgroundColor: Colors.transparent,
                  shadowColor: Colors.transparent,
                  padding: EdgeInsets.zero,
                ),
                child: const Icon(Icons.close),
              ),
            ),
          ],
        ),
      ],
    );
  }
}
