import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
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
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextFormField(
                  controller: controller.userNameController,
                  decoration: InputDecoration(labelText: "Name"),
                ),
                TextFormField(
                  controller: controller.userLastNameController,
                  decoration: InputDecoration(labelText: "Last Name"),
                ),
                TextFormField(
                  controller: controller.userEmailController,
                  decoration: InputDecoration(labelText: "Email"),
                ),
                Card(
                  child: FlatButton(
                      onPressed: () => controller.createUserAccount(),
                      child: Text("Save")),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
