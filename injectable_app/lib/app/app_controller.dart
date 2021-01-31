import 'package:injectable/injectable.dart';
import 'package:injectable_app/core/database/fake_database.dart';
import 'package:injectable_app/core/models/user.dart';
import 'package:mobx/mobx.dart';

part 'app_controller.g.dart';

@singleton
class AppController = _AppControllerBase with _$AppController;

abstract class _AppControllerBase with Store {
  final FakeDatabase _database;

  _AppControllerBase(this._database) {
    _database.addNewUser(User(
        name: "Gabriel",
        lastName: "Maluco",
        email: "maluco.gabriel@gmail.com"));
    _database.addNewUser(User(
        name: "Gabriel 1",
        lastName: "Maluco",
        email: "maluco.gabriel1@gmail.com"));
    _database.addNewUser(User(
        name: "Gabriel 2",
        lastName: "Maluco",
        email: "maluco.gabriel2@gmail.com"));
    _database.addNewUser(User(
        name: "Gabriel 3",
        lastName: "Maluco",
        email: "maluco.gabriel3@gmail.com"));
  }
}
