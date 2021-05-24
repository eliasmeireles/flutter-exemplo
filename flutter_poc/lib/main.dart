import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'chanel/zoom_channel.dart';

void main() {
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
      home: MyHomePage(title: 'POC Zoom with Flutter'),
    );
  }
}

class MyHomePage extends StatelessWidget {
  final String title;
  final TextEditingController _nomeController = TextEditingController();
  final TextEditingController _idReuniaoController = TextEditingController();
  final TextEditingController _senhaController = TextEditingController();

  MyHomePage({Key key, this.title}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    var width = (size.width > 350 ? 350 : size.width).toDouble();
    return Scaffold(
      appBar: AppBar(
        title: Text(this.title),
      ),
      body: SingleChildScrollView(
        child: ListView(
          shrinkWrap: true,
          padding: EdgeInsets.all(16.0),
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                'Entrar em uma reunião no Zoom',
                style: TextStyle(
                  color: Colors.black54,
                  fontSize: 24.0,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(
                  top: 24, bottom: 32, left: 16, right: 16),
              child: Column(
                children: [
                  SizedBox(
                    width: width,
                    child: TextField(
                      controller: _nomeController,
                      decoration: InputDecoration(
                        labelText: 'Nome',
                      ),
                      style: TextStyle(
                        fontSize: 24.0,
                      ),
                    ),
                  ),
                  SizedBox(
                    width: width,
                    child: TextField(
                      controller: _idReuniaoController,
                      decoration: InputDecoration(
                        labelText: 'ID Reunião',
                      ),
                      style: TextStyle(
                        fontSize: 24.0,
                      ),
                    ),
                  ),
                  SizedBox(
                    width: width,
                    child: TextField(
                      controller: _senhaController,
                      decoration: InputDecoration(
                        labelText: 'Senha',
                      ),
                      style: TextStyle(
                        fontSize: 24.0,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            SizedButton(
              action: _comecarReuniao,
              label: 'Começar uma nova reunião',
            ),
            SizedButton(
              action: _navigateToOutraPagina,
              label: 'Ir para outra pagina',
            ),
          ],
        ),
      ),
    );
  }

  void _comecarReuniao(BuildContext context) async {
    final String idReuniao = _idReuniaoController.text;
    final String senha = _senhaController.text;
    final String nome = _nomeController.text;

    print('ID Reunião: $idReuniao');
    print('Senha: $senha');
    print('Nome: $nome');
    print('Começar nova reunião');

    String platformVersion = '';
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Zoom.startMeeting(MeetingConfig(
        meetingNumber: idReuniao,
        meetingPassword: senha,
        userName: nome,
        jwtToken: null,
      ));
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    print(platformVersion);
  }

  void _navigateToOutraPagina(BuildContext context) {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => OutraPagina()),
    );
  }
}

class SizedButton extends StatelessWidget {
  final String label;
  final Function action;

  const SizedButton({Key key, this.label, this.action}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: 250,
      child: ElevatedButton(
        onPressed: () => action(context),
        child: Text(this.label),
      ),
    );
  }
}

class OutraPagina extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Outra Pagina'),
      ),
      body: Center(
        child: Container(
          child: Text('Outra Página'),
        ),
      ),
    );
  }
}
