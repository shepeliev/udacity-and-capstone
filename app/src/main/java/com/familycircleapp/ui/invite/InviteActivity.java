package com.familycircleapp.ui.invite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.Invite;
import com.familycircleapp.ui.AppCompatLifecycleActivity;
import com.familycircleapp.ui.common.CurrentCircleNameViewModel;
import com.familycircleapp.utils.Ctx;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public final class InviteActivity extends AppCompatLifecycleActivity {

  @Inject ViewModelProvider.Factory mFactory;

  @BindView(R.id.tw_invite_description) TextView mInviteDescription;
  @BindView(R.id.tw_invite_code) TextView mInviteCode;
  @BindView(R.id.tw_invite_expiration) TextView mInviteExpiration;
  @BindView(R.id.loader_screen) FrameLayout mLoaderScreen;
  @BindView(R.id.btn_send_code) Button mSendButton;

  private Disposable mDisposable;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite);
    App.getComponent().inject(this);
    ButterKnife.bind(this);

    final LiveData<String> currentCircleNameLiveData =
        Transformations.map(
            ViewModelProviders
                .of(this, mFactory)
                .get(CurrentCircleNameViewModel.class)
                .getCircle(),
            Circle::getName
        );

    currentCircleNameLiveData.observe(this, circleName ->
        mInviteDescription.setText(getString(R.string.invite_description, circleName)));

    final InviteViewModel inviteViewModel = ViewModelProviders
        .of(this, mFactory)
        .get(InviteViewModel.class);

    inviteViewModel.getRunningState().observe(this, this::handleRunningState);
    inviteViewModel.getResult().observe(this, this::handleResult);
    inviteViewModel.getError().observe(this, this::handleError);

    if (savedInstanceState == null) {
      inviteViewModel.start();
    }
  }

  @Override
  protected void onDestroy() {
    if (mDisposable != null) {
      mDisposable.dispose();
      mDisposable = null;
    }

    super.onDestroy();
  }

  private void shareInviteCode(final String inviteCode) {
    final String messageText = getString(
        R.string.share_invite_code_message, formatInviteCode(inviteCode)
    );
    final Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, messageText);
    sendIntent.setType("text/plain");
    startActivity(
        Intent.createChooser(sendIntent, getString(R.string.share_invite_code_chooser_title))
    );
  }

  private void handleError(final String errorText) {
    Ctx.toast(this, errorText);
  }

  private void handleResult(final Invite invite) {
    mInviteCode.setText(formatInviteCode(invite.getId()));
    final long expirationInDays = timeSpanInDays(invite.getExpiration());
    mInviteExpiration.setText(getString(R.string.invite_expiration, expirationInDays));

    if (mDisposable != null) {
      mDisposable.dispose();
    }
    mDisposable = RxView.clicks(mSendButton).subscribe(o -> shareInviteCode(invite.getId()));
  }

  private void handleRunningState(final boolean isRunning) {
    mLoaderScreen.setVisibility(isRunning ? View.VISIBLE : View.GONE);
    mSendButton.setEnabled(!isRunning);
  }

  private long timeSpanInDays(final long time) {
    return (time - System.currentTimeMillis()) / DateUtils.DAY_IN_MILLIS + 1;
  }

  private String formatInviteCode(final String inviteCode) {
    return getString(
        R.string.invite_code_template,
        inviteCode.substring(0, inviteCode.length() / 2),
        inviteCode.substring(inviteCode.length() / 2)
    );
  }
}
