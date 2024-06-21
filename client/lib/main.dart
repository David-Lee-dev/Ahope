import 'package:client/provider/collection.provider.dart';
import 'package:client/provider/member.provider.dart';
import 'package:client/screen/collection.screen.dart';
import 'package:client/screen/gacha.screen.dart';
import 'package:client/screen/landing.screen.dart';
import 'package:client/screen/settings.screen.dart';
import 'package:client/util/DiskStorageManager.util.dart';
import 'package:client/widget/bottom_nav/bottomNav.widget.dart';
import 'package:client/widget/bottom_nav/bottomNavItem.widget.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => MemberProvider()),
        ChangeNotifierProvider(create: (context) => CollectionProvider()),
      ],
      child: const App(),
    ),
  );
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
  void initState() {
    super.initState();

    DiskStorageManager.getMember().then((member) {
      setState(() {
        if (member != null) {
          Provider.of<MemberProvider>(context, listen: false).setAll(member);
        }
      });
    });
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  static final List<Widget> _pages = <Widget>[
    const GachaScreen(),
    const CollectionScreen(),
    const SettingsScreen()
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
    final textTheme = Theme.of(context).textTheme;

    return Consumer<MemberProvider>(
      builder: (context, member, child) {
        return MaterialApp(
          theme: ThemeData(
            textTheme: GoogleFonts.orbitronTextTheme(textTheme).copyWith(
              bodyMedium: GoogleFonts.orbitron(textStyle: textTheme.bodyMedium),
            ),
            scaffoldBackgroundColor: const Color(0xff051732),
          ),
          home: member.id == null
              ? const LandingScreen()
              : Scaffold(
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
                        icon: Icon(Icons.settings, color: Colors.white),
                        label: "Setting",
                      ),
                    ],
                  ),
                ),
        );
      },
    );
  }
}
