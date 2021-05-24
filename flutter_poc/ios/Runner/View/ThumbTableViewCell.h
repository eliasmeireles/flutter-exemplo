#import "MobileRTCSample-Prefix.pch"

#define ThumbViewBorderWidth 1

@interface ThumbTableViewCell : UITableViewCell
@property (strong, nonatomic) MobileRTCVideoView *  thumbView;
- (void)stopThumbVideo;
@end

