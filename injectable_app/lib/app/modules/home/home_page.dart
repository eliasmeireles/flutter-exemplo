import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:injectable_app/app/modules/new_account/new_account_page.dart';
import 'package:injectable_app/app/modules/user_detail/user_detail_page.dart';
import '../../app_widget.dart';
import 'home_controller.dart';

class HomePage extends StatefulWidget {
  final String title;

  const HomePage({Key key, this.title = "Home"}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends ModularState<HomePage, HomeController> {
  //use 'controller' variable to access controller

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => Modular.to.pushNamed(NewAccountPage.route),
        child: Icon(
          Icons.add,
          color: Colors.white,
        ),
      ),
      body: Observer(
        builder: (_) {
          if (controller.users.isEmpty) {
            return Center(
              child: Text("No users."),
            );
          }
          var users = controller.users;
          return ListView.builder(
              itemCount: users.length,
              itemBuilder: (context, index) {
                return Card(
                    child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: InkWell(
                    onTap: () => Modular.to.pushNamed(UserDetailPage.route,
                        arguments: users[index]),
                    child: Hero(
                      flightShuttleBuilder: flightShuttleBuilder,
                      tag: users[index].id,
                      child: Row(
                        children: [
                          Column(
                            mainAxisSize: MainAxisSize.min,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                  "${users[index].name} ${users[index].lastName}"),
                              Text("${users[index].email}"),
                            ],
                          ),
                          Spacer(),
                          GestureDetector(
                            onTap: () => controller.removeUser(users[index]),
                            child: Icon(
                              Icons.delete_forever,
                              color: Colors.red,
                            ),
                          )
                        ],
                      ),
                    ),
                  ),
                ));
              });
        },
      ),
    );
  }
}
