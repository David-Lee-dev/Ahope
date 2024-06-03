import 'package:client/model/metaData.model.dart';
import 'package:client/widget/cardFrame.widget.dart';
import 'package:client/widget/imageFrame.widget.dart';
import 'package:flutter/material.dart';

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
  bool imageLoadedOnce = false;

  bool _isCollected() {
    return widget.data.cards != null && widget.data.cards!.isNotEmpty;
  }

  @override
  Widget build(BuildContext context) {
    return CardFrame(
      title: _buildTitle(),
      content: _buildContent(),
      button: _buildButton(),
      paddingTop: 28,
      clipScale: 37,
      borderRadius: 8,
      greyscale: !_isCollected(),
    );
  }

  Widget _buildTitle() {
    return widget.data.cards != null
        ? Text(
            ' No. ${widget.data.cards![0].seq}',
            style: const TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.bold,
            ),
          )
        : const Text('');
  }

  Widget _buildContent() {
    return ImageFrame(
      imageUrl: widget.data.imageUrl,
      loadingUI: LoadingUI.once,
      loadingUiBaseColor: const Color(0xff051732),
      loadingUiHighlightColor: const Color(0xFF3F3F3F),
    );
  }

  Widget _buildButton() {
    return Container(
      width: 24,
      height: 24,
      decoration: BoxDecoration(
        color: widget.data.cards != null && widget.data.cards!.length > 1
            ? Colors.amber.shade300
            : const Color.fromRGBO(255, 213, 79, 0),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Icon(
        Icons.add,
        color: widget.data.cards != null && widget.data.cards!.length > 1
            ? Colors.black54
            : const Color.fromRGBO(0, 0, 1, 0),
        size: 20,
      ),
    );
  }
}
