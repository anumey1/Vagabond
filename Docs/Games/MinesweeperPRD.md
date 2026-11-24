# **Minesweeper – Product Requirements Document (PRD)**

**Scope note**: This PRD describes the complete specification to build **one** game module — *Minesweeper* — inside an existing Android application that houses multiple offline travel games. It focuses on Android-first implementation details, architecture, and integration. It **must** align with the master PRD (`PRD.md`) of the host app and **must strictly follow** the theme guidelines in `Theme.md`. Do not restate or reinterpret the theme here; simply enforce that all visuals/styles conform to **Theme.md**.

---

## **1\) Overview**

**Goal**: Ship a portrait-first Minesweeper module that fits entirely on-screen without scrolling, optimized for offline play on a broad range of Android devices (phones and small tablets), integrating seamlessly with the host app’s game catalog.

**Top outcomes**

* Portrait-friendly grid that **never scrolls**; the board scales to fit the visible area.

* Tactile, accessible inputs for small cell sizes (haptics, magnifier, error-guard).

* Clean Architecture \+ Jetpack Compose UI with high performance and low battery use.

* Self-contained feature module with a stable API to the host app.

**Non-goals**

* Multiplayer, cloud profiles, ads, or any network features (game is offline-only).

* Cross-platform targets beyond Android.

---

## **2\) Alignment & Dependencies**

* This module **must** comply with the host application’s **master PRD** (`PRD.md`).

* This module **must strictly follow** the visual/theming specifications in **`Theme.md`**. No alternate, additional, or conflicting theme rules are allowed in this PRD.

---

## **3\) Android-First Implementation Summary**

**Tech stack (stable-channel preferred)**

* **Language**: Kotlin

* **UI**: Jetpack Compose (Material 3 components where sensible; custom drawing for board)

* **DI**: Hilt

* **Navigation**: Jetpack Navigation (host-app routes \+ deep links)

* **Async**: Coroutines \+ Flow

* **State**: Unidirectional state (MVI/MVVM hybrid)

* **Persistence**: DataStore (Proto) for settings and local stats; Room optional if needed later

* **Graphics**: Compose `Canvas` / `LazyVerticalGrid` (see §8) with `remember`/`derivedStateOf` optimizations

* **Haptics**: `VibratorManager` / `HapticFeedback` APIs

* **Accessibility**: TalkBack semantics, large-tap assists, colorblind-safe palette

* **Testing**: Unit (JUnit), UI (Compose Testing), property tests for board rules

**Module layout**

:feature:minesweeper  
  ├─ api/                  // public interfaces to host app  
  ├─ ui/                   // Compose screens, components  
  ├─ domain/               // game rules, board generator, use cases  
  ├─ data/                 // DataStore (settings, stats)  
  ├─ di/                   // Hilt modules  
  └─ test/                 // unit \+ UI tests

**Minimum OS**: target a modern minSdk appropriate for the host (e.g., 24+). **targetSdk/compileSdk**: use latest stable at build time.

---

## **4\) Integration With the Host “Offline Travel Games” App**

**Discovery and launch**

* The host app exposes a **Game Registry**. Minesweeper registers a card with id, title, icon, short description, and deep link.

**Registry contract (example)**

interface GameDescriptor {  
  val id: String            // "minesweeper"  
  val displayName: String   // "Minesweeper"  
  val iconRes: Int  
  val route: String         // nav route or deep link, e.g., "app://games/minesweeper"  
  val offline: Boolean      // true  
}

**Navigation**

* Host calls `navigate("app://games/minesweeper")`.

* Minesweeper provides a `NavGraphBuilder.minesweeperGraph()` extension to install its destinations into the host graph.

**Persistence & isolation**

* All settings/stats are sandboxed to the module’s DataStore namespace.

* No runtime permissions required.

**Theming**

* The module consumes the host **Theme tokens** per **`Theme.md`**. No local overrides beyond tokenized variables.

---

## **5\) Game Design Requirements**

### **5.1 Rules & Interactions**

* Classic Minesweeper rules: uncover all non-mine tiles; hitting a mine ends the game.

* **First tap is always safe** (mine relocation on first reveal if needed).

* **Reveal**: single tap.

* **Flag**: long-press (press & hold) toggles flag.

* **Chord**: when a revealed number `n` has exactly `n` adjacent flags, tapping the number reveals unflagged neighbors.

* **Optional**: “Question mark” state for advanced users (off by default).

### **5.2 Difficulty & Board Sizes (portrait-first)**

**Requested layouts**

* **Easy**: 10 × 10 with **12** mines

