# Kujon Android app

## Release steps:

1. Update version in build.gradle
```
    versionCode 34
    versionName "0.9.28"
``` 
2. Create release notes file in `release_notes/0.9.28.txt`

3. Build apk and send to Fabric Beta (Crashlytics)
    * macOS/Linux: `./gradlew clean assembleRelease crashlyticsUploadDistributionRelease`
    * Windows: `gradlew.bat clean assembleRelease crashlyticsUploadDistributionRelease`




