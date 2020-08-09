import 'package:flutter_modular/flutter_modular.dart';
import 'package:slidy_modular_02/app/shared/repositories/poke_repository.dart';

import 'home_controller.dart';
import 'home_page.dart';

class HomeModule extends ChildModule {
  @override
  List<Bind> get binds => [
        Bind((i) => HomeController(repository: i.get<PokeRepository>())),
      ];

  @override
  List<Router> get routers => [
        Router(HomePage.route, child: (_, args) => HomePage()),
      ];

  static Inject get to => Inject<HomeModule>.of();
}
