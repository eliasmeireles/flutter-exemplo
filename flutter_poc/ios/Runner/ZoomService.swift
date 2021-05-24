import Foundation
import MobileRTC

class ZoomService: NSObject, MobileRTCAuthDelegate {

  static let sharedInstance = ZoomService()

  var isAPIAuthenticated = false
  var isUserAuthenticated = false
  var delegate: AutenticacaoZoomDelegate?

  func authenticateSDK(token: String, delegate: AutenticacaoZoomDelegate) {
    self.delegate = delegate
    let zoomSDK = MobileRTC.shared()
    let context = MobileRTCSDKInitContext.init()
    context.domain = "zoom.us"
    
    MobileRTC.shared().initialize(context)

    guard let authService = zoomSDK.getAuthService() else { return }
    authService.delegate = self
//    authService.jwtToken = token
    authService.clientKey = "vPpsPHah5Sa4dI9976Okke2RZgx8BLKt8BA0"
    authService.clientSecret = "5K5e5taSXLZbWlaNtvCIxrCVk5T1TtoQdLvx"
    authService.sdkAuth()
  }

  func onMobileRTCAuthReturn(_ returnValue: MobileRTCAuthError) {
    guard returnValue == MobileRTCAuthError_Success else {
      print("Zoom: API authentication task failed, error code: \(returnValue.rawValue)")
        delegate?.isAPIAuthenticated(isAPIAuthenticated: false)
      return
    }

    delegate?.isAPIAuthenticated(isAPIAuthenticated: true)
    print("Zoom: API authentication task completed.")
  }
}

protocol AutenticacaoZoomDelegate {
    func isAPIAuthenticated(isAPIAuthenticated: Bool)
}
