# Android CLI Development Guide

A comprehensive guide for building, testing, and maintaining Android applications entirely from the command line, minimizing dependency on Android Studio.

## Philosophy

Modern Android development can be done almost entirely via CLI using Gradle wrapper, ADB (Android Debug Bridge), and Git. Android Studio is only needed once during initial project setup for SDK configuration and first-time file generation. After that, all development, building, testing, deployment, and maintenance can be handled through terminal commands.

## Prerequisites

### Required Tools

1. **Java Development Kit (JDK)**
   - Version: JDK 17 recommended (for modern Android projects)
   - Verify: `java -version`

2. **Android SDK Command Line Tools**
   - Installed via Android Studio initial setup or standalone SDK manager
   - Location typically: `~/Library/Android/sdk` (macOS) or `$ANDROID_HOME`
   - Verify: `echo $ANDROID_HOME`

3. **ADB (Android Debug Bridge)**
   - Part of Android SDK Platform Tools
   - Add to PATH: `export PATH="$PATH:$ANDROID_HOME/platform-tools"`
   - Verify: `adb version`

4. **Git**
   - Version control system
   - Verify: `git --version`

5. **Gradle Wrapper**
   - Included in Android projects as `./gradlew` (Unix) or `gradlew.bat` (Windows)
   - No separate installation needed

### Environment Setup

Add to your shell profile (`~/.zshrc`, `~/.bashrc`, etc.):

```bash
# Android SDK
export ANDROID_HOME="$HOME/Library/Android/sdk"  # macOS
# export ANDROID_HOME="$HOME/Android/Sdk"        # Linux

# Add Android tools to PATH
export PATH="$PATH:$ANDROID_HOME/platform-tools"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin"
export PATH="$PATH:$ANDROID_HOME/emulator"
```

## Project Structure Understanding

```
AndroidProject/
├── app/                          # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/            # Kotlin/Java source code
│   │   │   ├── res/             # Resources (layouts, strings, etc.)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                # Unit tests
│   │   └── androidTest/         # Instrumented tests
│   ├── build.gradle.kts         # Module-level build config
│   └── proguard-rules.pro       # Code obfuscation rules
├── gradle/
│   ├── libs.versions.toml       # Centralized dependency versions
│   └── wrapper/                 # Gradle wrapper files
├── build.gradle.kts             # Project-level build config
├── settings.gradle.kts          # Module configuration
├── gradlew                      # Gradle wrapper script (Unix)
└── gradlew.bat                  # Gradle wrapper script (Windows)
```

## Core Gradle Commands

### Building APKs

```bash
# Navigate to project directory first
cd /path/to/your/android/project

# Build debug APK (unsigned, for testing)
./gradlew assembleDebug

# Build release APK (requires signing configuration)
./gradlew assembleRelease

# Build all variants
./gradlew assemble

# Clean build (remove all build artifacts)
./gradlew clean

# Clean and rebuild
./gradlew clean assembleDebug
```

**Output locations:**
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

### Building App Bundles (for Play Store)

```bash
# Generate Android App Bundle (AAB) for debug
./gradlew bundleDebug

# Generate AAB for release
./gradlew bundleRelease
```

**Output location:** `app/build/outputs/bundle/release/app-release.aab`

### Gradle Configuration

```bash
# List all available Gradle tasks
./gradlew tasks

# List all available Gradle tasks with descriptions
./gradlew tasks --all

# View project dependencies
./gradlew app:dependencies

# Check for dependency updates
./gradlew dependencyUpdates  # Requires plugin
```

## ADB: Device Management & Deployment

### Device Connection

```bash
# List connected devices
adb devices

# Connect to specific device (if multiple connected)
adb -s <device_id> <command>

# Check device details
adb shell getprop ro.build.version.release  # Android version
adb shell getprop ro.product.model          # Device model

# Restart ADB server (if connection issues)
adb kill-server
adb start-server
```

### Installing & Uninstalling Apps

```bash
# Install APK on connected device
adb install app/build/outputs/apk/debug/app-debug.apk

# Reinstall (replace existing app, keeps data)
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Install and immediately launch
adb install -r app/build/outputs/apk/debug/app-debug.apk && \
  adb shell am start -n com.yourpackage/.MainActivity

# Uninstall app
adb uninstall com.yourpackage

# Uninstall but keep data
adb uninstall -k com.yourpackage
```

### Combined Build & Deploy

```bash
# One-liner: Build debug APK and install
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk

# Build, install, and launch
./gradlew assembleDebug && \
  adb install -r app/build/outputs/apk/debug/app-debug.apk && \
  adb shell am start -n com.yourpackage/.MainActivity
```

