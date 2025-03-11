![GitHub repo size](https://img.shields.io/github/repo-size/raphdf201/techapp)
![Github build passing](https://github.com/raphdf201/techapp/actions/workflows/debug.yml/badge.svg)

This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)

Download the latest debug (unstable) apk from the latest [actions run](https://github.com/Raphdf201/techApp/actions/workflows/debug.yml?query=is%3Asuccess)
Download the latest release (stable) apk from the latest [actions run](https://github.com/Raphdf201/techApp/actions/workflows/release.yml?query=is%3Asuccess)
