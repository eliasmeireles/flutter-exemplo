import 'package:flutter/cupertino.dart';

class User {
  String id;
  String name;
  String lastName;
  String email;

  User({
    @required this.name,
    @required this.lastName,
    @required this.email,
  });
}
