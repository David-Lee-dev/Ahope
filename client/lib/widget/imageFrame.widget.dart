import 'dart:async';

import 'package:flutter/material.dart';
import 'package:shimmer/shimmer.dart';

enum LoadingUI { always, once }

class ImageFrame extends StatefulWidget {
  final String imageUrl;
  final LoadingUI loadingUI;
  final Color loadingUiBaseColor;
  final Color loadingUiHighlightColor;

  const ImageFrame({
    super.key,
    required this.imageUrl,
    required this.loadingUI,
    required this.loadingUiBaseColor,
    required this.loadingUiHighlightColor,
  });

  @override
  State<ImageFrame> createState() => _ImageFrameState();
}

class _ImageFrameState extends State<ImageFrame> {
  late bool _imageLoadedOnce = false;

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: 1,
      child: Container(
        clipBehavior: Clip.hardEdge,
        decoration: BoxDecoration(
          color: const Color(0xff364458),
          borderRadius: BorderRadius.circular(8),
        ),
        child: FutureBuilder<void>(
          future: _loadImage(),
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.done &&
                _imageLoadedOnce) {
              return Image.network(
                widget.imageUrl,
                fit: BoxFit.cover,
              );
            } else {
              return Shimmer.fromColors(
                baseColor: widget.loadingUiBaseColor,
                highlightColor: widget.loadingUiHighlightColor,
                period: const Duration(milliseconds: 500),
                child: Container(
                  decoration: const BoxDecoration(color: Colors.black),
                  child: const Text(''),
                ),
              );
            }
          },
        ),
      ),
    );
  }

  Future<void> _loadImage() async {
    if (!_imageLoadedOnce) {
      final image = NetworkImage(widget.imageUrl);
      final completer = Completer<void>();
      image.resolve(const ImageConfiguration()).addListener(
            ImageStreamListener(
              (info, _) {
                if (mounted) {
                  setState(() {
                    if (widget.loadingUI != LoadingUI.always) {
                      _imageLoadedOnce = true;
                    }
                  });
                }
                completer.complete();
              },
              onError: (error, stackTrace) {
                completer.completeError(error, stackTrace);
              },
            ),
          );
      return completer.future;
    }
    return Future.value();
  }
}
