package net.raphdf201.techapp

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

fun MainViewController(token: String) {
    ComposeUIViewController { App(token) }
}

@OptIn(ExperimentalForeignApi::class)
actual class Multi {
    private val directory = NSHomeDirectory() + "tech"

    @OptIn(BetaInteropApi::class)
    actual fun save(string: String) {
        NSString.create(string = string).writeToFile(directory, true, NSUTF8StringEncoding, null)
    }

    actual fun load(): String {
        return NSString.stringWithContentsOfFile(directory, NSUTF8StringEncoding, null).toString()
    }
}
