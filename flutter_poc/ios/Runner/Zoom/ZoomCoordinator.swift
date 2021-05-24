import Foundation
import UIKit

final class ZoomCoordinator: BaseCoordinator{
    
    weak var navigationController: UINavigationController?
    weak var delegate: ZoomToMainCoordinatorDelegate?
    
    func iniciar(meetingJson: String) {
        super.iniciar()
        
        let zoomViewController = JoinMeetingViewController()
        zoomViewController.coordinatorDelegate = self
        zoomViewController.meetingJson = meetingJson
        
        navigationController?.pushViewController(zoomViewController, animated: true)
    }
    
    init(navigationController: UINavigationController?) {
        super.init()
        self.navigationController = navigationController
    }
}

protocol ZoomCoordinatorDelegate {
    func navigateToFlutter()
}

extension ZoomCoordinator: ZoomCoordinatorDelegate{
    func navigateToFlutter() {
        self.delegate?.routeToFlutterViewController()
    }
}
