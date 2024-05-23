This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…


![Screenshot 2024-05-23 at 10 51 12 AM](https://github.com/Shashi7083/Shopping-Ui-ios-android-KMP/assets/88765330/470c9dce-03da-4568-9483-d629812e8c11)
![Screenshot 2024-05-23 at 11 04 15 AM](https://github.com/Shashi7083/Shopping-Ui-ios-android-KMP/assets/88765330/6fbe361b-21a1-43db-a0a7-870a8b1edf1c)
