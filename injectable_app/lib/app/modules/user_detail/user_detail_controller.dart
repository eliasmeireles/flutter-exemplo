import 'package:injectable/injectable.dart';
import 'package:mobx/mobx.dart';

part 'user_detail_controller.g.dart';

@injectable
class UserDetailController = _UserDetailControllerBase
    with _$UserDetailController;

abstract class _UserDetailControllerBase with Store {}
