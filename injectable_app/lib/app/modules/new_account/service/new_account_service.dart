import 'package:injectable_app/core/models/user.dart';

abstract class NewAccountService {
  Future<User> createUserAccount(User user);
}
