package com.familycircleapp.ui.common;

import com.google.firebase.database.DatabaseException;

import android.content.Context;
import android.support.annotation.NonNull;

import com.familycircleapp.R;

import timber.log.Timber;

public final class JoinCircleErrorTextResolver
    implements BackgroundTaskViewModel.ErrorTextResolver {

  private final Context mContext;

  public JoinCircleErrorTextResolver(final Context context) {
    mContext = context;
  }

  @Override
  public String getErrorText(@NonNull final Throwable error) {
    if (error instanceof InviteCodeNotFound) {
      return mContext.getString(R.string.error_invite_code_not_found);
    } else if (error instanceof InviteCodeExpired) {
      return mContext.getString(R.string.error_invite_code_expired);
    } else if (error instanceof DatabaseException) {
      return mContext.getString(R.string.error_database, error.getLocalizedMessage());
    } else {
      Timber.wtf("Unexpected error here", error);
      return error.getLocalizedMessage();
    }
  }

  public static class InviteCodeNotFound extends RuntimeException {}
  public static class InviteCodeExpired extends RuntimeException {}
}