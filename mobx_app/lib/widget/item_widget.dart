import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:mobx/mobx.dart';
import 'package:mobxapp/model/item_model.dart';

class ItemWidget extends StatelessWidget {
  final ItemModel itemModel;
  final Function onRemove;

  ItemWidget({
    @required this.itemModel,
    @required this.onRemove,
  });

  @override
  Widget build(BuildContext context) {
    return Observer(
      builder: (_) {
        return ListTile(
          title: Text(itemModel.title),
          leading: Checkbox(
            value: itemModel.check,
            onChanged: itemModel.setCheck,
          ),
          trailing: IconButton(
            color: Colors.red,
            iconSize: 30,
            icon: Icon(Icons.remove_circle),
            onPressed: onRemove,
          ),
        );
      },
    );
  }
}
