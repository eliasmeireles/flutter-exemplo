import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:slidy_modular_02/app/app_widget.dart';
import 'package:slidy_modular_02/app/modules/home/home_module.dart';
import 'package:slidy_modular_02/app/modules/home/home_page.dart';
import 'package:slidy_modular_02/app/modules/splash/splash_module.dart';
import 'package:slidy_modular_02/app/shared/repositories/poke_repository.dart';
import 'package:slidy_modular_02/app/shared/utils/constants.dart';

import 'app_controller.dart';

class AppModule extends MainModule {
  @override
  List<Bind> get binds => [
        Bind((i) => AppController()),
        Bind((i) => PokeRepository(Dio(BaseOptions(baseUrl: URL_BASE)))),
      ];

  @override
  List<Router> get routers => [
        Router(Modular.initialRoute, module: SplashModule()),
        Router(HomePage.route, module: HomeModule()),
      ];

  @override
  Widget get bootstrap => AppWidget();

  static Inject get to => Inject<AppModule>.of();
}
