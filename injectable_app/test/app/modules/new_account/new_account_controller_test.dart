import 'package:flutter_modular/flutter_modular_test.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:injectable_app/app/modules/new_account/new_account_controller.dart';
import 'package:injectable_app/app/modules/new_account/new_account_module.dart';

void main() {
  initModule(NewAccountModule());
  // NewAccountController newaccount;
  //
  setUp(() {
    //     newaccount = NewAccountModule.to.get<NewAccountController>();
  });

  group('NewAccountController Test', () {
    //   test("First Test", () {
    //     expect(newaccount, isInstanceOf<NewAccountController>());
    //   });

    //   test("Set Value", () {
    //     expect(newaccount.value, equals(0));
    //     newaccount.increment();
    //     expect(newaccount.value, equals(1));
    //   });
  });
}
