import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/new_account/repository/new_account_repository.dart';
import 'package:injectable_app/app/modules/new_account/service/new_account_service.dart';
import 'package:injectable_app/core/models/user.dart';

@prod
@Injectable(as: NewAccountRepository)
class NewAccountRepositoryImpl implements NewAccountRepository {
  final NewAccountService _service;

  NewAccountRepositoryImpl(this._service);

  Future<User> createUserAccount(User user) async {
    return await _service.createUserAccount(user);
  }
}
