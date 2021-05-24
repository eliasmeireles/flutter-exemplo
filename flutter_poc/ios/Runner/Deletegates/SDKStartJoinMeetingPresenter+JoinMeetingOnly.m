#import "SDKStartJoinMeetingPresenter+JoinMeetingOnly.h"

@implementation SDKStartJoinMeetingPresenter (JoinMeetingOnly)

- (void)joinMeeting:(NSString*)meetingNo withPassword:(NSString*)pwd name:(NSString*)name
{
    if (![meetingNo length]) {
        return;
    }
    
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms)
    {
                                            NSDictionary *paramDict = @{
                                            kMeetingParam_Username:name,
                                            kMeetingParam_MeetingNumber:meetingNo,
                                            kMeetingParam_MeetingPassword:pwd,
                                            kMeetingParam_NoAudio:@(YES),
                                            kMeetingParam_NoVideo:@(YES),
                                            };

        MobileRTCMeetError ret = [ms joinMeetingWithDictionary:paramDict];

        [[MobileRTC sharedRTC] getMeetingSettings].enableCustomMeeting = YES;
        [[MobileRTC sharedRTC] getMeetingSettings].meetingInviteHidden = YES;
        [[[MobileRTC sharedRTC] getMeetingSettings] setMuteAudioWhenJoinMeeting:YES];
        [[[MobileRTC sharedRTC] getMeetingSettings] setMuteVideoWhenJoinMeeting:YES];
        [[MobileRTC sharedRTC] getMeetingSettings].meetingTitleHidden = YES;
        [[MobileRTC sharedRTC] getMeetingSettings].meetingPasswordHidden = YES;

        NSLog(@"onJoinaMeeting ret:%d", ret);
    }
}

- (void)joinMeeting:(NSString*)meetingNo name:(NSString*)name
{
    if (![meetingNo length]) {
        return;
    }

    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms)
    {
                                            NSDictionary *paramDict = @{
                                            kMeetingParam_Username:name,
                                            kMeetingParam_MeetingNumber:meetingNo,
                                            kMeetingParam_NoAudio:@(YES),
                                            kMeetingParam_NoVideo:@(YES),
                                            };

        MobileRTCMeetError ret = [ms joinMeetingWithDictionary:paramDict];

        NSLog(@"onJoinaMeeting ret:%d", ret);
    }
}

@end
