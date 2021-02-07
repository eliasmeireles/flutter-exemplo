import 'package:flutter/material.dart';
import 'package:hive_test/controller/home_controller.dart';
import 'package:hive_test/hive/hive_config.dart';
import 'package:hive/hive.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:hive_test/model/message.dart';
import 'package:hive_test/widgets/delete_dismissible_background.dart';
import 'package:hive_test/widgets/message_list_tile.dart';

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
                valueListenable: controller.messages,
                builder: (controller, List<Message> messages, wdg) {
                  return ListView.builder(
                    itemCount: messages.length,
                    itemBuilder: (context, index) {
                      var message = messages[index];
                      return Padding(
                        padding:
                            const EdgeInsets.only(top: 8.0, left: 4, right: 4),
                        child: Dismissible(
                          confirmDismiss: (call) async {
                            // dialogueConfirmation(context, message);
                            return true;
                          },
                          onDismissed: (DismissDirection direction) =>
                              message.delete(),
                          background: DeleteDismissibleBackground(),
                          key: UniqueKey(),
                          direction: DismissDirection.horizontal,
                          child: MessageListTile(
                            message: message,
                          ),
                        ),
                      );
                    },
                  );
                },
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
  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }
}
