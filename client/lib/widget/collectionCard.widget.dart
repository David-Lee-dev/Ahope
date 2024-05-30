import 'package:client/model/metaData.model.dart';
import 'package:client/util/TopRightClipper.util.dart';
import 'package:flutter/material.dart';
import 'package:shimmer/shimmer.dart';

class CollcetionCard extends StatefulWidget {
  final Metadata data;

  const CollcetionCard({
    super.key,
    required this.data,
  });

  @override
  State<CollcetionCard> createState() => _CollcetionCardState();
}

class _CollcetionCardState extends State<CollcetionCard> {
  bool showImage = false;

  @override
  void initState() {
    Future.delayed(const Duration(milliseconds: 500)).then((_) {
      if (mounted) {
        setState(() {
          showImage = true;
        });
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        ClipPath(
          clipper: TopRightClipper(clipSize: 37),
          child: Container(
            clipBehavior: Clip.hardEdge,
            decoration: BoxDecoration(
              gradient: const LinearGradient(
                begin: Alignment(-1.0, -1.0),
                end: Alignment(1.0, 2.0),
                colors: [Color(0xff5c5ae4), Color(0xffDE4981)],
              ),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const SizedBox(height: 28),
                AspectRatio(
                  aspectRatio: 1,
                  child: Container(
                    clipBehavior: Clip.hardEdge,
                    decoration: BoxDecoration(
                      color: const Color(0xff364458),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Image.network(
                      widget.data.imageUrl,
                      loadingBuilder: (context, child, loadingProgress) {
                        bool imageLoaded = false;
                        if (loadingProgress == null) imageLoaded = true;

                        return showImage && imageLoaded
                            ? child
                            : Shimmer.fromColors(
                                baseColor: const Color(0xff051732),
                                highlightColor: const Color(0xFF3F3F3F),
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
              width: 24,
              height: 24,
              decoration: BoxDecoration(
                color: Colors.amber.shade300,
                borderRadius: BorderRadius.circular(8),
              ),
              child: const Icon(
                Icons.add,
                color: Colors.black54,
                size: 20,
              ),
            ),
          ],
        ),
      ],
    );
  }
}
