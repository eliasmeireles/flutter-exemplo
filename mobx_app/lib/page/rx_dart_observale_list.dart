import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:mobxapp/controller/items_controller.dart';
import 'package:mobxapp/controller/rx_dart_items_controller.dart';
import 'package:mobxapp/model/item_model.dart';
import 'package:mobxapp/widget/item_widget.dart';

class RxDartObservableList extends StatefulWidget {
  @override
  _RxDartObservableListState createState() => _RxDartObservableListState();
}

class _RxDartObservableListState extends State<RxDartObservableList> {
  final RxDartItemsController controller = RxDartItemsController();

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
          if (controller.output.data == null) {
            return Center(
              child: CircularProgressIndicator(),
            );
          }
          return ListView.builder(
            itemCount: controller.output.data.length,
            itemBuilder: (_, index) {
              var item = controller.output.data[index];
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
