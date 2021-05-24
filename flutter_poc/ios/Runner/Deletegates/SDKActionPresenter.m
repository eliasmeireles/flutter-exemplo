#import "Runner-Swift.h"
#import "SDKActionPresenter.h"

@implementation SDKActionPresenter

- (BOOL)isMeetingHost
{
    return [[[MobileRTC sharedRTC] getMeetingService] isMeetingHost];
}

- (void)leaveMeeting
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (!ms) return;
    [ms leaveMeetingWithCmd:LeaveMeetingCmd_Leave];
}

- (void)EndMeeting
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (!ms) return;
    [ms leaveMeetingWithCmd:LeaveMeetingCmd_End];
}

- (void)presentParticipantsViewController
{
    AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;

    [[[MobileRTC sharedRTC] getMeetingService] presentParticipantsViewController:[[appDelegate window] rootViewController]];
}

- (BOOL)lockMeeting
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    return [ms lockMeeting:!ms.isMeetingLocked];
}

- (BOOL)lockShare
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    return [ms lockShare:!ms.isShareLocked];
}

@end
