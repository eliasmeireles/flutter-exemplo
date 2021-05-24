import UIKit
import Flutter
import MobileRTC
import Foundation

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    
    private var coordinator: MainCoordinator?
    
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        
        GeneratedPluginRegistrant.register(with: self)
        
        let fvc: FlutterViewController = window?.rootViewController as! FlutterViewController
  
        let zoomChannel = FlutterMethodChannel(name: "ZOOM_CHANNEL",
                                              binaryMessenger: fvc.binaryMessenger)
        zoomChannel.setMethodCallHandler({
            (call: FlutterMethodCall, result: FlutterResult) -> Void in
            guard call.method == "zoomJoinMeeting" else {
                result(FlutterMethodNotImplemented)
                return
            }
            
            if let meetingJson = call.arguments as? String {
                self.coordinator?.iniciarZoom(meetingJson: meetingJson)
            }
        })
        
        GeneratedPluginRegistrant.register(with: self)
       
        let navigationController = UINavigationController(rootViewController: fvc)
        navigationController.isNavigationBarHidden = true
        window?.rootViewController = navigationController
        navigationController.setNavigationBarHidden(true, animated: false)
        coordinator = MainCoordinator(navigationController: navigationController)
        window?.makeKeyAndVisible()
        
        return super.application(application, didFinishLaunchingWithOptions: launchOptions) 
    }
}
