package com.familycircleapp;

import android.content.Context;

public final class AppComponentUtil {

  private AppComponentUtil() {
    throw new UnsupportedOperationException();
  }

  public static Component getInstance(final Context context) {
    return ((App) context.getApplicationContext()).buildAppComponent();
  }
}
