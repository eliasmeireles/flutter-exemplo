// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'items_controller.dart';

// **************************************************************************
// StoreGenerator
// **************************************************************************

// ignore_for_file: non_constant_identifier_names, unnecessary_brace_in_string_interps, unnecessary_lambdas, prefer_expression_function_bodies, lines_longer_than_80_chars, avoid_as, avoid_annotating_with_dynamic

mixin _$ItemsController on _ItemsControllerBase, Store {
  Computed<int> _$totalCheckedComputed;

  @override
  int get totalChecked =>
      (_$totalCheckedComputed ??= Computed<int>(() => super.totalChecked,
              name: '_ItemsControllerBase.totalChecked'))
          .value;
  Computed<List<ItemModel>> _$listFilteredComputed;

  @override
  List<ItemModel> get listFiltered => (_$listFilteredComputed ??=
          Computed<List<ItemModel>>(() => super.listFiltered,
              name: '_ItemsControllerBase.listFiltered'))
      .value;

  final _$listItemsAtom = Atom(name: '_ItemsControllerBase.listItems');

  @override
  ObservableList<ItemModel> get listItems {
    _$listItemsAtom.reportRead();
    return super.listItems;
  }

  @override
  set listItems(ObservableList<ItemModel> value) {
    _$listItemsAtom.reportWrite(value, super.listItems, () {
      super.listItems = value;
    });
  }

  final _$filterAtom = Atom(name: '_ItemsControllerBase.filter');

  @override
  String get filter {
    _$filterAtom.reportRead();
    return super.filter;
  }

  @override
  set filter(String value) {
    _$filterAtom.reportWrite(value, super.filter, () {
      super.filter = value;
    });
  }

  final _$_ItemsControllerBaseActionController =
      ActionController(name: '_ItemsControllerBase');

  @override
  dynamic setFilter(String value) {
    final _$actionInfo = _$_ItemsControllerBaseActionController.startAction(
        name: '_ItemsControllerBase.setFilter');
    try {
      return super.setFilter(value);
    } finally {
      _$_ItemsControllerBaseActionController.endAction(_$actionInfo);
    }
  }

  @override
  dynamic addItem(ItemModel value) {
    final _$actionInfo = _$_ItemsControllerBaseActionController.startAction(
        name: '_ItemsControllerBase.addItem');
    try {
      return super.addItem(value);
    } finally {
      _$_ItemsControllerBaseActionController.endAction(_$actionInfo);
    }
  }

  @override
  dynamic removeItem(ItemModel value) {
    final _$actionInfo = _$_ItemsControllerBaseActionController.startAction(
        name: '_ItemsControllerBase.removeItem');
    try {
      return super.removeItem(value);
    } finally {
      _$_ItemsControllerBaseActionController.endAction(_$actionInfo);
    }
  }

  @override
  dynamic setListItems(List<ItemModel> value) {
    final _$actionInfo = _$_ItemsControllerBaseActionController.startAction(
        name: '_ItemsControllerBase.setListItems');
    try {
      return super.setListItems(value);
    } finally {
      _$_ItemsControllerBaseActionController.endAction(_$actionInfo);
    }
  }

  @override
  String toString() {
    return '''
listItems: ${listItems},
filter: ${filter},
totalChecked: ${totalChecked},
listFiltered: ${listFiltered}
    ''';
  }
}
