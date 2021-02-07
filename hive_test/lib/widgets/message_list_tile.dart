import 'package:flutter/material.dart';
import 'package:hive_test/controller/home_controller.dart';
import 'package:hive_test/model/favorite_message.dart';
import 'package:hive_test/model/message.dart';

class MessageListTile extends StatelessWidget {
  final FavoriteMessage message;
  final HomeController controller;

  MessageListTile({@required this.message, this.controller});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      child: Card(
        color: Colors.white,
        margin: EdgeInsets.all(0),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          message.message.user.name,
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          message.message.createdAt.toString(),
                          style: TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                  IconButton(
                    icon: message.favorite
                        ? Icon(
                            Icons.favorite,
                            color: Colors.red,
                          )
                        : Icon(
                            Icons.favorite_border,
                          ),
                    onPressed: () => controller.favoritesMessages(message),
                  ),
                ],
              ),
              Container(
                padding: EdgeInsets.all(8),
                margin: EdgeInsets.only(top: 8),
                decoration: BoxDecoration(
                    color: Colors.grey[300],
                    borderRadius: BorderRadius.circular(12)),
                child: Text(message.message.content),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
