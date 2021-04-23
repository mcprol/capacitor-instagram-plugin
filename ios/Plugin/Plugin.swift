import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(Instagram)
public class Instagram: CAPPlugin {
    
    @objc func shareLocalMedia(_ call: CAPPluginCall) {
        
        guard let medias = call.getArray("medias", String.self) else {
            call.error("Missing 'medias' argument")
            return;
        }
        guard let mediaType = call.getString("mediaType") else {
            call.error("Missing 'mediaType' argument")
            return;
        }
        guard let target = call.getArray("target", String.self) else {
            call.error("Missing 'target' argument")
            return;
        }
        
        call.error("IOS Not implemented!")
    }
}
