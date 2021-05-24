#import <UIKit/UIKit.h>
#import <WebKit/WebKit.h>

@interface LocalShareViewController : UIViewController <WKNavigationDelegate>
@property (strong, nonatomic) WKWebView *   webView;

@end

