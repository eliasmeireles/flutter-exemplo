#import "MobileRTCSample-Prefix.pch"
#import "RemoteShareViewController.h"

@interface RemoteShareViewController ()

@end

@implementation RemoteShareViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor clearColor];

    [self.view addSubview:self.shareView];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc
{
    self.shareView = nil;
}

- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    
    self.shareView.frame = self.view.bounds;
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self updateShareView];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [self.shareView stopActiveShare];
}

#pragma mark - MobileRTCVideoView

- (MobileRTCActiveShareView *)shareView
{
    if (!_shareView)
    {
        _shareView = [[MobileRTCActiveShareView alloc] initWithFrame:self.view.bounds];
    }
    return _shareView;
}

- (void)updateShareView
{
    if (0 != self.activeShareID)
    {
        [self.shareView showActiveShareWithUserID:self.activeShareID];
    }
    else
    {
        [self.shareView stopActiveShare];
    }
}


@end
