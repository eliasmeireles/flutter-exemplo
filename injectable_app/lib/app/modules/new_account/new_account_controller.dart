import 'package:flutter/cupertino.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/new_account/repository/new_account_repository.dart';
import 'package:injectable_app/core/models/user.dart';
import 'package:mobx/mobx.dart';

part 'new_account_controller.g.dart';

@injectable
class NewAccountController = _NewAccountControllerBase
    with _$NewAccountController;

abstract class _NewAccountControllerBase extends Disposable with Store {
  final NewAccountRepository _repository;

  _NewAccountControllerBase(this._repository);

  final TextEditingController userNameController = TextEditingController();
  final TextEditingController userLastNameController = TextEditingController();
  final TextEditingController userEmailController = TextEditingController();

  Future createUserAccount() async {
    await _repository
        .createUserAccount(new User(
      name: userNameController.text,
      lastName: userLastNameController.text,
      email: userEmailController.text,
    ))
        .then((value) {
      print(value);
      Modular.to.pop();
    });
  }

  @override
  void dispose() {
    userNameController.dispose();
    userLastNameController.dispose();
    userEmailController.dispose();
  }
}
