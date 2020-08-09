import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:slidy_modular/app/page/home/home_controller.dart';


class OtherPage extends StatefulWidget {
  static final String route = '/other';

  final String text;

  const OtherPage({Key key, this.text}) : super(key: key);


  @override
  _OtherPageState createState() => _OtherPageState();
}

final homeController = Modular.get<HomeController>();

class _OtherPageState extends State<OtherPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Other'),
      ),
      body: Center(
        child: Text('${widget.text}'),
      ),
    );
  }
}
