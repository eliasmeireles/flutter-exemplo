import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:injectable_app/app/modules/home/home_module.dart';
import 'package:injectable_app/app/modules/new_account/new_account_module.dart';
import 'package:injectable_app/app/modules/new_account/new_account_page.dart';

import 'app_widget.dart';

class AppModule extends MainModule {
  @override
  List<Bind> get binds => [];

  @override
  List<ModularRouter> get routers => [
        ModularRouter(
          Modular.initialRoute,
          transition: TransitionType.fadeIn,
          module: HomeModule(),
        ),
        ModularRouter(
          NewAccountPage.route,
          transition: TransitionType.fadeIn,
          module: NewAccountModule(),
        ),
      ];

  @override
  Widget get bootstrap => AppWidget();

  static Inject get to => Inject<AppModule>.of();
}
