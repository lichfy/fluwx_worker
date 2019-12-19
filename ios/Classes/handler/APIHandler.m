
#import "APIHandler.h"
#import "StringUtil.h"
#import "FluwxWorkerPlugin.h"
#import "WWKApi.h"

@implementation APIHandler

- (void)registerApp:(FlutterMethodCall *)call result:(FlutterResult)result {
    if (isWeChatRegistered) {
        result(@YES);
        return;
    }

    NSString *schema = call.arguments[@"schema"];
    NSString *corpId = call.arguments[@"corpId"];
    NSString *agentId = call.arguments[@"agentId"];
    isWeChatRegistered = [WWKApi registerApp:schema corpId:corpId agentId:agentId];

    return result(@(isWeChatRegistered));
}

- (void)checkWeChatInstallation:(FlutterMethodCall *)call result:(FlutterResult)result {
    if (!isWeChatRegistered) {
        result([FlutterError errorWithCode:@"wwkapi not configured" message:@"please config  wwkapi first" details:nil]);
        return;
    }else{
        result(@([WWKApi isAppInstalled]));
    }
}

- (void)handleAuth:(FlutterMethodCall *)call result:(FlutterResult)result {
    NSString *state = call.arguments[@"state"];
    WWKSSOReq *req = [[WWKSSOReq alloc] init];
    req.state = state;

    [WWKApi sendReq:req];
    BOOL done = [WWKApi sendReq:req];

    result(@(done));
}

@end