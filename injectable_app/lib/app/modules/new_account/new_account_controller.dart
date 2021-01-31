import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/new_account/repository/new_account_repository.dart';
import 'package:mobx/mobx.dart';

part 'new_account_controller.g.dart';

@injectable
class NewAccountController = _NewAccountControllerBase
    with _$NewAccountController;

abstract class _NewAccountControllerBase with Store {
  final NewAccountRepository _repository;

  _NewAccountControllerBase(this._repository);

  Future createUserAccount() async {
    _repository.createUserAccount();
  }
}
