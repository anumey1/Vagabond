### **AI Instructions for Implementing Hangman ASCII Art**

To properly display the provided Hangman ASCII art array in a small, square container on an Android app, follow these critical instructions:

1. **Use a Monospaced Font:** This is the most important rule. The ASCII art relies on all characters (spaces, |, /, etc.) having the exact same width. Set the text component's font to a monospaced typeface.  
   * **Android XML:** android:fontFamily="monospace"  
   * **Jetpack Compose:** fontFamily \= FontFamily.Monospace  
   * **Kotlin/Java (Views):** textView.typeface \= Typeface.MONOSPACE  
2. **Container and Sizing:**  
   * The text component (e.g., TextView or Text) should be placed inside your small square container.  
   * Set the lineSpacingMultiplier to 1.0 or slightly less (e.g., 0.9) to ensure the lines are tight and the drawing is cohesive. Avoid large line spacing.  
   * The textSize must be small enough for the widest line (" \+---+" or "=====") to fit comfortably within the square's width *without* wrapping. You may need to test a few values (e.g., 10sp, 12sp).  
3. **Text Alignment:**  
   * The text *within* the text component must be **left-aligned** (or start-aligned). Do *not* use center alignment for the text itself, as this will misalign each line of the drawing.  
     * **Android XML:** android:gravity="start|top" (or simply omit android:gravity as start is default).  
     * **Jetpack Compose:** textAlign \= TextAlign.Start (this is the default).  
   * To center the *entire drawing* within the square container, set the layout gravity of the TextView component itself to center.  
     * **Example (XML):** \<FrameLayout ...\>\<TextView android:layout\_gravity="center" ... /\>\</FrameLayout\>  
     * **Example (Compose):** Box(contentAlignment \= Alignment.Center) { Text(...) }  
4. **Implementation Logic:**  
   * Store the hangmanStages array in your code (e.g., as a string array resource).  
   * Maintain an integer variable for the current game state (e.g., wrongGuesses \= 0).  
   * When the state changes, update the text component to display the string at that index: textView.text \= hangmanStages\[wrongGuesses\].

### 

### 

### **Hangman Drawing Text Code:**

// Array of 7 Hangman stages, from empty to complete.  
// Each string uses \\n for newlines to be rendered correctly.

const hangmanStages \= \[  
  // Stage 0 (Empty Gallows)  
  " \+---+\\n" \+  
  " |   |\\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  "=====",

  // Stage 1 (Head)  
  " \+---+\\n" \+  
  " |   |\\n" \+  
  " |   O\\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  "=====",

  // Stage 2 (Body)  
  " \+---+\\n" \+  
  " |   |\\n" \+  
  " |   O\\n" \+  
  " |   |\\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  "=====",

  // Stage 3 (One Arm)  
  " \+---+\\n" \+  
  " |   |\\n" \+  
  " |   O\\n" \+  
  " |  /|\\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  "=====",

  // Stage 4 (Two Arms)  
  " \+---+\\n" \+  
  " |   |\\n" \+  
  " |   O\\n" \+  
  " |  /|\\\\\\n" \+  
  " |    \\n" \+  
  " |    \\n" \+  
  "=====",

  // Stage 5 (One Leg)  
  " \+---+\\n" \+  
  " |   |\\n" \+  
  " |   O\\n" \+  
  " |  /|\\\\\\n" \+  
  " |  / \\n" \+  
  " |    \\n" \+  
  "=====",

  // Stage 6 (Full Hangman)  
  " \+---+\\n" \+  
  " |   |\\n" \+  
  " |   O\\n" \+  
  " |  /|\\\\\\n" \+  
  " |  / \\\\\\n" \+  
  " |    \\n" \+  
  "====="  
\];