* **Medium**: 12 × 22 with **40** mines

* **Hard**: 14 × 42 with **99** mines

**No-scroll requirement**

* The grid must **fully fit** in the visible game area without scrolling. Board tiles are **square**, sized by the smaller dimension rule:

   `cellSizeDp = floor( min( availableWidthDp / columns, availableHeightDp / rows ) )`

   This guarantees the board fits both horizontally and vertically. Any leftover width/height becomes symmetric gutters.

**HUD height budget**

* Reserve \~`HUDmin = 80–120dp` for top status bar (mine counter, timer, reset, difficulty switcher).

* Available height for the board: `availableHeightDp = screenHeightDp - systemInsets - HUDmin`.

**Feasibility analysis (typical phones)**

* Baseline phone (≈360dp × 720dp; HUD 100dp ⇒ availH ≈ 620dp)

  * Easy 10×10: `min(36, 62) = 36dp` cells ✅

  * Medium 12×22: `min(30, 28.2) ≈ 28dp` cells ✅

  * Hard 14×42: `min(25.7, 14.8) ≈ 14–15dp` cells ✅ fits but **very small**

* Small phone (≈320dp × 568dp; HUD 96dp ⇒ availH ≈ 472dp)

  * Easy: `min(32, 47) ≈ 32dp` ✅

  * Medium: `min(26.7, 21.5) ≈ 21–22dp` ✅ (tight but playable)

  * Hard: `min(22.9, 11.2) ≈ 11–12dp` ✅ fits mathematically but **too tiny** for reliable tapping

**Recommendation for Hard on small devices**

* Provide **adaptive board rows** for Hard while keeping **99 mines** and 14 columns to preserve portrait aspect:

  * **Hard-A (preferred)**: **14 × 36** (504 cells, \~19.6% density) — `cell ≈ 13–17dp` on small-to-baseline phones

  * **Hard-B**: **14 × 34** (476 cells, \~20.8% density)

  * **Hard-C**: **14 × 32** (448 cells, \~22.1% density)

**Adaptive rule**

* On launch, compute `cellSizeDp` for the requested 14×42. If `cellSizeDp < 13dp`, automatically fall back to **Hard-A (14×36)**; if still `< 13dp`, fall back to **Hard-B**, then **Hard-C**. Display a one-time tooltip: “Board adapted for your screen. Mines remain 99.”

**Rationale**: Maintains portrait, avoids scroll/zoom, and keeps Hard challenging with the canonical **99 mines**.

### **5.3 Layout & UI**

* **Top HUD**: mine counter (flags used vs total), timer (mm:ss), reset button, difficulty selector (segmented), pause.

* **Board**: centered; square tiles; uses remaining space per formula; no ScrollView.

* **Bottom bar (optional)**: quick toggles (flag mode, magnifier, vibration), only if space allows.

* **States**: Ready → In-Play → Won/Lost → Replay.

* **Animations**: light scale/fade on reveal, shake on mine hit, confetti on win (respect battery saver).

### **5.4 Input & Accessibility Aids**

* **Large-tap assist**: taps are snapped to the nearest tile center within a small radius.

* **Magnifier** (toggle): press-and-hold shows a lens with 2× zoom of a 5×5 neighborhood; release to confirm tile.

* **Haptics**: subtle on flag toggle and mine hit (respect system settings).

* **Colorblind**: colors drawn from Theme tokens that have daltonism-safe contrast; numbers also encoded by glyph shapes/weights.

* **TalkBack**: tiles expose content descriptions: `"Hidden tile"`, `"Flagged"`, `"Revealed: 3"`, `"Mine"` (after game over). Linear traversal uses grid semantics.

* **Minimum tap area**: if `cellSizeDp < 20dp`, expand the effective hit slop via `pointerInput` to ≥ 24dp.

### **5.5 Settings**

* Difficulty: Easy / Medium / Hard (with adaptive hard rule).

* Toggles: question marks (off by default), magnifier (off), haptics (on, obey system), sound (off by default if host app policy requires quiet travel mode).

* Reset stats.

### **5.6 Telemetry (local-only)**

* Track local stats: games played, wins, best time per difficulty, current streak. Stored in DataStore; **no network**.

---

## **6\) Functional Requirements**

1. **Start game** in any difficulty within 300ms on baseline phones.

2. **First tap safety**: never spawns a mine on the first revealed tile; recompute if necessary.

3. **Reveal flood**: empty areas flood-reveal via BFS/DFS within 16ms per interaction budget.

4. **Chord action**: number tile with `n` flags around it reveals remaining neighbors.

