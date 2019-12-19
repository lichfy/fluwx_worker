# fluwx_worker

flutter版企业微信插件

## 使用说明

如果你用过[flutter版微信插件](https://github.com/OpenFlutter/fluwx)，用这个就轻车熟路了，企业微信插件是按那个来封装的。
使用这个插件之前同样最好了解一下[企业微信API](https://work.weixin.qq.com/api/doc/90000/90136/90294),这个插件现在只实现了登录功能

> 注意：你的app要完成完整的登录流程，需要跟自己的服务端相配合，客户端的作用只是拉起企业微信完成授权，取回一个code。这个code相对应的用户信息是需要自己用这个code再发http请求去取回来的，按着[企业微信服务端API](https://work.weixin.qq.com/api/doc/90000/90135/90664)去实现就是了。

这个插件没有发布到pub，加依赖只能用git方式

```yaml
dependencies:
  fluwx_worker:
    git:
      url: https://github.com/lichfy/fluwx_worker
```

下面是主要代码
 ```dart
     import 'package:fluwx_worker/fluwx_worker.dart' as fluwxWorker;

    //初始化
    await fluwxWorker.register(schema: schema,corpId: corpId,agentId: agentId);
    var result = await fluwxWorker.isWeChatInstalled();  //判断是否已安装企业微信

    //企业微信授权
    fluwxWorker.sendAuth(schema: schema,appId: corpId,agentId: agentId);

    //等待授权结果
    fluwxWorker.responseFromAuth.listen((data) async {
      if (data.errCode == 0){
        _result = data.code;   //后续用这个code再发http请求取得UserID
      }else if (data.errCode == 1){
        _result = '授权失败';
      }else {
        _result = '用户取消';
      }
      setState(() {

      });
    });
 ```