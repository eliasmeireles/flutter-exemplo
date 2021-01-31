import 'package:get_it/get_it.dart';
import 'package:injectable/injectable.dart';

import 'get_config.config.dart';

final getIt = GetIt.instance;

@InjectableInit(
  initializerName: r'$initGetIt', // default
  preferRelativeImports: true, // default
  asExtension: false, // default
)
GetIt configureDependencies(String env) => $initGetIt(getIt, environment: env);

abstract class Env {
  static const DEV = 'dev';
  static const TEST = 'test';
  static const PROD = 'prod';
}
