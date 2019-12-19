package lyh.fluwx_worker;

import com.tencent.wework.api.model.BaseMessage;
import com.tencent.wework.api.model.WWAuthMessage;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

class ResponseHandler {
    private static MethodChannel channel;

    static void setMethodChannel(MethodChannel c){
        channel = c;
    }

    static void handleResponse(BaseMessage resp){
        if (resp instanceof WWAuthMessage.Resp) {
            Map<String,Object> result = new HashMap<>();
            WWAuthMessage.Resp rsp = (WWAuthMessage.Resp) resp;
            result.put("errCode",rsp.errCode);
            result.put("code",rsp.code);
            result.put("state",rsp.state);

            channel.invokeMethod("onAuthResponse",result);
        }
    }
}
