import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:slidy_modular/app/app_controller.dart';
import 'package:slidy_modular/app/app_widget.dart';
import 'package:slidy_modular/app/shared/repositories/poke_repository.dart';
import 'package:slidy_modular/app/shared/utils/constants.dart';

import 'page/home/home_controller.dart';
import 'page/home/home_page.dart';
import 'page/other/other_page.dart';

class AppModule extends MainModule {
  @override
  List<Bind> get binds => [
        Bind((i) => AppController()),
        Bind((i) => HomeController(repository: i.get<PokeRepository>())),
        Bind((i) => PokeRepository(i.get<Dio>())),
        Bind((i) => Dio(BaseOptions(baseUrl: URL_BASE))),
      ];

  @override
  List<Router> get routers => [
        Router(Modular.initialRoute, child: (_, args) => HomePage()),
        Router('${OtherPage.route}/:text',
            child: (_, args) => OtherPage(text: args.params['text'])),
      ];

  @override
  Widget get bootstrap => AppWidget();
}
