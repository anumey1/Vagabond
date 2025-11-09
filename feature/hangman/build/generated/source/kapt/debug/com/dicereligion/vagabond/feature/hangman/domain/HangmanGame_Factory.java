package com.dicereligion.vagabond.feature.hangman.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
  @Override
  public HangmanGame get() {
    return newInstance();
  }

  public static HangmanGame_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HangmanGame newInstance() {
    return new HangmanGame();
  }

  private static final class InstanceHolder {
    private static final HangmanGame_Factory INSTANCE = new HangmanGame_Factory();
  }
}
