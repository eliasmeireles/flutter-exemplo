import 'package:clubehealth/rest_client.dart';
import 'package:clubehealth/task.dart';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';
import 'package:dio/dio.dart';

import 'custom_interceptor.dart';

final logger = Logger();

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final List<Task> tasks = List<Task>();

  final dio = Dio();

  @override
  void initState() {
    super.initState();
    dio.interceptors.add(CustomInterceptor());
    dio.options.headers["Demo-Header"] =
        "demo header"; // config your dio headers globally
    final client = RestClient(dio);
    client
        .getTasks()
        .then((it) => setState(() => this.tasks.addAll(it)))
        .catchError((error) => logger.i(error));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: Scaffold(
        appBar: AppBar(),
        body: ListView.builder(
            itemCount: tasks.length,
            itemBuilder: (context, index) {
              var task = tasks[index];
              return Card(
                child: Image.network(
                  "https://coverfiles.alphacoders.com/113/113780.png",
                  width: double.infinity,
                  height: 255,
                ),
              );
            }),
      ),
    );
  }
}
