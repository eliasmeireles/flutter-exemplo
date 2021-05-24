#import "MobileRTCSample-Prefix.pch"

@interface SDKActionPresenter : NSObject

- (BOOL)isMeetingHost;

- (void)leaveMeeting;

- (void)EndMeeting;

- (void)presentParticipantsViewController;

- (BOOL)lockMeeting;

- (BOOL)lockShare;
@end


