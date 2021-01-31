import 'package:injectable_app/core/models/user.dart';

abstract class HomeService {
  List<User> getUser();

  Future removeUser(User user);
}
