import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'new_account_controller.dart';

class NewAccountPage extends StatefulWidget {
  static final String route = '/new-account-page';

  @override
  _NewAccountPageState createState() => _NewAccountPageState();
}

class _NewAccountPageState
    extends ModularState<NewAccountPage, NewAccountController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("New User"),
      ),
      body: SafeArea(
        child: Center(
          child: FlatButton(
            onPressed: () => controller.createUserAccount(),
            child: Text("Create a user account"),
          ),
        ),
      ),
    );
  }
}
