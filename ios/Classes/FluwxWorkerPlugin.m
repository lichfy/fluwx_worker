#import "FluwxWorkerPlugin.h"
#import "WWKApi.h"

@interface FluwxWorkerPlugin () <WWKApiDelegate>

@end

@implementation FluwxWorkerPlugin

BOOL isWWXRegistered = NO;
FlutterMethodChannel *channel;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  //FlutterMethodChannel* 
  channel = [FlutterMethodChannel
      methodChannelWithName:@"fluwx_worker"
            binaryMessenger:[registrar messenger]];
  FluwxWorkerPlugin* instance = [[FluwxWorkerPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
  [registrar addApplicationDelegate:instance];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
   if ([@"registerApp" isEqualToString:call.method]) {
     
        if (isWWXRegistered) {
            result(@YES);
            return;
        }

        NSString *schema = call.arguments[@"schema"];
        NSString *corpId = call.arguments[@"corpId"];
        NSString *agentId = call.arguments[@"agentId"];
        isWWXRegistered = [WWKApi registerApp:schema corpId:corpId agentId:agentId];

        result(@(isWWXRegistered));
        return;
    }

    if ([@"isWeChatInstalled" isEqualToString:call.method]) {
        if (!isWWXRegistered) {
            result([FlutterError errorWithCode:@"wwkapi not configured" message:@"please config  wwkapi first" details:nil]);
            return;
        }else{
            result(@([WWKApi isAppInstalled]));
        }
        return;
    }

    if ([@"sendAuth" isEqualToString:call.method]) {
        NSString *state = call.arguments[@"state"];
        WWKSSOReq *req = [[WWKSSOReq alloc] init];
        req.state = state;

        [WWKApi sendReq:req];
        BOOL done = [WWKApi sendReq:req];

        result(@(done));
        return;
    }

    result(FlutterMethodNotImplemented);  
}

- (void)onResp:(WWKBaseResp *)resp {
    /* SSO的回调 */
    if ([resp isKindOfClass:[WWKSSOResp class]]) {
        WWKSSOResp *authResp = (WWKSSOResp *)resp;
        NSDictionary *result = @{
                @"errCode": @(authResp.errCode),
                @"code": [self nilToEmpty:authResp.code],
                @"state": [self nilToEmpty:authResp.state]
        };
        [channel invokeMethod:@"onAuthResponse" arguments:result];
    }
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
    return [self handleOpenURL:url sourceApplication:nil];
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
    return [self handleOpenURL:url sourceApplication:sourceApplication];
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<NSString *, id> *)options {
    return [WWKApi handleOpenURL:url delegate:self];
}

- (BOOL)handleOpenURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication {
    /*! @brief 处理外部调用URL的时候需要将URL传给SDK进行相关处理
     * @param url 外部调用传入的url
     * @param delegate 当前类需要实现WWKApiDelegate对应的方法
     */
    return [WWKApi handleOpenURL:url delegate:self];
}

- (void)unregisterApp:(FlutterMethodCall *)call result:(FlutterResult)result {
    isWWXRegistered = false;
    result(@YES);
}

- (NSString *)nilToEmpty:(NSString *)string {
    return string == nil?@"":string;
}

@end
