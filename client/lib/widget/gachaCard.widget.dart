import 'package:client/widget/cardFrame.widget.dart';
import 'package:client/widget/imageFrame.widget.dart';
import 'package:flutter/material.dart';
import 'package:random_text_reveal/random_text_reveal.dart';

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
    return CardFrame(
      title: _buildTitle(),
      content: _buildContent(),
      button: _buildButton(),
      paddingTop: 60,
      clipScale: 80,
      borderRadius: 20,
    );
  }

  Widget _buildTitle() {
    return RandomTextReveal(
      text: '  No. ${widget.seq}',
      duration: const Duration(seconds: 1),
      style: const TextStyle(
        fontSize: 24,
        color: Colors.white70,
        fontWeight: FontWeight.bold,
      ),
      curve: Curves.easeIn,
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

  Widget _buildButton() {
    return Container(
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
    );
  }
}
