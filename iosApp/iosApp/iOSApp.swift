import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(_ app: UIApplication,
                     open url: URL,
                     options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        handleDeepLink(url)
        return true
    }

    private func handleDeepLink(_ url: URL) {
        print("Received deep link: \(url.absoluteString)")
        // Example: Navigate based on the URL path
        if url.host == "open" && url.path == "/home" {
            NotificationCenter.default.post(name: NSNotification.Name("DeepLink"), object: "home")
        }
    }
}
