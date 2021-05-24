#import "SDKSharePresenter.h"

@implementation SDKSharePresenter

- (void)startOrStopAppShare
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms) {
        if (ms.isStartingShare) {
            [ms stopAppShare];
        } else {
            [ms startAppShare];
        }
    }
}

- (void)appShareWithView:(UIView *)view
{
    [[[MobileRTC sharedRTC] getMeetingService] appShareWithView:view];
}

@end
