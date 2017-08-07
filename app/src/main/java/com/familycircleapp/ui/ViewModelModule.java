package com.familycircleapp.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.familycircleapp.repository.CircleRepository;
import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.InviteRepository;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.common.CreateCircleErrorTextResolver;
import com.familycircleapp.ui.common.CreateCircleViewModel;
import com.familycircleapp.ui.common.CurrentCircleNameViewModel;
import com.familycircleapp.ui.common.CurrentUserViewModel;
import com.familycircleapp.ui.common.JoinCircleErrorTextResolver;
import com.familycircleapp.ui.common.JoinCircleViewModel;
import com.familycircleapp.ui.invite.InviteViewModel;
import com.familycircleapp.ui.main.CircleUserViewModel;
import com.familycircleapp.ui.main.CurrentCircleUserIdsViewModel;

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
  @Singleton
  JoinCircleErrorTextResolver provideJoinCircleErrorTextResolver(final Context context) {
    return new JoinCircleErrorTextResolver(context);
  }

  @Provides
  @Singleton
  CreateCircleErrorTextResolver provideCreateCircleErrorTextResolver(final Context context) {
    return new CreateCircleErrorTextResolver(context);
  }

  @Provides
  @IntoMap
  @ClassKey(CurrentCircleUserIdsViewModel.class)
  ViewModel provideCurrentCircleUserIdsViewModel(
      final CurrentUser currentUser,
      final CurrentCircleRepository currentCircleRepository
  ) {
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
  ViewModel provideJoinCircleViewModel(
      final JoinCircleErrorTextResolver joinCircleErrorTextResolver,
      final CurrentUser currentUser,
      final InviteRepository inviteRepository
  ) {
    return new JoinCircleViewModel(joinCircleErrorTextResolver, currentUser, inviteRepository);
  }

  @Provides
  @IntoMap
  @ClassKey(CreateCircleViewModel.class)
  ViewModel provideCreateCircleViewModel(
      final CreateCircleErrorTextResolver createCircleErrorTextResolver,
      final CurrentUser currentUser,
      final CircleRepository circleRepository
  ) {
    return new CreateCircleViewModel(createCircleErrorTextResolver, currentUser, circleRepository);
  }

  @Provides
  @IntoMap
  @ClassKey(CurrentCircleNameViewModel.class)
  ViewModel provideCurrentCircleNameViewModel(final CurrentCircleRepository currentCircleRepository) {
    return new CurrentCircleNameViewModel(currentCircleRepository);
  }

  @Provides
  @IntoMap
  @ClassKey(CurrentUserViewModel.class)
  ViewModel provideCurrentUserViewModel(
      final CurrentUser currentUser, final UserRepository userRepository
  ) {
    return new CurrentUserViewModel(currentUser, userRepository);
  }

  @Provides
  @IntoMap
  @ClassKey(InviteViewModel.class)
  ViewModel provideInviteViewModel(
      final CurrentCircleRepository currentCircleRepository,
      final InviteRepository inviteRepository
  ) {
    return new InviteViewModel(currentCircleRepository, inviteRepository);
  }
}
