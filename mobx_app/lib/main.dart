import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:mobxapp/controller/controller.dart';
import 'package:mobxapp/page/home_page.dart';
import 'package:mobxapp/page/mobx_observale_list.dart';
import 'package:mobxapp/page/rx_dart_observale_list.dart';

/*

GetIt example

void main() {
  GetIt getIt = GetIt.I;
  getIt.registerLazySingleton<Controller>(() => Controller());
  runApp(AppWidget());
}

class AppWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Mobx',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: HomePage(),
    );
  }
}
*/

void main() {
  return runApp(AppWidget());
}

class AppWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MobX Observable List',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: RxDartObservableList(),
    );
  }
}

