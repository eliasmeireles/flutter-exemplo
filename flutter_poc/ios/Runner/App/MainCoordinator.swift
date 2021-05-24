import Foundation
import UIKit

class MainCoordinator: BaseCoordinator{
    weak var navigationController: UINavigationController?
    
    init(navigationController: UINavigationController) {
        super.init()
        self.navigationController = navigationController
    }

    override func iniciar() {
        super.iniciar()
    }
    
    func iniciarZoom(meetingJson: String) {
        super.iniciar()
        routeToZoomViewController(meetingJson: meetingJson)
    }
}
protocol ZoomToMainCoordinatorDelegate: class {
    func routeToFlutterViewController()
}

protocol FlutterToMainCoordinatorDelegate: class {
    func routeToZoomViewController(meetingJson: String)
}

extension MainCoordinator: ZoomToMainCoordinatorDelegate{
    
    func routeToFlutterViewController() {
        let coordinator = FlutterCoordinator(navigationController: self.navigationController)
        coordinator.delegate = self
        self.adicionar(coordinator)
        coordinator.iniciar()
    }
}
extension MainCoordinator: FlutterToMainCoordinatorDelegate{
    
    func routeToZoomViewController(meetingJson: String) {
        let coordinator = ZoomCoordinator(navigationController: self.navigationController)
        coordinator.delegate = self
        self.adicionar(coordinator)
        coordinator.iniciar(meetingJson: meetingJson)
    }
}
