package com.familycircleapp;

import android.app.Activity;
import android.arch.lifecycle.LifecycleActivity;
import android.content.Intent;
import android.os.Bundle;

import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.main.MainActivity;
import com.familycircleapp.ui.newuser.NewUserActivity;
import com.familycircleapp.utils.Ctx;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import javax.inject.Inject;

public final class EntryPointActivity extends LifecycleActivity {

  private static final int RC_LOGIN = 1;

  @Inject CurrentUser mCurrentUser;
  @Inject UserRepository mUserRepository;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getComponent().inject(this);

    if (!mCurrentUser.isAuthenticated()) {
      final Intent intent = AuthUI.getInstance().createSignInIntentBuilder().build();
      startActivityForResult(intent, RC_LOGIN);
    } else {
      Ctx.startActivity(this, MainActivity.class);
      finish();
    }
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
    if (requestCode == RC_LOGIN) {
      handleLoginResult(resultCode, data);
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  private void handleLoginResult(final int resultCode, final Intent data) {
    final IdpResponse response = IdpResponse.fromResultIntent(data);

    if (resultCode == Activity.RESULT_CANCELED || response == null) {
      finish();
      return;
    }

    switch (response.getErrorCode()) {
      case ErrorCodes.NO_NETWORK:
        Ctx.toast(this, R.string.error_login_no_internet_connection);
        break;
      case ErrorCodes.UNKNOWN_ERROR:
        Ctx.toast(this, R.string.error_login_unknown_error);
        break;
      default:
        handleSuccessLogin();
    }
  }

  private void handleSuccessLogin() {
    final String userId = mCurrentUser.getId();
    assert userId != null;
    mUserRepository.getUser(userId).observe(this, user -> {
      if (user != null) {
        Ctx.startActivity(this, MainActivity.class);
      } else {
        Ctx.startActivity(this, NewUserActivity.class);
      }
      finish();
    });
  }
}
