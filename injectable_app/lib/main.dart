import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';

import 'app/app_module.dart';
import 'core/dependecies/get_config.dart';

void main() {
  configureDependencies(Env.PROD);
  runApp(ModularApp(module: AppModule()));
}