5. **Win detection**: when the count of revealed safe tiles equals total safe tiles.

6. **Loss**: reveal mine → show all mines, highlight incorrect flags.

7. **Pause**: hides board overlay, timer pauses; backgrounded app also pauses timer.

8. **Persistent settings/stats** survive process death.

9. **Adaptive hard** fallback triggers only if computed cell size is below threshold; show tooltip once.

---

## **7\) Non-Functional Requirements**

* **Performance**: steady 60fps animations; touch → reveal within 1 frame.

* **Battery**: no busy loops; timers via `Choreographer`/`LaunchedEffect` \+ `withFrameNanos` or `Ticker`.

* **Memory**: board objects not to exceed a few hundred KB per session (see §9 model).

* **Stability**: zero crashes in normal play; robust against rapid taps, rotation, and background/restore.

* **Localization**: strings externalized; RTL mirrored; numerals localizable.

---

## **8\) UI & Rendering Approach (Compose)**

**Board rendering options**

* **Option A – Canvas**: custom draw squares, numbers, flags, mines. Maximum control, minimal overhead.

* **Option B – LazyVerticalGrid**: simpler composition; acceptable for ≤ 600 tiles.

**Decision**: Use **Option A (Canvas)** for consistent frame pacing and precise hit testing, with a lightweight data model. Use `pointerInput` for gestures; map touch to tile by math (no layout measuring per tile).

**Sizing pipeline**

1. Read `BoxWithConstraints` to get `maxWidth`/`maxHeight` in **dp**.

2. Subtract HUD height to compute `availableHeightDp`.

3. Compute `cellSizeDp` per formula; clamp to an integer dp.

4. Compute `boardWidth = cellSizeDp * cols`, `boardHeight = cellSizeDp * rows`.

5. Center the board; render gutters as themed background.

**Hit testing**

* Translate tap to (row, col) by `(x - boardLeft)/cellSize` and `(y - boardTop)/cellSize`; guard bounds; apply snap radius & hit slop.

---

## **9\) Domain Model & Algorithms**

**Types** (simplified)

data class Cell(  
  val isMine: Boolean,  
  val adjacent: Int,  
  val state: CellState  
)  
sealed interface CellState { object Hidden; object Flagged; object Question; data class Revealed(val t: Long \= 0\) }

data class Board(val rows: Int, val cols: Int, val totalMines: Int, val cells: IntArray /\*bit-packed\*/)

enum class GamePhase { Ready, Playing, Won, Lost, Paused }

**Bit-packing** (optional optimization)

* Use an `IntArray` and pack per-cell flags: `bit0..bit3 = adjacent(0..8)`, `bit4 = isMine`, `bit5 = revealed`, `bit6 = flagged`, `bit7 = question`.

**Board generation**

1. Fill an index list of all cells.

2. On first tap, remove tapped index and its neighborhood from candidates (to ensure a safe opening).

3. Shuffle (Kotlin `Random.Default` or injected RNG) and mark first `totalMines` as mines.

4. Compute `adjacent` counts in O(R×C).

**Flood reveal**

* BFS from the tapped cell if `adjacent == 0`, revealing neighbors until front exhausted.

**Chord**

* If a revealed number has `adjacent == flagsAround`, reveal all other neighbors; if a mine is revealed, lose.

**RNG**

* Use Kotlin `Random` with module-level seed (for reproducible sessions in tests). First-tap-safe relocation is deterministic per session.

---

## **10\) App Architecture**

**Clean Architecture with MVVM**

* **ui**: Compose screens; observes immutable `UiState` via `StateFlow`.

* **domain**: use cases (`NewGame`, `RevealCell`, `ToggleFlag`, `ChordAt`, `PauseGame`, `ResumeGame`, `ChangeDifficulty`).

* **data**: settings/stats repositories backed by DataStore Proto.

* **di**: Hilt graph; module provides repositories and use cases.

**Threading**

* All game logic runs on Default dispatcher; UI on Main; state reduced to a single `UiState`.

**Navigation graph**

* `MinesweeperHome` (difficulty select) → `MinesweeperGame` → `ResultsDialog`.

---

## **11\) UX Specs (Key Screens)**

1. **Game Screen (Portrait)**

   * Top HUD (left→right): Mine counter, Timer, Reset (face), Difficulty chips, Pause.

   * Board centered; gutters visible if board narrower than screen.

   * Bottom optional toolbar (if space): toggles for Flag Mode, Magnifier.

2. **Pause Overlay**

   * Dim board; buttons: Resume, New Game, Settings.

3. **Results Dialog**

   * Show Win/Lose, time, best time badge, replay.

