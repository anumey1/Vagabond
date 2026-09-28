package com.dicereligion.vagabond.feature.hangman.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0002\u0010\f\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u001c\b\u0087\b\u0018\u00002\u00020\u0001BQ\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000eJ\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010\"\u001a\u00020\bH\u00c6\u0003J\t\u0010#\u001a\u00020\bH\u00c6\u0003J\t\u0010$\u001a\u00020\u000bH\u00c6\u0003J\t\u0010%\u001a\u00020\u000bH\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003JU\u0010\'\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010(\u001a\u00020\u000b2\b\u0010)\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010*\u001a\u00020\bH\u00d6\u0001J\t\u0010+\u001a\u00020\u0003H\u00d6\u0001R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0019R\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001c\u00a8\u0006,"}, d2 = {"Lcom/dicereligion/vagabond/feature/hangman/domain/HangmanGameState;", "", "secretWord", "", "guessedLetters", "", "", "incorrectGuesses", "", "maxIncorrectGuesses", "isGameOver", "", "isGameWon", "message", "(Ljava/lang/String;Ljava/util/Set;IIZZLjava/lang/String;)V", "availableLetters", "", "getAvailableLetters", "()Ljava/util/List;", "getGuessedLetters", "()Ljava/util/Set;", "guessesLeft", "getGuessesLeft", "()I", "getIncorrectGuesses", "()Z", "maskedWord", "getMaskedWord", "()Ljava/lang/String;", "getMaxIncorrectGuesses", "getMessage", "getSecretWord", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "toString", "hangman_debug"})
@androidx.compose.runtime.Immutable()
public final class HangmanGameState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String secretWord = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.Character> guessedLetters = null;
    private final int incorrectGuesses = 0;
    private final int maxIncorrectGuesses = 0;
    private final boolean isGameOver = false;
    private final boolean isGameWon = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String message = null;
    
    public HangmanGameState(@org.jetbrains.annotations.NotNull()
    java.lang.String secretWord, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.Character> guessedLetters, int incorrectGuesses, int maxIncorrectGuesses, boolean isGameOver, boolean isGameWon, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSecretWord() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.Character> getGuessedLetters() {
        return null;
    }
    
    public final int getIncorrectGuesses() {
        return 0;
    }
    
    public final int getMaxIncorrectGuesses() {
        return 0;
    }
    
    public final boolean isGameOver() {
        return false;
    }
    
    public final boolean isGameWon() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMaskedWord() {
        return null;
    }
    
    public final int getGuessesLeft() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Character> getAvailableLetters() {
        return null;
    }
    
    public HangmanGameState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.Character> component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean component6() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String secretWord, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.Character> guessedLetters, int incorrectGuesses, int maxIncorrectGuesses, boolean isGameOver, boolean isGameWon, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}