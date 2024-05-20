import 'package:client/model/collection.model.dart';
import 'package:client/provider/collection.provider.dart';
import 'package:client/provider/member.provider.dart';
import 'package:client/util/DiskStorageManager.util.dart';
import 'package:client/util/RequestManager.dart';
import 'package:client/widget/gachaCard.widget.dart';
import 'package:client/widget/gredientButton.widget.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class GachaScreen extends StatefulWidget {
  const GachaScreen({super.key});

  @override
  State<GachaScreen> createState() => _GachaScreenState();
}

class _GachaScreenState extends State<GachaScreen> {
  @override
  void initState() {
    final mp = Provider.of<MemberProvider>(context, listen: false);
    final cp = Provider.of<CollectionProvider>(context, listen: false);

    RequestManager.requestCollection(mp.id).then((value) {
      cp.setCollection(value);
    });

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        GradientButton(
          borderRadius: BorderRadius.circular(35),
          width: 250,
          height: 70,
          startColor: const Color(0xff5c5ae4),
          endColor: const Color(0xffDE4981),
          onPressed: () => showDialog(
            context: context,
            builder: (BuildContext context) => Dialog(
              backgroundColor: Colors.transparent,
              child: GachaCard(
                onClose: () {
                  Navigator.pop(context);
                },
              ),
            ),
          ),
          child: const Text(
            'Gacha',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 32,
            ),
          ),
        ),
      ],
    );
  }
}
