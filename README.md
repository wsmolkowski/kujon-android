Release steps:
1. Update version in build.gradle
    versionCode 34
    versionName "0.9.28"
    
2. Create release notes file in release_notes/0.9.28.txt

3. ./gradlew clean assembleRelease crashlyticsUploadDistributionRelease