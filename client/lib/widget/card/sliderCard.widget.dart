import 'package:client/widget/cardFrame.widget.dart';
import 'package:client/widget/imageFrame.widget.dart';
import 'package:flutter/material.dart';

class SliderCard extends StatefulWidget {
  final String imageUrl;
  final int seq;

  const SliderCard({
    super.key,
    required this.imageUrl,
    required this.seq,
  });

  @override
  State<SliderCard> createState() => _SliderCardState();
}

class _SliderCardState extends State<SliderCard> {
  @override
  Widget build(BuildContext context) {
    return CardFrame(
      title: _buildTitle(),
      content: _buildContent(),
      button: _buildButton(context),
      paddingTop: 60,
      clipScale: 80,
      borderRadius: 20,
    );
  }

  Widget _buildTitle() {
    return Text(
      '  No. ${widget.seq}',
      style: const TextStyle(
        fontSize: 24,
        color: Colors.white70,
        fontWeight: FontWeight.bold,
      ),
    );
  }

  Widget _buildContent() {
    return ImageFrame(
      imageUrl: widget.imageUrl,
      loadingUI: LoadingUI.once,
      loadingUiBaseColor: const Color(0xff5c5ae4),
      loadingUiHighlightColor: const Color(0xffDE4981),
    );
  }

  Widget _buildButton(BuildContext context) {
    return GestureDetector(
      onTap: () => Navigator.pop(context),
      child: Container(
        width: 55,
        height: 55,
        decoration: BoxDecoration(
          color: const Color(0xff364458),
          borderRadius: BorderRadius.circular(20),
        ),
        child: Container(
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(20),
            color: Colors.transparent,
          ),
          child: const Icon(
            Icons.close,
            color: Colors.white,
            size: 32,
          ),
        ),
      ),
    );
  }
}
