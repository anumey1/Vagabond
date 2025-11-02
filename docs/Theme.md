# **PixelBrute Style Guide**

## **1\. Design Philosophy**

The PixelBrute UI is a design system for Android built on the principles of retro 8-bit and 16-bit video game aesthetics. The goal is to evoke a sense of nostalgia while providing a clean, functional, and delightful user experience.

This is not simply a "pixelated" theme; it's a detailed approach that combines specific typography, blocky UI elements, hard-edged shadows, and vibrant, thematic color palettes to create an immersive, game-like feel without sacrificing usability.

### **Core Principles:**

* **Nostalgic, Not Dated:** The aesthetic is retro, but the UX principles (clarity, feedback, responsiveness) are modern.  
* **Crisp & Clean:** Every element, especially text, should render with sharp, pixel-perfect edges. Anti-aliasing should be disabled wherever possible.  
* **Tactile & Responsive:** User interactions should provide immediate, tangible feedback, making the UI feel like a physical game controller.  
* **Thematically Cohesive:** Colors are not arbitrary. Each palette is a curated theme that tells a small story, ensuring all UI elements work in harmony.

## **2\. Core UI Guidelines**

### **2.1. Typography**

* **Primary Font:** Press Start 2P (from Google Fonts). This is the cornerstone of the aesthetic and should be used for all UI text.  
* **Implementation:**  
  1. Add the font file (e.g., press\_start\_2p.ttf) to your app's res/font/ directory.  
  2. Create a font-family XML (e.g., res/font/pixel\_font.xml):  
     \<?xml version="1.0" encoding="utf-8"?\>  
     \<font-family xmlns:android="\[http://schemas.android.com/apk/res/android\](http://schemas.android.com/apk/res/android)"\>  
         \<font  
             android:fontStyle="normal"  
             android:fontWeight="400"  
             android:font="@font/press\_start\_2p" /\>  
     \</font-family\>

  3. Apply this font family to your app's theme in res/values/styles.xml:  
     \<style name="AppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar"\>  
         \<\!-- ... other attributes ... \--\>  
         \<item name="android:fontFamily"\>@font/pixel\_font\</item\>  
     \</style\>

* **Rendering (Crisp Text):**  
  * **XML Views:** To achieve a crisp, non-aliased look, you may need to disable anti-aliasing on your Paint object in custom views.  
    // In a custom View's onDraw or init block  
    myTextPaint.isAntiAlias \= false

  * **Jetpack Compose:** When using Canvas or custom draw modifiers, ensure the Paint object has anti-aliasing disabled.  
    val paint \= Paint().asFrameworkPaint().apply {  
        isAntiAlias \= false  
        // ... other properties  
    }

### **2.2. Borders & Shadows**

Avoid soft, blurred shadows (elevation) entirely. The signature style is achieved with thick borders and hard, offset shadows.

* **Borders:** Use thick, solid borders on all containers. This is best done with \<shape\> drawables.  
* **Hard Shadow (XML Views):** Use a \<layer-list\> drawable. The bottom layer is the "shadow" (a dark shape, offset), and the top layer is the main content background.  
  *Example (drawable/pixel\_container\_background.xml):*  
  \<?xml version="1.0" encoding="utf-8"?\>  
  \<layer-list xmlns:android="\[http://schemas.android.com/apk/res/android\](http://schemas.android.com/apk/res/android)"\>  
      \<\!-- The shadow: a dark shape, offset 4dp right and 4dp down \--\>  
      \<item  
          android:left="4dp"  
          android:top="4dp"\>  
          \<shape android:shape="rectangle"\>  
              \<solid android:color="@color/text\_dark" /\>  
              \<corners android:radius="0dp" /\>  
          \</shape\>  
      \</item\>  
      \<\!-- The content: the main bg, on top of the shadow \--\>  
      \<item  
          android:bottom="4dp"  
          android:right="4dp"\>  
          \<shape android:shape="rectangle"\>  
              \<solid android:color="@color/container\_bg" /\>  
              \<stroke android:width="4dp" android:color="@color/container\_border" /\>  
              \<corners android:radius="0dp" /\>  
          \</shape\>  
      \</item\>  
  \</layer-list\>

* **Hard Shadow (Jetpack Compose):** Use Modifier.drawBehind or stack two Box elements with an offset.  
  Box(  
      modifier \= Modifier  
          .offset(x \= 4.dp, y \= 4.dp) // The shadow's offset  
          .background(color \= colorResource(R.color.text\_dark))  
          .size(width, height)  
  )  
  Box(  
      modifier \= Modifier  
          .background(color \= colorResource(R.color.container\_bg))  
          .border(4.dp, color \= colorResource(R.color.container\_border))  
          .size(width, height)  
  ) {  
      // Content  
  }

