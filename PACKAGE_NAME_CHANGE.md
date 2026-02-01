# How to Change Package Name

Complete guide to customize the package name for your app.

## Current Package Name

```
com.bankstatement.pdftoexcel
```

## Change to Your Package Name

Example: Changing to `com.yourcompany.bankstatement`

### Step 1: Update Gradle Files

#### 1.1 Update `app/build.gradle.kts`

Find and replace:

```kotlin
android {
    namespace = "com.bankstatement.pdftoexcel"  // Change this
    
    defaultConfig {
        applicationId = "com.bankstatement.pdftoexcel"  // Change this
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
}
```

**Change to**:

```kotlin
android {
    namespace = "com.yourcompany.bankstatement"  // Your new package
    
    defaultConfig {
        applicationId = "com.yourcompany.bankstatement"  // Your new package
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
}
```

### Step 2: Update AndroidManifest.xml

#### 2.1 Update package attribute

Find:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
```

No changes needed in modern Android - namespace is handled by Gradle.

#### 2.2 Update application name reference

Ensure the application tag references the correct class:
```xml
<application
    android:name=".BankStatementApp"
    ...
```

The dot (.) means relative to the namespace, so this will automatically use your new package.

### Step 3: Refactor Package Structure in Android Studio

This is the most important step to update all imports and references.

#### 3.1 Open Android Studio

1. Open the project in Android Studio
2. Wait for Gradle sync to complete

#### 3.2 Use Refactor Tool

1. In **Project view**, expand:
   - `app/src/main/java/com/bankstatement/pdftoexcel/`

2. **Right-click** on the `pdftoexcel` package

3. Select **Refactor → Rename**

4. In the dialog:
   - Uncheck "Search in comments and strings"
   - Check "Search for text occurrences"
   - Enter new package name: `bankstatement` (or your desired name)

5. Click **Refactor**

6. Review all changes in preview window

7. Click **Do Refactor**

#### 3.3 Refactor Parent Package (Optional)

If you want to change `com.bankstatement` to `com.yourcompany`:

1. Right-click on `bankstatement` package
2. Select **Refactor → Rename**
3. Enter new name: `yourcompany`
4. Follow same steps as above

#### 3.4 Alternative: Manual Method

If refactoring doesn't work properly:

1. **Create new package structure**:
   - Right-click on `java` folder
   - New → Package
   - Enter: `com.yourcompany.bankstatement`

2. **Move files**:
   - Select all folders/files from old package
   - Drag and drop to new package
   - Android Studio will ask: "Move" or "Copy"
   - Select **Move**
   - Check "Search for references"

3. **Delete old package**:
   - Right-click old `com/bankstatement` folder
   - Delete

### Step 4: Update Import Statements

After refactoring, Android Studio should update all imports automatically. If not:

1. Open each `.kt` file
2. Update package declaration at top:

```kotlin
// Old
package com.bankstatement.pdftoexcel.ui.home

// New
package com.yourcompany.bankstatement.ui.home
```

3. Update imports if needed:

```kotlin
// Old
import com.bankstatement.pdftoexcel.data.BankTransaction

// New  
import com.yourcompany.bankstatement.data.BankTransaction
```

### Step 5: Find and Replace (Verification)

Use Android Studio's Find and Replace to catch any missed references:

1. Press `Ctrl+Shift+R` (or `Cmd+Shift+R` on Mac)
2. Enter old package: `com.bankstatement.pdftoexcel`
3. Replace with: `com.yourcompany.bankstatement`
4. Click "Replace All"

### Step 6: Gradle Sync

1. Click **File → Sync Project with Gradle Files**
2. Wait for sync to complete
3. Check for any errors

### Step 7: Clean and Rebuild

1. Click **Build → Clean Project**
2. Wait for clean to complete
3. Click **Build → Rebuild Project**
4. Fix any errors that appear

### Step 8: Test the App

1. Run the app on emulator or device
2. Verify all screens work
3. Check:
   - App launches correctly
   - All activities navigate properly
   - No crash on startup
   - Billing initialization works
   - PDF processing works

## Files That Need Package Name

After changing, verify these files reference correct package:

### Kotlin Files (All)

```
✓ app/src/main/java/com/yourcompany/bankstatement/
  ✓ BankStatementApp.kt
  ✓ data/BankTransaction.kt
  ✓ billing/BillingManager.kt
  ✓ ui/home/HomeActivity.kt
  ✓ ui/preview/PreviewActivity.kt
  ✓ ui/preview/TransactionAdapter.kt
  ✓ ui/export/ExportActivity.kt
  ✓ ui/settings/SettingsActivity.kt
  ✓ utils/PdfProcessor.kt
  ✓ utils/OcrExtractor.kt
  ✓ utils/CsvExporter.kt
  ✓ utils/ExcelExporter.kt
  ✓ utils/PreferencesManager.kt
