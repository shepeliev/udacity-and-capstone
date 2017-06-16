package com.familycircleapp.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.familycircleapp.repository.CircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.main.CurrentCircleUserIdsViewModel;
import com.familycircleapp.ui.main.CurrentCircleUsersViewModel;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public final class ViewModelModule {

  @Provides
  @Singleton
  ViewModelProvider.Factory provideViewModelFactory(
      final Map<Class<?>, Provider<ViewModel>> models
  ) {
    return new ViewModelFactory(models);
  }

  @Provides
  @IntoMap
  @ClassKey(CurrentCircleUsersViewModel.class)
  ViewModel provideCurrentCircleUsersViewModel(
      final CurrentUser currentUser,
      final CircleRepository circleRepository,
      final UserRepository userRepository
  ) {
    return new CurrentCircleUsersViewModel(currentUser, circleRepository, userRepository);
  }

  @Provides
  @IntoMap
  @ClassKey(CurrentCircleUserIdsViewModel.class)
  ViewModel provideCurrentCircleUserIdsViewModel(
      final CurrentUser currentUser,
      final CircleRepository circleRepository,
      final UserRepository userRepository
  ) {
    return new CurrentCircleUserIdsViewModel(currentUser, circleRepository, userRepository);
  }
}
