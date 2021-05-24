#import "SDKStartJoinMeetingPresenter.h"
#import "SDKStartJoinMeetingPresenter+JoinMeetingOnly.h"
#import "SDKStartJoinMeetingPresenter+MeetingServiceDelegate.h"

@interface SDKStartJoinMeetingPresenter()

@end

@implementation SDKStartJoinMeetingPresenter


- (void)joinMeeting:(NSString*)meetingNo withPassword:(NSString*)pwd name:(NSString*)name rootVC:(UIViewController *)rootVC temChat:(BOOL)temChat temCompartilhamento:(BOOL)temCompartilhamento
{
    self.rootVC = rootVC;

    [self initSettings: temChat temCompartilhamento:temCompartilhamento];

    [self initDelegate];

    if (pwd != nil) {
        [self joinMeeting:(NSString *)meetingNo withPassword:pwd name:name];
    } else {
        [self joinMeeting:(NSString *)meetingNo name:name];
    }
}

- (void)initDelegate
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms)
    {
        ms.delegate = self;
    }
    
    //optional for custom-ui meeting
    if ([[[MobileRTC sharedRTC] getMeetingSettings] enableCustomMeeting]) {
        MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
        if (ms)
        {
            ms.customizedUImeetingDelegate = self;
        }
    }
}

- (void)initSettings:(BOOL)temChat temCompartilhamento:(BOOL)temCompartilhamento
{
    [[MobileRTC sharedRTC] getMeetingSettings].enableCustomMeeting = YES;
    [[MobileRTC sharedRTC] getMeetingSettings].meetingInviteHidden = YES;
    [[MobileRTC sharedRTC] getMeetingSettings].meetingChatHidden = !temChat;
    [[MobileRTC sharedRTC] getMeetingSettings].meetingShareHidden = !temCompartilhamento;
    [[[MobileRTC sharedRTC] getMeetingSettings] setMuteAudioWhenJoinMeeting:YES];
    [[[MobileRTC sharedRTC] getMeetingSettings] setMuteVideoWhenJoinMeeting:YES];
    [[MobileRTC sharedRTC] getMeetingSettings].meetingTitleHidden = YES;
    [[MobileRTC sharedRTC] getMeetingSettings].meetingPasswordHidden = YES;
}

- (void)dealloc
{
    self.rootVC = nil;
    self.customMeetingVC = nil;
    
    [[MobileRTC sharedRTC] getMeetingService].customizedUImeetingDelegate = nil;
}

@end
