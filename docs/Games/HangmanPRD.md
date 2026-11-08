# **PRD: Hangman Mini-Game (Android Native)**

Version: 1.0  
Date: 31 October 2025  
Status: Draft

## **1\. Overview**

### **1.1. Objective**

To design and build a self-contained, offline Hangman mini-game as a new, modular feature within the existing "Vagabond" Android application. This game will allow for two-player (pass-and-play) or single-player (against the "computer") modes.

### **1.2. Goal**

Provide users with a classic, simple, and engaging word-guessing game that functions entirely offline, enhancing the value proposition of the main travel games app.

## **2\. Context & Dependencies**

### **2.1. Main Application Integration**

This Hangman module is not a standalone application. It must be built as a new feature module (e.g., a new Activity or Jetpack Compose NavHost) that is launched from the main app's game selection screen.

All architectural patterns, build.gradle configurations, and code styles must conform to the standards already established in the main application's PRD.

### **2.2. Theme and UI**

All UI components, including buttons, text fields, fonts, colors, spacing, and animations, must **strictly** adhere to the definitions and guidelines specified in the project's theme document called Theme.md document. No new or custom styling outside of this theme should be introduced.

## **3\. Target Platform & Technology**

* **Platform:** Android (Native)  
* **Language:** Kotlin  
* **UI:** Jetpack Compose (preferred) or existing View-based system (if mandated by the main app).  
* **State Management:** ViewModel for all screen state, UI logic, and game logic.  
* **Data:** Game state must be self-contained and reset upon exit. No persistent storage (e.g., Room, DataStore) is required beyond what is needed for the "Computer" mode's word list.

## **4\. User Flow & Screen Requirements**

### **4.1. Screen 1: Start Screen (Integration Point)**

* **Context:** User has already launched the main "Offline Travel Games" app and selected "Hangman" from the game list.  
* **Components:**  
  * **Title:** "Hangman"  
  * **Button:** "Start Game"  
* **Action:** Tapping "Start Game" navigates to Screen 2 (Input Screen).

### **4.2. Screen 2: Input Screen (Mode Selection)**

* **Purpose:** Allows the user to either input a custom phrase (2-player) or play against the "computer" (1-player).  
* **Components:**  
  * **Title:** "Enter a Word or Phrase"  
  * **Text Input:** EditText or TextField for manual entry.  
  * **Button:** "Play" (enabled only if text input is not empty).  
  * **Button:** "Computer" (for "Against the Bot" mode).  
  * **Button:** "Back" (returns to Screen 1).  
* **Actions:**  
  * **"Play":** Validates the text input. If valid (not empty), the app stores this phrase (uppercased) as the secretWord and navigates to Screen 3 (Game Screen).  
  * **"Computer":** Triggers the "Computer Mode" logic (see Section 6), which selects a random secretWord and navigates directly to Screen 3\.

### **4.3. Screen 3: Game Screen**

* **Purpose:** The main interface for playing the game.  
* **Components:**  
  * **Hangman Visual:** A visual representation of the gallows and the hangman. This must have 7 distinct failure states.  
  * **Guess Counter:** A TextView or Text composable displaying "Guesses left: X" (where X starts at 7).  
  * **Word Display:** The area showing the hidden phrase (see Section 5.1).  
  * **Keyboard:** The custom-built keyboard for input (see Section 5.2).  
  * **Button:** "Quit" or "Back" (prompts the user if they are sure, then returns to Screen 2).

### **4.4. Screen 4: End Screen (Modal or Full Screen)**

* **Purpose:** Displays the result of the game.  
* **Trigger:** Game ends (player wins by guessing all letters, or loses by exceeding 7 wrong guesses).  
* **Components:**  
  * **Result Message:** "You Win\!" (in victory color) or "You Lose\!" (in failure color).  
  * **Correct Phrase:** "The phrase was: \[secretWord\]"  
  * **Button:** "Play Again" (navigates back to Screen 2).  
  * **Button:** "Back to Menu" (exits Hangman and returns to the main app's game list).

## **5\. Detailed Feature Requirements**

### **5.1. Word Display**

* The secretWord must be processed and displayed as a series of placeholders.  
* **Spaces:** Any space ' ' in the secretWord must be rendered as a forward slash / to clearly delineate words.  
* **Placeholders:** Each alphanumeric character (A-Z, 0-9) must be shown as an underlined blank space or an empty box (per Theme.md).  
* **Auto-Reveal:** All other characters (e.g., apostrophes, hyphens, colons, etc.) are not guessable and **must be revealed by default** from the start.  
* **Example 1:**  
  * secretWord: "IT'S-A ME"  
  * **Display:** \_ \_ ' \_ \- \_ / \_ \_  
* **Example 2:**  
  * secretWord: "John Wick: Chapter 3 \- Parabellum"  
  * **Display:** \_ \_ \_ \_ / \_ \_ \_ \_ / : / \_ \_ \_ \_ \_ \_ \_ / \_ / \- / \_ \_ \_ \_ \_ \_ \_ \_ \_ \_

### **5.2. Keyboard Layout**

* A custom keyboard must be built using native Button or composable Button components.  
* The layout must be as follows:  
  1. **Row 1 (Numbers):** 1 2 3 4 5 6 7 8 9 0  
  2. **Separator:** A horizontal View or Divider (line) across the width.  
  3. **Row 2 (Alphabet):** Q W E R T Y U I O P  
  4. **Row 3 (Alphabet):** A S D F G H J K L  
  5. **Row 4 (Alphabet):** Z X C V B N M  
* **State:** Buttons must be stateful. Once tapped, a button is disabled. Its visual state should change based on the guess:  
  * **Correct Guess:** Change to "correct" color (correct accent, per Theme.md).  
  * **Incorrect Guess:** Change to "incorrect" color (negative accent, per Theme.md).

### **5.3. Game Logic**

* The game state must be managed in a ViewModel.  
* **Guess Limit:** The player has **7** incorrect guesses. The counter must start at 7 and decrement *only* on an incorrect guess.  
* **Win Condition:** The player wins if all alphanumeric characters in the secretWord are revealed.  
* **Loss Condition:** The player loses if the incorrect guess counter reaches 0 (i.e., after the 7th wrong guess).  
* **Hangman Visual:** The visual must update sequentially for each incorrect guess, corresponding to the 7-guess limit (e.g., 1\. Gallows, 2\. Head, 3\. Body, 4\. Arm L, 5\. Arm R, 6\. Leg L, 7\. Leg R).

## **6\. New Feature: "Computer" Mode**

### **6.1. Word List**

* A list of approximately 100 movie and TV show titles must be created.  
* This list must be bundled with the app as a local asset (e.g., a JSON file in assets/ or, preferably, a string array in res/values/arrays.xml).  
* The list must be easily editable by developers for future additions.

### **6.2. Logic**

* When the "Computer" button is tapped on Screen 2, the ViewModel must:  
  1. Load the word list from resources.  
  2. Select one entry at random.  
  3. Store this entry as the secretWord.  
  4. Navigate to Screen 3 (Game Screen).  
* The rest of the game flow is identical to the two-player mode.

## **7\. Assets Required**

* **Hangman Visuals:** A set of 7 sequential vector drawables (or one AVD) for the hangman progression.  
* **Word List Data:** The .xml or .json file containing \~100 titles.

## **8\. Out of Scope**

* Online multiplayer.  
* Persistent scoring or leaderboards.  
* Saving a game in progress.  
* Difficulty levels (the word list is the only "difficulty").  
* Any UI or theme elements not explicitly defined in Theme.md.