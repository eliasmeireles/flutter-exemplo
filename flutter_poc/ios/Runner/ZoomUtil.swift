import Foundation
import AVFoundation

@objc class ZoomUtil: NSObject {

    @objc func camera(controller: UIViewController) -> Bool {

        var temPermissao = false

        guard UIImagePickerController.isSourceTypeAvailable(.camera) else {
            let alertController = UIAlertController(title: NSLocalizedString("tituloCameraNaoDisponivel", comment: ""), message: nil, preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .cancel, handler: nil))
            controller.present(alertController, animated: true, completion: nil)
            return temPermissao
        }

        let authStatus = AVCaptureDevice.authorizationStatus(for: AVMediaType.video)
        if authStatus == .denied {
            let alertController = UIAlertController(title: NSLocalizedString("tituloCameraNaoDisponivel", comment: ""), message: NSLocalizedString("msgCameraNaoDisponivel", comment: ""), preferredStyle: .alert)
            temPermissao = false
            alertController.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .cancel, handler: nil))
            controller.present(alertController, animated: true, completion: nil)
        }
        else if authStatus == .notDetermined {
            AVCaptureDevice.requestAccess(for: AVMediaType.video, completionHandler: { (granted) in
                if granted {
                    temPermissao = true
                }
            })
        }
        else {
            temPermissao = true
        }

        return temPermissao
    }

    @objc func microfone(controller: UIViewController) -> Bool {

        var temPermissao = false

        let authStatus = AVCaptureDevice.authorizationStatus(for: AVMediaType.audio)
        if authStatus == .denied {
            temPermissao = false
        }
        else if authStatus == .notDetermined {
            AVCaptureDevice.requestAccess(for: AVMediaType.video, completionHandler: { (granted) in
                if granted {
                    temPermissao = true
                }
            })
        }
        else {
            temPermissao = true
        }

        return temPermissao
    }

}
