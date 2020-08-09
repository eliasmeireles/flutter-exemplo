import 'package:flutter_modular/flutter_modular.dart';
import 'package:slidy_modular_02/app/modules/splash/splash_controller.dart';
import 'package:slidy_modular_02/app/modules/splash/splash_page.dart';

class SplashModule extends ChildModule {
  @override
  List<Bind> get binds => [
        Bind((i) => SplashController()),
      ];

  @override
  List<Router> get routers => [
        Router(Modular.initialRoute, child: (_, args) => SplashPage()),
      ];

  static Inject get to => Inject<SplashModule>.of();
}
