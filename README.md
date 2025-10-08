# 🏦 ISO 8583 Message Builder

An Android application for building and parsing **ISO 8583** financial transaction messages, built with modern Android development best practices.

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language">
  <img src="https://img.shields.io/badge/Architecture-MVI-orange.svg" alt="Architecture">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-purple.svg" alt="UI">
</div>

---

## 📖 About

ISO 8583 is the international standard for financial transaction card-originated messages. This app helps developers:

- **Build** ISO 8583 messages with automatic field generation
- **Parse** existing messages (optional feature)
- **Test** payment system integrations
- **Learn** ISO 8583 message structure

---

## ✨ Features

### 🛠️ Message Builder
- Input card number (16 digits) and transaction amount
- Automatically generates 4 ISO 8583 fields:
  - **Field 2**: Primary Account Number (PAN)
  - **Field 4**: Transaction Amount (12-digit format)
  - **Field 7**: Transmission Date/Time (MMDDhhmmss)
  - **Field 11**: System Trace Audit Number (STAN) - auto-incrementing
- Displays complete ISO 8583 message with MTI and Bitmap
- Copy functionality for each field

### 🔍 Message Parser (Optional)
- Parse raw ISO 8583 messages
- Extract and display individual fields
- Validate message structure

---

## 🏗️ Architecture

This project follows **Clean Architecture** principles with **MVI** (Model-View-Intent) pattern:

```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│    (Compose UI + ViewModel)         │
│         BuilderScreen.kt            │
│         BuilderViewModel.kt         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Domain Layer               │
│    (Use Cases + Repository Interface)│
│    BuildIsoMessageUseCase.kt        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Data Layer                 │
│   (Repository Implementation)       │
│   IsoMessageRepositoryImpl.kt       │
│   StanManager.kt (DataStore)        │
└─────────────────────────────────────┘
```

### MVI Flow
```
User Action (Intent) → ViewModel → Use Case → Repository → Update State → Recompose UI
```

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Architecture** | Clean Architecture + MVI |
| **Dependency Injection** | Hilt |
| **ISO 8583 Library** | j8583 (net.sf.j8583) |
| **State Management** | StateFlow + DataStore |
| **Async** | Coroutines + Flow |
| **Design** | Material Design 3 |

---

## 📦 Dependencies

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// ViewModel & Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")

// Hilt (DI)
implementation("com.google.dagger:hilt-android:2.52")

// ISO 8583
implementation("net.sf.j8583:j8583:1.15.0")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 24+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/aliIsazadeh/iso8583-builder.git
cd iso8583-builder
```

2. Open in Android Studio

3. Sync Gradle files

4. Run on emulator or device

---

## 📱 Usage

### Building a Message

1. Enter **Card Number** (16 digits)
   - Example: `6274123456789012`

2. Enter **Amount** (in Rials)
   - Example: `5000`

3. Click **"ساخت پیام ISO 8583"**

4. View generated fields:
   - Field 2: `6274123456789012`
   - Field 4: `000000005000`
   - Field 7: `0701143022` (auto-generated)
   - Field 11: `000001` (auto-increments)

5. Copy individual fields or full message

### STAN Management

The app automatically manages STAN (System Trace Audit Number):
- First message: `000001`
- Second message: `000002`
- ...
- After `999999`: resets to `000001`

STAN is persisted using **DataStore** and survives app restarts.

---

## 📂 Project Structure

```
app/src/main/java/com/task/iso8583builder/
├── MainActivity.kt
├── Iso8583Application.kt
├── ui/
│   ├── builder/
│   │   ├── BuilderScreen.kt          # Compose UI
│   │   ├── BuilderViewModel.kt       # MVI ViewModel
│   │   ├── BuilderState.kt           # UI State
│   │   └── BuilderIntent.kt          # User Actions
│   └── theme/
│       └── Theme.kt
├── domain/
│   ├── model/
│   │   └── IsoMessageResult.kt
│   ├── usecase/
│   │   └── BuildIsoMessageUseCase.kt
│   └── repository/
│       └── IsoMessageRepository.kt   # Interface
├── data/
│   ├── repository/
│   │   └── IsoMessageRepositoryImpl.kt
│   └── local/
│       └── StanManager.kt            # DataStore
└── di/
    └── AppModule.kt                   # Hilt Module
```

---

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

---

## 📸 Screenshots

| Builder Screen | Generated Message |
|---------------|------------------|
| <img width="295" height="640" alt="image" src="https://github.com/user-attachments/assets/14c58edb-ec05-47cd-ab68-f909f1a919fa" />
 | 
 <img width="295" height="640" alt="image" src="https://github.com/user-attachments/assets/1fe17846-0662-44b2-b94e-7810fc9d89e3" />|

---

## 📚 ISO 8583 Reference

### Field Descriptions

| Field | Name | Format | Example |
|-------|------|--------|---------|
| **2** | Primary Account Number (PAN) | LLVAR (up to 19) | `6274123456789012` |
| **4** | Transaction Amount | N12 | `000000005000` |
| **7** | Transmission Date/Time | N10 (MMDDhhmmss) | `0701143022` |
| **11** | System Trace Audit Number | N6 | `000001` |

### Message Structure
```
[MTI (4 bytes)][Bitmap (16 hex)][Field Data]
```

Example:
```
0200522000000000000062741234567890120000000050000701143022000001
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Ali Isazadeh (JesusAli)**
- GitHub: [@AliIsazadeh](https://github.com/aliIsazadeh)
- LinkedIn: [AliIsazadeh](https://www.linkedin.com/in/ali-isazadeh-7b2524215/)

---

## 🙏 Acknowledgments

- [j8583 Library](https://github.com/chochos/j8583) by Enrique Zamudio
- ISO 8583 Standard Documentation
- Android Jetpack Team

---

## 📞 Contact

For questions or support, please open an issue or contact [isazadhali@gmail.com]

---

<div align="center">
  Made with ❤️ for the Banking Tech Community
</div>
