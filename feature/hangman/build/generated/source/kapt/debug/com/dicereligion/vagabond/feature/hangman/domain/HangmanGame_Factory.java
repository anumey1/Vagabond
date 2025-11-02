package com.dicereligion.vagabond.feature.hangman.domain;

import com.dicereligion.vagabond.feature.hangman.data.HangmanRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class HangmanGame_Factory implements Factory<HangmanGame> {
  private final Provider<HangmanRepository> hangmanRepositoryProvider;

  public HangmanGame_Factory(Provider<HangmanRepository> hangmanRepositoryProvider) {
    this.hangmanRepositoryProvider = hangmanRepositoryProvider;
  }

  @Override
  public HangmanGame get() {
    return newInstance(hangmanRepositoryProvider.get());
  }

  public static HangmanGame_Factory create(Provider<HangmanRepository> hangmanRepositoryProvider) {
    return new HangmanGame_Factory(hangmanRepositoryProvider);
  }

  public static HangmanGame newInstance(HangmanRepository hangmanRepository) {
    return new HangmanGame(hangmanRepository);
  }
}
