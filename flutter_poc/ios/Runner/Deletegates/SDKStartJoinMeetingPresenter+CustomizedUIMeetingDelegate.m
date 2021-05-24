#import "SDKStartJoinMeetingPresenter+CustomizedUIMeetingDelegate.h"

@implementation SDKStartJoinMeetingPresenter (CustomizedUIMeetingDelegate)

- (void)onInitMeetingView
{
    NSLog(@"onInitMeetingView....");
    CustomMeetingViewController *vc = [[CustomMeetingViewController alloc] init];
    self.customMeetingVC = vc;

    [self.rootVC addChildViewController:self.customMeetingVC];
    [self.rootVC.view addSubview:self.customMeetingVC.view];
    [self.customMeetingVC didMoveToParentViewController:self.rootVC];

    self.customMeetingVC.view.frame = self.rootVC.view.bounds;
}

- (void)onDestroyMeetingView
{
    NSLog(@"onDestroyMeetingView....");
    
    [self.customMeetingVC willMoveToParentViewController:nil];
    [self.customMeetingVC.view removeFromSuperview];
    [self.customMeetingVC removeFromParentViewController];
    self.customMeetingVC = nil;
}

@end
