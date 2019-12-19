#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

@class StringUtil;

@interface APIHandler : NSObject
- (void)registerApp:(FlutterMethodCall *)call result:(FlutterResult)result;
- (void)checkWeChatInstallation:(FlutterMethodCall *)call result:(FlutterResult)result;
- (void)handleAuth:(FlutterMethodCall *)call result:(FlutterResult)result;
@end