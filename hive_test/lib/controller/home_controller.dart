import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:hive/hive.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:hive_test/hive/hive_config.dart';
import 'package:hive_test/model/favorite_message.dart';
import 'package:hive_test/model/message.dart';
import 'package:hive_test/model/user.dart';
import 'package:rxdart/rxdart.dart';
import 'package:uuid/uuid.dart';

class HomeController {
  Box boxMessages;
  final TextEditingController messageInputTextController =
      new TextEditingController();
  final User user = new User(
      idUser: "b0578f43-3262-4590-b588-c393f4a06b6e", name: "Elias Meireles");

  final List<FavoriteMessage> fMessages = List<FavoriteMessage>();
  final StreamController<List<Message>> messagesStreamController =
      StreamController();
  Stream<List<FavoriteMessage>> stream;
  final StreamController<List<FavoriteMessage>>
      favoriteMessagesStreamController = StreamController();

  Future init() async {
    await Hive.openBox<Message>(HiveConfig.boxMessages).then((box) {
      boxMessages = box;
      boxMessages.listenable().addListener(() => _messageChangeListener());
      _messageChangeListener();
    });

    stream = Rx.combineLatest2(messagesStreamController.stream,
        favoriteMessagesStreamController.stream,
        (List<Message> messages, List<FavoriteMessage> favoritesMessages) {
      return messages.map((message) {
        final isFavorite = favoritesMessages?.firstWhere(
                (favorite) =>
                    favorite?.message?.idMessage == message?.idMessage,
                orElse: () => null) !=
            null;
        return FavoriteMessage(message: message, favorite: isFavorite);
      }).toList();
    });
  }

  void send() async {
    var messageContent = messageInputTextController.text.trim();
    messageInputTextController.text = '';

    Message message = new Message(
      user: user,
      idMessage: Uuid().v4(),
      content: messageContent,
    );

    await boxMessages.put('${DateTime.now().microsecondsSinceEpoch}', message);
  }

  void _messageChangeListener() {
    favoriteMessagesStreamController.add(fMessages);
    messagesStreamController.add(boxMessages.values.toList());
  }

  void favoritesMessages(FavoriteMessage favoriteMessage) {
    var favoriteExists = false;

    for (FavoriteMessage element in fMessages) {
      if (element.message.idMessage == favoriteMessage.message.idMessage) {
        fMessages.remove(element);
        favoriteExists = true;
        break;
      }
    }

    if (!favoriteExists) {
      favoriteMessage.favorite = true;
      fMessages.add(favoriteMessage);
    }
    favoriteMessagesStreamController.add(fMessages);
  }

  void dispose() {
    messagesStreamController.close();
    favoriteMessagesStreamController.close();
    boxMessages.listenable().removeListener(() => _messageChangeListener());
  }
}
