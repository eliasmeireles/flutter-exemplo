import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';
import 'package:mobxapp/controller/controller.dart';

class BodyWidget extends StatelessWidget {

  Widget _textField({
    String labelText,
    Function onChanged,
    String Function() validation,
  }) {
    return Padding(
      padding: const EdgeInsets.only(top: 16.0),
      child: TextField(
        decoration: InputDecoration(
          border: OutlineInputBorder(),
          labelText: labelText,
          errorText: validation == null ? null : validation(),
        ),
        onChanged: onChanged,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final controller = GetIt.I.get<Controller>();

    return Center(
      child: Padding(
        padding: const EdgeInsets.only(left: 16.0, right: 16.0, bottom: 25),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            Observer(
              builder: (_) {
                return _textField(
                  labelText: "Name",
                  onChanged: controller.client.changeName,
                  validation: controller.nameValidation,
                );
              },
            ),
            Observer(
              builder: (_) {
                return _textField(
                  labelText: "Middle name",
                  onChanged: controller.client.changeMiddleName,
                  validation: controller.middleNameValidation,
                );
              },
            ),
            Observer(
              builder: (_) {
                return _textField(
                  labelText: "Email",
                  onChanged: controller.client.changeEmail,
                  validation: controller.emailValidation,
                );
              },
            ),
            Observer(
              builder: (_) {
                return _textField(
                  labelText: "Cpf",
                  onChanged: controller.client.changeCpf,
                  validation: controller.cpfValidation,
                );
              },
            ),
            Observer(
              builder: (_) {
                return RaisedButton(
                  onPressed: controller.isValid() ? () {} : null,
                  child: Text("Run"),
                );
              },
            )
          ],
        ),
      ),
    );
  }
}
