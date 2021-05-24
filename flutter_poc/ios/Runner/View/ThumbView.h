#import <UIKit/UIKit.h>

extern const CGFloat BTN_HEIGHT;

@interface ThumbView : UIView
@property (nonatomic)         NSUInteger                  pinUserID;
- (void)updateFrame;
- (void)updateThumbViewVideo;
- (void)showThumbView;
- (void)hiddenThumbView;
@end

