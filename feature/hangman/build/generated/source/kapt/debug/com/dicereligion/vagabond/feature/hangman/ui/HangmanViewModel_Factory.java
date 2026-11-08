package com.dicereligion.vagabond.feature.hangman.ui;

import com.dicereligion.vagabond.feature.hangman.domain.HangmanGame;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class HangmanViewModel_Factory implements Factory<HangmanViewModel> {
  private final Provider<HangmanGame> hangmanGameProvider;

  public HangmanViewModel_Factory(Provider<HangmanGame> hangmanGameProvider) {
    this.hangmanGameProvider = hangmanGameProvider;
  }

  @Override
  public HangmanViewModel get() {
    return newInstance(hangmanGameProvider.get());
  }

  public static HangmanViewModel_Factory create(Provider<HangmanGame> hangmanGameProvider) {
    return new HangmanViewModel_Factory(hangmanGameProvider);
  }

  public static HangmanViewModel newInstance(HangmanGame hangmanGame) {
    return new HangmanViewModel(hangmanGame);
  }
}
