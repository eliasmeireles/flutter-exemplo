#import "MobileRTCSample-Prefix.pch"

#define Top_Height               100

#define kTagButtonShrink         1000
#define kTagButtonCameraSwitch   (kTagButtonShrink+1)
#define kTagButtonEnd            (kTagButtonShrink+2)

@interface TopPanelView : UIView

@property (strong, nonatomic) UIButton        *shrinkBtn;
@property (nonatomic,copy) void(^shrinkButtonClickBlock)(void);

- (void)updateFrame;
- (void)showTopPanelView;
- (void)hiddenTopPanelView;
@end

