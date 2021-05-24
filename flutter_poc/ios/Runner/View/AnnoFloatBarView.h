#import "MobileRTCSample-Prefix.pch"

@protocol AnnoFloatBarViewDelegate <NSObject>

@optional

- (BOOL)onClickStartAnnotate;
- (BOOL)onClickStopAnnotate;

@end

@interface AnnoFloatBarView : UIView

@property (assign, nonatomic) id<AnnoFloatBarViewDelegate>  delegate;

- (void)stopAnnotate;

@end
