import 'package:client/widget/gredientButton.widget.dart';
import 'package:flutter/material.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const SizedBox(height: 100),
        GradientButton(
          borderRadius: BorderRadius.circular(10),
          width: 300,
          height: 50,
          onPressed: () {},
          startColor: const Color.fromARGB(255, 94, 105, 120),
          endColor: const Color(0xff28374D),
          child: const Text('Delete My data'),
        )
      ],
    );
  }
}
