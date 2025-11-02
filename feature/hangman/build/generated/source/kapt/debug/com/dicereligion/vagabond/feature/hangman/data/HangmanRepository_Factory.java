package com.dicereligion.vagabond.feature.hangman.data;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class HangmanRepository_Factory implements Factory<HangmanRepository> {
  private final Provider<Context> contextProvider;

  public HangmanRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public HangmanRepository get() {
    return newInstance(contextProvider.get());
  }

  public static HangmanRepository_Factory create(Provider<Context> contextProvider) {
    return new HangmanRepository_Factory(contextProvider);
  }

  public static HangmanRepository newInstance(Context context) {
    return new HangmanRepository(context);
  }
}
