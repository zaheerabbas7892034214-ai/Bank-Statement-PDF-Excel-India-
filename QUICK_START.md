# Quick Start Guide

Get started with the Bank Statement PDF → Excel (India) project in 5 minutes!

## Prerequisites

- Android Studio Giraffe (2022.3.1) or later
- JDK 17 or later
- Android SDK with API 34
- Git

## Step 1: Clone the Repository

```bash
git clone <repository-url>
cd Bank-Statement-PDF-Excel-India-
```

## Step 2: Open in Android Studio

1. Launch Android Studio
2. Click **File → Open**
3. Navigate to the project directory
4. Click **OK**
5. Wait for Gradle sync to complete (may take a few minutes)

## Step 3: Verify Project Structure

After opening, you should see:

```
Bank-Statement-PDF-Excel-India-
├── app/
│   ├── src/main/
│   │   ├── java/com/bankstatement/pdftoexcel/
│   │   │   ├── BankStatementApp.kt
│   │   │   ├── billing/
│   │   │   ├── data/
│   │   │   ├── ui/
│   │   │   └── utils/
│   │   └── res/
│   │       ├── drawable/
│   │       ├── layout/
│   │       └── values/
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

## Step 4: Sync Gradle

If Gradle sync doesn't start automatically:

1. Click **File → Sync Project with Gradle Files**
2. Wait for dependencies to download
3. Check "Build" window for any errors

## Step 5: Run the App

### Option A: Using Emulator

1. Click **Device Manager** in Android Studio
2. Create a new virtual device (if not exists):
   - Choose Pixel 5 or similar
   - System Image: Android 13 (API 33) or higher
   - Click Finish
3. Click the green **Run** button (or press Shift+F10)
4. Select your emulator
5. Wait for app to launch

### Option B: Using Physical Device

1. Enable Developer Options on your Android device:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
2. Enable USB Debugging:
   - Go to Settings → Developer Options
   - Enable "USB Debugging"
3. Connect device via USB
4. Click the green **Run** button
5. Select your device
6. Allow USB debugging prompt on device

## Step 6: Test Basic Functionality

Once the app launches:

1. **Home Screen**: You should see:
   - App title
   - Description text
   - "Select PDF File" button
   - "Convert to Excel" button (disabled initially)

2. **Test PDF Selection**:
   - Click "Select PDF File"
   - Choose any PDF (can be any PDF for initial testing)
   - File name should appear
   - Convert button should enable

3. **Access Settings**:
   - Click the Settings icon (⚙️) in top-right
   - Settings screen should open
   - Check version number displayed

## Common First-Time Issues

### Issue: Gradle Sync Failed

**Solution**:
```bash
./gradlew clean
./gradlew build
```

### Issue: "SDK not found"

**Solution**:
1. Open **File → Project Structure**
2. Set **SDK Location** to your Android SDK path
3. Usually: `/Users/<username>/Library/Android/sdk` (Mac) or `C:\Users\<username>\AppData\Local\Android\Sdk` (Windows)

### Issue: Build takes too long

**Solution**: First build downloads dependencies, be patient. Subsequent builds will be faster.

### Issue: App crashes on launch

**Solution**: Check Logcat for errors. Most likely missing dependencies or sync issue.

## What to Customize

Before deploying, customize:

### 1. Package Name (Required for Play Store)

See detailed guide: [PACKAGE_NAME_CHANGE.md](PACKAGE_NAME_CHANGE.md)

Quick steps:
```kotlin
// app/build.gradle.kts
android {
    namespace = "com.yourcompany.yourapp"
    defaultConfig {
        applicationId = "com.yourcompany.yourapp"
    }
}
```

Then refactor package in Android Studio.

### 2. App Name

Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your App Name</string>
```

### 3. Privacy Policy URL

Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="privacy_policy_url">https://yoursite.com/privacy</string>
```

### 4. App Colors (Optional)

Edit `app/src/main/res/values/colors.xml` to match your brand.

### 5. App Icon (Before Release)

Replace:
- `app/src/main/res/drawable/ic_launcher.xml`
- `app/src/main/res/drawable/ic_launcher_round.xml`

Or use Android Studio's Image Asset tool:
- Right-click `res` folder
- New → Image Asset
- Follow wizard

## Development Workflow

### Making Code Changes

1. Edit files in `app/src/main/java/`
2. Android Studio auto-saves
3. Rebuild if needed: **Build → Rebuild Project**
4. Run app to test changes

### Editing Layouts

1. Open XML layout in `app/src/main/res/layout/`
2. Use Design or Code view
3. Preview updates in real-time
4. Run app to test

### Adding Dependencies

Edit `gradle/libs.versions.toml`:
```toml
[versions]
yourlib = "1.0.0"

