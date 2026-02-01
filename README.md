# Bank Statement PDF → Excel (India)

A production-ready Android application that converts Indian bank statement PDFs into structured Excel/CSV tables. Supports both text-based and scanned PDFs using OCR.

## Features

✅ **PDF Processing**
- Text-based PDF extraction using PdfRenderer
- Scanned PDF OCR using ML Kit Text Recognition
- Automatic transaction parsing for Indian banks
- Support for multiple date formats (DD/MM/YYYY, DD-MM-YYYY, DD.MM.YYYY)

✅ **Data Processing**
- Extract Date, Description, Debit, Credit, Balance columns
- Automatic header/footer removal
- Currency symbol (₹) handling
- Multi-line description support

✅ **Export Options**
- CSV export
- Excel (XLSX) export using Apache POI
- File sharing functionality

✅ **Premium Features**
- Free users: Preview first 10 transactions
- Premium unlock (₹299 one-time purchase)
- Restore previous purchases

✅ **Modern UI**
- Material 3 Design
- Dark and Light theme support
- Intuitive navigation
- Production-ready icons and assets

✅ **Architecture**
- MVVM pattern
- Clean package structure
- Lifecycle-aware components
- Coroutines for async operations

## Technical Stack

- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM
- **UI**: Material 3
- **PDF**: PdfRenderer
- **OCR**: ML Kit Text Recognition
- **Export**: Apache POI (Excel), Custom CSV
- **Billing**: Google Play Billing Library 6.1.0
- **Build**: Gradle 8.2 with Version Catalog

## Project Structure

```
app/
├── src/main/
│   ├── java/com/bankstatement/pdftoexcel/
│   │   ├── data/
│   │   │   └── BankTransaction.kt
│   │   ├── billing/
│   │   │   └── BillingManager.kt
│   │   ├── ui/
│   │   │   ├── home/
│   │   │   │   └── HomeActivity.kt
│   │   │   ├── preview/
│   │   │   │   ├── PreviewActivity.kt
│   │   │   │   └── TransactionAdapter.kt
│   │   │   ├── export/
│   │   │   │   └── ExportActivity.kt
│   │   │   └── settings/
│   │   │       └── SettingsActivity.kt
│   │   ├── utils/
│   │   │   ├── PdfProcessor.kt
│   │   │   ├── OcrExtractor.kt
│   │   │   ├── CsvExporter.kt
│   │   │   ├── ExcelExporter.kt
│   │   │   └── PreferencesManager.kt
│   │   └── BankStatementApp.kt
│   └── res/
│       ├── layout/
│       ├── values/
│       ├── drawable/
│       └── menu/
└── build.gradle.kts
```

## Setup Instructions

### 1. Clone & Open Project

```bash
git clone <repository-url>
```

Open the project in Android Studio Giraffe or later.

### 2. Change Package Name

To customize the package name for your app:

1. Open `settings.gradle.kts` and update if needed
2. Update package name in `app/build.gradle.kts`:
   ```kotlin
   android {
       namespace = "com.yourcompany.yourapp"
       defaultConfig {
           applicationId = "com.yourcompany.yourapp"
       }
   }
   ```
3. Update in `AndroidManifest.xml`:
   ```xml
   <manifest xmlns:android="http://schemas.android.com/apk/res/android"
       package="com.yourcompany.yourapp">
   ```
4. Refactor package structure:
   - Right-click on package in Android Studio
   - Select Refactor → Rename
   - Update all references

### 3. Configure Google Play Billing

1. **Create App in Google Play Console**:
   - Go to https://play.google.com/console
   - Create a new app
   - Fill in app details

2. **Create In-App Product**:
   - Navigate to: Monetize → In-app products
   - Click "Create product"
   - Product ID: `premium_unlock`
   - Product type: One-time product
   - Price: ₹299 INR
   - Title: "Premium Unlock"
   - Description: "Unlock unlimited transaction exports"
   - Save and activate

3. **Add License Test Accounts**:
   - Go to: Setup → License testing
   - Add test Gmail accounts
   - These accounts can make test purchases without charges

4. **Update Product ID** (if changed):
   - Edit `BillingManager.kt`:
     ```kotlin
     companion object {
         const val PREMIUM_PRODUCT_ID = "your_product_id"
     }
     ```

### 4. Update Privacy Policy URL

