package com.familycircleapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.widget.Toast;

public final class Utils {

  private Utils() {
    throw new UnsupportedOperationException();
  }

  public static void startActivity(final Context context, final Class<?> clazz) {
    context.startActivity(new Intent(context, clazz));
  }

  public static void toast(final Context context, @StringRes final int resId) {
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
  }
}