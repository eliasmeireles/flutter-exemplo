import 'package:flutter_modular/flutter_modular_test.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:injectable_app/app/modules/user_detail/user_detail_controller.dart';
import 'package:injectable_app/app/modules/user_detail/user_detail_module.dart';

void main() {
  initModule(UserDetailModule());
  // UserDetailController userdetail;
  //
  setUp(() {
    //     userdetail = UserDetailModule.to.get<UserDetailController>();
  });

  group('UserDetailController Test', () {
    //   test("First Test", () {
    //     expect(userdetail, isInstanceOf<UserDetailController>());
    //   });

    //   test("Set Value", () {
    //     expect(userdetail.value, equals(0));
    //     userdetail.increment();
    //     expect(userdetail.value, equals(1));
    //   });
  });
}
