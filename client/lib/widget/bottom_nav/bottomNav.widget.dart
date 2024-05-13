import 'package:client/widget/bottom_nav/bottomNavItem.widget.dart';
import 'package:flutter/material.dart';

class BottomNav extends StatefulWidget {
  final int currentIndex;
  final List<BottomNavItem> items;
  final ValueChanged<int> onTap;

  const BottomNav({
    super.key,
    required this.currentIndex,
    required this.items,
    required this.onTap,
  });

  @override
  State<BottomNav> createState() => _BottomNavState();
}

class _BottomNavState extends State<BottomNav> {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 20, right: 20, bottom: 20, top: 10),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          for (var i = 0; i < widget.items.length; i++)
            GestureDetector(
              onTap: () {
                widget.onTap(i);
              },
              child: _Tile(
                active: i == widget.currentIndex,
                icon: widget.items[i].icon,
                label: widget.items[i].label,
              ),
            ),
        ],
      ),
    );
  }
}

class _Tile extends StatelessWidget {
  final Icon icon;
  final String label;
  final bool active;

  const _Tile({
    required this.active,
    required this.icon,
    required this.label,
  });

  @override
  Widget build(BuildContext context) {
    return AnimatedContainer(
      duration: const Duration(milliseconds: 200),
      width: active ? 150 : 55,
      curve: Curves.easeInOut, // Set the curve of the animation
      clipBehavior: Clip.hardEdge,
      padding: const EdgeInsets.all(5),
      decoration: BoxDecoration(
        color: const Color(0xff3D4A5D),
        borderRadius: BorderRadius.circular(40),
      ),
      child: Row(children: [
        Container(
          width: 45,
          height: 45,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(30),
            color: active ? null : Colors.transparent,
            gradient: active
                ? const LinearGradient(
                    begin: Alignment(-1.0, -1.0),
                    end: Alignment(1.0, 2.0),
                    colors: [Color(0xff5c5ae4), Color(0xffDE4981)],
                  )
                : null,
          ),
          child: icon,
        ),
        const SizedBox(width: 10),
        Text(
          label,
          style: const TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(width: 10),
      ]),
    );
  }
}
