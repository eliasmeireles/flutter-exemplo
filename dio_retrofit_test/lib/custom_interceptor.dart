import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:logger/logger.dart';

final logger = Logger();

class CustomInterceptor extends InterceptorsWrapper {
  @override
  Future onRequest(RequestOptions options) async {
    options.headers.addAll({"Authorization": UniqueKey().toString()});
    log("==> ${options.method} ${options.uri}");
    log("Headers${options.headers}");
    return super.onRequest(options);
  }

  @override
  Future onResponse(Response response) async {
    log("<== ${response.request.method} -> (${response.statusCode}) ${response.request.uri}");
    log("Headers ${response.headers.toString()}");
    log("${response.data}");
    super.onResponse(response);
  }

  @override
  Future onError(DioError err) async {
    return super.onError(err);
  }
}
