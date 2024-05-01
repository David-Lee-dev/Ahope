import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Bundle extends StatelessWidget {
  const Bundle({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Container(
        decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(5), color: Colors.amber),
        child: const Center(child: Text("demo")),
      ),
    );
  }
}