**Gestures**

* Tap: reveal (or chord when on number).

* Long-press: flag toggle.

* Long-press-and-hold (if Magnifier on): lens preview; release to confirm.

---

## **12\) Visuals & Theme**

* **Mandatory**: Use the host app’s tokens per **`Theme.md`** for colors, typography, spacing, shapes, and elevations.

* Tile states (examples only; **actual values come from Theme tokens**):

  * Hidden, Hover (if enabled), Pressed, Flagged, Question, Revealed(0..8), Mine, Exploded.

* Use high-contrast number mapping and a secondary encoding (weight/shape) for colorblind safety.

**Do not copy theme values here.** This module must **strictly** inherit all theming from `Theme.md`.

---

## **13\) Settings & Data**

**DataStore (Proto)**

message MinesweeperPrefs {  
  enum Difficulty { EASY \= 0; MEDIUM \= 1; HARD \= 2; }  
  Difficulty difficulty \= 1;  
  bool haptics \= 2;  
  bool magnifier \= 3;  
  bool questionMarks \= 4;  
}

message MinesweeperStats {  
  int32 gamesEasy \= 1;  
  int32 winsEasy \= 2;  
  int32 bestTimeEasySec \= 3; // 0 means none  
  // ... repeat for Medium/Hard  
}

---

## **14\) Error Handling & Edge Cases**

* Rapid multi-taps debounced per frame; queue actions when flood is running.

* Resume after process death restores board and timer.

* If adaptive Hard fallback changed the board size mid-session (should not), abandon change and prompt to restart.

* Guard against integer overflow (indices, timers) and invalid RNG seeds.

---

## **15\) QA & Acceptance Criteria**

**Unit tests**

* Board generation places exactly `totalMines`.

* First tap never a mine; adjacent counts are correct.

* Flood reveal equivalence with a reference solver (property-based).

* Chord correctness; win detection correctness.

**UI tests**

* Reveal/flag flows; TalkBack labels; rotation pause/resume; adaptive hard tooltip path.

**Performance checks**

* Reveal action ≤ 16ms on baseline; memory footprint stable across 100 reveals.

**Acceptance**

* Grid never scrolls on devices ≥ small phone size; adaptive fallback triggers when needed.

* Visuals 100% consistent with **`Theme.md`**; navigation & registry integration verified.

---

## **16\) Build, Packaging, and Delivery**

* Gradle module `:feature:minesweeper` with a clear API surface.

* One icon (vector) and localized strings.

* Feature toggles (if host app uses a flag system): `minesweeper.enabled`.

* Deliver as part of the host APK; dynamic feature optional if host architecture supports it.

---

## **17\) Risks & Mitigations**

| Risk | Impact | Mitigation |
| ----- | ----- | ----- |
| Hard board too tiny on small phones | Miss-taps, frustration | Adaptive Hard fallbacks (14×36 → 14×34 → 14×32), magnifier, hit slop, haptics |
| Performance regressions in Compose | Jank | Canvas rendering, memoized state, avoid per-cell recomposition |
| Accessibility gaps | Exclusion | TalkBack semantics, colorblind-safe palette via Theme tokens, large-tap assist |
| Theme drift | Visual inconsistency | Enforce tokens from `Theme.md`; no hardcoded colors |

---

## **18\) Open Questions (to resolve in `PRD.md`/host scope)**

* Exact iconography source and naming (provided by host?); confirm navigation deep-link scheme.

* Minimum device size officially supported by the host catalog.

* Whether sound is globally on/off in travel mode.

---

## **19\) Appendix – Developer Notes**

**Example API to register with host**

object MinesweeperGameDescriptor : GameDescriptor {  
  override val id \= "minesweeper"  
  override val displayName \= "Minesweeper"  
  override val iconRes \= R.drawable.ic\_minesweeper  
  override val route \= "app://games/minesweeper"  
  override val offline \= true  
}

**Compose entry point**

fun NavGraphBuilder.minesweeperGraph() {  
  composable("app://games/minesweeper") { MinesweeperScreen() }  
}

**Cell size computation**

val cell \= floor(min(availableWidth / cols, availableHeight / rows))

**Difficulty presets**

* Easy: 10×10, 12 mines

* Medium: 12×22, 40 mines

* Hard: 14×42, 99 mines (with adaptive fallbacks to 14×36 → 14×34 → 14×32 if cell \< 13dp)

---

### **Final Compliance Reminder**

* Follow the host’s **`PRD.md`** for process and cross-game conventions.

* **Strictly** follow **`Theme.md`** for all visuals. This PRD does not redefine theme.

