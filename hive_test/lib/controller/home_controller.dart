import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:hive/hive.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:hive_test/hive/hive_config.dart';
import 'package:hive_test/model/message.dart';
import 'package:hive_test/model/user.dart';
import 'package:uuid/uuid.dart';

class HomeController {
  Box boxMessages;
  final TextEditingController messageInputTextController =
      new TextEditingController();
  final User user = new User(
      idUser: "b0578f43-3262-4590-b588-c393f4a06b6e", name: "Elias Meireles");

  ValueNotifier<List<Message>> messages = ValueNotifier<List<Message>>([]);

  Future init() async {
    await Hive.openBox<Message>(HiveConfig.boxMessages).then(
      (value) {
        boxMessages = value;
        boxMessages.listenable().addListener(() => _messageChangeListener());
        _messageChangeListener();
      },
    );
  }

  void send() async {
    var messageContent = messageInputTextController.text.trim();
    messageInputTextController.text = '';

    Message message = new Message(
        user: user, idMessage: Uuid().v4(), content: messageContent);

    await boxMessages.put('${DateTime.now().microsecondsSinceEpoch}', message);
  }

  void _messageChangeListener() {
    messages.value = boxMessages.values.toList();
  }

  void dispose() {
    boxMessages.listenable().removeListener(() => _messageChangeListener());
  }
}
