import 'package:mobx/mobx.dart';
import 'package:mobxapp/model/client.dart';

part 'controller.g.dart';

class Controller = ControllerBase with _$Controller;

abstract class ControllerBase with Store {
  final Client client = Client();

  bool isValid() =>
      nameValidation() == null &&
      middleNameValidation() == null &&
      emailValidation() == null &&
      cpfValidation() == null;

  String nameValidation() {
    if (client.name == null || client.name.isEmpty) {
      return "Required field";
    }
    return null;
  }

  String middleNameValidation() {
    if (client.middleName == null || client.middleName.isEmpty) {
      return "Required field";
    }
    return null;
  }

  String emailValidation() {
    if (client.email == null || client.email.isEmpty) {
      return "Required field";
    }
    return null;
  }

  String cpfValidation() {
    if (client.cpf == null || client.cpf.isEmpty) {
      return "Required field";
    }
    return null;
  }

  void dispose() {

  }
}
