package com.familycircleapp.ui.newuser;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.databinding.ActivityNewUserBinding;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.main.MainActivity;
import com.familycircleapp.utils.Ctx;

import javax.inject.Inject;

public class NewUserActivity extends LifecycleActivity {

  @Inject CurrentUser mCurrentUser;
  @Inject UserRepository mUserRepository;
  @Inject ViewModelProvider.Factory mViewModelFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final ActivityNewUserBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_new_user);

    App.getComponent().inject(this);


    final ViewModelProvider provider = ViewModelProviders.of(this, mViewModelFactory);
    final JoinCircleViewModel joinCircleViewModel = provider.get(JoinCircleViewModel.class);
    final CreateCircleViewModel createCircleViewModel = provider.get(CreateCircleViewModel.class);

    binding.setJoinCircleViewModel(joinCircleViewModel);
    binding.setCreateCircleViewModel(createCircleViewModel);

    joinCircleViewModel.getResult().observe(this, this::handleCreateJoinCircleResult);
    createCircleViewModel.getResult().observe(this, this::handleCreateJoinCircleResult);

    if (savedInstanceState == null) {
      saveDisplayName();
    }
  }

  private void handleCreateJoinCircleResult(final CreateJoinCircleResult result) {
    result.onSuccess(() -> {
      Ctx.startActivity(this, MainActivity.class);
      finish();
    });
    result.onFail(errorResId -> Ctx.toast(this, errorResId));
  }

  private void saveDisplayName() {
    final String userId = mCurrentUser.getId();
    assert userId != null;

    final String displayName = mCurrentUser.getDisplayName() != null
        ? mCurrentUser.getDisplayName()
        : getString(R.string.na);

    mUserRepository.saveDisplayName(userId, displayName);
  }
}
