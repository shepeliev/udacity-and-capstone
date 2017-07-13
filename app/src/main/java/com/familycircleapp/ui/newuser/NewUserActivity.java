package com.familycircleapp.ui.newuser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;

import javax.inject.Inject;

public class NewUserActivity extends AppCompatActivity {

  @Inject CurrentUser mCurrentUser;
  @Inject UserRepository mUserRepository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_user);
    App.getComponent().inject(this);

    saveDisplayName();
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
