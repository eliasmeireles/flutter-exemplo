
import 'package:mobx/mobx.dart';

part 'client.g.dart';

class Client  = _ClientBase with _$Client;

abstract class _ClientBase with Store {
  @observable
  String name;

  @observable
  String middleName;

  @observable
  String email;

  @observable
  String cpf;

  @action
  changeName(String value) => name = value;

  @action
  changeMiddleName(String value) => middleName = value;

  @action
  changeEmail(String value) => email = value;

  @action
  changeCpf(String value) => cpf = value;
}