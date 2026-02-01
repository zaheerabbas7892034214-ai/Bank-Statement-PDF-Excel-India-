# Google Play Store Setup Guide

Complete guide for deploying "Bank Statement PDF → Excel (India)" to Google Play Store.

## Prerequisites

- Google Play Developer Account ($25 one-time fee)
- Android Studio Giraffe+
- Signed release build (AAB or APK)
- Privacy Policy hosted on a publicly accessible URL

## Part 1: Create In-App Product

### Step 1: Register Product in Play Console

1. **Navigate to In-App Products**
   - Login to [Google Play Console](https://play.google.com/console)
   - Select your app
   - Go to: **Monetize → In-app products**

2. **Create New Product**
   - Click **"Create product"**
   - **Product ID**: `premium_unlock` (must match the code)
   - **Product type**: One-time product

3. **Configure Product Details**
   - **Name**: Premium Unlock
   - **Description**: Unlock unlimited transaction exports with one-time payment
   - **Price**: ₹299 INR
   - **Status**: Active

4. **Save and Activate**
   - Click Save
   - Click Activate

### Step 2: Update Product ID in Code (If Changed)

If you used a different Product ID, update `BillingManager.kt`:

```kotlin
package com.bankstatement.pdftoexcel.billing

class BillingManager(private val context: Context) {
    companion object {
        const val PREMIUM_PRODUCT_ID = "your_custom_product_id" // Update here
    }
    // ... rest of code
}
```

## Part 2: Generate Signed Build

### Step 1: Create Keystore

1. **In Android Studio**:
   - Go to: **Build → Generate Signed Bundle / APK**
   - Select **Android App Bundle**
   - Click **Next**

2. **Create New Keystore**:
   - Click **Create new...**
   - **Key store path**: Choose secure location (e.g., `~/keystores/bank-statement.jks`)
   - **Password**: Strong password (save securely!)
   - **Key alias**: `bank-statement-key`
   - **Key password**: Can be same as keystore password
   - **Validity**: 25 years (Google requirement)
   - **Certificate**: 
     - First and Last Name: Your Name/Company
     - Organizational Unit: (Optional)
     - Organization: Your Company
     - City/Locality: Your City
     - State/Province: Your State
     - Country Code: IN

3. **Important**: 
   - Save keystore file securely
   - Back up to multiple locations
   - Never commit to Git
   - Store passwords in password manager

### Step 2: Sign the Build

1. Select **release** build variant
2. Click **Next**
3. Review and click **Finish**
4. Wait for build to complete
5. AAB will be in: `app/release/app-release.aab`

## Part 3: Upload to Play Console

### Step 1: Create Release

1. **Navigate to Production**:
   - Go to: **Release → Production**
   - Click **Create new release**

2. **Upload AAB**:
   - Click **Upload** and select your AAB file
   - Wait for upload and processing

3. **Add Release Notes**:
   ```
   Version 1.0.0
   - Convert Indian bank statement PDFs to Excel/CSV
   - Support for text-based and scanned PDFs
   - OCR powered by ML Kit
   - Export to CSV and XLSX formats
   - Premium unlock for unlimited transactions
   - Material 3 design with dark mode support
   ```

4. Click **Next**

### Step 2: Review Release

1. Check for warnings/errors
2. Fix any issues reported
3. Click **Start rollout to Production**

## Part 4: Complete Store Listing

### Step 1: Store Presence → Main Store Listing

1. **App name**: Bank Statement PDF to Excel
2. **Short description** (80 chars max):
   ```
   Convert Indian bank statements to Excel/CSV. Supports OCR for scanned PDFs.
   ```

3. **Full description** (4000 chars max):
   ```
   Transform your Indian bank statement PDFs into Excel spreadsheets or CSV files with ease!

   📄 FEATURES:
   • Convert PDF bank statements to Excel (XLSX) or CSV format
   • Support for both text-based and scanned PDFs
   • Powerful OCR using ML Kit for scanned documents
   • Extract Date, Description, Debit, Credit, and Balance columns
   • Automatic formatting and data cleaning
   • Share exported files directly from the app

   🏦 SUPPORTED FORMATS:
   • Indian bank statement PDFs in standard format
   • Multiple date formats (DD/MM/YYYY, DD-MM-YYYY, DD.MM.YYYY)
   • Currency symbols and Indian numbering (₹, lakhs, crores)

   💎 PREMIUM FEATURES:
   • Free: Preview first 10 transactions
   • Premium: Unlock unlimited transaction exports
   • One-time payment of ₹299
   • Restore purchases on new devices

   🎨 MODERN DESIGN:
   • Material 3 design language
   • Dark and Light themes
   • Smooth animations
   • Intuitive user interface

   📊 EXPORT OPTIONS:
   • Excel (XLSX) format with formatting
   • CSV format for universal compatibility
   • Share exported files via any app

   🔒 PRIVACY & SECURITY:
   • All processing happens on your device
   • No data sent to external servers
   • No account required
   • Secure Google Play billing

   Perfect for:
   • Personal finance tracking
   • Tax preparation
   • Expense analysis
   • Accounting purposes
   • Financial planning

   Download now and simplify your financial data management!
   ```

### Step 2: Graphics

#### App Icon (Required)
- **Size**: 512 x 512 pixels
- **Format**: 32-bit PNG
- **Color**: Use brand colors from the app

#### Feature Graphic (Required)
- **Size**: 1024 x 500 pixels
- **Format**: JPEG or 24-bit PNG
- **Design**: Showcase app features with text overlay

#### Screenshots (Required - Minimum 2)
Create screenshots showing:
1. Home screen with PDF selection
2. Transaction preview with data
3. Export options screen
4. Premium unlock dialog
5. Settings screen

**Phone Screenshots**:
- Minimum: 320px
- Maximum: 3840px
- Aspect ratio: 16:9 or 9:16

**Recommendations**:
- Use device frames
- Show actual app functionality
- Add text descriptions on screenshots
- Showcase premium features

### Step 3: Categorization

1. **App category**: Finance (or Productivity)
2. **Tags**: Select relevant tags
   - Finance
   - PDF
   - Excel
   - Converter
   - Banking

### Step 4: Contact Details

1. **Email**: your.email@domain.com
2. **Phone** (optional): +91-XXXXXXXXXX
3. **Website** (optional): https://yourwebsite.com

### Step 5: Privacy Policy

1. **Privacy policy URL**: https://yourwebsite.com/privacy
2. Must include:
   - Data collection practices
   - How data is used
   - Third-party services (Google Play Billing, ML Kit)
   - User rights
   - Contact information

**Sample Privacy Policy Template**:
```
Privacy Policy for Bank Statement PDF to Excel

Last updated: [Date]

1. Data Collection
We collect minimal data required for app functionality:
- PDF files selected by the user (processed locally)
- Purchase history (through Google Play Billing)

2. Data Usage
- PDF files are processed entirely on your device
- No PDF content is sent to external servers
- Purchase information is managed by Google Play

3. Third-Party Services
- Google Play Billing: For premium purchases
- ML Kit Text Recognition: For OCR processing (Google service)

4. Data Storage
- Premium status stored locally on device
- No cloud storage of your documents

5. Your Rights
- Request data deletion
- Contact us for privacy concerns

Contact: your.email@domain.com
```

## Part 5: Content Rating

1. **Start Questionnaire**:
   - Go to: **Policy → App content → Content rating**
   - Click **Start questionnaire**

2. **Select Category**: Utility, Productivity, Communication, or other

3. **Answer Questions**:
   - Does your app contain violence? **No**
   - Does your app contain sexual content? **No**
   - Does your app contain language? **No**
   - Does your app contain controlled substances? **No**
   - Does your app contain gambling? **No**

4. **Get Rating**:
   - Submit questionnaire
   - Receive content ratings for different regions

## Part 6: Pricing & Distribution

### Pricing

1. **Free app**: Yes (with in-app purchases)
2. **Contains ads**: No
3. **In-app purchases**: Yes (₹299)

### Countries

1. **Available in**: Select countries
   - Recommended: Start with India
   - Can expand later to other countries

2. **Distributed on**: 
   - ☑ Google Play
   - ☐ Other Android app stores (optional)

### Program Opt-in (Optional)

1. **Google Play Pass**: Consider opting in
2. **Play Points**: Consider opting in

## Part 7: Testing Track (Recommended)

Before production release, test the app:

### Internal Testing

1. **Create Internal Test Track**:
   - Go to: **Release → Testing → Internal testing**
   - Create release
   - Upload same AAB

2. **Add Testers**:
   - Add email addresses
   - Send them test link
   - Test all features including billing

3. **Test Billing**:
   - Go to: **Setup → License testing**
   - Add test Gmail accounts
   - These can make test purchases without charges

### Testing Checklist

- [ ] PDF file selection works
- [ ] PDF processing (text and OCR) works
- [ ] Transaction preview displays correctly
- [ ] Premium unlock flow works
- [ ] Export to CSV works
- [ ] Export to Excel works
- [ ] File sharing works
- [ ] Settings and privacy policy link works
- [ ] Restore purchases works
- [ ] Dark/Light theme switching works

## Part 8: Submit for Review

1. **Review Checklist**:
   - [ ] Store listing completed
   - [ ] Screenshots uploaded
   - [ ] Content rating received
   - [ ] Privacy policy URL added
   - [ ] In-app product created
   - [ ] AAB uploaded
   - [ ] Release notes added

2. **Submit**:
   - Go to production release
   - Click **Start rollout to Production**
   - Confirm submission

3. **Wait for Review**:
   - Usually takes 1-3 days
   - Monitor status in Play Console
   - Check email for updates

## Part 9: Post-Launch

### Monitor Performance

1. **Dashboard**: Check key metrics
   - Installs
   - Uninstalls
   - Crashes
   - ANRs (App Not Responding)
   - Ratings & Reviews

2. **User Feedback**: 
   - Respond to reviews
   - Fix reported issues
   - Update app regularly

### Handle Crashes

1. **View Crash Reports**:
   - Go to: **Quality → Android vitals → Crashes & ANRs**
   - Review crash logs
   - Fix critical issues

2. **Release Updates**:
   - Fix bugs
   - Increment version (1.0.1, 1.0.2, etc.)
   - Upload new AAB
   - Add release notes

### Marketing

1. **Share**: Social media, website
2. **ASO**: Optimize store listing for search
3. **Updates**: Regular updates show active development

## Troubleshooting

### Common Issues

**Issue**: "You need to use a different package name"
- **Solution**: Package name already in use. Change in build.gradle.kts

**Issue**: "Upload failed - Invalid signature"
- **Solution**: Ensure you're using release keystore, not debug

**Issue**: "Test purchase not working"
- **Solution**: 
  - Verify app in internal testing track
  - Check test account in license testing
  - App must be signed with release key

**Issue**: "In-app product not showing"
- **Solution**:
  - Product must be activated in Play Console
  - App must be published (at least internal testing)
  - Clear Play Store cache

## Support Resources

- [Google Play Console Help](https://support.google.com/googleplay/android-developer)
- [Billing Library Documentation](https://developer.android.com/google/play/billing)
- [ML Kit Documentation](https://developers.google.com/ml-kit)

## Checklist Summary

- [ ] Google Play Developer Account created
- [ ] App registered in Play Console
- [ ] In-app product created (premium_unlock, ₹299)
- [ ] Keystore created and backed up
- [ ] Signed AAB generated
- [ ] Store listing completed
- [ ] Graphics uploaded (icon, feature graphic, screenshots)
- [ ] Privacy policy published and linked
- [ ] Content rating completed
- [ ] Test track created and tested
- [ ] Production release submitted

---

**Good luck with your launch! 🚀**
