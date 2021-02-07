import 'package:hive_test/model/message.dart';

class FavoriteMessage {
  String idFavoriteMessage;
  Message message;
  bool favorite;

  FavoriteMessage({this.idFavoriteMessage, this.message, this.favorite = false});
}
