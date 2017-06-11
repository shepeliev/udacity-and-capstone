package com.familycircleapp.repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class RepositoryModule {

  @Provides
  @Singleton
  public CurrentUser provideCurrentUser() {
    return new CurrentUserImpl();
  }
}
