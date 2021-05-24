#import "MobileRTCSample-Prefix.pch"

@interface SDKVideoPresenter : NSObject

- (void)muteMyVideo;

- (void)switchMyCamera;

- (BOOL)pinVideo:(BOOL)on withUser:(NSUInteger)userId;

// only meeting host can run this function.
- (BOOL)stopUserVideo:(NSUInteger)userID;

// only meeting host can run this function.
- (BOOL)askUserStartVideo:(NSUInteger)userID;

@end

