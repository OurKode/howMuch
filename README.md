# HowMuch? (Android App)

**HowMuch?** is a clean, modern Android application built with Jetpack Compose that calculates the true cost of items by converting monetary prices into the duration of work (days and hours of labor) required to purchase them.

---

## 🌟 Key Features

- **Salary Configuration**: Easily configure your monthly income, working days per month, and working hours per day.
- **True Cost Calculation**: Convert prices into:
  - Required working days.
  - Required working hours.
  - Percentage of your monthly income.
- **Modern Bento-Grid UI**: A beautiful, editorial-style dark dark theme design utilizing flat bento card grids and responsive preview layouts.
- **Thousand Separator Input Formatting**: Automatic Indonesian-formatted currency inputs (e.g. typing `1500000` automatically displays as `1.500.000` in real-time) with precise cursor handling.
- **In-App Language Selector**: Seamless toggle between **English** and **Indonesian** in the app header, instantly translating all UI components and taglines without activity reloads.
- **Persistence**: Calculation history and salary settings are cached locally using `SharedPreferences` and serialized via `Gson`.
- **Custom Adaptive Icon**: Premium launcher icon combining the **Hourglass** (time/labor) and **Coin/Price Tag** (spending) metaphors.

---

## 🛠️ Project Structure

The project follows a standard Clean Architecture and MVVM (Model-View-ViewModel) design:

```
app/src/main/java/com/example/howmuch/
├── MainActivity.kt        # Application Entry Point Activity
├── model/
│   ├── CalculationModel.kt # SalaryConfig and CalculationResult data models
│   └── Translation.kt      # Localization enums, Dictionary format and EN/ID translations
├── viewmodel/
│   └── CalculatorViewModel.kt # Manage inputs, calculations, and SharedPreferences persistence
└── ui/
    ├── screens/
    │   └── MainScreen.kt   # Dynamic Bento-Grid UI Layout, fields, list, and components
    └── theme/
        ├── Color.kt        # Branding palette (Obsidian canvas, Emerald accents, Pale badges)
        ├── Theme.kt        # MaterialTheme wrapper config
        └── Type.kt         # Custom modern typography configuration
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (Koala/Ladybug or newer recommended)
- JDK 21 (configured in Android Studio)
- Android API level 24+ device/emulator

### Installation & Build

Clone the repository and compile using the Gradle wrapper:

```bash
# Clone the repository
git clone https://github.com/your-username/howMuch.git
cd howMuch

# Compile the debug APK
./gradlew assembleDebug
```

The compiled APK will be available under `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🎨 Design DNA & Color Palette

- **Canvas Background**: Deep Obsidian Charcoal (`#0A0A0C` / `#0F0F11`)
- **Action Accents**: Electric Emerald Green (`#10B981`)
- **Typography**: Geometric Sans-Serif (Silver-White contrast `#FFFFFF` and Slate-Secondary `#8E8E93`)

---

## 📜 License
This project is licensed under the MIT License - see the LICENSE file for details.
