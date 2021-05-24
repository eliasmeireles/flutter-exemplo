#import "MobileRTCSample-Prefix.pch"

#define Bottom_Height           (IPHONE_X ? (60 + 34) : 60) 

#define kTagButtonAudio         1000
#define kTagButtonVideo         (kTagButtonAudio+1)
#define kTagButtonShare         (kTagButtonAudio+2)
#define kTagButtonChat          (kTagButtonAudio+3)
#define kTagButtonMore          (kTagButtonAudio+4)

@interface BottomPanelView : UIView
- (void)updateFrame;
- (void)showBottomPanelView;
- (void)hiddenBottomPanelView;
- (void)updateMyAudioStatus;
- (void)updateMyVideoStatus;
- (void)updateMyShareStatus;
@end

