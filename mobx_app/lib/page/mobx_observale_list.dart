import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:mobxapp/controller/items_controller.dart';
import 'package:mobxapp/model/item_model.dart';
import 'package:mobxapp/widget/item_widget.dart';

class MobXObservableList extends StatefulWidget {
  @override
  _MobXObservableListState createState() => _MobXObservableListState();
}

class _MobXObservableListState extends State<MobXObservableList> {
  final ItemsController controller = ItemsController();

  _dialog() {
    var itemModel = ItemModel();
    showDialog(
      context: context,
      builder: (_) {
        return AlertDialog(
          title: Text('Add Item'),
          content: TextField(
            onChanged: itemModel.setTitle,
            decoration: InputDecoration(
                border: OutlineInputBorder(), labelText: 'New item'),
          ),
          actions: <Widget>[
            FlatButton(
              child: Text('Save'),
              onPressed: () {
                controller.addItem(itemModel);
                Navigator.pop(context);
              },
            ),
            FlatButton(
              child: Text('Cancel'),
              onPressed: () => Navigator.pop(context),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: TextField(
          decoration: InputDecoration(hintText: 'Search...'),
          onChanged: controller.setFilter,
        ),
        actions: <Widget>[
          IconButton(
            icon: Observer(
              builder: (_) {
                return Text('${controller.totalChecked}');
              },
            ),
            onPressed: () {},
          )
        ],
      ),
      body: Observer(
        builder: (_) {
          return ListView.builder(
            itemCount: controller.listFiltered.length,
            itemBuilder: (_, index) {
              var item = controller.listFiltered[index];
              return ItemWidget(
                itemModel: item,
                onRemove: () => controller.removeItem(item),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: _dialog,
      ),
    );
  }
}
