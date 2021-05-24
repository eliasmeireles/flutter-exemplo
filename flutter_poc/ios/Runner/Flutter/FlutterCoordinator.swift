import Foundation
import UIKit

final class FlutterCoordinator: BaseCoordinator{
    weak var navigationController: UINavigationController?
    weak var delegate: FlutterToMainCoordinatorDelegate?
    override func iniciar() {
        super.iniciar()
        navigationController?.setNavigationBarHidden(true, animated: false)
        navigationController?.popToRootViewController(animated: true)
    }
    init(navigationController: UINavigationController?) {
        super.init()
        self.navigationController = navigationController
    }
}
