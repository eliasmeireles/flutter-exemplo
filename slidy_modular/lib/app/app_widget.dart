import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';

class AppWidget extends StatelessWidget {
  final appKey = GlobalKey();
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      navigatorKey: Modular.navigatorKey,
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      initialRoute: Modular.initialRoute,
      onGenerateRoute: Modular.generateRoute,
    );
  }
}
