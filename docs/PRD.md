# **Vagabond — Product Requirements & Technical Spec (PRD)**

**Repo path:** `/Work/Android/Vagabond`  
 **Android package:** `com.dicereligion.vagabond`  
 **Supported OS:** Android 11+ (minSdk **30**)  
 **Target SDK:** Android 15 (API **35**) — compile with the latest stable SDK.

**Tooling (CLI-only):** Gradle Wrapper, JDK 17, Android SDK cmdline tools, Git, and an AI-driven CLI (Gemini CLI or equivalent) orchestrated via Makefile/scripts. **No Android Studio usage.**

**CRITICAL POLICY — NO EXPERIMENTAL FEATURES:**  
 The AI or any developer working on Vagabond must **never** use experimental/preview features. This includes (but isn’t limited to) preview/canary/alpha/beta libraries, experimental compiler flags, preview AGP/Gradle, or `@OptIn`/`@Experimental` APIs. Only stable, GA releases are allowed. Any violation must be rejected at review and blocked by CI.

---

## **1\) Vision & Goals**

### **1.1 Vision**

Vagabond is a lightweight “traveling games” container app designed for 1–2 players, optimized for on-the-go usage with minimal taps, clear UI, and robust offline behavior. Version 1 focuses on the shell (“housing app”) that will later host multiple mini-games.

### **1.2 Primary Goals**

1. Deliver the shell app with a polished **Landing** screen and two core routes: **Collections** (games list) and **Settings** (theme toggle).

2. Establish a reliable, reproducible **CLI-only** dev workflow (codegen, build, test, install) suitable for AI-agent assistance.

3. Ensure responsiveness across popular device sizes and orientations; emphasize accessibility, stability, low battery impact, and offline-first behavior.

### **1.3 Non-goals (v1)**

* Shipping any fully playable games (Collections shows placeholders only).

* Network features, sign-in, analytics, payments, or IAP.

* Tablet-specific layouts beyond responsive best practices (no multi-pane editors yet).

### **1.4 Success Metrics (v1)**

* App launches cold-to-interactive in ≤ 800 ms on a mid-range device from 2022+.

* No ANRs, no crashes in smoke tests across API 30, 33, 34, 35 devices/emulators.

* All acceptance tests pass on CI / `make verify`.

### **1.5 Target Audience (new explicit personas from Gemini PRD)**

* Commuters on trains, buses, and subways.

* Travelers in transit (airports, waiting areas, offline/no-signal environments).

* Pairs of friends who have a few spare minutes and want a quick shared activity.

* Users who prefer minimalist, low-friction, offline-first experiences.

These personas motivate: fast startup, zero network dependency, large obvious actions, and gameplay loops that don't require commitment.

---

## **2\) Scope & User Stories**

### **2.1 MVP Scope**

**Landing screen**

* Header band with a prominent title **“Vagabond”**.

* Two primary CTA buttons: **Collections** and **Settings**.

**Collections screen**

* Responsive grid of exactly **8** placeholder tiles.

* Each tile labeled **“Coming Soon”**; no gameplay yet.

**Settings screen**

* A single theme selector to switch between two app-wide themes:

  * **Cosmic Teal**

  * **Red Star**

* Selection is applied immediately, persisted, and restored on next launch.

### **2.2 Representative User Stories**

* As a traveler, I want fast access to games with **two obvious choices** from the landing screen.

* As a user, I want to preview that **games are coming**, but not be confused by inactive content.

* As a user, I want to **switch themes** and have my choice stick without extra steps.

* As a dev, I want a **repeatable CLI** to build/test/install in one command, locally or via AI.

### **2.3 Out of Scope (MVP)**

* Game settings, multiplayer lobbies, network play.

* Background services or notifications.

---

## **3\) Information Architecture & Navigation**

### **3.1 Route Map & Destinations**

App structure is single-Activity with multiple Compose destinations. We use Jetpack Navigation for Compose. Back behaves as expected; system back from the Landing screen exits the app.

Two equivalent naming layers are in play:

* **User-facing / docs naming:** “Landing” (sometimes “Home”), “Collections”, “Settings”.

* **NavHost route IDs:**

  * `"landing"` — start destination (a.k.a. Home/Landing).

  * `"collections"` — Collections grid screen.

  * `"settings"` — Settings screen.  
     The start destination is always `"landing"`. We may refer to the first screen as “Home” or “Landing” interchangeably in docs and UI copy, but the route ID remains `"landing"`.

