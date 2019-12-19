
#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

#import "WWKApi.h"

@interface ResponseHandler : NSObject<WWKApiDelegate>
+ (instancetype)defaultManager;
- (void) setMethodChannel:(FlutterMethodChannel *) flutterMethodChannel;

@end