```

### XML Files (Check references)

- `AndroidManifest.xml` - Uses relative path (.), should work automatically
- Layout files - Don't reference package directly
- Resource files - Don't reference package directly

## Verification Checklist

After changing package name, verify:

- [ ] `build.gradle.kts` has correct namespace and applicationId
- [ ] All Kotlin files have correct package declaration
- [ ] All imports are updated
- [ ] AndroidManifest.xml references correct application class
- [ ] Gradle sync successful
- [ ] Clean build successful
- [ ] App runs without crashes
- [ ] No "Class not found" errors in logcat

## Common Issues and Solutions

### Issue 1: "Class not found" Error

**Error**: Application class or activities not found

**Solution**:
1. Verify `AndroidManifest.xml` references use correct relative paths
2. Check `applicationId` matches package structure
3. Clean and rebuild project

### Issue 2: Import Errors

**Error**: Red underlines on imports

**Solution**:
1. Use Android Studio's "Optimize Imports" (Ctrl+Alt+O)
2. Manually fix incorrect imports
3. Rebuild project

### Issue 3: Resource Not Found

**Error**: R.layout or R.string not found

**Solution**:
1. Ensure `namespace` in `build.gradle.kts` is correct
2. Sync gradle files
3. Clean project
4. Rebuild

### Issue 4: Duplicate Classes

**Error**: Multiple files with same class name

**Solution**:
1. Delete old package folder completely
2. Keep only new package structure
3. Clean build

## Best Practices

1. **Use descriptive names**: 
   - Good: `com.yourcompany.bankstatement`
   - Bad: `com.app.myapp`

2. **Follow conventions**:
   - Use lowercase
   - Use your company's domain in reverse
   - Be specific about app purpose

3. **Keep it short but meaningful**:
   - Good: `com.fintech.pdfconverter`
   - Too long: `com.mycompany.android.application.pdftoexcelconverter`

4. **Unique package name**:
   - Check it's not already used on Play Store
   - Use company domain to ensure uniqueness

## Testing After Change

Run these tests to ensure everything works:

1. **Build Test**:
   ```bash
   ./gradlew clean build
   ```

2. **Install Test**:
   ```bash
   ./gradlew installDebug
   ```

3. **Functional Test**:
   - Launch app
   - Select PDF file
   - Process PDF
   - View preview
   - Export to CSV/Excel
   - Test settings

4. **Billing Test** (if configured):
   - Premium purchase flow
   - Restore purchases

## Example: Complete Change

### Before:
```
Package: com.bankstatement.pdftoexcel
App ID: com.bankstatement.pdftoexcel
Namespace: com.bankstatement.pdftoexcel
```

### After:
```
Package: com.acmetech.bankconverter
App ID: com.acmetech.bankconverter
Namespace: com.acmetech.bankconverter
```

### Steps Applied:
1. Updated `build.gradle.kts`: namespace and applicationId
2. Refactored package structure in Android Studio
3. Verified all imports
4. Synced gradle
5. Clean and rebuild
6. Tested app
7. Success! ✅

## Need Help?

If you encounter issues:

1. Check Android Studio's "Build" output for specific errors
2. Look at "Logcat" for runtime errors
3. Google the specific error message
4. Check StackOverflow for similar issues

## Summary

Changing package name involves:
1. Update Gradle configuration
2. Refactor code using Android Studio
3. Verify all files updated
4. Clean and rebuild
5. Test thoroughly

**Time Required**: 10-15 minutes

---

**Remember**: Package name must be unique on Google Play Store. Once published, it cannot be changed without creating a new app!
