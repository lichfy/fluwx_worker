package lyh.fluwx_worker;

import com.tencent.wework.api.IWWAPI;
import com.tencent.wework.api.IWWAPIEventHandler;
import com.tencent.wework.api.WWAPIFactory;
import com.tencent.wework.api.model.BaseMessage;
import com.tencent.wework.api.model.WWAuthMessage;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;


class APIHandler {
    private static FlutterPlugin.FlutterPluginBinding flutterPluginBinding;
    /*private static PluginRegistry.Registrar registrar;*/
    static IWWAPI iwwapi;
//
    static void setFlutterPluginBinding(FlutterPlugin.FlutterPluginBinding arg){
        flutterPluginBinding = arg;
    }

    static void registerApp(MethodCall call, MethodChannel.Result result){
        if (iwwapi != null){
            result.success(true);
            return;
        }

        Object schema = call.argument("schema");
        if (schema == null || schema.toString().length() == 0){
            result.error("invalid schema", "are you sure your schema is correct ?",schema);
            return;
        }

        iwwapi = WWAPIFactory.createWWAPI(flutterPluginBinding.getApplicationContext());
        boolean registered = iwwapi.registerApp(schema.toString());

        result.success(registered);
    }

    static void checkWeChatInstallation(MethodChannel.Result result) {
        if (iwwapi == null) {
            result.error("wxapi not configured", "please config  wxapi first", null);
        } else {
            result.success(iwwapi.isWWAppInstalled());
        }

    }

    static void sendAuth(MethodCall call, MethodChannel.Result result){
        final WWAuthMessage.Req req = new WWAuthMessage.Req();
        req.sch = call.argument("schema");
        req.appId = call.argument("appId");
        req.agentId = call.argument("agentId");
        req.state = call.argument("state");

        boolean ret = iwwapi.sendMessage(req,new IWWAPIEventHandler(){

            @Override
            public void handleResp(BaseMessage resp) {
                ResponseHandler.handleResponse(resp);
            }
        });

        result.success(ret);
    }
}
