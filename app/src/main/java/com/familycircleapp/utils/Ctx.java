package com.familycircleapp.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

public final class Ctx {

  private Ctx() {
    throw new UnsupportedOperationException();
  }

  public static void startActivity(@NonNull  final Context context, @NonNull final Class<?> clazz) {
    context.startActivity(new Intent(context, clazz));
  }

  public static void toast(@NonNull final Context context, @StringRes final int resId) {
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
  }

  public static void toast(@NonNull final Context context, @NonNull final String text) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
  }
}
