#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import <MobileRTC/MobileRTC.h>

@interface RemoteShareViewController : UIViewController

@property (assign, nonatomic) NSUInteger activeShareID;

@property (strong, nonatomic) MobileRTCActiveShareView* shareView;

- (void)updateShareView;

@end
