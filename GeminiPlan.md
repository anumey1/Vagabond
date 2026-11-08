# Gemini Plan for Vagabond App Development

This document outlines the plan for implementing the Vagabond Android application, adhering to the specifications in `PRD.md`, `Theme.md`, and leveraging the `AndroidCLI.md` for development workflow.

## 1. Project Setup & Initial Configuration

### 1.1 Verify Environment
*   Ensure JDK 17, Android SDK Command Line Tools, ADB, and Git are correctly installed and configured as per `AndroidCLI.md`.
*   Verify `ANDROID_HOME` and `PATH` variables.

### 1.2 Gradle Configuration
*   Review `build.gradle.kts` (project and app level) and `settings.gradle.kts` for `minSdk`, `targetSdk`, `compileSdk` versions (minSdk 30, targetSdk 35).
*   Add necessary Compose dependencies (Material 3, Navigation Compose, Hilt, DataStore).
*   Configure R8 shrinking and resource shrinking for release builds.

### 1.3 Initial Project Structure (app/ module first)
*   For v1, we will initially implement within the `app/` module as per the "Initial File Map" in `PRD.md` (Section 8.2). Multi-module extraction will be considered for later stages.

## 2. Core Theming & Design System Implementation

This is a critical first step as it defines the visual identity of the app.

### 2.1 Font Integration (Press Start 2P)
*   Download `Press Start 2P` font (e.g., `press_start_2p.ttf`).
*   Create `app/src/main/res/font/press_start_2p.ttf`.
*   Create `app/src/main/res/font/pixel_font.xml` to define the font family.
*   Update `app/src/main/res/values/themes.xml` to apply `pixel_font` as the default `fontFamily`.

### 2.2 Color Palettes
*   Create `app/src/main/java/com/dicereligion/vagabond/ui/theme/Color.kt`.
*   Define `AppTheme` enum (`COSMIC_TEAL`, `RED_STAR`).
*   Define Material 3 `ColorScheme` objects for both `Cosmic Teal` and `Red Star` themes using the hex codes provided in `Theme.md`.
    *   Map the generic color roles (e.g., `background`, `container_bg`, `text_light`, `accent_one`) to Material 3 color properties (e.g., `surface`, `onSurface`, `primary`, `onPrimary`).
*   Ensure all colors meet WCAG AA contrast requirements.

### 2.3 `VagabondTheme` Composable
*   Create `app/src/main/java/com/dicereligion/vagabond/ui/theme/Theme.kt`.
*   Implement `VagabondTheme` composable that takes an `AppTheme` enum as input and applies the corresponding `ColorScheme` and `Typography`.
*   Ensure anti-aliasing is disabled for text rendering within the theme, as specified in `Theme.md`.

### 2.4 Typography
*   Create `app/src/main/java/com/dicereligion/vagabond/ui/theme/Type.kt`.
*   Define `Typography` using `Press Start 2P` font, including a custom large-display style for the "Vagabond" title.
*   Implement text shadow for headers as per `Theme.md`.

### 2.5 Borders & Hard Shadows
*   Develop reusable Composables or Modifiers for applying thick borders and hard, offset shadows to UI elements, as detailed in `Theme.md`. This will be crucial for buttons and containers.
*   Ensure tactile pressed states for interactive elements.

## 3. Data Persistence (Theme Selection)

### 3.1 `SettingsRepository`
*   Create `app/src/main/java/com/dicereligion/vagabond/data/SettingsRepository.kt`.
*   Implement a DataStore-backed repository to store and retrieve the selected `AppTheme` enum.
*   Expose the current theme as a `Flow<AppTheme>` and provide a suspend setter function.

## 4. Navigation Setup

### 4.1 `MainActivity.kt`
*   Create `app/src/main/java/com/dicereligion/vagabond/MainActivity.kt`.
*   Set up the `NavHost` with `"landing"` as the start destination.
*   Collect the current `AppTheme` from `SettingsRepository` and wrap the content in `VagabondTheme`.

### 4.2 `VagabondApp.kt` (Optional Root Composable)
*   Create `app/src/main/java/com/dicereligion/vagabond/VagabondApp.kt` if a root composable is needed to wire `NavController` and top-level surfaces.

