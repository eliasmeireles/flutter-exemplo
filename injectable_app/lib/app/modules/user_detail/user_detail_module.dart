import 'package:get_it/get_it.dart';
import 'package:injectable_app/core/models/user.dart';

import 'user_detail_controller.dart';
import 'package:flutter_modular/flutter_modular.dart';

import 'user_detail_page.dart';

class UserDetailModule extends ChildModule {
  @override
  List<Bind> get binds => [
        Bind((i) => GetIt.instance.get<UserDetailController>()),
      ];

  @override
  List<ModularRouter> get routers => [
        ModularRouter(
          UserDetailPage.route,
          child: (_, args) => UserDetailPage(args.data as User),
        ),
      ];

  static Inject get to => Inject<UserDetailModule>.of();
}
