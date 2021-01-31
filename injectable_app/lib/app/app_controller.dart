import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';

part 'app_controller.g.dart';

@singleton
class AppController = _AppControllerBase with _$AppController;

abstract class _AppControllerBase with Store {}