For clarity, PRDv3 previously expressed routes as `/`, `/collections`, `/settings`. Treat those as conceptual paths; the canonical source of truth for actual Compose navigation is the `"landing"`, `"collections"`, `"settings"` route ID set above.

### **3.2 Flow**

1. App opens to `"landing"`.

2. Tapping **Collections** navigates to `"collections"`.

   * Back arrow in the Top App Bar and system back both return to `"landing"`.

3. Tapping **Settings** navigates to `"settings"`.

   * Back arrow in the Top App Bar and system back both return to `"landing"`.

---

## **4\) UX/UI Requirements**

### **4.1 Design Principles**

* **Glanceable:** Large tap targets, minimal copy, high contrast.

* **Responsive:** Adaptive layouts for compact / medium / expanded width classes.

* **Accessible:** Minimum 4.5:1 contrast for text; TalkBack labels; predictable focus order.

* **Touch-friendly:** 48dp min touch targets; avoid edge-of-screen traps.

### **4.2 Layout & Screen Requirements**

#### **Landing / Home**

* Full-width header band (theme-colored background) with title **Vagabond** in a display style (e.g. `headlineLarge`).

* Vertically stacked buttons: **Collections**, **Settings**.

  * Buttons are full-width on phones; on larger screens, center within a max-width column.

#### **Collections**

* **Top App Bar:** A Material 3 `TopAppBar` with title “Collections” and a visible Back arrow `IconButton`.

  * Tapping the Back arrow pops the NavController back stack to `"landing"`.

  * System back must still behave normally; the app bar is explicit affordance, not a custom back hack.

* **Content Grid:** Responsive `LazyVerticalGrid`, ≥2 columns on compact widths, scaling up to 4+ on expanded layouts.

* Exactly **8** inert placeholder tiles (no ripple). Each tile’s label is **“Coming Soon”** and must visually communicate disabled state.

#### **Settings**

* **Top App Bar:** A Material 3 `TopAppBar` with title “Settings” and a visible Back arrow `IconButton`.

  * Back arrow pops to `"landing"`.

  * System back still works as normal.

* **Theme Row:** A single settings row labeled “App Theme”.

  * A segmented control / radio-style selector / switch chooses between **Cosmic Teal** and **Red Star**.

  * Changing the selection updates the theme immediately and persists.

### **4.3 Typography & Spacing**

* Material 3 defaults plus a custom large-display style for the Vagabond title.

* Spacing rhythm: base 8dp grid; ≥16dp screen padding; header band height ≥56dp.

### **4.4 Icons & Imagery**

* No external iconography in v1 besides the system Back arrow in Top App Bars.

* Rely on text-forward, high-contrast blocks for clarity.

### **4.5 Empty States & Errors**

* The Collections grid itself is the empty state (“Coming Soon”).

* No snackbars/toasts are required in v1.

---

## **5\) Theming & Design System**

**Source of truth:** `/docs/Theme.md`. That document owns typography, color tokens, shadows, pressed states, etc. If this PRD and Theme.md ever disagree, **Theme.md wins**.

### **5.1 Theme Family & Visual Language**

* **PixelBrute**: “Pixel Art × Brutalist.”

  * Crisp pixel fonts (e.g. *Press Start 2P*) rendered without smoothing for retro sharpness.

  * Thick borders and hard, offset shadows instead of soft elevation.

  * Tactile pressed states via offset/shadow changes.

  * High-contrast palettes that feel loud, arcade-y, and portable.

### **5.2 Palette & App Theme Stance (refined)**

* v1 ships with two selectable palettes: **Cosmic Teal** and **Red Star**.

* Both palettes are **dark-first** in v1. Light variants may ship later but are not required for Phase 1\.

* The global theme is modeled as an enum, e.g. `AppTheme` \= `{ COSMIC_TEAL, RED_STAR }`, **not** a boolean dark/light toggle.

  * This enum is the persisted user preference and drives Material color scheme selection.

  * The user picks one in Settings, and it applies immediately and persists across restarts.

### **5.3 Implementation Notes**

* The Settings screen updates a central repository (DataStore-backed) when the user changes theme.

* The app UI (including the header band, buttons, and Top App Bars) must reactively restyle based on the current `AppTheme`.

* Compose and/or XML is allowed for visuals as long as it matches Theme.md rules (offset shadows, crisp edges, etc.).

---

## **6\) Functional Requirements**

1. **Startup & Landing:** The app always launches on the `"landing"`/Landing/Home screen; deep links are out of scope for v1.

