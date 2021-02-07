import 'package:hive/hive.dart';
import 'package:hive_test/model/user.dart';

part 'message.g.dart';

@HiveType(typeId: 2)
class Message extends HiveObject {
  @HiveField(0)
  String idMessage;
  @HiveField(1)
  User user;
  @HiveField(2)
  String content;
  @HiveField(3)
  DateTime createdAt;

  Message({
    this.idMessage,
    this.user,
    this.content,
  }) {
    this.createdAt = DateTime.now();
  }

  @override
  String toString() {
    return 'Message{idMessage: $idMessage, user: $user, content: $content, createdAt: $createdAt}';
  }
}
