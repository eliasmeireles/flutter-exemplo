import 'package:flutter_modular/flutter_modular.dart';
import 'package:mobx/mobx.dart';
import 'package:slidy_modular_02/app/modules/home/home_page.dart';

part 'splash_controller.g.dart';

class SplashController = _SplashControllerBase with _$SplashController;

abstract class _SplashControllerBase with Store {
  void goHomePage() {
    Future.delayed(Duration(seconds: 2), () {
      Modular.to.pushReplacementNamed(HomePage.route);
    });
  }
}