2. **Collections Screen:** Shows exactly 8 inert “Coming Soon” tiles in a responsive grid under a Top App Bar labeled “Collections.”

3. **Settings Screen:** Shows a Top App Bar labeled “Settings” and a single theme preference row for **Cosmic Teal** vs **Red Star**.

4. **Theme Persistence:** Switching theme applies instantly, updates global styling, and persists across restarts via DataStore.

5. **Orientation Changes:** Current destination and UI state survive rotation/fold changes.

6. **Back Navigation:**

   * System Back works normally.

   * The Back arrow in each secondary screen’s Top App Bar must pop to `"landing"`.

---

## **7\) Non-Functional Requirements**

### **7.1 Stability & Performance**

* Zero known crashes or ANRs in smoke/UI tests across API 30, 33, 34, 35\.

* Cold start ≤ 800 ms on a mid-range 2022+ device; transitions ≤ 2% slow frames.

* Must stay crash-safe on theme change and rotation.

### **7.2 Responsiveness & Layout**

* Phone-first layout must gracefully adapt up through foldables/tablets/Chromebooks.

* Use adaptive Compose primitives (e.g. `LazyVerticalGrid`, `BoxWithConstraints`) to avoid overflow in landscape and larger screens.

### **7.3 Accessibility**

* All interactive elements are ≥48dp touch targets.

* Every actionable control (Button, IconButton, Switch, segmented toggle) must expose an a11y label / `contentDescription`.

* Text contrast must meet WCAG AA in all shipped palettes.

* Focus order must follow visual order; TalkBack should announce all primary actions.

### **7.4 Power, Privacy, Security**

* No background work in v1; no wake locks.

* App must **not** request `android.permission.INTERNET` in v1 (no network stack).

* No PII is stored.

### **7.5 Localization**

* English-only for v1.

* All user-facing strings live in `res/values/strings.xml`; no hard-coded UI text in Kotlin code.

---

## **8\) Architecture & Tech Stack**

* **Language:** Kotlin (JVM), Coroutines, Flows.

* **UI:** Jetpack Compose \+ Material 3\.

* **Architecture Pattern:** MVVM (Model–View–ViewModel) with unidirectional data flow (UDF). Each screen has a ViewModel that exposes immutable UI state.

* **Navigation:** Navigation Compose, single-Activity host with multiple composable destinations.

* **State Management:**

  * Prefer **stateless Composables**: Composables render based purely on parameters.

  * Screen-level state is **hoisted** into ViewModels (and backed by StateFlow / Flow).

  * UI events call ViewModel methods which update state, not local mutable state, except for short-lived UI-only state (e.g. button ripple).

  * This enforces predictable, testable UDF and makes generated code easier to review.

* **DI:** Hilt (keep wiring minimal in v1; inject ViewModels, repositories).

* **Persistence:** Preferences DataStore via a `SettingsRepository` that stores and exposes the current `AppTheme` enum (`COSMIC_TEAL`, `RED_STAR`) as a `Flow<AppTheme>`, plus a suspend setter.

* **Theming Wrapper:** A `VagabondTheme` composable applies the correct Material color scheme for the selected enum theme, and is consumed at the Activity/root level so color updates cascade instantly across screens.

* **Build:** Gradle (AGP 8.x), JDK 17; minSdk 30; target/compile SDK latest stable.

### **8.1 Project Structure (modules)**

* `app/` — Application module (navigation host, Hilt entry point, theming wrapper).

* `core/designsystem/` — Tokens, theme, typography.

* `feature/landing/` — Landing screen UI \+ logic.

* `feature/collections/` — Collections grid screen.

* `feature/settings/` — Settings screen \+ theme toggle \+ ViewModel integration.

* `docs/` — PRD.md (this), Theme.md, ADRs.  
   This separation enforces clean boundaries between UI features, shared design tokens, and persistence.

### **8.2 Initial File Map (for bootstrap / codegen seeding)**

Before full multi-module extraction (or when generating the first scaffold), codegen and contributors can assume these canonical file locations under the repo root:

* `app/src/main/java/com/dicereligion/vagabond/MainActivity.kt` — hosts `NavHost`, collects the current theme from `SettingsRepository`, and wraps content in `VagabondTheme`.

* `app/src/main/java/com/dicereligion/vagabond/VagabondApp.kt` — optional root composable that wires NavController \+ top-level surfaces.

* `app/src/main/java/com/dicereligion/vagabond/data/SettingsRepository.kt` — DataStore-backed theme persistence (get/set `AppTheme`).

