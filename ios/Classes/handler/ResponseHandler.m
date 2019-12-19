
#import "ResponseHandler.h"
#import "StringUtil.h"

@implementation ResponseHandler

+ (instancetype)defaultManager {
    static dispatch_once_t onceToken;
    static ResponseHandler *instance;
    dispatch_once(&onceToken, ^{
        instance = [[ResponseHandler alloc] init];
    });
    return instance;
}

FlutterMethodChannel *fluwxMethodChannel = nil;

- (void)setMethodChannel:(FlutterMethodChannel *)flutterMethodChannel {
    fluwxMethodChannel = flutterMethodChannel;
}

- (void)onResp:(WWKBaseResp *)resp {
    /* SSO的回调 */
    if ([resp isKindOfClass:[WWKSSOResp class]]) {
        WWKSSOResp *authResp = (WWKSSOResp *)resp;
        NSDictionary *result = @{
                @"errCode": @(authResp.errCode),
                @"code": [StringUtil nilToEmpty:authResp.code],
                @"state": [StringUtil nilToEmpty:authResp.state]
        };
        [fluwxMethodChannel invokeMethod:@"onAuthResponse" arguments:result];
    }
}

@end