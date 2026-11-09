# Vagabond: status

> **Draft (MakeStats Create, C2).** Not yet verified.

> Offline "traveling games" app for 1–2 players: a shell (Landing, Collections, Settings) hosting mini-games, with
> Hangman playable. Read §1 before changing anything. History: `docs/Changelog.md`.

| | |
|---|---|
| **Updated** | 2026-11-09: stats.md created (MakeStats Create) |
| **Verified** | 2026-11-09, full, against code, build, unit-test compile and lint (method: §13) |
| **Identity** | `com.dicereligion.vagabond` (app id and namespace), version 4.1 (1) |
| **Stack** | Kotlin 2.0.21, AGP 8.13.0, Gradle 8.13, Compose BOM 2024.09.00, Material3 1.2.1, Hilt 2.51.1 (kapt), DataStore 1.1.1; minSdk 30, target/compileSdk 36; Gradle runs on JetBrains Runtime 21 |
| **Branch · verify** | `main` · `./gradlew testDebugUnitTest assembleDebug` |
| **Live** | Not released: no store listing or signing setup in the repo [inferred]; owner to confirm (O-2) |
| **Active plan** | None |
| **Formats** | Preferences DataStore `settings`, key `app_theme` = an `AppTheme` name; no version number (§6) |
| **Read first** | `AGENTS.md` (this project has no CLAUDE.md) |

## 0. Current state

| Item | State |
|---|---|
| Build and tests | 2026-11-09 [build]: **verify fails**: `assembleDebug` passes, but no unit test compiles (L-6), so 0 tests run. `assembleRelease` fails in R8 (L-5). `lintDebug` fails with 5 errors in `feature:hangman` (L-3, L-7) |
| Live | Not released [inferred] (O-2) |
| Pending release | None |
| Bugs that matter now | Hangman Computer mode crashes on Android 11 to 14 (L-3); verify is red (L-6); no release build possible (L-5) |
| Waiting on the owner | O-1 branch policy, O-2 live state, O-3 `@OptIn` uses, O-4 PRD rules still wanted |
| Latest Changelog entry | 2026-11-09: stats.md created |

## 1. Rules and hazards

### 1.1 Working rules

| Rule | Reason |
|---|---|
| No experimental features: no preview, alpha, beta or canary libraries, no experimental compiler flags, no `@OptIn` / `@Experimental` APIs | PRD "CRITICAL POLICY": stable releases only, and a violation "must be rejected at review" [doc]. Six `@OptIn` uses exist already (O-3) |
| Never change the app id `com.dicereligion.vagabond` | PRD §17 "do not change" [doc]; a new id is a different app to Android |
| No `INTERNET` permission, no runtime permissions, no personal data stored | PRD §7.4 and §15: the app is offline-first by design [doc] |
| Never invent a game rule; take it from the game's spec in `docs/Games/`, or ask | The specs are the source of truth for game behaviour (§12) |
| `AGENTS.md` is the project instruction file; do not add a CLAUDE.md | Gemini and Antigravity read `AGENTS.md` too (`AGENTS.md` first lines) |
| Work on `main` | Owner ruling R-1 (2026-11-08). PRD §9.1 still describes a `development` flow (O-1) |

### 1.2 Hazards

- **Nothing is live** as far as the repo shows [inferred] (O-2). If the app is on a phone, §1.3's theme-name rule
  protects it.
- **The verify command is red before any change** (L-6, as of 2026-11-09) [build]. A failing
  `testDebugUnitTest` is not proof that a change broke something; compare against the L-6 errors, and fix
  O-14 before relying on verify.
- **Renaming or removing an `AppTheme` value crashes the app at start** for anyone who saved that theme:
  `SettingsRepository.appTheme` reads it with `AppTheme.valueOf`, which throws on an unknown name.
- **Tracked build output:** 856 files under module `build/` folders are still tracked although `.gitignore` ignores
  `build/`. After any build they show as changed. Never stage them; the owner untracks them (O-6).
- **`make run` installs and launches on a device** (`Makefile`). `.claude/settings.json` makes `adb install`,
  `adb shell`, `android run` / `install` and `./gradlew *install*` ask first.
- **No release build can be made** (L-5) [build], and no signing config exists. Do not hand out a debug build as
  a release.

