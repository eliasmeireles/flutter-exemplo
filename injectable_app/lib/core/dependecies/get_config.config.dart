// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// InjectableConfigGenerator
// **************************************************************************

import 'package:get_it/get_it.dart';
import 'package:injectable/injectable.dart';

import '../../app/app_controller.dart';
import '../database/fake_database.dart';
import '../../app/modules/home/home_controller.dart';
import '../../app/modules/home/repository/home_repository.dart';
import '../../app/modules/home/repository/home_repository_impl.dart';
import '../../app/modules/home/service/home_service.dart';
import '../../app/modules/home/service/home_service_impl.dart';
import '../../app/modules/new_account/new_account_controller.dart';
import '../../app/modules/new_account/repository/new_account_repository.dart';
import '../../app/modules/new_account/repository/new_account_repository_impl.dart';
import '../../app/modules/new_account/service/new_account_service.dart';
import '../../app/modules/new_account/service/new_account_service_impl.dart';
import '../../app/modules/user_detail/user_detail_controller.dart';

/// Environment names
const _prod = 'prod';

/// adds generated dependencies
/// to the provided [GetIt] instance

GetIt $initGetIt(
  GetIt get, {
  String environment,
  EnvironmentFilter environmentFilter,
}) {
  final gh = GetItHelper(get, environment, environmentFilter);
  gh.factory<HomeService>(() => HomeServiceImpl(get<FakeDatabase>()),
      registerFor: {_prod});
  gh.factory<NewAccountService>(
      () => NewAccountServiceImpl(get<FakeDatabase>()),
      registerFor: {_prod});
  gh.factory<UserDetailController>(() => UserDetailController());
  gh.factory<HomeRepository>(() => HomeRepositoryImpl(get<HomeService>()),
      registerFor: {_prod});
  gh.factory<NewAccountRepository>(
      () => NewAccountRepositoryImpl(get<NewAccountService>()),
      registerFor: {_prod});
  gh.factory<HomeController>(() => HomeController(get<HomeRepository>()));
  gh.factory<NewAccountController>(
      () => NewAccountController(get<NewAccountRepository>()));

  // Eager singletons must be registered in the right order
  gh.singleton<FakeDatabase>(FakeDatabase());
  gh.singleton<AppController>(AppController(get<FakeDatabase>()));
  return get;
}
