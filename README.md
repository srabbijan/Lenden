# Lenden 📱

**Lenden** is a modern, cross-platform transaction tracking application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. It empowers shopkeepers and individuals to easily manage customer accounts, track debts and credits ("Len-Den"), and keep their financial records organized.

## 🚀 Key Features

*   **Cross-Platform Performance**: Runs natively on both **Android** and **iOS** with a single shared codebase.
*   **Dashboard**: A clean and intuitive dashboard to oversee your financial status.
*   **Customer Management**: Easily add and manage customers.
*   **Transaction Tracking**: Record and view detailed transaction histories for each customer.
*   **Offline First**: Built with Room database to ensure your data is always accessible, even without internet.
*   **Modern UI**: Beautiful, responsive user interface built with Compose Multiplatform.

## 🛠️ Tech Stack

This project leverages the latest in Kotlin Multiplatform development:

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) (Android & iOS)
*   **Dependency Injection**: [Koin](https://insert-koin.io/)
*   **Database**: [Room Multiplatform](https://developer.android.com/kotlin/multiplatform/room) (SQLite)
*   **Navigation**: [JetBrains Navigation 3](https://github.com/jetbrains/androidx/tree/jb-main/navigation)
*   **Architecture**: MVVM / MVI with Unidirectional Data Flow
*   **Asynchronous Programming**: Coroutines & Flow
*   **Serialization**: Kotlinx Serialization
*   **Date & Time**: Kotlinx Datetime

## 📸 Screenshots

*(Add your screenshots here)*

| Android | iOS |
|:---:|:---:|
| <img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/e2014d76-f942-473b-86c5-4e30e9062341" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/76a869a0-be7b-4754-a6e2-2d6ad11f1cb2" />
 | <img width="1179" height="2556" alt="Simulator Screenshot - iPhone 15 Pro - 2025-12-06 at 23 52 21" src="https://github.com/user-attachments/assets/e305f24d-ed66-4090-96be-098b0b2ce3c3" />
<img width="1179" height="2556" alt="Simulator Screenshot - iPhone 15 Pro - 2025-12-06 at 23 49 56" src="https://github.com/user-attachments/assets/3408b2fd-b13e-4723-99f0-7e906421f844" />
 |

## 🏁 Getting Started

### Prerequisites
*   Android Studio (latest version recommended)
*   Xcode (for iOS development, macOS only)
*   JDK 17 or higher

### Build and Run

#### Android
Run the `composeApp` configuration in Android Studio, or use the terminal:
```shell
./gradlew :composeApp:assembleDebug
```

#### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run the app, or use the KMP wizard configuration in Android Studio.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
