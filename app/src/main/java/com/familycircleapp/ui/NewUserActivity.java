package com.familycircleapp.ui;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.common.CreateCircleViewModel;
import com.familycircleapp.ui.common.JoinCircleViewModel;
import com.familycircleapp.ui.main.MainActivity;
import com.familycircleapp.utils.Ctx;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class NewUserActivity extends LifecycleActivity {

  @Inject CurrentUser mCurrentUser;
  @Inject UserRepository mUserRepository;
  @Inject ViewModelProvider.Factory mViewModelFactory;

  @BindView(R.id.loader_screen) FrameLayout mLoaderScreen;
  @BindView(R.id.btn_join_to_circle) Button mJoinCircleButton;
  @BindView(R.id.btn_create_new_circle) Button mCreateCircleButton;
  @BindView(R.id.et_invite_code) EditText mInviteCodeEditText;

  private CompositeDisposable mCompositeDisposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_user);
    ButterKnife.bind(this);

    App.getComponent().inject(this);

    final ViewModelProvider provider = ViewModelProviders.of(this, mViewModelFactory);
    final JoinCircleViewModel joinCircleViewModel = provider.get(JoinCircleViewModel.class);
    final CreateCircleViewModel createCircleViewModel = provider.get(CreateCircleViewModel.class);

    joinCircleViewModel.getRunningState().observe(this, this::handleRunningState);
    joinCircleViewModel.getResult().observe(this, this::handleSuccessResult);
    joinCircleViewModel.getError().observe(this, this::handleFailResult);

    createCircleViewModel.getRunningState().observe(this, this::handleRunningState);
    createCircleViewModel.getResult().observe(this, this::handleSuccessResult);
    createCircleViewModel.getError().observe(this, this::handleFailResult);
    createCircleViewModel.setCircleName(getString(R.string.family_circle_name));

    mCompositeDisposable = new CompositeDisposable(
        RxView.clicks(mJoinCircleButton).subscribe(o -> joinCircleViewModel.start()),
        RxView.clicks(mCreateCircleButton).subscribe(o -> createCircleViewModel.start()),

        inviteCodeChanges()
            .map(code -> !TextUtils.isEmpty(code))
            .subscribe(mJoinCircleButton::setEnabled),

        inviteCodeChanges().subscribe(joinCircleViewModel::setInviteCode)
    );

    if (savedInstanceState == null) {
      saveDisplayName();
    }
  }

  @Override
  protected void onDestroy() {
    if (mCompositeDisposable != null) {
      mCompositeDisposable.dispose();
      mCompositeDisposable = null;
    }

    super.onDestroy();
  }

  private Observable<String> inviteCodeChanges() {
    return RxTextView
        .afterTextChangeEvents(mInviteCodeEditText)
        .map(event -> event.view().getText().toString());
  }

  private void handleRunningState(final boolean isRunning) {
    mLoaderScreen.setVisibility(isRunning ? View.VISIBLE : View.GONE);
    mJoinCircleButton.setEnabled(!isRunning && !TextUtils.isEmpty(mInviteCodeEditText.getText()));
    mCreateCircleButton.setEnabled(!isRunning);
    mInviteCodeEditText.setEnabled(!isRunning);
  }

  private void handleFailResult(final String errorText) {
    if (!TextUtils.isEmpty(errorText)) {
      Ctx.toast(this, errorText);
    }
  }

  private void handleSuccessResult(@SuppressWarnings("unused") final String result) {
    Ctx.startActivity(this, MainActivity.class);
    finish();
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
