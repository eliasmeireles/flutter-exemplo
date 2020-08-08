import 'package:mobx/mobx.dart';
import 'package:mobxapp/model/item_model.dart';

part 'items_controller.g.dart';

class ItemsController = _ItemsControllerBase with _$ItemsController;

abstract class _ItemsControllerBase with Store {
  @observable
  ObservableList<ItemModel> listItems = [
    ItemModel(title: 'Item 01', check: false),
    ItemModel(title: 'Item 02', check: false),
    ItemModel(title: 'Item 03', check: true),
    ItemModel(title: 'Item 04', check: false),
  ].asObservable();

  @computed
  int get totalChecked => listItems.where((element) => element.check).length;

  @computed
  List<ItemModel> get listFiltered {
    if (filter.isEmpty) {
      return listItems;
    } else {
      return listItems
          .where(
              (item) => item.title.toLowerCase().contains(filter.toLowerCase()))
          .toList();
    }
  }

  @observable
  String filter = '';

  @action
  setFilter(String value) => filter = value;

  @action
  addItem(ItemModel value) {
    listItems.add(value);
  }

  @action
  removeItem(ItemModel value) {
    listItems.removeWhere((item) => item.title == value.title);
  }

  @action
  setListItems(List<ItemModel> value) => this.listItems = value;
}
