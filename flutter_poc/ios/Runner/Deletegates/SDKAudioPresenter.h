#import "MobileRTCSample-Prefix.pch"

@interface SDKAudioPresenter : NSObject

- (void)muteMyAudio;

- (void)switchMyAudioSource;

- (MobileRTCAudioType)myAudioType;

- (BOOL)connectMyAudio:(BOOL)on;

// only meeting host can run this function.
- (BOOL)muteUserAudio:(BOOL)mute withUID:(NSUInteger)userID;

// only meeting host can run this function.
- (BOOL)muteAllUserAudio:(BOOL)allowSelfUnmute;
@end
