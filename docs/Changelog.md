# Vagabond: changelog

Newest first. One entry per change that touched the code or `stats.md`. The status doc is `docs/stats.md`.

## 2026-11-09: stats.md created

- **What:** Vagabond now has a status doc, `docs/stats.md`, in the standard 15-section form, and this changelog.
  A new session can find the rules, the open work and the known problems without reading the whole PRD.
- **Why:** Owner request (`/makestats`, Create mode); Vagabond was the one Android project without a status doc.
- **Files:** `docs/stats.md`, `docs/Changelog.md`, `docs/stats-work.md` (work file), `AGENTS.md` (status-doc
  block; branch now `main`)
- **Verified:** `./gradlew testDebugUnitTest assembleDebug` → **fails** (tests do not compile; was already failing);
  `assembleDebug` → passes; `assembleRelease` → fails in R8; `lintDebug` → 5 errors in `feature:hangman`;
  `:app:compileDebugAndroidTestKotlin` → 20 errors. Not checked on a device.
- **Issues found** (no code changed; each is open in `stats.md` §2 Open work):
  - O-15 / L-3: Hangman Computer mode crashes on Android 11 to 14 (`removeLast()` resolves to an API 35 method).
  - O-14 / L-6: no test compiles; 5 test files import the theme from its old `ui.theme` package.
  - O-12 / L-5: no release build possible; the library modules shrink away their own public API.
  - O-16 / L-7: two lint errors, `getBackStackEntry` called during composition without `remember`.
  - O-5 / L-2: two `CollectionsScreenTest` assertions no longer match the screen.
  - O-6: 856 build-output files are still tracked by git, so every build shows them as changed.
  - Owner questions O-1 to O-4, O-8 to O-10, O-13: branch policy, live state, `@OptIn` uses, PRD rules,
    save file, stray folder, next game, theme-setting bump rule.
- **Closed:** none · **stats.md:** all sections (new)

---

## Older history

None moved. Earlier work is in git history (from 2025-10-29) and in `docs/Saves/savefile1.md`, a project snapshot
at version 4.1 (whether it moves here is O-8).
