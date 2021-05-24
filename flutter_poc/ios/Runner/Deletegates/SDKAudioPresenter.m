#import "Runner-Swift.h"
#import "SDKAudioPresenter.h"

@implementation SDKAudioPresenter

- (void)muteMyAudio
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms)
    {
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
            case MobileRTCAudioType_None:
            {
                //Supported VOIP
                if ([ms isSupportedVOIP])
                {
                    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"ZoomJoinAudio", @"")
                                                                                              message:nil
                                                                                       preferredStyle:UIAlertControllerStyleAlert];

                     [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"ZoomLigarInternet", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                         //Join VOIP
                         MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
                         if (ms)
                         {
                             [ms connectMyAudio:YES];
                         }
                     }]];

                     /*[alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"Dial in", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                         [self dialIn];
                     }]];*/

                     [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"Cancelar", nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
                     }]];

                    AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
                    [[[appDelegate window] rootViewController] presentViewController:alertController animated:YES completion:nil];
                }

                break;
            }
        }
    }
}

- (void)dialIn
{
    NSLog(@"MeetingNumber=%@",[[MobileRTCInviteHelper sharedInstance] ongoingMeetingNumber]);
    NSLog(@"participantID=%@",@([[[MobileRTC sharedRTC] getMeetingService] getParticipantID]));
    
    MobileRTCCallCountryCode *currentCountryCode = [[[MobileRTC sharedRTC] getMeetingService] getDialInCurrentCountryCode];
    
    NSArray *countryCodes = [[[MobileRTC sharedRTC] getMeetingService] getDialInCallCodesWithCountryId:currentCountryCode.countryId];

    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:currentCountryCode.countryName
                                                                             message:nil
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    
    for (MobileRTCCallCountryCode *countrgCode in countryCodes) {
        
        NSString *description = countrgCode.tollFree?[NSString stringWithFormat:@" (%@)", NSLocalizedString(@"Toll Free", @"")]:@"";
        NSString *displayCountryNumber = [NSString stringWithFormat:@"%@%@", countrgCode.countryNumber, description];
        
        [alertController addAction:[UIAlertAction actionWithTitle:displayCountryNumber style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
             [[[MobileRTC sharedRTC] getMeetingService] dialInCall:countrgCode.countryNumber];
        }]];
    }
    
    [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"Cancel", nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
    }]];
    
    AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    [[[appDelegate window] rootViewController] presentViewController:alertController animated:YES completion:nil];
}

- (void)switchMyAudioSource
{
    MobileRTCAudioError error = [[[MobileRTC sharedRTC] getMeetingService] switchMyAudioSource];
    NSLog(@"Switch My Audio error:%d...", error);
}

- (MobileRTCAudioType)myAudioType
{
    MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
    if (ms) {
        MobileRTCAudioType audioType = [ms myAudioType];
        return audioType;
    }
    return MobileRTCAudioType_None;
}

- (BOOL)connectMyAudio:(BOOL)on;
{
    return [[[MobileRTC sharedRTC] getMeetingService] connectMyAudio:on];
}

- (BOOL)muteUserAudio:(BOOL)mute withUID:(NSUInteger)userID
{
    return [[[MobileRTC sharedRTC] getMeetingService] muteUserAudio:mute withUID:userID];
}

- (BOOL)muteAllUserAudio:(BOOL)allowSelfUnmute
{
    return [[[MobileRTC sharedRTC] getMeetingService] muteAllUserAudio:allowSelfUnmute];
}

@end
