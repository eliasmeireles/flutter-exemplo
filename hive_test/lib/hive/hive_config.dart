import 'dart:io';

import 'package:hive/hive.dart';
import 'package:hive_test/model/message.dart';
import 'package:hive_test/model/user.dart';
import 'package:path/path.dart' as path;
import 'package:path_provider/path_provider.dart';

class HiveConfig {
  static final String boxUsers = 'users';
  static final String boxMessages = 'messages';

  static Future<void> init({Function onCompleted}) async {
    var dir = await getApplicationDocumentsDirectory();
    Hive.init('${dir.path}/hive_test');
    Hive.registerAdapter(MessageAdapter());
    Hive.registerAdapter(UserAdapter());

    onCompleted?.call();
  }
}
