package com.familycircleapp.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.main.CircleUserViewModel;
import com.familycircleapp.ui.main.CurrentCircleUserIdsViewModel;
import com.familycircleapp.ui.newuser.CreateCircleViewModel;
import com.familycircleapp.ui.newuser.JoinCircleViewModel;

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
  @ClassKey(CurrentCircleUserIdsViewModel.class)
  ViewModel provideCurrentCircleUserIdsViewModel(
      final CurrentUser currentUser,
      final CurrentCircleRepository currentCircleRepository) {
    return new CurrentCircleUserIdsViewModel(currentUser, currentCircleRepository);
  }

  @Provides
  @IntoMap
  @ClassKey(CircleUserViewModel.class)
  ViewModel provideCircleUserViewModel(final UserRepository userRepository) {
    return new CircleUserViewModel(userRepository);
  }

  @Provides
  @IntoMap
  @ClassKey(JoinCircleViewModel.class)
  ViewModel provideJoinCircleViewModel() {
    return new JoinCircleViewModel();
  }

  @Provides
  @IntoMap
  @ClassKey(CreateCircleViewModel.class)
  ViewModel provideCreateCircleViewModel() {
    return new CreateCircleViewModel();
  }
}
