import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/new_account/repository/new_account_repository.dart';
import 'package:injectable_app/app/modules/new_account/service/new_account_service.dart';

@prod
@Injectable(as: NewAccountRepository)
class NewAccountRepositoryImpl implements NewAccountRepository {
  final NewAccountService _service;

  NewAccountRepositoryImpl(this._service);

  Future createUserAccount() async {
    _service.createUserAccount();
  }
}
