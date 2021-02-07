import 'package:flutter/material.dart';
import 'package:hive_test/controller/home_controller.dart';
import 'package:hive_test/hive/hive_config.dart';
import 'package:hive/hive.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:hive_test/model/message.dart';

void main() async {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Hive Test'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  final String title;

  MyHomePage({this.title});

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final HomeController controller = new HomeController();

  @override
  void initState() {
    super.initState();
    HiveConfig.init(onCompleted: () => controller.init());
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: SafeArea(
        child: Column(
          children: [
            Expanded(
              child: ValueListenableBuilder(
                valueListenable: controller.read,
                builder: (_, value, wid) => !value
                    ? CircularProgressIndicator()
                    : ValueListenableBuilder(
                        valueListenable:
                            Hive.box(HiveConfig.boxMessages).listenable(),
                        builder: (_, Box box, wdg) => ListView.builder(
                            itemCount: box.values.length,
                            itemBuilder: (context, index) {
                              var message =
                                  box.values.elementAt(index) as Message;
                              return Padding(
                                padding: const EdgeInsets.only(
                                    top: 8.0, left: 4, right: 4),
                                child: Dismissible(
                                  confirmDismiss: (call) async {
                                    dialogueConfirmation(context, message);
                                    return false;
                                  },
                                  onDismissed: (DismissDirection direction) =>
                                      message.delete(),
                                  background: Container(
                                    color: Colors.red,
                                    child: Padding(
                                      padding: const EdgeInsets.only(
                                          left: 20, right: 20),
                                      child: Row(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.center,
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceBetween,
                                        children: [
                                          Icon(
                                            Icons.delete_forever,
                                            size: 35,
                                            color: Colors.white,
                                          ),
                                          Icon(
                                            Icons.delete_forever,
                                            size: 35,
                                            color: Colors.white,
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                  key: UniqueKey(),
                                  direction: DismissDirection.horizontal,
                                  child: Container(
                                    width: double.infinity,
                                    child: Card(
                                      color: Colors.white,
                                      margin: EdgeInsets.all(0),
                                      child: Padding(
                                        padding: const EdgeInsets.all(16.0),
                                        child: Column(
                                          mainAxisSize: MainAxisSize.min,
                                          mainAxisAlignment:
                                              MainAxisAlignment.start,
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              message.user.name,
                                              style: TextStyle(
                                                fontSize: 16,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                            Text(
                                              message.createdAt.toString(),
                                              style: TextStyle(
                                                fontSize: 12,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                            Container(
                                              padding: EdgeInsets.all(8),
                                              margin: EdgeInsets.only(top: 8),
                                              decoration: BoxDecoration(
                                                  color: Colors.grey[300],
                                                  borderRadius:
                                                      BorderRadius.circular(
                                                          12)),
                                              child: Text(message.content),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ),
                                ),
                              );
                            }),
                      ),
              ),
            ),
            Divider(
              height: 8,
            ),
            TextFormField(
              maxLines: 5,
              minLines: 1,
              controller: controller.messageInputTextController,
              decoration: InputDecoration(
                contentPadding: EdgeInsets.all(16.0),
                hintText: 'Message',
                suffixIcon: GestureDetector(
                  onTap: controller.send,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Icon(Icons.send),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void dialogueConfirmation(BuildContext context, Message message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        content: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Text('Would you really like to delete this message?'),
          ),
        ),
        actions: [
          FlatButton(
              onPressed: () => Navigator.pop(context), child: Text('CANCEL')),
          FlatButton(
              onPressed: () {
                message.delete();
                Navigator.pop(context);
              },
              child: Text('YES'))
        ],
      ),
    );
  }
}
