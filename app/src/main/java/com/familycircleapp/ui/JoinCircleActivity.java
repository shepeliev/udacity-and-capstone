package com.familycircleapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.ui.common.JoinCircleViewModel;
import com.familycircleapp.utils.Ctx;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class JoinCircleActivity extends AppCompatLifecycleActivity {

  @Inject ViewModelProvider.Factory mViewModelFactory;

  @BindView(R.id.loader_screen) FrameLayout mLoaderScreen;
  @BindView(R.id.btn_join_to_circle) Button mJoinCircleButton;
  @BindView(R.id.et_invite_code) EditText mInviteCodeEditText;

  private CompositeDisposable mCompositeDisposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_join_circle);
    ButterKnife.bind(this);

    App.getComponent().inject(this);

    final JoinCircleViewModel joinCircleViewModel = ViewModelProviders
        .of(this, mViewModelFactory)
        .get(JoinCircleViewModel.class);

    joinCircleViewModel.getRunningState().observe(this, this::handleRunningState);
    joinCircleViewModel.getResult().observe(this, this::handleSuccessResult);
    joinCircleViewModel.getError().observe(this, this::handleFailResult);

    mCompositeDisposable = new CompositeDisposable(
        RxView.clicks(mJoinCircleButton).subscribe(o -> joinCircleViewModel.start()),

        inviteCodeChanges()
            .map(code -> !TextUtils.isEmpty(code))
            .subscribe(mJoinCircleButton::setEnabled),

        inviteCodeChanges().subscribe(joinCircleViewModel::setInviteCode)

    );
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
    mInviteCodeEditText.setEnabled(!isRunning);
  }

  private void handleFailResult(final String errorText) {
    if (!TextUtils.isEmpty(errorText)) {
      Ctx.toast(this, errorText);
    }
  }

  private void handleSuccessResult(@SuppressWarnings("unused") final String result) {
    NavUtils.navigateUpFromSameTask(this);
  }
}
