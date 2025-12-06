import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinKt.doInitKoin(config: nil)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}