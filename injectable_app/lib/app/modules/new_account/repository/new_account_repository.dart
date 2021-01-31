import 'package:injectable_app/core/models/user.dart';

abstract class NewAccountRepository {
  Future<User> createUserAccount(User user);
}
