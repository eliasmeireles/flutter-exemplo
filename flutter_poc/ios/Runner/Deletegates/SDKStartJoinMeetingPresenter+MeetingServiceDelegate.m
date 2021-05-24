#import "SDKStartJoinMeetingPresenter+MeetingServiceDelegate.h"
#import "CustomMeetingViewController+MeetingDelegate.h"
#import "Runner-Swift.h"

@implementation SDKStartJoinMeetingPresenter (MeetingServiceDelegate)

#pragma mark - Meeting Service Delegate

- (void)onJoinMeetingConfirmed
{
    NSString *meetingNo = [[MobileRTCInviteHelper sharedInstance] ongoingMeetingNumber];
    NSLog(@"onJoinMeetingConfirmed MeetingNo: %@", meetingNo);
    [[self delegate] onJoinMeetingConfirmed];
}

#pragma mark -- For CustomUI Meeting
- (void)onWaitingRoomStatusChange:(BOOL)needWaiting
{
    if (self.customMeetingVC)
    {
        [self.customMeetingVC onWaitingRoomStatusChange:needWaiting];
    }
}

- (void)onSinkWebinarNeedRegister:(NSString *)registerURL
{
    NSLog(@"%@",registerURL);
}


- (void)onMeetingError:(MobileRTCMeetError)error message:(NSString*)message
{
    [[self delegate] onMeetingError:error message:message];
    NSLog(@"onMeetingError:%zd, message:%@", error, message);
}

- (void)onMeetingStateChange:(MobileRTCMeetingState)state
{
    [[self delegate] onMeetingStateChange:state];
}

- (void)onOngoingShareStopped
{
    NSLog(@"There does not exist ongoing share");
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms)
    {
        [ms startAppShare];
    }
}

-(void)onClickShareScreen:(UIViewController *)parentVC {

    NSLog(@"share");
}

- (BOOL)onClickedAudioButton:(UIViewController*)parentVC {
    
    return NO; // will show the default SDK UI
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (!ms) {
        return NO;
    }
    
    MobileRTCAudioType audioType = [ms myAudioType];
    switch (audioType)
    {
        case MobileRTCAudioType_VoIP: //voip
        case MobileRTCAudioType_Telephony: //phone
        {
            if (![ms canUnmuteMyAudio])
            {
                break;
            }
            BOOL isMuted = [ms isMyAudioMuted];
            [ms muteMyAudio:!isMuted];
            break;
        }
        /*case MobileRTCAudioType_None:
        {
            //Supported VOIP
            if ([ms isSupportedVOIP])
            {
                if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8"))
                {
                    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"To hear others\n please join audio", @"")
                                                                                             message:nil
                                                                                      preferredStyle:UIAlertControllerStyleAlert];
                    
                    [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"Call via Internet", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                        //Join VOIP
                        MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
                        if (ms)
                        {
                            [ms connectMyAudio:YES];
                        }
                    }]];
                    
                    [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"Cancel", nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
                    }]];

                    [[UIApplication.sharedApplication] ]
                      UIApplication.topViewController()?.present(UIViewController, animated: true, completion: nil)
                    [[[self window] rootViewController] presentViewController:alertController animated:YES completion:nil];
                }
            }
            break;
        }*/
    }
    
    return YES;
}

- (void)onSinkMeetingShowMinimizeMeetingOrBackZoomUI:(MobileRTCMinimizeMeetingState)state
{
//    [[self mainVC] ocultarViewCarregando];
//    NSLog(@"onSinkMeetingShowMinimizeMeetingOrBackZoomUI %@",@(state));
}

- (void)onSinkAttendeeChatPriviledgeChanged:(MobileRTCMeetingChatPriviledgeType)currentPrivilege
{
//    [[self mainVC] ocultarViewCarregando];
//    NSLog(@"onSinkAttendeeChatPriviledgeChanged %@",@(currentPrivilege));
}

- (void)onMeetingEndedReason:(MobileRTCMeetingEndReason)reason
{
    [self.delegate onMeetingEndedReason:reason];
    NSLog(@"onMeetingEndedReason %d", reason);
}

#if 0
- (void)onMicrophoneStatusError:(MobileRTCMicrophoneError)error
{
    NSLog(@"onMicrophoneStatusError %d", error);
}
#endif

- (void)onJBHWaitingWithCmd:(JBHCmd)cmd
{
    if (self.mainVC) {
        [self.delegate onJBHWaitingWithCmd:cmd];
    }
}

#if 0
- (BOOL)onClickedInviteButton:(UIViewController*)parentVC addInviteActionItem:(NSMutableArray *)array
{
    return [self.mainVC onClickedInviteButton:parentVC addInviteActionItem:array];
}
#endif

#if 0
- (BOOL)onClickedParticipantsButton:(UIViewController*)parentVC;
{
    return [self.mainVC onClickedParticipantsButton:parentVC];
}
#endif

#if 0
- (BOOL)onClickedEndButton:(UIViewController*)parentVC endButton:(UIButton *)endButton
{
    return [self.mainVC onClickedEndButton:parentVC endButton:endButton];
}
#endif

#if 0
- (void)onClickedDialOut:(UIViewController*)parentVC isCallMe:(BOOL)me
{
    if (self.mainVC) {
        [self.mainVC onClickedDialOut:parentVC isCallMe:me];
    }
}

- (void)onDialOutStatusChanged:(DialOutStatus)status
{
    NSLog(@"onDialOutStatusChanged: %zd", status);
}
#endif

#pragma mark - Handle Session Key
#if 0
- (void)onWaitExternalSessionKey:(NSData*)key
{
    if (self.mainVC) {
        [self.mainVC onWaitExternalSessionKey:key];
    }
}
#endif

#pragma mark - H.323/SIP call state changed
#if 0
- (void)onSendPairingCodeStateChanged:(MobileRTCH323ParingStatus)state MeetingNumber:(unsigned long long)meetingNumber
{
    NSLog(@"onSendPairingCodeStateChanged %zd", state);
}

- (void)onCallRoomDeviceStateChanged:(H323CallOutStatus)state
{
    NSLog(@"onCallRoomDeviceStateChanged %zd", state);
}
#endif

#pragma mark - ZAK expired
#if 0
- (void)onZoomIdentityExpired
{
    NSLog(@"onZoomIdentityExpired");
}

#pragma mark - Closed Caption
- (void)onClosedCaptionReceived:(NSString *)message
{
    NSLog(@"onClosedCaptionReceived : %@",message);
}
#endif

@end
