package lyh.fluwx_worker;

import androidx.annotation.NonNull;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.embedding.engine.plugins.FlutterPlugin;

/** FluwxWorkerPlugin */
public class FluwxWorkerPlugin implements FlutterPlugin,MethodCallHandler {

  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "fluwx_worker");
    APIHandler.setFlutterPluginBinding(flutterPluginBinding);
    ResponseHandler.setMethodChannel(channel);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel.setMethodCallHandler(null);
  }

  /** Plugin registration. */
//  public static void registerWith(Registrar registrar) {
//    final MethodChannel channel = new MethodChannel(registrar.messenger(), "fluwx_worker");
//    APIHandler.setRegistrar(registrar);
//    ResponseHandler.setMethodChannel(channel);
//
//    channel.setMethodCallHandler(new FluwxWorkerPlugin());
//  }


  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("registerApp")){
      APIHandler.registerApp(call,result);
      return;
    }

    if (call.method.equals("isWeChatInstalled")){
      APIHandler.checkWeChatInstallation(result);
      return;
    }

    if (call.method.equals("sendAuth")){
        APIHandler.sendAuth(call,result);
        return;
    }

    result.notImplemented();
  }
}
