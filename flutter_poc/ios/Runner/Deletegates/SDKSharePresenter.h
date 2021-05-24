#import "MobileRTCSample-Prefix.pch"
#import <Foundation/Foundation.h>

@interface SDKSharePresenter : NSObject

- (void)startOrStopAppShare;

- (void)appShareWithView:(UIView *)view;

@end
