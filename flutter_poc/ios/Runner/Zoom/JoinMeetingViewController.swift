//
//  JoinMeetingViewController.swift
//  Runner
//
//  Created by Elias Meireles on 24/05/21.
//

import Foundation
import UIKit

class JoinMeetingViewController: UIViewController {
    
    var coordinatorDelegate: ZoomCoordinatorDelegate?

    var presenter : SDKStartJoinMeetingPresenter?
    var meetingJson: String?
    var meetingConfig: MeetingConfig?
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        if let meetingConfigFromJson = meetingConfigFromJson(jsonData: meetingJson) {
            self.meetingConfig = meetingConfigFromJson
            ZoomService.sharedInstance.authenticateSDK(token: "", delegate: self)
        }
        
        view.backgroundColor = .white
        navigationController?.setNavigationBarHidden(true, animated: false)
        
        if #available(iOS 13.0, *) {
            navigationItem.leftBarButtonItem = UIBarButtonItem(barButtonSystemItem: .close, target: self, action: #selector(exitSelf))
        } else {
            navigationItem.leftBarButtonItem = UIBarButtonItem(title: "Leave", style: .plain, target: self, action: #selector(exitSelf))
        }
    }
    
    
    @objc private func exitSelf() {
        navigationController?.popViewController(animated: true)
        dismiss(animated: true, completion: nil)
    }
    
    func present() -> SDKStartJoinMeetingPresenter {
        
        if let presenter = self.presenter {
            return presenter
        }
        
        let presenter = SDKStartJoinMeetingPresenter()
        presenter.mainVC = self
        presenter.delegate = self
        
        self.presenter = presenter
        
        return presenter
    }
    
    func meetingConfigFromJson(jsonData: String?) -> MeetingConfig? {
        if let json = jsonData {
            do {
                return try JSONDecoder().decode(MeetingConfig.self, from: json.data(using: .utf8)!)
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
}

extension JoinMeetingViewController : MobileRTCMeetingServiceDelegate {
    
    func onJBHWaiting(with cmd: JBHCmd) {
        
        switch cmd {
        case JBHCmd_Show:
            
            let vc = WaitingMeetingViewController()
            
            let nav = UINavigationController(rootViewController: vc)
            nav.modalPresentationStyle = .fullScreen
            present(nav, animated: true)
            
        case JBHCmd_Hide:
            fallthrough
        default:
            dismiss(animated: true)
        }
    }
    
    func onMeetingError(_ error: MobileRTCMeetError, message: String?) {
        print("onMeetingError(\(error),  \(String(describing: message))")
        if error != MobileRTCMeetError(0) {
            coordinatorDelegate?.navigateToFlutter()
        }
    }
    
    func onMeetingStateChange(_ state: MobileRTCMeetingState) {
        print("onMeetingStateChange(\(state.rawValue)")
        
    }
    
    func onJoinMeetingConfirmed() {
        print("onJoinMeetingConfirmed()")
    }
    
    func onMeetingEndedReason(_ reason: MobileRTCMeetingEndReason) {
        print("onMeetingEndedReason(\(reason)")
        coordinatorDelegate?.navigateToFlutter()
    }
}

extension JoinMeetingViewController: AutenticacaoZoomDelegate {
    
    func isAPIAuthenticated(isAPIAuthenticated: Bool) {
        
        if isAPIAuthenticated {
            self.present().joinMeeting(self.meetingConfig?.meetingNumber ?? "", withPassword: self.meetingConfig?.meetingPassword ?? "", name: self.meetingConfig?.userName ?? "", rootVC: self, temChat: true, temCompartilhamento: true)
        } else {
            ZoomService.sharedInstance.authenticateSDK(token: "", delegate: self)
        }
    }
}


