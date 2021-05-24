#import "SDKVideoPresenter.h"

@implementation SDKVideoPresenter

- (void)muteMyVideo
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms)
    {
        if (![ms canUnmuteMyVideo])
        {
            return;
        }
        BOOL mute = [ms isSendingMyVideo];
        MobileRTCVideoError error = [ms muteMyVideo:mute];
        NSLog(@"MobileRTCVideoError:%d",error);
    }
}

- (void)switchMyCamera
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (!ms) return;
    [ms switchMyCamera];
}

- (BOOL)pinVideo:(BOOL)on withUser:(NSUInteger)userId
{
    return [[[MobileRTC sharedRTC] getMeetingService] pinVideo:YES withUser:userId];
}

- (BOOL)stopUserVideo:(NSUInteger)userID
{
    return [[[MobileRTC sharedRTC] getMeetingService] stopUserVideo:userID];
}

- (BOOL)askUserStartVideo:(NSUInteger)userID
{
    return [[[MobileRTC sharedRTC] getMeetingService] askUserStartVideo:userID];
}

@end
