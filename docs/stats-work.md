# stats.md work file: Vagabond, Create, started 2026-11-08

| Step | Status | Date | Notes |
|---|---|---|---|
| C1 Gather facts | [x] | 2026-11-08 | Read only. Questions for the owner at the end |
| C2 Draft | [x] | 2026-11-08 | 374 lines; owner questions became §2 O-1 to O-13. Preview check.sh: only FAIL is the missing Changelog (C4) |
| C3 Verify | [x] | 2026-11-09 | Verify is red (tests don't compile); release and lint fail; 3 new limitations, 3 new open items |
| C4 Changelog | [ ] | | |
| C5 Pointers | [ ] | | |
| C6 Checks | [ ] | | |

Kinds (B6): **Game whose rules come from a spec** (Hangman from `docs/Games/HangmanPRD.md`; Minesweeper spec
written, not built). Not a published app and no store presence found in the repo (*Live* to confirm with the owner).

## Owner decisions for this job

- 2026-11-08 `[owner]`: build on **`main`** (checked out; 2 commits ahead of `development`). The doc's Branch row
  says `main`; AGENTS.md and WorkMap §3 change to match in C5.
- 2026-11-08 `[owner]`: keep lowercase **`docs/`**: `docs/stats.md`, `docs/Changelog.md`, this file.

## Facts by section (source in brackets)

### Header

- **Identity:** `applicationId` and `namespace` `com.dicereligion.vagabond`, `versionName` "4.1", `versionCode` 1
  (`app/build.gradle.kts:11,15,18,19`). PRD §17: app id "do not change".
- **Stack:** Kotlin 2.0.21, AGP 8.13.0, Gradle wrapper 8.13, Compose BOM 2024.09.00, Material3 1.2.1 (pinned
  outside the BOM), Hilt 2.51.1 via kapt, Navigation Compose 2.7.7, DataStore Preferences 1.1.1
  (`gradle/libs.versions.toml`, `gradle/wrapper/gradle-wrapper.properties`). minSdk 30, target/compileSdk 36 in
  `app`, `core:designsystem`, `feature:hangman`; **`core:utils` compileSdk 35** (`core/utils/build.gradle.kts:7`).
  JVM target 11, except `core:utils` 17.
- **Branch · verify:** `main` (owner decision above) · `./gradlew testDebugUnitTest assembleDebug` (AGENTS.md;
  WorkMap §3 says the same command, branch `development`).
- **Live:** nothing in the repo shows a release (no signing config, no store docs). *Ask the owner.*
- **Active plan:** none. `GeminiPlan.md` is an old bootstrap plan; no `*PDP*` / `CR*` docs.
- **Formats:** one Preferences DataStore file `settings`, key `app_theme` (string = `AppTheme` enum name)
  (`app/.../data/SettingsRepository.kt:29,45`).
- **Read first:** `AGENTS.md` (no CLAUDE.md; `.claude/rules/` does not exist).

### §1 Rules and hazards

- **No experimental features** (PRD top "CRITICAL POLICY", AGENTS.md): no preview/alpha/beta libraries, no
  experimental compiler flags, no `@OptIn`/`@Experimental` APIs. Reason in the PRD: stable-only, "must be rejected
  at review and blocked by CI".
  - **The code already breaks it:** `@OptIn(ExperimentalMaterial3Api::class)` in `CollectionsScreen.kt:92`,
    `SettingsScreen.kt:40`, `HangmanInputScreen.kt:44`, `HangmanGameScreen.kt:47` (for `TopAppBar` in Material3
    1.2.1), and `@OptIn(ExperimentalCoroutinesApi::class)` in both coroutine tests. *Ask the owner* (see Q4).
- **App id never changes** (PRD §17).
- **No INTERNET permission, no runtime permissions, no PII** (PRD §7.4, §15). Manifest declares no permissions
  (`app/src/main/AndroidManifest.xml`; library modules have no manifest). Guard: none (no test or lint check).
- **Strings in `strings.xml`, no hard-coded UI text** (PRD §7.5, §16). The code hard-codes nearly all UI text
  (`LandingScreen.kt:60,98,115`, all Hangman screens); only `app_name` is a resource. Deviation for §10.2.
- **KDoc on every public symbol** (PRD §8.3). Not followed in most files. Deviation for §10.2.
- **Hazards:** WorkMap §4 lists none for Vagabond. Nothing live found. `make run` installs and launches on a device
  (`Makefile`); `.claude/settings.json` makes `adb install/shell`, `android run/install` and `./gradlew *install*`
  ask first.
- **Invariants found in the code:**
  - Stored theme must stay a valid `AppTheme` name: `AppTheme.valueOf(themeName)` throws on an unknown value
    (`SettingsRepository.kt:57`), so renaming or removing `COSMIC_TEAL` / `RED_STAR` would crash existing installs
    at start. Guard: none `[inferred]`.
  - Every `AppTheme` is handled in `when` in `Theme.kt:22` and `SettingsScreen.kt:100,112` (compile-time
    exhaustive; the toggle only cycles two values).
- **Git branches policy in PRD §9.1:** `main` protected/release-ready, `development` integration branch, feature
  branches `feature/<area>-<short-desc>`. Conflicts with the owner's decision to work on `main`; see Q1.
- **Tracked build output:** 856 files under module `build/` folders are tracked in git although `.gitignore` now
  ignores `build/` (`git ls-files | grep /build/`). A build makes them show as changed. Owner action open in
  Res `Docs/stats.md` O-6 `[doc]`.

### §2 Open work (candidates; none are tracked anywhere yet)

- PRD §20 open questions: final colour tokens with WCAG AA; branded icon and splash; Settings control style.
- Minesweeper: full PRD (`docs/Games/MinesweeperPRD.md`), no module.
- Collections: 7 disabled "Title in the Works" placeholders (`CollectionsScreen.kt:48-89`).
- Stale instrumented tests (see §9). Unused Hangman drawables (see §11). Tracked build output (Res O-6).
- No ID scheme exists yet: new `O-`, `R-`, `L-` IDs from 1.

### §3 What it is

- Offline "traveling games" container for 1–2 players: commuters, travellers, pairs of friends (PRD §1.1, §1.5).
- Shell: Landing → Collections (game list) and Settings (theme toggle) (PRD §2.1, §3).
- PRD v1 non-goals: no network, sign-in, analytics, payments; no tablet-specific layouts (PRD §1.3, §2.3).
- PRD v1 said "no playable games"; the code has moved past it: Hangman is playable (deviation, §10.2).

### §4 How it works

- Single activity `MainActivity` (`@AndroidEntryPoint`), `VagabondApplication` (`@HiltAndroidApp`).
- `MainActivity` collects `SettingsRepository.appTheme`, wraps a `NavHost` in `VagabondTheme`. Routes: `landing`
  (start), `collections`, `settings`, nested graph `hangman` → `hangman_start`, `hangman_input`, `hangman_game`
  (`MainActivity.kt:40-60`).
- Hangman screens share one `HangmanViewModel` scoped to the `hangman` graph back-stack entry
  (`HangmanInputScreen.kt:47`, `HangmanGameScreen.kt:50`).
- DI: `DataStoreModule` provides the `DataStore<Preferences>`; `SettingsRepository` and `HangmanGame` are
  `@Singleton`.
- Manifest: one launcher activity, `allowBackup="true"` with the template (empty) backup rules; no permissions,
  services or receivers.
- Module graph: `app` → `feature:hangman`, `core:designsystem`; `feature:hangman` → `core:utils`,
  `core:designsystem` (build files). PRD §8.1 planned `feature/landing`, `feature/collections`,
  `feature/settings`; they live in `app` instead (deviation).

### §5 Where things live

- `app/src/main/java/com/dicereligion/vagabond/`: `MainActivity.kt` (nav graph), `data/SettingsRepository.kt`,
  `di/DataStoreModule.kt`, `ui/screens/` (Landing, Collections, Settings + `SettingsViewModel`).
- `core/designsystem/.../core/designsystem/`: `Color.kt` (`AppTheme` enum + both palettes), `Theme.kt`
  (`VagabondTheme`), `Type.kt` (`PixelFontFamily`, Press Start 2P font in `res/font/`), `PixelBruteStyle.kt`
  (`Modifier.pixelBrute`, `PixelBruteContainer`).
- `core/utils/.../core/utils/`: `RandomMech.kt` (seeded PRNG; savefile1 calls it Xoshiro128++ `[doc]`), `Chaos.kt`
  (app-wide `Chaos.mech` singleton).
- `feature/hangman/.../feature/hangman/`: `domain/HangmanGame.kt` (rules, `HangmanGameState`),
  `domain/HangmanData.kt` (word lists + shuffle-bag pickers), `ui/` (4 screens' worth of composables + dialogs +
  `HangmanViewModel` + `HangmanVisual`).
- **Stray folder:** `core/common/utils/` holds older copies of `Chaos.kt` and `RandomMech.kt` (package
  `com.yourcompany.yourgame.utils`), not in any Gradle module (`settings.gradle.kts` includes only 4 modules).
- Tasks: add a game to Collections → `CollectionsScreen.kt` `gameList` + a route in `MainActivity.kt`; change a
  palette → `Color.kt`; change Hangman rules → `HangmanGame.kt` (no unit test exists); add words →
  `HangmanData.kt`.

### §6 Data, formats and limits

- DataStore `settings`, key `app_theme`; default `COSMIC_TEAL`. No migrations, no version number.
- No other storage. Hangman state is in memory only (PRD Hangman §8 out of scope: saving a game).
- Limits: custom phrase max 120 characters (`HangmanInputScreen.kt:76,86`); 7 wrong guesses
  (`HangmanGame.kt:13`); masked-word line wrap at 26 characters (`HangmanGameScreen.kt:236`); word lists 1,000
  movies + 1,000 shows (recount: see C3).
- Backup: `allowBackup="true"`, rules files are the unedited template (`res/xml/backup_rules.xml`,
  `data_extraction_rules.xml` with its TODO), so the DataStore file is included in Auto Backup by default
  `[inferred]`.

### §7 Features

- **Shell:** Landing (title, Collections, Settings, version name at the bottom), Collections (Hangman card + 7
  disabled placeholders), Settings (one row toggling Cosmic Teal ↔ Red Star, saved to DataStore).
- **Theme / PixelBrute:** two dark colour schemes only; `darkTheme` and `dynamicColor` params of `VagabondTheme`
  unused for colour choice (`Theme.kt:17-25`); status bar colour set via deprecated `window.statusBarColor`.
- **Hangman:** Start → Input (type a phrase for 2-player, or "Computer") → Game (ASCII gallows, guesses left,
  masked word, on-screen keyboard 0-9 + QWERTY) → End dialog (Play Again / Back to Menu).
  - Masking: letters/digits hidden, spaces shown as `/`, everything else revealed (`HangmanGameState.maskedWord`).
  - Computer word: `ReturnMovieOrShow()` shuffle bag over both lists, no repeat until the bag empties; bag is
    process-lifetime only (`HangmanData.kt:2038-2109`).
  - `HangmanGame.guessLetter` does not uppercase the letter; the keyboard only sends uppercase, so it works
    `[inferred]`.

### §8 Build, release and deploy

- Verify: `./gradlew testDebugUnitTest assembleDebug`. `make build` = `assembleDebug`; `make run` = build + adb
  install + launch (device; owner only).
- Release: `isMinifyEnabled = true` in all three modules; no `signingConfigs` anywhere; `app/proguard-rules.pro`
  exists, but `core/designsystem` and `feature/hangman` reference `proguard-rules.pro` and `consumer-rules.pro`
  files that do not exist. Effect on `assembleRelease`: *Not checked* (C3 can run it; no device needed).
- Duplicate `composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }` blocks (4 in `app`, 3 in
  designsystem). With Kotlin 2.0 the compose compiler comes from the `kotlin.compose` plugin, so these are
  probably ignored: *Not checked*, confirm in C3 from the build log or official docs.
- PRD §9.2 says the Makefile has `init … verify … clean`; the real Makefile has only `build` and `run`.
  PRD §12 CI: no `.github/` folder exists.
- Toolchain: PRD says JDK 17. Local JDK: *Not checked* (C3).

### §9 Testing

- Unit (`app/src/test`): `SettingsViewModelTest` (2 tests: initial theme, save updates repo + state; MockK),
  `ExampleUnitTest` (template). No unit tests in `feature:hangman`, `core:*`.
- Instrumented (`app/src/androidTest`, need a device; not part of verify): `SettingsRepositoryTest` (2),
  `LandingScreenTest` (3), `CollectionsScreenTest` (3), `SettingsScreenTest` (4), `ExampleInstrumentedTest` (1).
- Likely stale `[inferred]`, not run: `collectionsScreen_displaysEightComingSoonTiles` looks for 8 "Coming Soon"
  texts but the screen now shows "Hangman" + 7 "Title in the Works"; `collectionsScreen_backArrowPopsBackStack`
  verifies `popBackStack("landing", false)` but the screen calls `popBackStack()`.
- Test count after a build: sum `tests=` over `build/test-results/**/TEST-*.xml` (C3).

### §10 Decisions

- 10.1 Owner rulings: none recorded in any doc with a date. The two from this job (branch, `docs/`) may go here.
- 10.2 Deviations from the specs (PRD, HangmanPRD): Hangman exists in v1; Collections content; screens in `app`
  not feature modules; hard-coded strings; missing KDoc; `@OptIn` use; targetSdk 36 vs PRD 35; JVM 11 vs PRD's
  JDK 17 (toolchain vs target, to confirm); word list in Kotlin not `res/values/arrays.xml`, ~2,000 not ~100;
  gallows is ASCII text, not the 7 vector drawables; End "Play Again" goes to Input (matches HangmanPRD); no ADRs
  folder although the PRD says deviations go to `/docs/adrs/`.
- 10.3 Accepted, don't chase: none known; *ask the owner*.

### §11 Limitations, traps

- Hangman visual has 7 stages (index 0-6) but a game allows 7 wrong guesses (0-7), so the 6th and 7th wrong
  guesses show the same picture before the lose dialog `[inferred]` (`HangmanVisual.kt:88` uses `coerceIn`).
- `hangman_state_0.xml` … `hangman_state_6.xml` drawables are unused (no reference in code).
- `MutableList.removeLast()` in `HangmanData.kt` (3 calls) with compileSdk 36 and minSdk 30: possible
  `NoSuchMethodError` on Android 14 and lower (Java 21 `List.removeLast` shadows the Kotlin extension).
  *Not checked*: confirm in C3 with lint and official docs before writing it.
- `core:utils` on compileSdk 35 / JVM 17 while the rest are 36 / 11.
- `CollectionsScreen` back arrow uses `popBackStack()`; Settings uses `popBackStack("landing", false)`.

### §12 Documents

| Doc | Lines (2026-11-08, `wc -l`) | State |
|---|---|---|
| `docs/PRD.md` | 628 | Shell spec v1; partly outdated (no games, 8 "Coming Soon" tiles, Makefile, CI, branch policy) |
| `docs/Theme.md` | 294 | PixelBrute style guide; source for palettes |
| `docs/Games/HangmanPRD.md` | 143 | Draft 2025-10-31; mostly built, deviations above |
| `docs/Games/HangmanDrawing.md` | 98 | ASCII-art instructions used by `HangmanVisual` |
| `docs/Games/MinesweeperPRD.md` | 527 | Spec for an unbuilt game |
| `docs/Saves/savefile1.md` | 166 | Project snapshot at v4.1 (history; becomes Changelog "Older history"?) |
| `docs/AndroidCLI.md` | 859 | Older CLI guide; `android-cli` skill is current (AGENTS.md) |
| `GeminiPlan.md` | 127 | Earlier implementation plan (historical) |
| `README.md` | 1 | Title only |
| `.claude/skills/` | — | Project skills: `agp-9-upgrade`, `android-cli`, `testing-setup` |

### §14 Glossary candidates

PixelBrute (design system / `pixelBrute` modifier), Cosmic Teal / Red Star (the two `AppTheme`s), Collections
(the game list screen), Chaos / RandomMech (the app PRNG), shuffle bag (no-repeat word picker), Computer mode
(1-player Hangman with a random title), masked word.

## Gaps and questions for the owner (C1)

1. **Branch policy:** PRD §9.1 says `main` is protected and work happens on `development` via feature branches.
   With work now on `main`, is that policy retired (doc says "work on `main`"), or kept for later?
2. **Live:** is Vagabond on Play or on anyone's phone (yours?), and which version? If not: "Not released".
3. **Active plan / next game:** is Minesweeper the next game, or is anything else in progress?
4. **`@OptIn(ExperimentalMaterial3Api)`:** six uses break the no-experimental rule. Record as an accepted
   exception (§10.3), or an open item to remove (§2)?
5. **Stale PRD claims** (8 "Coming Soon" tiles, strings in resources, KDoc, Makefile targets, CI, ADRs): still
   wanted (open items) or dropped (deviations accepted)?
6. **`docs/Saves/savefile1.md`:** keep as a document, or treat as history for the Changelog's "Older history"?
7. **Stray `core/common/utils/`:** known leftover? (Only recorded; no code change in this job.)

## C2 notes

- Owner said "go ahead" without answering the C1 questions, so each question is a §2 item (O-1 to O-4, O-8 to
  O-10, O-13) and the matching facts are tagged `[inferred]`.
- Facts still to confirm in C3: L-3 (`removeLast`), the `composeOptions` trap (§11.2), release build (L-5), local
  JDK (§8), test count.
- check.sh preview warnings, kept on purpose: paths that are named because they are missing (`CLAUDE.md`,
  `docs/adrs/`, `feature/hangman/src/test/`, library ProGuard files); `§15`, `§16`, `§1.5`, `§2.1`, `§2.3` are PRD
  section numbers, each written as "PRD §N".

## C3 record (2026-11-09)

| Check | How | Result |
|---|---|---|
| Verify | `gradle-quiet.sh ./gradlew testDebugUnitTest assembleDebug` | **FAIL**: `:app:compileDebugUnitTestKotlin`, `SettingsViewModelTest` imports `vagabond.ui.theme.AppTheme` (L-6, O-14) |
| Debug build | `./gradlew assembleDebug` | PASS |
| Release build | `./gradlew assembleRelease` | **FAIL**: R8 missing classes `AppTheme`, `ThemeKt`, `PixelBruteStyleKt` (L-5) |
| Lint | `./gradlew lintDebug`; `:app:lintDebug` | **FAIL** in `feature:hangman`: 3 NewApi `removeLast`, 2 unremembered `getBackStackEntry`; `app` warnings only |
| Instrumented compile | `./gradlew :app:compileDebugAndroidTestKotlin` | **FAIL**, 20 errors (same imports). Not run: needs a device |
| JDK | `./gradlew --version` | JetBrains Runtime 21.0.10 from Android Studio; no JDK set |
| L-3 cause | `android docs fetch kb://android/about/versions/15/behavior-changes-15` | Confirmed: compileSdk 35+ binds to `List.removeLast`, `NoSuchMethodError` on Android 14 and lower |
| L-5 cause | `android docs fetch kb://android/topic/performance/app-optimization/library-optimization` | Library build keep rules "must keep the library's public API from being removed"; libs shrink with no rules |
| composeOptions trap | Same Compose setup doc | Plugin confirmed for Kotlin 2.0+; "ignored" not stated anywhere: stays `[inferred]` |
| Paths | `test -e` on every backticked path (globs for `.../`) | Fixed `_6.xml`; un-backticked names that are deliberately absent (CLAUDE.md, ADR folder, test folder, ProGuard files, arrays.xml) |
| Counts | `grep -c '^    "'` HangmanData.kt = 2000; `@OptIn` = 6; tracked build files = 856 (excluding `.claude/`); `@Test` = 3 unit + 13 instrumented; files importing old package = 5 | Match the doc |
| Tags and rules | Read through §1, §10, §11 | Each §1.1 rule has a reason; each §1.3 row names a guard or "Guard: none" |
| Missing rules | §6 bump rule | Written as current practice `[inferred]`, O-13 for the owner |
| check.sh | after edits | Only FAIL: Changelog missing (C4). Warnings kept: PRD §15, §16, §1.5, §2.1, §2.3 are PRD section numbers, each written "PRD §N" |

Not run: `make run`, `connectedDebugAndroidTest` (device); nothing on a console.
