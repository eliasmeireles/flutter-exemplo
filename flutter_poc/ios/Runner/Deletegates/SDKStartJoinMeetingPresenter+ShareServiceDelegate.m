#import "SDKStartJoinMeetingPresenter+ShareServiceDelegate.h"
#import "CustomMeetingViewController+MeetingDelegate.h"

@implementation SDKStartJoinMeetingPresenter (ShareServiceDelegate)

- (void)onSinkMeetingActiveShare:(NSUInteger)userID
{
    if (self.customMeetingVC)
    {
        [self.customMeetingVC onSinkMeetingActiveShare:userID];
    }
}

- (void)onSinkShareSizeChange:(NSUInteger)userID
{
    if (self.customMeetingVC)
    {
        [self.customMeetingVC onSinkShareSizeChange:userID];
    }
}

- (void)onSinkMeetingShareReceiving:(NSUInteger)userID
{
    if (self.customMeetingVC)
    {
        [self.customMeetingVC onSinkMeetingShareReceiving:userID];
    }
}


@end