* `app/src/main/java/com/dicereligion/vagabond/ui/screens/HomeScreen.kt` (a.k.a. Landing), `CollectionsScreen.kt`, `SettingsScreen.kt` — stateless Composables that render based on parameters.

* `app/src/main/java/com/dicereligion/vagabond/ui/screens/SettingsViewModel.kt` — exposes current theme and handles theme change events.

* `app/src/main/java/com/dicereligion/vagabond/ui/theme/Color.kt`, `Theme.kt`, `Type.kt` — defines palettes (Cosmic Teal, Red Star), typography, and VagabondTheme.

This map is the “starter layout” that AI agents and humans should follow when bootstrapping; it’s allowed to evolve into the multi-module layout described in 8.1 as the project matures.

### **8.3 Documentation & Code Quality Standards**

* **Every public class, interface, and function MUST have KDoc.**  
* Generated code without KDoc or with placeholder KDoc is considered incomplete.  
* ViewModels are responsible for business/UI state; Composables are primarily dumb render functions.  
* No experimental/preview/alpha dependencies (reiterating global policy above).

---

## **9\) CLI-Only Development Workflow**

We will build, run, test, lint, and install entirely from the command line and via scripted AI helpers. The project’s Makefile is the canonical entry point for all tasks (`build`, `run`, `test`, `ui-test`, `lint`, `fmt`, `verify`).

### **9.1 Directory & Git**

* **Branches:**

  * `main` — protected, release-ready.

  * `development` — primary integration branch. All feature work merges here.

    * NOTE: The branch name is `development` (not `develop`) to align with repo convention and CI triggers.

* **Feature branches:**

  * Branch off `development` using the format `feature/<area>-<short-desc>` (e.g. `feature/settings-theme`).

  * Open PRs back into `development`.

  * `make verify` (or the equivalent CI job) must pass before merge.

  * After merge, delete the feature branch.

* **Policy:**

  * Commit in small, reviewable slices.

  * Never merge code that fails `make verify`.

### **9.2 Makefile Targets (authoritative)**

The Makefile defines `init`, `sdk`, `sync`, `build`, `run`, `test`, `ui-test`, `lint`, `fmt`, `verify`, `clean`.  
 `verify` must run the full pipeline (assemble debug+release, unit tests, UI tests, static analysis).

### **9.3 AI CLI Integration**

Scripted helpers (e.g. `scripts/ai/plan.sh`, `scripts/ai/impl.sh <feature>`, `scripts/ai/fix.sh`) assist planning, codegen, and iterative fixes — but AI must never push code that fails `make verify`.

---

## **10\) Acceptance Criteria (MVP)**

1. **Landing / Home:**

   * Landing screen (route `"landing"`) shows a header band with text **“Vagabond”** and two large buttons: **Collections**, **Settings**.

   * App always launches here.

2. **Collections:**

   * Navigating to `"collections"` shows:

     * A visible Material 3 Top App Bar with title “Collections” and a Back arrow.

     * A responsive grid with exactly **8** inert tiles labeled “Coming Soon.”

     * Pressing the Back arrow or system back returns to `"landing"`.

3. **Settings:**

   * Navigating to `"settings"` shows:

     * A visible Material 3 Top App Bar with title “Settings” and a Back arrow.

     * A single theme toggle row to choose **Cosmic Teal** or **Red Star**.

     * Changing the theme updates the UI immediately and persists across process death / relaunch.

4. **Theme Persistence:**

   * The chosen `AppTheme` enum value (`COSMIC_TEAL` or `RED_STAR`) is stored via DataStore and reapplied on next launch.

5. **Platform / Build:**

   * minSdk \= 30; targetSdk \= 35; release build uses shrink/minify.

6. **CLI-Only:**

   * Full lifecycle (build, install, run tests, lint) is possible with Makefile targets; no IDE is required.

7. **Accessibility:**

   * TalkBack announces all actionable controls.

   * All interactive elements respect 48dp min hit targets and WCAG AA contrast.

8. **CI Gate:**

   * All unit/UI tests pass locally and in CI before merging to `development`.

---

## **11\) Testing Strategy**

**Unit tests:**

* Theme persistence repository — verify saving/restoring of `AppTheme`.

* ViewModels — verify initial state, theme toggle logic, and propagation to UI state.

**UI tests (Compose instrumentation):**

* Landing: title “Vagabond” plus “Collections” and “Settings” buttons are present and clickable.

