import 'package:injectable_app/core/models/user.dart';

abstract class HomeRepository {
  List<User> getUsers();

  Future removeUser(User user);
}
