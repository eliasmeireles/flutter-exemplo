import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:slidy_modular/app/page/other/other_page.dart';

import 'home_controller.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final homeController = Modular.get<HomeController>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.check),
        onPressed: () {
          Modular.to.pushNamed('${OtherPage.route}/${homeController.text}');
        },
      ),
      appBar: AppBar(
        title: Text('Home'),
      ),
      body: Observer(
        builder: (_) {
          if (homeController.pokemons.error != null) {
            return Center(
              child: Text('Something wrong is not right!'),
            );
          }

          if (homeController.pokemons.value == null) {
            return Center(
              child: CircularProgressIndicator(),
            );
          }
          var list = homeController.pokemons.value;

          return ListView.builder(
              itemCount: list.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(list[index].name),
                );
              });
        },
      ),
    );
  }
}
