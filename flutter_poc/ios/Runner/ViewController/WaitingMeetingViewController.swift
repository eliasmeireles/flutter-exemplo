import UIKit

class WaitingMeetingViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let viewDeEspera = UIView()
        let leaveItem = UIBarButtonItem(title: NSLocalizedString("zoomUiSair", comment: ""), style: .plain, target: self, action: #selector(onLeave))
        self.navigationItem.rightBarButtonItem = leaveItem
        self.navigationItem.rightBarButtonItem?.tintColor = UIColor.white
        self.navigationController?.navigationBar.barTintColor = UIColor.red

        viewDeEspera.backgroundColor = .white

        let imageView = UIImageView()
        imageView.image = UIImage(named: "AppIcon")

        let label = UILabel()
        label.text = NSLocalizedString("zoomEsperaMsg", comment: "")
        label.textColor = UIColor.lightGray
        label.textAlignment = .center
        label.numberOfLines = 0

        viewDeEspera.addSubview(imageView)
        viewDeEspera.addSubview(label)
        view.addSubview(viewDeEspera)

        imageView.translatesAutoresizingMaskIntoConstraints = false
        label.translatesAutoresizingMaskIntoConstraints = false
        viewDeEspera.translatesAutoresizingMaskIntoConstraints = false

        let constraints = [
            imageView.centerYAnchor.constraint(equalTo: viewDeEspera.centerYAnchor, constant: -60),
            imageView.centerXAnchor.constraint(equalTo: viewDeEspera.centerXAnchor),
            imageView.heightAnchor.constraint(equalToConstant: 80),
            imageView.widthAnchor.constraint(equalToConstant: 80),
            label.centerXAnchor.constraint(equalTo: viewDeEspera.centerXAnchor),
            label.topAnchor.constraint(equalTo: imageView.bottomAnchor, constant: 16),
            label.leadingAnchor.constraint(greaterThanOrEqualTo: viewDeEspera.leadingAnchor, constant: 32),
            label.trailingAnchor.constraint(greaterThanOrEqualTo: viewDeEspera.trailingAnchor, constant: -32),
            viewDeEspera.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            viewDeEspera.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            viewDeEspera.heightAnchor.constraint(equalTo: view.heightAnchor),
            viewDeEspera.widthAnchor.constraint(equalTo: view.widthAnchor)
        ]
        NSLayoutConstraint.activate(constraints)

        self.view.bringSubviewToFront(viewDeEspera)
    }

    @objc func onLeave() {

        MobileRTC.shared().getMeetingService()?.leaveMeeting(with: LeaveMeetingCmd_Leave)
        self.dismiss(animated: true, completion: nil)
    }

}

protocol waitingLeaveProtocol {

}
