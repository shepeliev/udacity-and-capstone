package com.familycircleapp.ui.common;

import android.content.Context;
import android.support.annotation.NonNull;

import com.familycircleapp.R;
import com.google.firebase.database.DatabaseException;

import timber.log.Timber;

public final class CreateCircleErrorTextResolver
    implements BackgroundTaskViewModel.ErrorTextResolver {

  private final Context mContext;

  public CreateCircleErrorTextResolver(final Context context) {
    mContext = context;
  }

  @Override
  public String getErrorText(@NonNull final Throwable error) {
    if (error instanceof DatabaseException) {
      return mContext.getString(R.string.error_database, error.getLocalizedMessage());
    } else {
      Timber.wtf("Unexpected error here %s", error);
      return error.getLocalizedMessage();
    }
  }
}

