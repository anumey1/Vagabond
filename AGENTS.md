# Vagabond

Instructions for any coding agent (Claude Code reads this file because the project has no CLAUDE.md; Gemini /
Antigravity read it too).

A lightweight offline "traveling games" container app for 1–2 players, built CLI-only. Package
`com.dicereligion.vagabond`. Kotlin 2.0 + Jetpack Compose, Hilt (kapt), DataStore, AGP 8.13. minSdk 30,
targetSdk/compileSdk 36. Work happens on `main` (owner ruling, `docs/stats.md` §10 R-1).

## Status doc
- `docs/stats.md` is the status doc. Load it with `/stats` (header + section index), then read only the
  sections the task needs. Read `stats.md §1 Rules and hazards` before changing anything.
- Every change updates the sections it touched and the *Updated* row, and adds an entry at the top of
  `docs/Changelog.md`, in the same change.

## Where things are
- `docs/PRD.md` — product + technical spec (read by section): §5 theming, §6 functional requirements, §8
  architecture, §9 CLI workflow, §10 acceptance criteria, §11 testing. `docs/Theme.md` — design system.
  `docs/Games/` — per-game PRDs (Hangman, Minesweeper). `docs/AndroidCLI.md` is an older hand-written CLI guide;
  the `android-cli` skill is the current reference. `GeminiPlan.md` — an earlier implementation plan.
- Modules: `app` (shell, landing/collections/settings screens, DI), `core:designsystem` (PixelBrute style),
  `core:utils`, `feature:hangman`.

## Build and test
- Verify: `./gradlew testDebugUnitTest assembleDebug` (Claude: wrap it in `~/.claude/hooks/gradle-quiet.sh`).
- `make build` / `make run` exist; `make run` installs and launches on a device — only when the owner asks.

## Rules
- **No experimental features** (PRD critical policy): no preview/alpha/beta/canary libraries, no `@OptIn`
  experimental APIs. Stable releases only.
- Never commit or push; the owner does all git.
- `.gitignore` now ignores every module's `build/`. Build output that git already tracks still shows as changed
  until the owner untracks it.