* **Headers (Text Shadow):**  
  * **XML:** Use attributes on your TextView.  
    \<TextView  
        ...  
        android:shadowColor="@color/text\_dark"  
        android:shadowDx="2"  
        android:shadowDy="2"  
        android:shadowRadius="0.1" /\>

  * **Compose:** Use TextStyle.  
    Text(  
        text \= "HEADER",  
        style \= TextStyle(  
            shadow \= Shadow(  
                color \= colorResource(R.color.text\_dark),  
                offset \= Offset(2.0f, 2.0f),  
                blurRadius \= 0.1f  
            )  
        )  
    )

### **2.3. Interactivity & Feedback**

Button states should feel tactile. This is managed with State List Drawables (\<selector\>) in XML or InteractionSource in Compose.

* **XML (drawable/pixel\_button\_selector.xml):**  
  \<?xml version="1.0" encoding="utf-8"?\>  
  \<selector xmlns:android="\[http://schemas.android.com/apk/res/android\](http://schemas.android.com/apk/res/android)"\>  
      \<\!-- Pressed State: No shadow, moved "down" \--\>  
      \<item android:state\_pressed="true"\>  
          \<shape android:shape="rectangle"\>  
              \<solid android:color="@color/accent\_one" /\>  
              \<stroke android:width="4dp" android:color="@color/accent\_one\_dark" /\>  
          \</shape\>  
      \</item\>  
      \<\!-- Default State: Has shadow \--\>  
      \<item\>  
          \<layer-list\>  
              \<\!-- Shadow \--\>  
              \<item android:left="4dp" android:top="4dp"\>  
                  \<shape\>  
                      \<solid android:color="@color/accent\_one\_dark" /\>  
                  \</shape\>  
              \</item\>  
              \<\!-- Button \--\>  
              \<item android:bottom="4dp" android:right="4dp"\>  
                  \<shape\>  
                      \<solid android:color="@color/accent\_one" /\>  
                      \<stroke android:width="4dp" android:color="@color/accent\_one\_dark" /\>  
                  \</shape\>  
              \</item\>  
          \</layer-list\>  
      \</item\>  
  \</selector\>

  * To create the "move" effect, the pressed state drawable should *not* be a layer-list, and the Button's padding should be adjusted. A simpler way is to use a StateListAnimator.  
* **Jetpack Compose:**  
  val interactionSource \= remember { MutableInteractionSource() }  
  val isPressed by interactionSource.collectIsPressedAsState()

  val offset \= if (isPressed) Offset(4f, 4f) else Offset.Zero  
  val shadowOffset \= if (isPressed) Offset.Zero else Offset(4f, 4f)

  // ... use these offsets in Modifiers

## **3\. Component Style Guide**

### **3.1. Containers (CardView, Compose Surface)**

* **Background:** Use a \<layer-list\> drawable as described in **2.2. Borders & Shadows** to combine the background, border, and hard shadow.  
* **Jetpack Compose:** Use the stacked Box or Modifier.drawBehind technique.

### **3.2. Buttons (AppCompatButton, Compose Button)**

* **Text:** android:textAllCaps="true", android:textColor="@color/text\_dark".  
* **Background (XML):** Set the android:background to a \<selector\> drawable (see **2.3. Interactivity**).  
* **Background (Compose):** Use ButtonDefaults.buttonColors() with custom colors and modify the graphicsLayer or offset based on interactionSource.

### **3.3. Inputs & Selects (EditText, Spinner, Compose TextField)**

* **Remove Default Styling:**  
  * **XML:** Set android:background to a custom \<shape\> drawable (or \<selector\> for focus).  
  * **Compose:** Use TextFieldDefaults.textFieldColors() with backgroundColor \= Color.Transparent and custom indicator colors.  
* **Background:** android:background="@drawable/pixel\_input\_background"  
* **Focus State (XML):** Create a \<selector\> for the background that changes the stroke color.  
  \<\!-- drawable/pixel\_input\_background.xml \--\>  
  \<selector\>  
      \<item android:state\_focused="true"\>  
          \<shape\>  
              \<solid android:color="@color/background" /\>  
              \<stroke android:width="4dp" android:color="@color/accent\_one" /\>  
          \</shape\>  
      \</item\>  
      \<item\>  
          \<shape\>  
              \<solid android:color="@color/background" /\>  
              \<stroke android:width="4dp" android:color="@color/container\_border" /\>  
          \</shape\>  
      \</item\>  
  \</selector\>

* **Spinner Arrow:** For a Spinner, use android:drawableEnd with a custom pixel-art vector drawable to replace the default arrow.

