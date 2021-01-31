import 'dart:developer';

import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/new_account/service/new_account_service.dart';
import 'package:injectable_app/core/database/fake_database.dart';
import 'package:injectable_app/core/models/user.dart';

@prod
@Injectable(as: NewAccountService)
class NewAccountServiceImpl implements NewAccountService {
  final FakeDatabase _database;

  NewAccountServiceImpl(this._database);

  Future<User> createUserAccount(User user) async {
    return await _database.addNewUser(user);
  }
}