## 5. Screen Implementations (MVP)

### 5.1 Landing Screen
*   Create `app/src/main/java/com/dicereligion/vagabond/ui/screens/LandingScreen.kt`.
*   Implement the header band with the "Vagabond" title (using the custom large-display typography and text shadow).
*   Implement two full-width buttons: "Collections" and "Settings", applying the PixelBrute border and shadow styles, and tactile pressed states.
*   Ensure responsiveness for different screen sizes.

### 5.2 Collections Screen
*   Create `app/src/main/java/com/dicereligion/vagabond/ui/screens/CollectionsScreen.kt`.
*   Implement a Material 3 `TopAppBar` with the title "Collections" and a visible Back arrow `IconButton`.
*   Implement a responsive `LazyVerticalGrid` with at least 2 columns on compact widths.
*   Create exactly 8 inert placeholder tiles, each labeled "Coming Soon", applying PixelBrute styling (borders, shadows, disabled visual state).
*   Configure back navigation to pop to `"landing"`.

### 5.3 Settings Screen
*   Create `app/src/main/java/com/dicereligion/vagabond/ui/screens/SettingsScreen.kt`.
*   Implement a Material 3 `TopAppBar` with the title "Settings" and a visible Back arrow `IconButton`.
*   Implement a single theme toggle row labeled "App Theme".
*   Use a segmented control/radio-style selector/switch for "Cosmic Teal" and "Red Star" options, applying PixelBrute styling.
*   Integrate with `SettingsViewModel` to update and persist the selected theme.
*   Configure back navigation to pop to `"landing"`.

## 6. ViewModel Integration

### 6.1 `SettingsViewModel`
*   Create `app/src/main/java/com/dicereligion/vagabond/ui/screens/SettingsViewModel.kt`.
*   Inject `SettingsRepository`.
*   Expose the current theme as UI state (e.g., `StateFlow`).
*   Provide a function to handle theme change events, updating the `SettingsRepository`.

## 7. Testing Strategy

### 7.1 Unit Tests
*   Create `app/src/test/java/com/dicereligion/vagabond/data/SettingsRepositoryTest.kt` to verify saving/restoring `AppTheme`.
*   Create `app/src/test/java/com/dicereligion/vagabond/ui/screens/SettingsViewModelTest.kt` to verify initial state, theme toggle logic, and state propagation.

### 7.2 UI Tests (Instrumentation)
*   Create `app/src/androidTest/java/com/dicereligion/vagabond/ui/screens/LandingScreenTest.kt`, `CollectionsScreenTest.kt`, `SettingsScreenTest.kt`.
*   Verify screen elements (titles, buttons, tiles, theme toggle).
*   Verify navigation flows and back stack behavior.
*   Verify theme changes are applied and persisted.
*   Verify accessibility (content descriptions, touch targets).

## 8. CLI-Only Development Workflow Integration

### 8.1 Makefile (Placeholder)
*   A `Makefile` will be assumed to exist or created later to orchestrate `build`, `run`, `test`, `ui-test`, `lint`, `verify` commands as per `AndroidCLI.md`.
*   Ensure `make verify` runs the full pipeline (assemble debug+release, unit tests, UI tests, static analysis).

### 8.2 Linting & Code Quality
*   Ensure KDoc is added to all public classes, interfaces, and functions.
*   Run `./gradlew lint` regularly.

## 9. Open Questions / TBD (from PRD.md)

*   Finalize exact color tokens for Cosmic Teal and Red Star in `/docs/Theme.md` (ensure WCAG AA). (This will be handled during implementation based on the provided hex codes).
*   Decide whether v1 ships with a branded app icon and splash screen or if that’s v1.1. (Will assume default for v1 unless specified).
*   Lock down control style in Settings: segmented buttons vs radio buttons vs switch for theme picker. (Will use a segmented control as it's a common and clear pattern for two distinct choices).

This plan will guide the implementation process, ensuring all requirements from the PRD and Theme.md are met, and the development workflow aligns with the CLI-only approach.
