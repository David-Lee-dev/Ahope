import 'package:client/page/collection.page.dart';
import 'package:client/page/gacha.page.dart';
import 'package:client/widget/bottom_nav/bottomNav.widget.dart';
import 'package:client/widget/bottom_nav/bottomNavItem.widget.dart';
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

  static final List<Widget> _pages = <Widget>[
    const GachaPage(lastGachaTimestampe: 1714527654000, ticketCount: 2),
    CollectionPage(),
    const Center(
      child: Text('Profile Page',
          style: TextStyle(fontSize: 35, fontWeight: FontWeight.bold)),
    ),
    const Center(
      child: Text('Settgins Page',
          style: TextStyle(fontSize: 35, fontWeight: FontWeight.bold)),
    ),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
      _pageController.jumpToPage(index);
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
      theme: ThemeData(
        scaffoldBackgroundColor: const Color(0xff051732),
      ),
      home: Scaffold(
        body: PageView(
          controller: _pageController,
          onPageChanged: _onPageChanged,
          children: _pages,
        ),
        bottomNavigationBar: BottomNav(
          currentIndex: _selectedIndex,
          onTap: _onItemTapped,
          items: const [
            BottomNavItem(
              icon: Icon(Icons.home, color: Colors.white),
              label: "Gacha",
            ),
            BottomNavItem(
              icon: Icon(Icons.collections, color: Colors.white),
              label: "Collections",
            ),
            BottomNavItem(
              icon: Icon(Icons.price_change, color: Colors.white),
              label: "Market",
            ),
            BottomNavItem(
              icon: Icon(Icons.settings, color: Colors.white),
              label: "Setting",
            ),
          ],
        ),
      ),
    );
  }
}
