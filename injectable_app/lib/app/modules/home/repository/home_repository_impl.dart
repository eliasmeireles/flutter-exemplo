import 'package:injectable/injectable.dart';
import 'package:injectable_app/app/modules/home/service/home_service.dart';
import 'package:injectable_app/core/models/user.dart';

import 'home_repository.dart';

@prod
@Injectable(as: HomeRepository)
class HomeRepositoryImpl implements HomeRepository {
  final HomeService _service;

  HomeRepositoryImpl(this._service);

  @override
  List<User> getUsers() {
    return _service.getUser();
  }

  @override
  Future removeUser(User user) async {
    _service.removeUser(user);
  }
}
