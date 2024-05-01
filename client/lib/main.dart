import 'package:client/page/collection.page.dart';
import 'package:client/page/gacha.page.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const App());
}

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  int _selectedIndex = 0;
  final PageController _pageController = PageController();

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  static final List<Widget> _pageTitles = <Widget>[
    const Text('Gacha',
        style: TextStyle(fontSize: 20, fontWeight: FontWeight.w500)),
    const Text('Collection',
        style: TextStyle(fontSize: 20, fontWeight: FontWeight.w500)),
    const Text('not yet',
        style: TextStyle(fontSize: 20, fontWeight: FontWeight.w500)),
  ];

  static final List<Widget> _pages = <Widget>[
    const GachaPage(lastGachaTimestampe: 1714527654000, ticketCount: 2),
    CollectionPage(),
    const Center(
      child: Text('Profile Page',
          style: TextStyle(fontSize: 35, fontWeight: FontWeight.bold)),
    ),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
      _pageController.animateToPage(
        index,
        duration: const Duration(milliseconds: 200),
        curve: Curves.ease,
      );
    });
  }

  void _onPageChanged(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: _pageTitles[_selectedIndex],
        ),
        body: PageView(
          controller: _pageController,
          onPageChanged: _onPageChanged,
          children: _pages,
        ),
        bottomNavigationBar: BottomNavigationBar(
          items: const [
            BottomNavigationBarItem(
              icon: Icon(Icons.gamepad_rounded),
              label: 'Gacha',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.home),
              label: 'Collection',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.home),
              label: 'not yet',
            ),
          ],
          currentIndex: _selectedIndex,
          selectedItemColor: Colors.blue,
          onTap: _onItemTapped,
        ),
      ),
    );
  }
}
