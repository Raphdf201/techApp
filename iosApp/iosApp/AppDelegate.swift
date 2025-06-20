import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     open url: URL,
                     options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {

        if url.scheme == "techforkids", url.host == "login", url.path == "/callback" {
            let components = URLComponents(url: url, resolvingAgainstBaseURL: false)
            let accessToken = components?.queryItems?.first { $0.name == "access_token" }?.value
            let refreshToken = components?.queryItems?.first { $0.name == "refresh_token" }?.value

            if let accessToken = accessToken, let refreshToken = refreshToken {
                AuthBridge().handleAuthCallback(accessToken: accessToken, refreshToken: refreshToken)
            }

            return true
        }

        return false
    }
}
