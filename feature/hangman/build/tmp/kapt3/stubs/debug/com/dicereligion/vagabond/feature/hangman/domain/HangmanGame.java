package com.dicereligion.vagabond.feature.hangman.domain;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0016\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u0012\u0010\t\u001a\u00020\u00042\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bJ\b\u0010\f\u001a\u00020\u000bH\u0002J$\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000b0\u000e2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a8\u0006\u0010"}, d2 = {"Lcom/dicereligion/vagabond/feature/hangman/domain/HangmanGame;", "", "()V", "checkGameEndCondition", "Lcom/dicereligion/vagabond/feature/hangman/domain/HangmanGameState;", "currentState", "guessLetter", "letter", "", "newGame", "secretWord", "", "selectRandomWord", "updateGuessCountAndMessage", "Lkotlin/Pair;", "", "hangman_debug"})
public final class HangmanGame {
    
    @javax.inject.Inject()
    public HangmanGame() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState newGame(@org.jetbrains.annotations.Nullable()
    java.lang.String secretWord) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState guessLetter(@org.jetbrains.annotations.NotNull()
    com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState currentState, char letter) {
        return null;
    }
    
    private final java.lang.String selectRandomWord() {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Integer, java.lang.String> updateGuessCountAndMessage(com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState currentState, char letter) {
        return null;
    }
    
    private final com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState checkGameEndCondition(com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState currentState) {
        return null;
    }
}