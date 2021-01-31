import 'dart:developer';

import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/new_account/service/new_account_service.dart';

@prod
@Injectable(as: NewAccountService)
class NewAccountServiceImpl implements NewAccountService {
  Future createUserAccount() async {
    log("NewAccountServiceImpl.createUserAccount() was called!");
  }
}
