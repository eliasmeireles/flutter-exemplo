import 'package:flutter/cupertino.dart';
import 'package:injectable/injectable.dart';
import 'package:injectable_app/core/models/user.dart';
import 'package:mobx/mobx.dart';

@singleton
class FakeDatabase {
  ObservableList<User> users = ObservableList<User>().asObservable();
  
  Future<User> addNewUser(User user) async {
    var userId = UniqueKey().toString();
    user.id = userId;
    users.add(user);
    return user;
  }

  Future removeUser(User user) async {
    if (user.id != null) {
      for (User u in users) {
        if (u.id == user.id) {
          users.remove(u);
          break;
        }
      }
    }
  }
}