### App Control

```bash
# Launch app (replace with your package and activity)
adb shell am start -n com.yourpackage/.MainActivity

# Force stop app
adb shell am force-stop com.yourpackage

# Clear app data and cache
adb shell pm clear com.yourpackage

# Grant runtime permissions
adb shell pm grant com.yourpackage android.permission.CAMERA

# Revoke permissions (useful for testing permission flows)
adb shell pm revoke com.yourpackage android.permission.CAMERA
```

## Testing

### Unit Tests (JVM-based)

```bash
# Run all unit tests
./gradlew test

# Run tests for specific build variant
./gradlew testDebugUnitTest
./gradlew testReleaseUnitTest

# Run specific test class
./gradlew test --tests="com.yourpackage.ExampleUnitTest"

# Run specific test method
./gradlew test --tests="com.yourpackage.ExampleUnitTest.testExample"

# Run tests with detailed output
./gradlew test --info

# Generate test report (HTML)
# Report location: app/build/reports/tests/testDebugUnitTest/index.html
./gradlew test
open app/build/reports/tests/testDebugUnitTest/index.html  # macOS
```

### Instrumented Tests (Android device/emulator required)

```bash
# Run all instrumented tests on connected device
./gradlew connectedAndroidTest

# Run for specific build variant
./gradlew connectedDebugAndroidTest

# Run specific test class
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.yourpackage.ExampleInstrumentedTest

# Run specific test method
./gradlew connectedAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.yourpackage.ExampleInstrumentedTest#testExample

# Generate test report
# Report location: app/build/reports/androidTests/connected/index.html
./gradlew connectedAndroidTest
open app/build/reports/androidTests/connected/index.html  # macOS
```

### Test Coverage

```bash
# Enable coverage in build.gradle.kts first:
# android {
#     buildTypes {
#         debug {
#             enableUnitTestCoverage = true
#             enableAndroidTestCoverage = true
#         }
#     }
# }

# Generate coverage report
./gradlew createDebugCoverageReport

# Report location: app/build/reports/coverage/androidTest/debug/index.html
```

## Logging & Debugging

### Logcat: Real-time Logs

```bash
# View all logs
adb logcat

# Clear log buffer first, then show new logs
adb logcat -c && adb logcat

# Filter by application package
adb logcat | grep com.yourpackage

# Filter by log level (V=Verbose, D=Debug, I=Info, W=Warn, E=Error, F=Fatal)
adb logcat *:E  # Show only errors

# Filter by tag
adb logcat -s "YourTag"

# Multiple filters
adb logcat -s "Tag1:D" "Tag2:I"

# Save logs to file
adb logcat > logfile.txt

# Time-stamped logs
adb logcat -v time

# Show logs from specific process
adb logcat --pid=$(adb shell pidof -s com.yourpackage)

# Filter and colorize (requires grep with color support)
adb logcat | grep --color=auto -E "Error|Exception"
```

### Build Logs

```bash
# Show detailed build information
./gradlew assembleDebug --info

# Show debug-level build information
./gradlew assembleDebug --debug

# Show build errors with stack traces
./gradlew assembleDebug --stacktrace

# Show full stack traces
./gradlew assembleDebug --full-stacktrace

# Scan for build issues
./gradlew assembleDebug --scan  # Generates online build report
```

### App State Inspection

```bash
# Check if app is running
adb shell ps | grep com.yourpackage

# Get app process ID
adb shell pidof com.yourpackage

# Monitor app memory usage
adb shell dumpsys meminfo com.yourpackage

# Monitor app battery usage
adb shell dumpsys batterystats com.yourpackage

# Check app permissions
adb shell dumpsys package com.yourpackage | grep permission
```

### AlarmManager Debugging (for reminder/scheduling apps)

```bash
# View scheduled alarms for your app
adb shell dumpsys alarm | grep com.yourpackage

# Force device into Doze mode (test alarm behavior during sleep)
adb shell dumpsys deviceidle force-idle

# Step through Doze states
adb shell cmd deviceidle step

# Exit Doze mode
adb shell cmd deviceidle unforce

# Check battery optimization whitelist
adb shell dumpsys deviceidle whitelist
```

### Notification Debugging

```bash
# View notification channels
adb shell cmd notification list

# Post test notification
adb shell cmd notification post -t "Test Title" com.yourpackage test_tag "Test message"

# Clear all notifications for your app
adb shell cmd notification clear com.yourpackage
```

## Code Quality & Linting

### Lint Checks

