import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinKt.doInitKoinIOS()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}