* Collections:

  * Top App Bar title “Collections” renders.

  * Exactly 8 disabled “Coming Soon” tiles render.

  * Back arrow returns to Landing.

* Settings:

  * Top App Bar title “Settings” renders.

  * Theme toggle exists and switching it updates the theme and survives rotation / process death.

* Accessibility:

  * contentDescription / labels are present on interactive elements.

* Performance checks:

  * Startup time and frame pacing measured via `adb shell am start -W` and frame metrics on a mid-range device.

---

## **12\) CI/CD (CLI-First)**

**Provider:** GitHub Actions (or GitLab CI) with JDK 17\.  
 **Jobs:** `build`, `test`, `ui-test` (instrumented/Compose tests on an emulator), `lint`, `verify`.  
 **Artifacts:** Debug+release APKs and test reports are uploaded each run.  
 **Gradle cache:** Enabled for faster builds.

**Branch triggers:**

* CI runs on pushes and pull requests targeting the `development` branch.

* CI runs `make sdk` or equivalent to install SDK components, then runs the `verify` job (assemble, tests, lint).

* CI must block merges that fail `verify`.

**Sample Workflow Skeleton (`.github/workflows/ci.yml`):**

* Checkout repo

* Setup Java 17

* Install Android SDK / build-tools 35.0.0

* Cache Gradle

* Run `./gradlew assembleDebug assembleRelease testDebug lint` (or `make verify`)

* Upload APKs \+ test reports

---

## **13\) Performance, Quality & Telemetry**

* R8 shrink \+ minify release builds; enable resource shrinking.

* StrictMode (debug only): flag disk/network calls on main thread.

* Proguard: keep Compose/Hilt rules as needed.

* Logs: Debug-only; absolutely no remote telemetry in v1.

---

## **14\) Accessibility Checklist (go/no-go)**

* All actionable UI elements ≥48dp tap target.

* Every actionable control has an accurate contentDescription/label; purely decorative elements are not announced.

* Focus order matches visual order; D-Pad / keyboard navigation works; TalkBack reads “Collections”, “Settings”, etc.

* All text meets WCAG AA contrast in **Cosmic Teal** and **Red Star** palettes.

* Dynamic font scaling does not clip/overlap.

* No horizontal scroll traps; vertical scroll areas are bounded.

* Landscape, portrait, and foldable hinge modes are verified without clipping.

---

## **15\) Security & Privacy**

* No internet permission in v1 (`android.permission.INTERNET` must not be declared unless tooling absolutely requires it).

* No runtime permissions in v1.

* No PII collection or storage.

---

## **16\) Internationalization & Localization**

* English-only strings for v1.

* All user-visible text must live in `res/values/strings.xml`.

* No hard-coded UI strings in Kotlin files.

---

## **17\) Packaging & Release**

* **App ID:** `com.dicereligion.vagabond` — do not change.

* **Versioning:** Semantic-ish `versionName` and monotonically increasing `versionCode` starting at `1`.

* **Build types:** `debug` and `release`. Release is shrunk/minified.

* **Signing:** Debug keystore local; release keystore stored securely, not checked in.

---

## **18\) Risks & Mitigations**

* **AI codegen drift:** Enforce small PRs, human review, and `make verify` gates to keep generated code aligned with standards.

* **Visual inconsistency:** Centralize design tokens and theming in a shared designsystem module; consider snapshot tests.

* **Future mini-game modules:** Keep shell navigation/theme APIs stable so future `feature/games-*` modules can land without breaking core.

---

## **19\) Deliverables (v1)**

* A compilable, CLI-buildable Android project targeting minSdk 30 / targetSdk 35\.  
* Three screens implemented exactly as specified (Landing/Home, Collections, Settings).  
* Working theme toggle with persisted `AppTheme`.  
* Passing unit \+ UI test suite.  
* CI pipeline that runs on the `development` branch, gates merges with `verify`, and produces APK artifacts.

---

## **20\) Open Questions / TBD (tracked as issues)**

* Finalize exact color tokens for **Cosmic Teal** and **Red Star** in `/docs/Theme.md` (ensure WCAG AA).

* Decide whether v1 ships with a branded app icon and splash screen or if that’s v1.1.

* Lock down control style in Settings: segmented buttons vs radio buttons vs switch for theme picker (must remain obvious, tappable at 48dp+, and screen-reader-friendly).  
---

  **This updated PRD replaces the previous PRDv3.md as the single source of truth for Vagabond v1. All deviations must land in an ADR (`/docs/adrs/`) and link back here.**