In `app/src/main/res/values/strings.xml`:
```xml
<string name="privacy_policy_url">https://yourwebsite.com/privacy</string>
```

### 5. Build the App

```bash
./gradlew assembleDebug
```

Or in Android Studio: Build → Build Bundle(s) / APK(s) → Build APK(s)

## Play Store Deployment

### 1. Generate Signed APK/AAB

1. In Android Studio: Build → Generate Signed Bundle / APK
2. Choose "Android App Bundle" (recommended for Play Store)
3. Create or select keystore
4. Fill in keystore details:
   - Key alias
   - Passwords
   - Validity (25+ years)
5. **Save keystore safely** - you'll need it for all future updates

### 2. Prepare Store Listing

Required assets:
- **App Icon**: 512x512 PNG (upload in Play Console)
- **Screenshots**: Minimum 2, recommended 8
  - Phone: 16:9 or 9:16 aspect ratio
  - Tablet (optional): 16:10 or 10:16
- **Feature Graphic**: 1024x500 PNG
- **Privacy Policy**: URL to hosted privacy policy

### 3. Upload to Play Console

1. Go to: Production → Create new release
2. Upload the AAB file
3. Fill in release notes
4. Complete store listing:
   - Title
   - Short description (80 chars)
   - Full description (4000 chars)
   - Screenshots
   - Feature graphic
   - Category: Finance or Productivity
5. Content rating questionnaire
6. Pricing: Free (with in-app purchases)
7. Submit for review

### 4. Testing Before Launch

1. **Internal Testing**:
   - Create internal test track
   - Add test users
   - Upload APK/AAB
   - Test all features including billing

2. **Closed/Open Testing** (optional):
   - Beta test with larger audience
   - Gather feedback

## Configuration Options

### Free vs Premium Row Limit

In `PreviewActivity.kt`:
```kotlin
private val freeRowsLimit = 10  // Change this value
```

### ML Kit OCR

The app automatically downloads ML Kit models on first use. Ensure:
- Device has internet connection
- Minimum 50MB free space

### ProGuard (Release Builds)

The app includes ProGuard rules for Apache POI. Review `app/proguard-rules.pro` before release.

## Testing

### Test PDF Processing
1. Prepare sample bank statement PDFs (text-based and scanned)
2. Use the app to select and convert
3. Verify extracted data accuracy

### Test Billing
1. Add your Gmail to license testing
2. Make test purchase
3. Verify premium unlock
4. Test restore purchases

### Test Export
1. Export as CSV
2. Export as Excel
3. Verify data integrity in exported files
4. Test sharing functionality

## Troubleshooting

### Build Issues

**Gradle sync failed**:
```bash
./gradlew clean
./gradlew build
```

**Dependency conflicts**:
- Check `gradle/libs.versions.toml`
- Ensure all library versions are compatible

### Runtime Issues

**OCR not working**:
- Verify internet connection (for first-time model download)
- Check device storage space
- Ensure ML Kit permissions in manifest

**Billing not working**:
- Verify app is signed with release key
- Product ID must match Play Console
- Use license testing account
- App must be in at least internal testing track

**PDF parsing errors**:
- Different banks have different formats
- Customize `PdfProcessor.parseTransactionLine()` for your needs

## Customization Guide

### Add Support for More Banks

Edit `PdfProcessor.kt`:
```kotlin
private fun parseTransactionLine(line: String): BankTransaction? {
    // Add custom parsing logic for specific bank formats
    // Example: Check for bank-specific patterns
}
```

### Modify UI Theme

Colors in `app/src/main/res/values/colors.xml`
Themes in `app/src/main/res/values/themes.xml`

### Change Export Format

Modify `CsvExporter.kt` or `ExcelExporter.kt` to customize output format.

## Security Considerations

✅ No hardcoded API keys
✅ SharedPreferences for local premium status
✅ Secure billing verification via Google Play
✅ ProGuard rules for code obfuscation
✅ Permissions: Only INTERNET and BILLING

## Support & Updates

For issues or feature requests:
1. Check existing GitHub issues
2. Create new issue with:
   - Device model
   - Android version
   - Steps to reproduce
   - Expected vs actual behavior

## License

This project is provided as-is for educational and commercial use.

## Version History

**v1.0.0** - Initial release
- PDF to Excel/CSV conversion
- OCR support
- Premium billing
- Material 3 UI

---

**Built with ❤️ for Indian bank statement conversion**
