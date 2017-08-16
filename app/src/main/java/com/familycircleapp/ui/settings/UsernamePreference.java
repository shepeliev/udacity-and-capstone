package com.familycircleapp.ui.settings;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.User;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.utils.Ctx;

import javax.inject.Inject;

public class UsernamePreference extends EditTextPreference {

  private final String mUserId;

  @Inject CurrentUser mCurrentUser;
  @Inject UserRepository mUserRepository;

  private String mDisplayName;

  public UsernamePreference(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    App.getComponent().inject(this);

    mUserId = mCurrentUser.getId();
    if (mUserId == null) {
      throw new IllegalStateException("user must be authenticated");
    }

    mUserRepository.getUser(mUserId).map(User::getDisplayName).subscribe(displayName -> {
      mDisplayName = displayName;
      setSummary(displayName);
    });
  }

  @Override
  public boolean isPersistent() {
    return false;
  }

  @Override
  public String getText() {
    return mDisplayName;
  }

  @Override
  public void setText(final String text) {
    if (TextUtils.isEmpty(text)) {
      Ctx.toast(getContext(), R.string.error_name_is_empty);
    } else {
      mUserRepository.saveDisplayName(mUserId, text);
      mDisplayName = text;
      setSummary(text);
    }
  }
}
