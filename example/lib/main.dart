import 'dart:io';

import 'package:flutter/material.dart';

import 'package:fluwx_worker/fluwx_worker.dart' as fluwxWorker;

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var _result = 'None';

  final schema = 'wwauth38302c6e89219150000013';
  final corpId = 'ww38302c6e89219150';
  final agentId = '1000013';

  @override
  void initState() {
    super.initState();
    _initFluwx();

    //等待授权结果
    fluwxWorker.responseFromAuth.listen((data) async {
      if (data.errCode == 0){
        _result = data.code;  //后续用这个code再发http请求取得UserID
      }else if (data.errCode == 1){
        _result = '授权失败';
      }else {
        _result = '用户取消';
      }
      setState(() {

      });
    });
  }

  _initFluwx() async {
        await fluwxWorker.register(schema: schema,corpId: corpId,agentId: agentId);
    var result = await fluwxWorker.isWeChatInstalled();
    print("is installed $result");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            OutlineButton(
              onPressed: () {
                fluwxWorker.sendAuth(schema: schema,appId: corpId,agentId: agentId);
              },
              child: Text('企业微信授权'),
            ),
            const Text("响应结果;"),
            Text(_result)
          ],
        ),
      ),
    );
  }
}
