#import "FluwxWorkerPlugin.h"
#import "APIHandler.h"

@implementation FluwxWorkerPlugin

BOOL isWeChatRegistered = NO;
APIHandler *_APIHandler;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"fluwx_worker"
            binaryMessenger:[registrar messenger]];
  FluwxWorkerPlugin* instance = [[FluwxWorkerPlugin alloc] init];
  [[ResponseHandler defaultManager] setMethodChannel:channel];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (instancetype)initWithRegistrar:(NSObject <FlutterPluginRegistrar> *)registrar methodChannel:(FlutterMethodChannel *)flutterMethodChannel {
    self = [super init];
    if (self) {
        _APIHandler = [[APIHandler alloc] init];
    }

    return self;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
   if ([@"registerApp" isEqualToString:call.method]) {
        [_APIHandler registerApp:call result:result];
        return;
    }

    if ([@"isWeChatInstalled" isEqualToString:call.method]) {
        [_APIHandler checkWeChatInstallation:call result:result];
        return;
    }


    if ([@"sendAuth" isEqualToString:call.method]) {
        [_APIHandler handleAuth:call result:result];
        return;
    }

    result(FlutterMethodNotImplemented);  
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
    return [self handleOpenURL:url delegate:[FluwxResponseHandler defaultManager]];
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(nullable NSString *)sourceApplication annotation:(id)annotation {
    return [self handleOpenURL:url delegate:[ResponseHandler defaultManager]];
}

- (BOOL)handleOpenURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication {
    /*! @brief 处理外部调用URL的时候需要将URL传给SDK进行相关处理
     * @param url 外部调用传入的url
     * @param delegate 当前类需要实现WWKApiDelegate对应的方法
     */
    return [WWKApi handleOpenURL:url delegate:delegate:[ResponseHandler defaultManager]];
}

- (void)unregisterApp:(FlutterMethodCall *)call result:(FlutterResult)result {
    isWeChatRegistered = false;
    result(@YES);
}

@end
