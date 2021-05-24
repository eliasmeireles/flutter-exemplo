import Foundation
import UIKit

protocol Coordinator: class {
    var childCoordinators: [Coordinator] { get }
    func adicionar(_ coordinator: Coordinator)
    func remover(_ coordinator: Coordinator)
    func removerTodos()
    func iniciar()
}

class BaseCoordinator: Coordinator{
    
    private var _childCoordinators: [Coordinator] = []
    
    var childCoordinators: [Coordinator] {
        return self._childCoordinators
    }
    
    func iniciar() {}
    
    func adicionar(_ coordinator: Coordinator) {
        self._childCoordinators.append(coordinator)
    }
    
    func remover(_ coordinator: Coordinator) {
        self._childCoordinators = self.childCoordinators.filter { $0 !== coordinator }
    }
    
    func removerTodos() {
        self._childCoordinators.removeAll()
    }
    
    init() {
        guard type(of: self) != BaseCoordinator.self else {
            fatalError("BaseCoordinator cannot be instantiated")
        }}
}
