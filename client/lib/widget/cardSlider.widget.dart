import 'package:carousel_slider/carousel_slider.dart';
import 'package:client/model/cardItem.model.dart';
import 'package:client/widget/card/sliderCard.widget.dart';
import 'package:flutter/material.dart';

class CardSlider extends StatefulWidget {
  final String imageUrl;
  final List<CardItem> cards;

  const CardSlider({
    super.key,
    required this.cards,
    required this.imageUrl,
  });

  @override
  State<CardSlider> createState() => _CardSliderState();
}

class _CardSliderState extends State<CardSlider> {
  int _current = 0;
  final CarouselController _controller = CarouselController();

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      child: Column(
        children: [
          Expanded(
            child: CarouselSlider(
              items: [
                for (final card in widget.cards)
                  SliderCard(
                    onClose: () {},
                    imageUrl: widget.imageUrl,
                    seq: card.seq,
                  )
              ],
              carouselController: _controller,
              options: CarouselOptions(
                enlargeCenterPage: true,
                aspectRatio: 1.0,
                enableInfiniteScroll: false,
                onPageChanged: (index, reason) {
                  setState(() {
                    _current = index;
                  });
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}