```bash
# Run lint checks
./gradlew lint

# Run lint for specific variant
./gradlew lintDebug
./gradlew lintRelease

# Generate lint report
# Report location: app/build/reports/lint-results.html
./gradlew lint
open app/build/reports/lint-results.html  # macOS
```

### Kotlin/Java Code Style

```bash
# If using ktlint plugin
./gradlew ktlintCheck
./gradlew ktlintFormat  # Auto-format code

# If using detekt plugin (static analysis)
./gradlew detekt
```

## Git Version Control

### Initial Setup

```bash
# Initialize repository
git init

# Add Android-specific .gitignore
# Create .gitignore with standard Android exclusions:
cat > .gitignore << 'EOF'
# Built application files
*.apk
*.aab
*.ap_
*.aab

# Files for the ART/Dalvik VM
*.dex

# Java class files
*.class

# Generated files
bin/
gen/
out/
build/

# Gradle files
.gradle/
gradle-app.setting
!gradle-wrapper.jar
.gradletasknamecache

# Local configuration file (sdk path, etc)
local.properties

# Android Studio
.idea/
*.iml
*.iws
*.ipr
captures/
.externalNativeBuild
.cxx/

# Keystore files
*.jks
*.keystore

# OS files
.DS_Store
Thumbs.db
EOF

# Stage and commit
git add .
git commit -m "Initial commit"
```

### Daily Workflow

```bash
# Check status
git status

# View changes
git diff
git diff app/src/main/java/com/yourpackage/MainActivity.kt

# Stage changes
git add .
git add app/src/main/java/com/yourpackage/MainActivity.kt

# Commit changes
git commit -m "Add feature X"

# Amend last commit (if you forgot something)
git add forgotten_file.kt
git commit --amend --no-edit

# View commit history
git log --oneline
git log --oneline --graph --all
```

### Branch Management

```bash
# List branches
git branch
git branch -a  # Include remote branches

# Create new branch
git branch feature/new-feature
git checkout feature/new-feature

# Create and checkout in one command
git checkout -b feature/new-feature

# Switch between branches
git checkout main
git checkout feature/new-feature

# Merge branch into current branch
git checkout main
git merge feature/new-feature

# Delete branch
git branch -d feature/new-feature  # Safe delete (merged only)
git branch -D feature/new-feature  # Force delete
```

### Remote Repository

```bash
# Add remote
git remote add origin https://github.com/username/repository.git

# View remotes
git remote -v

# Push to remote
git push origin main
git push origin feature/new-feature

# Push and set upstream
git push -u origin main

# Pull from remote
git pull origin main

# Fetch without merging
git fetch origin

# Clone repository
git clone https://github.com/username/repository.git
```

### Tagging Releases

```bash
# Create tag (for version releases)
git tag v1.0.0
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push tags
git push origin v1.0.0
git push origin --tags  # Push all tags

# List tags
git tag
git tag -l "v1.*"

# Checkout specific tag
git checkout v1.0.0
```

### Stashing Changes

```bash
# Save work in progress
git stash
git stash save "WIP: feature description"

# List stashes
git stash list

# Apply stash
git stash apply
git stash apply stash@{0}

# Apply and remove stash
git stash pop

# Drop stash
git stash drop stash@{0}
```

### Undoing Changes

```bash
# Discard unstaged changes in file
git checkout -- app/src/main/java/com/yourpackage/MainActivity.kt

# Discard all unstaged changes
git checkout -- .

# Unstage file (keep changes)
git reset HEAD app/src/main/java/com/yourpackage/MainActivity.kt

# Reset to last commit (discard all changes)
git reset --hard HEAD

# Revert commit (create new commit that undoes changes)
git revert <commit-hash>
```

## Performance & Profiling

### Build Performance

```bash
# Enable Gradle build cache (add to gradle.properties)
org.gradle.caching=true
org.gradle.parallel=true
org.gradle.configureondemand=true

# Profile build
./gradlew assembleDebug --profile
# Report location: build/reports/profile/

# Check build configuration
./gradlew assembleDebug --dry-run
```

### App Performance

```bash
# CPU profiling (method tracing)
adb shell am profile start com.yourpackage /sdcard/profile.trace
# ... use the app ...
adb shell am profile stop com.yourpackage
adb pull /sdcard/profile.trace

# Memory dump
adb shell am dumpheap com.yourpackage /sdcard/heap.hprof
adb pull /sdcard/heap.hprof

# Frame rendering metrics
adb shell dumpsys gfxinfo com.yourpackage
```

## Advanced Scenarios

### Working with Emulators

