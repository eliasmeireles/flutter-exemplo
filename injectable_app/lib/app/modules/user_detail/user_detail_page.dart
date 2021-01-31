import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:injectable_app/core/models/user.dart';
import '../../app_widget.dart';
import 'user_detail_controller.dart';

class UserDetailPage extends StatefulWidget {
  static final String route = '/user-detail-page';

  final User user;

  UserDetailPage(this.user);

  @override
  _UserDetailPageState createState() => _UserDetailPageState();
}

class _UserDetailPageState
    extends ModularState<UserDetailPage, UserDetailController> {
  //use 'controller' variable to access controller

  @override
  Widget build(BuildContext context) {
    var user = widget.user;
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Hero(
          flightShuttleBuilder: flightShuttleBuilder,
          tag: user.id,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text("${user.name} ${user.lastName}"),
              Text("${user.email}"),
            ],
          ),
        ),
      ),
    );
  }
}