### **3.4. Checkboxes (CheckBox, Compose Checkbox)**

* **Remove Default Styling:** Use android:button="@null" and android:background="@drawable/pixel\_checkbox\_selector".  
* **Custom Drawable (XML):**  
  \<\!-- drawable/pixel\_checkbox\_selector.xml \--\>  
  \<selector\>  
      \<\!-- Checked State \--\>  
      \<item android:state\_checked="true"\>  
          \<layer-list\>  
              \<\!-- Background \--\>  
              \<item\>  
                  \<shape\>  
                      \<solid android:color="@color/accent\_one" /\>  
                      \<stroke android:width="3dp" android:color="@color/container\_border" /\>  
                  \</shape\>  
              \</item\>  
              \<\!-- Checkmark (use a vector drawable) \--\>  
              \<item  
                  android:drawable="@drawable/ic\_pixel\_checkmark"  
                  android:gravity="center" /\>  
          \</layer-list\>  
      \</item\>  
      \<\!-- Unchecked State \--\>  
      \<item android:state\_checked="false"\>  
          \<shape\>  
              \<solid android:color="@color/background" /\>  
              \<stroke android:width="3dp" android:color="@color/container\_border" /\>  
          \</shape\>  
      \</item\>  
  \</selector\>

* **Compose:** Use Checkbox with CheckboxDefaults.colors() to set custom colors for checked and unchecked states.

## **4\. Color Palettes & Themes**

Implement themes by defining all colors in res/values/colors.xml and creating different app themes in res/values/styles.xml.

### **Role of Each Color Resource**

* background: Overall page background.  
* container\_bg: Background for content cards/sections.  
* container\_border: Border for content cards/sections.  
* text\_light: Main text color on dark backgrounds.  
* text\_dark: Text color for buttons and other elements on lighter backgrounds.  
* accent\_one: Primary interactive accent (focus, active states).  
* accent\_one\_dark: Darker shade of the first action accent for borders/shadows.  
* accent\_two: A second action accent color.  
* accent\_two\_dark: Darker shade of the second accent.  
* accent\_three: A negative action, something like "Danger", ”Close” or "Delete" action color.  
* accent\_three\_dark: Darker shade of the third action accent.  
* priority\_high / medium / low: Semantic colors for specialized indications.

### **Theme 1: Red Star**

A warm, retro-Soviet inspired theme with deep reds, oranges, and creamy off-whites.

*Example (res/values/colors\_red\_star.xml or defined in a theme):*

\<resources\>  
    \<color name="background"\>\#1A0A0A\</color\>  
    \<color name="container\_bg"\>\#331A1A\</color\>  
    \<color name="container\_border"\>\#f78d98\</color\>  
    \<color name="text\_light"\>\#FFF8E1\</color\>  
    \<color name="text\_dark"\>\#3D0000\</color\>  
    \<color name="accent\_one"\>\#F44336\</color\>  
    \<color name="accent\_one\_dark"\>\#C62828\</color\>  
    \<color name="accent\_two"\>\#ff737a\</color\>  
    \<color name="accent\_two\_dark"\>\#8f3539\</color\>  
    \<color name="accent\_three"\>\#FFEB3B\</color\>  
    \<color name="accent\_three\_dark"\>\#FBC02D\</color\>  
    \<color name="priority\_high"\>\#F44336\</color\>  
    \<color name="priority\_medium"\>\#FFEB3B\</color\>  
    \<color name="priority\_low"\>\#FFCC80\</color\>  
\</resources\>

### **Theme 2: Cosmic Teal**

A deep-sea or outer-space theme with dark teals and glowing, aquatic blues and greens.

*Example (res/values/colors\_cosmic\_teal.xml or defined in a theme):*

\<resources\>  
    \<color name="background"\>\#071212\</color\>  
    \<color name="container\_bg"\>\#162929\</color\>  
    \<color name="container\_border"\>\#008080\</color\>  
    \<color name="text\_light"\>\#e0ffff\</color\>  
    \<color name="text\_dark"\>\#012b2b\</color\>  
    \<color name="accent\_one"\>\#2ecc71\</color\>  
    \<color name="accent\_one\_dark"\>\#27ae60\</color\>  
    \<color name="accent\_two"\>\#7fdbff\</color\>  
    \<color name="accent\_two\_dark"\>\#0074d9\</color\>  
    \<color name="accent\_three"\>\#3df2e0\</color\>  
    \<color name="accent\_three\_dark"\>\#0b544d\</color\>  
    \<color name="priority\_high"\>\#7fdbff\</color\>  
    \<color name="priority\_medium"\>\#2ecc71\</color\>  
    \<color name="priority\_low"\>\#a2d2ff\</color\>  
\</resources\>  