```bash
# List available emulators
emulator -list-avds

# Start emulator from command line
emulator -avd <avd_name> &

# Start emulator headless (no GUI)
emulator -avd <avd_name> -no-window &

# Start with specific RAM
emulator -avd <avd_name> -memory 2048 &
```

### Multi-Module Projects

```bash
# Build specific module
./gradlew :moduleName:assembleDebug

# Run tests for specific module
./gradlew :moduleName:test

# List all modules
./gradlew projects
```

### Signing Configuration (Release Builds)

Add to `app/build.gradle.kts`:

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

Build signed release:

```bash
# Set environment variables
export KEYSTORE_PASSWORD="your_password"
export KEY_ALIAS="your_alias"
export KEY_PASSWORD="your_key_password"

# Build signed APK
./gradlew assembleRelease
```

### Dependency Management

```bash
# Check for outdated dependencies
./gradlew dependencyUpdates  # Requires com.github.ben-manes.versions plugin

# Resolve dependency conflicts
./gradlew app:dependencies --configuration debugRuntimeClasspath
```

## Workflow Best Practices

### 1. Development Cycle

```bash
# 1. Pull latest changes
git pull origin main

# 2. Create feature branch
git checkout -b feature/add-login

# 3. Make changes, test locally
./gradlew test
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk

# 4. Run lint and fix issues
./gradlew lintDebug

# 5. Commit changes
git add .
git commit -m "Add login functionality"

# 6. Push to remote
git push -u origin feature/add-login

# 7. Create pull request (via GitHub/GitLab web interface or CLI tools)
```

### 2. Pre-Commit Checklist

```bash
# Run all checks before committing
./gradlew test && \
./gradlew lintDebug && \
./gradlew assembleDebug && \
echo "All checks passed!"
```

### 3. Continuous Integration Simulation

```bash
# Mimic CI pipeline locally
./gradlew clean && \
./gradlew lintDebug && \
./gradlew testDebugUnitTest && \
./gradlew assembleDebug && \
./gradlew assembleRelease && \
echo "CI simulation passed!"
```

## Troubleshooting

### Common Issues

```bash
# Gradle daemon issues
./gradlew --stop  # Stop all Gradle daemons
./gradlew --no-daemon assembleDebug  # Build without daemon

# Dependency resolution failures
./gradlew --refresh-dependencies assembleDebug

# ADB not detecting device
adb kill-server
adb start-server
adb devices

# Build cache corruption
./gradlew cleanBuildCache
./gradlew clean

# Incorrect Java version
# Check: java -version
# Set JAVA_HOME to correct version
export JAVA_HOME=/path/to/jdk17

# Android SDK not found
# Verify ANDROID_HOME is set correctly
echo $ANDROID_HOME
# Update local.properties with SDK path
echo "sdk.dir=/path/to/android/sdk" > local.properties
```

### Debug Build Failures

```bash
# Get full stack trace
./gradlew assembleDebug --stacktrace --info

# Check for conflicting dependencies
./gradlew app:dependencies | grep conflict

# Validate Gradle configuration
./gradlew help
```

## Quick Reference Cheat Sheet

```bash
# BUILD
./gradlew assembleDebug              # Build debug APK
./gradlew assembleRelease            # Build release APK
./gradlew clean                      # Clean build

# TEST
./gradlew test                       # Run unit tests
./gradlew connectedAndroidTest       # Run instrumented tests

# INSTALL
adb install -r app/build/outputs/apk/debug/app-debug.apk

# BUILD + INSTALL
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk

# LOGS
adb logcat | grep com.yourpackage   # View app logs
./gradlew assembleDebug --info      # Build with logs

# QUALITY
./gradlew lint                       # Run lint checks

# GIT
git status                           # Check status
git checkout -b feature/name         # New branch
git add . && git commit -m "msg"     # Stage & commit
git push origin branch-name          # Push branch

# DEVICE
adb devices                          # List devices
adb shell pm clear com.yourpackage  # Clear app data
```

## Conclusion

This CLI-based workflow enables:
- ✅ **Faster iteration**: No Android Studio startup time
- ✅ **Automation**: Scriptable builds and tests
- ✅ **CI/CD ready**: Commands work in any environment
- ✅ **Version control friendly**: All changes trackable via Git
- ✅ **Lightweight**: Works on any machine with JDK and Android SDK

Android Studio remains useful for:
- Initial project setup
- Visual layout design
- Advanced debugging with breakpoints
- Profiling with built-in tools

However, 95% of daily development can be accomplished through the terminal, making it ideal for AI-assisted development, remote work, and automated workflows.
