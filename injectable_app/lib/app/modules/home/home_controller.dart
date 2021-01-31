import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/home/repository/home_repository.dart';
import 'package:injectable_app/core/models/user.dart';
import 'package:mobx/mobx.dart';

part 'home_controller.g.dart';

@injectable
class HomeController = _HomeControllerBase with _$HomeController;

abstract class _HomeControllerBase with Store {
  final HomeRepository _repository;

  _HomeControllerBase(this._repository);

  List<User> get users => _repository.getUsers();

  Future removeUser(User user) async {
    _repository.removeUser(user);
  }
}
