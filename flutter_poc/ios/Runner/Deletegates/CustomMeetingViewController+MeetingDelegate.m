#import "CustomMeetingViewController+MeetingDelegate.h"

@implementation CustomMeetingViewController (MeetingDelegate)

- (void)onSinkMeetingActiveVideo:(NSUInteger)userID
{
//    self.shrinkVC.activeVideoID = userID;
    [self updateVideoOrShare];
}

- (void)onSinkMeetingPreviewStopped
{
}

- (void)onSinkMeetingAudioStatusChange:(NSUInteger)userID
{
    [self updateMyAudioStatus];

    [self updateVideoOrShare];
}

- (void)onSinkMeetingVideoStatusChange:(NSUInteger)userID
{
    [self updateMyVideoStatus];

    [self updateVideoOrShare];
}

- (void)onMyVideoStateChange
{
    [self updateMyVideoStatus];

    [self updateVideoOrShare];
}

- (void)onSinkMeetingUserJoin:(NSUInteger)userID
{
    [self updateVideoOrShare];
}

- (void)onSinkMeetingUserLeft:(NSUInteger)userID
{
    [self updateVideoOrShare];
}

- (void)onSinkMeetingActiveShare:(NSUInteger)userID
{
    [self updateMyShareStatus];
    BOOL sharing = (0 != userID);
    if (sharing)
    {
//        self.topPanelView.shrinkBtn.hidden = YES;
        MobileRTCMeetingService *ms = [[MobileRTC sharedRTC] getMeetingService];
        //Local Side Share
        if ([ms isSameUser:[ms myselfUserID] compareTo:userID])
        {
            [self showLocalShareView];
        }
        //Remote Side Share
        else
        {
            self.remoteShareVC.activeShareID = userID;
            [self showRemoteShareView];
        }
    }
    else
    {
//        self.topPanelView.shrinkBtn.hidden = NO;
        [self hideAnnotationView];
        [self showVideoView];
    }
}

- (void)onSinkShareSizeChange:(NSUInteger)userID
{
    if (!self.remoteShareVC.parentViewController)
        return;

    [self.remoteShareVC.shareView changeShareScaleWithUserID:userID];
}

- (void)onSinkMeetingShareReceiving:(NSUInteger)userID
{
   {
       if (!self.remoteShareVC.parentViewController)
           return;

       [self.remoteShareVC.shareView changeShareScaleWithUserID:userID];
   }
}

- (void)onWaitingRoomStatusChange:(BOOL)needWaiting
{
    if (needWaiting)
    {
        UIViewController *vc = [UIViewController new];
        
        vc.title = NSLocalizedString(@"zoomUiEsperandoSerAprovado", @"");
        
        UIBarButtonItem *leaveItem = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(@"zoomUiSair", @"") style:UIBarButtonItemStylePlain target:self action:@selector(onEndButtonClick:)];
        [vc.navigationItem setRightBarButtonItem:leaveItem];
        
        UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:vc];
        nav.modalPresentationStyle = UIModalPresentationFullScreen;
        [self presentViewController:nav animated:YES completion:NULL];
        
    }
    else
    {
        [self dismissViewControllerAnimated:YES completion:NULL];
    }
}

- (void)onEndButtonClick:(id)sender
{
    [self.actionPresenter leaveMeeting];
    [self dismissViewControllerAnimated:YES completion:NULL];
}
@end
