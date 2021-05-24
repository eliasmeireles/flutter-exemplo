#import <Foundation/Foundation.h>
#import "CustomMeetingViewController.h"

@interface SDKStartJoinMeetingPresenter : NSObject

- (void)joinMeeting:(NSString*)meetingNo withPassword:(NSString*)pwd name:(NSString*)name rootVC:(UIViewController *)rootVC temChat:(BOOL)temChat temCompartilhamento:(BOOL)temCompartilhamento;

@property (retain, nonatomic) UIViewController *rootVC;

@property (retain, nonatomic) CustomMeetingViewController *customMeetingVC;

@property (retain, nonatomic) UIViewController *mainVC;

@property (weak) id<MobileRTCMeetingServiceDelegate>delegate;

@end

