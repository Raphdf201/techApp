import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    var target: String

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(target)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Optionally update the view controller with new target values if needed
    }
}

struct ContentView: View {
    @State private var deepLinkTarget: String = ""

    var body: some View {
        ComposeView(target: deepLinkTarget)
            .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
            .onReceive(NotificationCenter.default.publisher(for: NSNotification.Name("DeepLink"))) { notification in
                if let target = notification.object as? String {
                    deepLinkTarget = target
                    print("Navigating to target: \(target)")
                }
            }
    }
}