### 1.3 Invariants and guards

| Invariant | Guard |
|---|---|
| `AppTheme` constant names (`COSMIC_TEAL`, `RED_STAR`) never change once stored | Guard: none |
| Every `AppTheme` value is handled where themes are chosen (`Theme.kt`, `SettingsScreen.kt`) | Kotlin compiler: exhaustive `when` |
| The default theme is `COSMIC_TEAL` | `SettingsViewModelTest.initialAppThemeIsCosmicTeal` and `SettingsRepositoryTest`, but neither compiles (L-6): Guard: none in practice |
| The manifest declares no permissions | Guard: none |
| Library modules keep their public API in a release build | `assembleRelease` (fails now, L-5) |
| Hangman allows 7 wrong guesses; spaces show as `/`; non-alphanumerics are revealed from the start | Guard: none (no Hangman tests, O-7) |

### 1.4 Contracts

None: no other repo, server or public claim depends on this app.

## 2. Open work

IDs are stable and never reused (`O-` open, `R-` ruling, `L-` limitation). Closed items move to the Changelog.

| ID | Item | State | Who | Next step |
|---|---|---|---|---|
| O-1 | Branch policy: PRD §9.1 says `main` is protected and work goes through `development`; R-1 says work on `main` | Open | Owner | Say whether the PRD flow is retired or kept for later |
| O-2 | Live state: is Vagabond on Play or on a phone, and which version? | Open | Owner | Confirm; then set the *Live* row with an as-of date |
| O-3 | Six `@OptIn` uses break §1.1: `ExperimentalMaterial3Api` in 4 screens (for `TopAppBar`), `ExperimentalCoroutinesApi` in 2 tests | Open | Owner, then agent | Decide: accepted exception (§10.3) or remove |
| O-4 | PRD rules the code does not follow: strings in `strings.xml`, KDoc on every public symbol, Makefile targets, CI, an ADR folder | Open | Owner | Say which still hold (open items) and which are dropped (§10.2) |
| O-5 | Fix the stale instrumented test assertions (L-2) | Open | Agent | After O-14 and O-4; needs a device to run, so the owner runs them |
| O-6 | Untrack tracked `build/` output (same as Res `Docs/stats.md` O-6) | Open | Owner | `git ls-files -z '*/build/*' \| xargs -0 git rm --cached --quiet`, then commit |
| O-7 | No unit tests for Hangman rules (`HangmanGame`, `maskedWord`) or the word picker | Open | Agent | Add JVM tests in a new feature/hangman/src/test folder |
| O-8 | `docs/Saves/savefile1.md`: keep as a document, or move into the Changelog's older history? | Open | Owner | Decide |
| O-9 | Stray `core/common/utils/` (old copies of `Chaos.kt`, `RandomMech.kt`, in no module) | Open | Owner | Confirm it can be deleted |
| O-10 | Next game: `docs/Games/MinesweeperPRD.md` is written; no module exists | Open | Owner | Confirm Minesweeper is next and when |
| O-11 | PRD §20 open questions: final colour tokens with WCAG AA contrast; branded icon and splash; Settings control style | Open | Owner | Decide each [doc] |
| O-12 | Make the release build work (L-5) | Open | Owner, then agent | Choose: turn off `isMinifyEnabled` in the two library modules (the docs' usual advice), or add keep rules for their public API |
| O-13 | The theme setting has no bump rule; §6 describes current practice [inferred] | Open | Owner | Confirm the rule in §6 |
| O-14 | Make the tests compile again: 5 test files import `com.dicereligion.vagabond.ui.theme`, which moved to `core.designsystem` (L-6) | Open | Agent | Fix the imports; run verify; first priority, as verify is red |
| O-15 | Fix the Computer-mode crash on Android 11 to 14 (L-3) | Open | Agent | Replace the 3 `removeLast()` calls with `removeAt(lastIndex)`; `lintDebug` must pass |
| O-16 | Two lint errors: `getBackStackEntry` called during composition without `remember` (L-7) | Open | Agent | Wrap the call in `remember(navController.currentBackStackEntry)`, per the lint message; rerun lint |

**Parked (don't propose these):** None yet.

## 3. What it is

- **What and for whom:** a lightweight offline games container for 1–2 players: commuters, travellers in no-signal
  places, and pairs of friends with a few spare minutes (PRD §1.1, §1.5) [doc].
- **How a person uses it:** open the app on Landing; tap Collections to pick a game (Hangman is the one playable
  game) or Settings to switch the theme between Cosmic Teal and Red Star. The choice is kept across restarts.
- **Hangman:** two players (one types a phrase, the other guesses) or one player against "Computer", which picks a
  movie or TV show title.
- **Scope:** shell plus mini-game modules, English only, portrait and landscape phones.
- **Non-goals (PRD v1):** network play, sign-in, analytics, payments, background work, tablet-specific layouts
  (PRD §1.3, §2.3) [doc].

## 4. How it works

**Governing principle:** an offline, single-activity shell that hosts each game as its own feature module, with one
shared design system so every game looks the same. Games plug in as a nested navigation graph and a Collections
card; the shell owns theme and navigation.

**Architecture**

| Module | Role | Depends on |
|---|---|---|
| `app` | `MainActivity` (nav host), `VagabondApplication` (Hilt), Landing / Collections / Settings, theme storage | `feature:hangman`, `core:designsystem` |
| `core:designsystem` | `AppTheme`, both colour schemes, `VagabondTheme`, pixel font, `pixelBrute` modifier | — |
| `core:utils` | `RandomMech` seeded random generator, `Chaos.mech` app-wide instance | — |
| `feature:hangman` | Hangman rules, word lists, screens and `HangmanViewModel` | `core:utils`, `core:designsystem` |

**Flow**

- `MainActivity` collects `SettingsRepository.appTheme` and wraps the `NavHost` in `VagabondTheme`, so a theme
  change recolours every screen at once.
- Routes: `landing` (start), `collections`, `settings`, and the nested graph `hangman` with `hangman_start`,
  `hangman_input`, `hangman_game`.
- The Hangman screens share one `HangmanViewModel`, scoped to the `hangman` graph's back-stack entry; game state
  lives only as long as that graph.
- Settings: `SettingsScreen` → `SettingsViewModel.saveAppTheme` → `SettingsRepository` → DataStore.
- DI: Hilt. `DataStoreModule` provides the DataStore; `SettingsRepository` and `HangmanGame` are singletons.

**Platform pieces** (`app/src/main/AndroidManifest.xml`): one exported launcher activity; no permissions,
services or receivers; `allowBackup="true"` with the template backup rules (§6). The library modules have no
manifest.

## 5. Where things live

Source roots: `app/.../vagabond/` is `app/src/main/java/com/dicereligion/vagabond/`; the other modules follow the
same pattern under their package (`.../core/designsystem/`, `.../core/utils/`, `.../feature/hangman/`).

| Path | Holds |
|---|---|
| `app/.../vagabond/MainActivity.kt` | The navigation graph and theme wrapper |
| `app/.../data/SettingsRepository.kt` | Theme storage (DataStore) |
| `app/.../di/DataStoreModule.kt` | Hilt provider for the DataStore |
| `app/.../ui/screens/` | `LandingScreen`, `CollectionsScreen`, `SettingsScreen`, `SettingsViewModel` |
| `core/designsystem/.../designsystem/` | `Color.kt` (`AppTheme`, palettes), `Theme.kt`, `Type.kt`, `PixelBruteStyle.kt` |
| `core/designsystem/src/main/res/font/press_start_2p_regular.ttf` | The pixel font |
| `core/utils/.../utils/` | `RandomMech.kt`, `Chaos.kt` |
| `feature/hangman/.../domain/` | `HangmanGame.kt` (rules, `HangmanGameState`), `HangmanData.kt` (word lists, pickers) |
| `feature/hangman/.../ui/` | Start, Input and Game screens, end and instructions dialogs, `HangmanVisual`, `HangmanViewModel` |
| `core/common/utils/` | Stray old copies, in no module: leave alone (O-9) |
| `*/build/` | Build output, partly tracked by git (O-6): never edit or stage |

**Where do I look for…**

| I want to… | Open | Test to run |
|---|---|---|
| Add a game to Collections | `CollectionsScreen.kt` (`gameList`), a nested graph in `MainActivity.kt`, a new `feature/` module in `settings.gradle.kts` | Verify command; `CollectionsScreenTest` on a device (owner) |
| Change a palette or add a theme | `Color.kt`, then `Theme.kt` and the toggle in `SettingsScreen.kt` (§1.3: never rename a stored name) | `SettingsViewModelTest` |
| Change Hangman rules or masking | `HangmanGame.kt` | None yet (O-7) |
| Add or remove Computer-mode titles | `HangmanData.kt` (`movie_list`, `tv_show_list`) | None yet (O-7) |
| Change the gallows picture | `HangmanVisual.kt` (`hangmanStages`); `docs/Games/HangmanDrawing.md` | None |
| Change the button and card look | `PixelBruteStyle.kt` (`pixelBrute`) | Verify command |

## 6. Data, formats and limits

**Stored data:** one Preferences DataStore named `settings` (`SettingsRepository.kt`), one key `app_theme`
holding the `AppTheme` constant name; missing means `COSMIC_TEAL`. Nothing else is stored: Hangman games are in
memory only (saving a game is out of scope, HangmanPRD §8) [doc].

**Format version and bump rule:** there is no version number and no migration. Current practice [inferred]: never
rename or remove an `AppTheme` constant; a new theme only adds a constant. If one must go, map the old name to a
default before `valueOf` instead of letting it throw. Owner to confirm (O-13).

**Backup:** `allowBackup="true"` and both rules files (`app/src/main/res/xml/backup_rules.xml`,
`data_extraction_rules.xml`) are the unedited template, so Android's default backup applies [inferred].

**Limits and fixed numbers**

| Value | Where |
|---|---|
| 7 wrong guesses per game | `HangmanGameState.maxIncorrectGuesses` |
| Custom phrase up to 120 characters | `HangmanInputScreen.kt` |
| Masked word wraps at 26 characters per line | `formatMaskedWordIntoLines` in `HangmanGameScreen.kt` |
| Computer word list: 1,000 movies and 1,000 TV shows (as of 2026-11-08; recount: §13) | `HangmanData.kt` |
| Collections: 1 game card and 7 disabled placeholders | `CollectionsScreen.kt` (`gameList`) |

## 7. Features

### 7.1 Shell screens

- **Landing:** title band, Collections and Settings buttons, and the installed version name at the bottom (read
  from the package manager, "N/A" on failure).
- **Collections:** a list of cards; Hangman opens the `hangman` graph, the "Title in the Works" placeholders are
  disabled. The back arrow calls `popBackStack()`.
- **Settings:** one row showing the current theme name and a swap button that toggles Cosmic Teal and Red Star;
  the back arrow pops to `landing`.
- Files: `app/.../ui/screens/`.

### 7.2 Theme and PixelBrute design system

- Two dark colour schemes (`CosmicTealDarkColorScheme`, `RedStarDarkColorScheme` in `Color.kt`); the system
  dark/light setting and `dynamicColor` do not affect the colours (`VagabondTheme` ignores them for the scheme).
- `VagabondTheme` also sets the status bar colour to the scheme's primary colour.
- `Modifier.pixelBrute` gives the hard offset shadow and thick border; when pressed, the shadow collapses and the
  content shifts into its place. It handles its own click, so screens pass `onClick` to it as well as to `Button`.
- All text uses Press Start 2P (`PixelFontFamily` in `Type.kt`). Design rules: `docs/Theme.md`.

### 7.3 Hangman

- **Flow:** Start → Input (type a phrase and Play, or Computer) → Game → end dialog (Play Again returns to Input,
  Back to Menu returns to Start). Spec: `docs/Games/HangmanPRD.md` [doc].
- **Masking** (`HangmanGameState.maskedWord`): letters and digits are hidden until guessed, a space shows as `/`,
  every other character is shown from the start.
- **Rules** (`HangmanGame`): a repeated or post-game guess changes nothing; a wrong guess adds one to the count;
  win when every letter and digit is guessed, lose at 7 wrong. The secret word is uppercased; the on-screen
  keyboard (digits and QWERTY) only sends uppercase.
- **Computer mode:** `ReturnMovieOrShow()` fills a "shuffle bag" with both lists, shuffles it with
  `Chaos.mech.shuffleList`, and takes one title at a time, so no title repeats until all 2,000 have been used.
  The bag lasts only while the app process lives.
- **Gallows:** text drawing, 7 stages (`HangmanVisual.hangmanStages`) in a 150 dp box (L-1).
- Files: `feature/hangman/`.

### 7.4 Random numbers

- `RandomMech` is a seeded 32-bit generator with range, percentage, dice and shuffle helpers; `Chaos.mech` is the
  one app-wide instance, seeded from the clock and `kotlin.random.Random` on first use. Games use it instead of
  `kotlin.random` directly. `docs/Saves/savefile1.md` names the algorithm Xoshiro128++ [doc].

## 8. Build, release and deploy

- **Toolchain:** Gradle 8.13 wrapper, AGP 8.13.0, Kotlin 2.0.21 with the Compose compiler plugin, kapt for Hilt.
  JVM target 11 (`core:utils`: 17). The PRD asks for JDK 17 [doc]; Gradle runs on Android Studio's JetBrains
  Runtime 21.0.10, as no JDK is set (`./gradlew --version`, 2026-11-09) [build].
- **Verify:** `./gradlew testDebugUnitTest assembleDebug` (wrap it in `~/.claude/hooks/gradle-quiet.sh`). Red
  as of 2026-11-09 (L-6) [build].
- **Lint:** `./gradlew lintDebug` stops at the first failing module; `:app:lintDebug` alone passes with warnings
  only (2026-11-09) [build].
- **Other commands:** `make build` = `./gradlew assembleDebug`; `make run` builds, installs and launches on a device
  (owner only). The PRD's other Makefile targets (`test`, `lint`, `verify` …) do not exist (O-4).
- **Release:** not set up. `isMinifyEnabled = true` in `app`, `core:designsystem` and `feature:hangman`; no
  `signingConfigs`; release keystore location: none (PRD §17 says it must stay out of the repo [doc]).
  `app/proguard-rules.pro` exists; the two library modules name proguard-rules.pro and consumer-rules.pro
  files that do not exist. `assembleRelease` fails (L-5) [build].
- **CI:** none (no `.github/` folder); PRD §12 describes one [doc] (O-4).

## 9. Testing

| Suite | Where | Guards | Runs in verify |
|---|---|---|---|
| `SettingsViewModelTest` (MockK) | `app/src/test/` | Default theme; saving a theme reaches the repository and the state | Yes |
| `ExampleUnitTest` | `app/src/test/` | Template only | Yes |
| `SettingsRepositoryTest` | `app/src/androidTest/` | DataStore default and save | No: needs a device |
| `LandingScreenTest`, `CollectionsScreenTest`, `SettingsScreenTest` | `app/src/androidTest/.../ui/screens/` | Screen content and navigation calls | No: needs a device |

- **Run:** unit tests with the verify command; instrumented tests with `./gradlew connectedDebugAndroidTest` on a
  device or emulator (owner only).
- **None of the tests compile** (L-6) [build]: as of 2026-11-09, 0 unit tests run, and
  `:app:compileDebugAndroidTestKotlin` fails with 20 errors.
- **Not covered:** Hangman (rules, masking, word picker, screens), `RandomMech`, the design system (O-7).
- **Stale:** two `CollectionsScreenTest` tests no longer match the screen (L-2).
- **Rule:** a new test must fail without the fix it guards.
- Test count: 0 run as of 2026-11-09 [build]; 3 unit and 13 instrumented test methods exist (recount: §13).

## 10. Decisions

### 10.1 Owner rulings

| ID | Date | Ruling | Reason |
|---|---|---|---|
| R-1 | 2026-11-08 | Work happens on `main` | Owner's choice when creating this doc; `main` was checked out and ahead of `development` [owner] |
| R-2 | 2026-11-08 | Docs stay in lowercase `docs/`, including `stats.md` and `Changelog.md` | Git already tracks `docs/` [owner] |
| R-3 | PRD | No experimental features (§1.1) | Stable releases only (PRD, top) [doc] |
| R-4 | PRD §17 | App id `com.dicereligion.vagabond` never changes | [doc] |

### 10.2 Deviations from the spec

| Spec says | Code does |
|---|---|
| PRD §1.3, §2.1: no playable games in v1; Collections shows exactly 8 "Coming Soon" tiles in a grid | Hangman is playable; a list of 1 game and 7 "Title in the Works" placeholders |
| PRD §8.1: landing, collections and settings as feature modules | They live in `app` |
| PRD §7.5, §16: all UI text in `strings.xml` | Nearly all text is in Kotlin (O-4) |
| PRD §8.3: KDoc on every public symbol | Mostly missing (O-4) |
| PRD top: no `@OptIn` | Six uses (O-3) |
| PRD header: targetSdk 35 | 36 |
| PRD §9.1: work on `development` via feature branches | Work on `main` (R-1, O-1) |
| PRD: deviations recorded as ADRs in a docs/adrs folder | No ADR folder; this table instead (O-4) |
| HangmanPRD §6: about 100 titles in res/values/arrays.xml | 2,000 titles in `HangmanData.kt` |
| HangmanPRD §7: 7 vector drawables for the gallows | Text drawing in `HangmanVisual`; the 7 drawables are unused (L-4) |

### 10.3 Accepted, don't chase

None yet.

## 11. Limitations, traps and diagnoses

### 11.1 Known limitations

| ID | Limitation |
|---|---|
| L-1 | The gallows has 7 pictures for 8 counts (0–7 wrong), so the 6th and 7th wrong guesses show the same picture before the lose dialog [inferred] (`coerceIn` in `HangmanVisual`) |
| L-2 | `CollectionsScreenTest`: `displaysEightComingSoonTiles` expects 8 "Coming Soon" texts and `backArrowPopsBackStack` expects `popBackStack("landing", false)`; the screen has neither [inferred], not run (O-5) |
| L-3 | Computer mode crashes on Android 11 to 14: the 3 `removeLast()` calls in `HangmanData.kt` compile to Java's `List.removeLast` (API 35) under compileSdk 36. Lint `NewApi` error [build]; cause and `NoSuchMethodError` per Android 15 behaviour changes [doc]; not seen on a device (O-15) |
| L-4 | `feature/hangman/src/main/res/drawable/hangman_state_0.xml` to `hangman_state_6.xml` are unused |
| L-5 | `assembleRelease` fails in `:app:minifyReleaseWithR8`: "Missing class" for `AppTheme`, `ThemeKt`, `PixelBruteStyleKt` [build]. Both library modules shrink their own release build with no keep rules, which removes their public API (Android "Optimization for library authors") [doc] (O-12) |
| L-6 | No test compiles: 5 test files still import `com.dicereligion.vagabond.ui.theme.AppTheme` / `VagabondTheme`, moved to `core.designsystem` on 2025-11-03 (git log) [build] (O-14) |
| L-7 | Lint errors in `HangmanInputScreen.kt` and `HangmanGameScreen.kt`: `getBackStackEntry` during composition without `remember` [build] (O-16) |

### 11.2 Traps

| Trap | Fix |
|---|---|
| `core:utils` uses compileSdk 35 and JVM 17; the other modules use 36 and 11 | Check `core/utils/build.gradle.kts` before assuming the modules match |
| `app` has four identical `composeOptions` blocks and `core:designsystem` three. With Kotlin 2.0 the Compose compiler comes from the `kotlin.compose` plugin (Android "Set up the Compose Compiler Gradle plugin") [doc]; that the old setting is ignored is [inferred], as no doc states it | Don't edit `kotlinCompilerExtensionVersion` expecting an effect |
| Screens pass the click to both `Button.onClick` and `pixelBrute(onClick = …)` | Change both, or the press effect and the action disagree |
| `core/common/utils/` looks like source but is in no module | Edit `core/utils/` instead |
| `./gradlew lintDebug` aborts at the first module with errors, hiding later modules | Lint a module alone (`:app:lintDebug`) or add `--continue` |

### 11.3 Diagnoses

None yet.

## 12. Documents

| Doc | Status | Read it before touching | Trust it for |
|---|---|---|---|
| `AGENTS.md` | Current | Anything | Verify command, hard rules |
| `docs/PRD.md` | Partly stale (§10.2) | The shell, theming, privacy | Product intent, privacy rules, app id; not for game scope, Makefile or CI |
| `docs/Theme.md` | Current | Colours, components | PixelBrute design rules and palettes |
| `docs/Games/HangmanPRD.md` | Draft 2025-10-31, mostly built | Hangman | Game rules (§1.1: the source of truth); not for assets or word list |
| `docs/Games/HangmanDrawing.md` | Current | `HangmanVisual` | How the text gallows must render |
| `docs/Games/MinesweeperPRD.md` | Spec, not built | A Minesweeper module | Its rules and layout (O-10) |
| `docs/Saves/savefile1.md` | Historical snapshot at 4.1 | — | Background on the random generator and word wrap (O-8) |
| `docs/AndroidCLI.md` | Stale | — | Little; the `android-cli` skill is current |
| `GeminiPlan.md` | Historical | — | How the shell was first planned |
| `README.md` | Title only | — | — |
| `.claude/skills/` | Current | Gradle or CLI work | Project skills: `agp-9-upgrade`, `android-cli`, `testing-setup` |

## 13. How this doc is verified

**Evidence tags:** untagged claims were read from the code on the *Verified* date. `[build]` seen in a build or
test run · `[device]` seen on a device or emulator · `[console]` seen in an outside console · `[doc]` reported by
another doc, not re-checked · `[inferred]` worked out from the code, not run · `[owner]` reported by the owner.

**How facts were checked (2026-11-08 to 2026-11-09):** identity, stack and SDK levels from the Gradle files and
`gradle/libs.versions.toml`; architecture from `settings.gradle.kts`, the manifest and the source; tests by
listing the test source folders; documents by reading each. Builds run on 2026-11-09: verify,
`assembleDebug`, `assembleRelease`, `lintDebug`, `:app:lintDebug`, `:app:compileDebugAndroidTestKotlin`.
Android behaviour from the official docs via `android docs fetch`. Counts recounted with the commands below.

**Re-verify commands** (from the repo root):

```sh
# Verify (the one command that must be green)
~/.claude/hooks/gradle-quiet.sh ./gradlew testDebugUnitTest assembleDebug
# Release build, lint, instrumented-test compile (no device needed)
~/.claude/hooks/gradle-quiet.sh ./gradlew assembleRelease
~/.claude/hooks/gradle-quiet.sh ./gradlew lintDebug --continue
~/.claude/hooks/gradle-quiet.sh ./gradlew :app:compileDebugAndroidTestKotlin
# Test methods in the sources (unit and instrumented)
grep -rc '@Test' app/src/test app/src/androidTest | grep -v ':0$'
# Unit test count after a build: sum of tests= over every TEST-*.xml, no name filter
find . -path '*/build/test-results/*' -name 'TEST-*.xml' -exec grep -ho 'tests="[0-9]*"' {} + \
  | tr -dc '0-9\n' | paste -sd+ - | bc
# @OptIn uses (O-3)
grep -rn '@OptIn' --include='*.kt' app core feature | grep -v /build/
# Computer-mode titles, both lists together
grep -c '^    "' feature/hangman/src/main/java/com/dicereligion/vagabond/feature/hangman/domain/HangmanData.kt
# Tracked build files (O-6)
git ls-files | grep '/build/' | grep -vc '^\.claude/'
grep -c 'uses-permission' app/src/main/AndroidManifest.xml   # permissions: expect 0
```

**Cannot be checked from a session:** what is installed on any phone or on Play (O-2); instrumented and UI tests
(need a device); how the screens look.

Not repo paths: none.

## 14. Glossary

| Term | Meaning |
|---|---|
| Collections | The game-list screen (route `collections`) |
| PixelBrute | The retro design system: pixel font, thick borders, hard offset shadows (`docs/Theme.md`, `pixelBrute`) |
| Cosmic Teal, Red Star | The two app themes (`AppTheme.COSMIC_TEAL`, `RED_STAR`) |
| Chaos, RandomMech | The app's random generator: `Chaos.mech` is the shared `RandomMech` instance |
| Shuffle bag | The no-repeat picker for Computer-mode titles in `HangmanData.kt` |
| Computer mode | One-player Hangman with a random movie or TV title |
| Masked word | The secret phrase as shown: `_` for unguessed letters and digits, `/` for spaces |
| Save file | `docs/Saves/savefile1.md`, a snapshot of the project, not app save data |
