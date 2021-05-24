#import "SDKStartJoinMeetingPresenter.h"

@interface SDKStartJoinMeetingPresenter (JoinMeetingOnly)

- (void)joinMeeting:(NSString*)meetingNo withPassword:(NSString*)pwd name:(NSString*)name;

- (void)joinMeeting:(NSString*)meetingNo name:(NSString*)name;

@end

