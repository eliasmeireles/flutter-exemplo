import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/home/service/home_service.dart';
import 'package:injectable_app/core/database/fake_database.dart';
import 'package:injectable_app/core/models/user.dart';

@prod
@Injectable(as: HomeService)
class HomeServiceImpl implements HomeService {
  final FakeDatabase _database;

  HomeServiceImpl(this._database);

  @override
  List<User> getUser() {
    return _database.users;
  }

  @override
  Future removeUser(User user) async {
    _database.removeUser(user);
  }
}
