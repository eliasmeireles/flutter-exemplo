import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class Zoom {
  static const MethodChannel _channel = const MethodChannel('ZOOM_CHANNEL');

  static Future<String> startMeeting(MeetingConfig config) async {
    var encode = json.encode(config);
    final String version =
        await _channel.invokeMethod('zoomJoinMeeting', encode);
    return version;
  }
}

class MeetingConfig {
  final String meetingNumber;
  final String meetingPassword;
  final String jwtToken;
  final String userName;

  MeetingConfig({
    @required this.meetingNumber,
    @required this.meetingPassword,
    @required this.jwtToken,
    @required this.userName,
  });

  Map<String, dynamic> toJson() => {
        'meetingNumber': meetingNumber,
        'meetingPassword': meetingPassword,
        'jwtToken': jwtToken,
        'userName': userName,
      };
}
