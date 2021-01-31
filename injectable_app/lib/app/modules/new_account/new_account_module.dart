import 'package:get_it/get_it.dart';

import 'new_account_controller.dart';
import 'package:flutter_modular/flutter_modular.dart';

import 'new_account_page.dart';

class NewAccountModule extends ChildModule {
  @override
  List<Bind> get binds => [
        Bind((i) => GetIt.instance.get<NewAccountController>()),
      ];

  @override
  List<ModularRouter> get routers => [
        ModularRouter(Modular.initialRoute,
            child: (_, args) => NewAccountPage()),
      ];

  static Inject get to => Inject<NewAccountModule>.of();
}