[libraries]
your-library = { group = "com.example", name = "library", version.ref = "yourlib" }
```

Then add to `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation(libs.your.library)
}
```

Sync Gradle after changes.

## Testing Features

### Test PDF Selection
1. Click "Select PDF File"
2. Choose any PDF
3. Verify file name displays

### Test Premium Status
Premium is locked by default. To test:
- Billing requires Play Store setup
- See [PLAY_STORE_SETUP.md](PLAY_STORE_SETUP.md)

### Test Settings
1. Click Settings icon
2. Click Privacy Policy
3. Should open browser (with placeholder URL)

## Debugging

### View Logs

1. Open Logcat tab (bottom of Android Studio)
2. Filter by your package name
3. Look for errors or warnings

### Set Breakpoints

1. Click in the gutter next to line number
2. Red dot appears
3. Run in Debug mode (bug icon)
4. App pauses at breakpoint

## Building for Release

### Debug Build (Testing)
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build (Production)

See detailed guide: [PLAY_STORE_SETUP.md](PLAY_STORE_SETUP.md)

Quick steps:
1. **Build → Generate Signed Bundle / APK**
2. Choose **Android App Bundle**
3. Create or select keystore
4. Generate AAB

Output: `app/release/app-release.aab`

## Next Steps

1. ✅ Project running successfully
2. 📖 Read [README.md](README.md) for full feature list
3. 🔧 Follow [PACKAGE_NAME_CHANGE.md](PACKAGE_NAME_CHANGE.md) to customize
4. 🚀 Follow [PLAY_STORE_SETUP.md](PLAY_STORE_SETUP.md) to deploy
5. 📊 Read [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) for architecture details

## Useful Android Studio Shortcuts

- **Ctrl+Space** (Cmd+Space): Code completion
- **Ctrl+Alt+L** (Cmd+Alt+L): Format code
- **Ctrl+Alt+O** (Cmd+Alt+O): Optimize imports
- **Shift+F10** (Ctrl+R): Run app
- **Shift+F9** (Ctrl+D): Debug app
- **Alt+Enter** (Option+Enter): Quick fix
- **Ctrl+B** (Cmd+B): Go to declaration

## Project Structure Overview

### Key Directories

```
app/src/main/
├── java/              # Kotlin source code
│   └── com/bankstatement/pdftoexcel/
│       ├── ui/        # Activities (screens)
│       ├── utils/     # Helper classes
│       ├── data/      # Data models
│       └── billing/   # IAP logic
└── res/               # Resources
    ├── layout/        # UI layouts
    ├── drawable/      # Icons, images
    ├── values/        # Strings, colors, themes
    └── menu/          # Menus
```

### Key Files

- **HomeActivity.kt**: Main screen, PDF selection
- **PreviewActivity.kt**: Transaction preview
- **ExportActivity.kt**: Export options
- **SettingsActivity.kt**: Settings screen
- **BillingManager.kt**: Google Play billing
- **PdfProcessor.kt**: PDF parsing logic
- **activity_home.xml**: Home screen layout

## Getting Help

If you encounter issues:

1. Check the error message in "Build" or "Logcat"
2. Google the error message
3. Check [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) "Known Limitations" section
4. Search Stack Overflow for similar issues
5. Review Android Studio logs

## Resources

- [Android Developer Docs](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs)
- [Material Design](https://material.io/design)
- [ML Kit](https://developers.google.com/ml-kit)
- [Play Billing](https://developer.android.com/google/play/billing)

## Estimated Time

- **Setup**: 5-10 minutes
- **First Run**: 2-5 minutes
- **Basic Customization**: 15-30 minutes
- **Play Store Setup**: 2-3 hours
- **Full Customization**: 1-2 days

---

**You're ready to start development! 🎉**

For detailed information, see other documentation files:
- [README.md](README.md) - Overview
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - Technical details
- [PACKAGE_NAME_CHANGE.md](PACKAGE_NAME_CHANGE.md) - Customization
- [PLAY_STORE_SETUP.md](PLAY_STORE_SETUP.md) - Deployment
