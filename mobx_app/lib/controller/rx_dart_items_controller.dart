import 'package:mobx/mobx.dart';
import 'package:mobxapp/model/item_model.dart';
import 'package:rxdart/rxdart.dart';

part 'rx_dart_items_controller.g.dart';

class RxDartItemsController = _RxDartItemsControllerBase
    with _$RxDartItemsController;

abstract class _RxDartItemsControllerBase with Store {
  final listItems = BehaviorSubject<List<ItemModel>>.seeded([]);
  final filter = BehaviorSubject<String>.seeded('');

  ObservableStream<List<ItemModel>> output;

  _RxDartItemsControllerBase() {
    output = Rx.combineLatest2<List<ItemModel>, String, List<ItemModel>>(
        listItems.stream, filter.stream, (list, filter) {
      if (filter.isEmpty) {
        return list;
      } else {
        return list
            .where((item) =>
                item.title.toLowerCase().contains(filter.toLowerCase()))
            .toList();
      }
    }).asObservable(initialValue: []);
  }

  @computed
  int get totalChecked => output.value.where((element) => element.check).length;


  setFilter(String value) => filter.add(value);

  @action
  addItem(ItemModel value) {
    var list = List<ItemModel>.from(listItems.value);
    list.add(value);
    listItems.add(list);
  }

  @action
  removeItem(ItemModel value) {
    var list = List<ItemModel>.from(listItems.value);
    list.removeWhere((item) => item.title == value.title);
    listItems.add(list);
  }
}
