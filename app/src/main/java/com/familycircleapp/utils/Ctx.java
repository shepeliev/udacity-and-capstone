package com.familycircleapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

  public static void checkSelfPermission(
      @NonNull final Context context,
      @NonNull final String permission,
      @NonNull final Consumer<Boolean> onResult) {
    final int selfPermission = ContextCompat.checkSelfPermission(context, permission);
    onResult.accept(selfPermission == PackageManager.PERMISSION_GRANTED);
  }

  public static void requestPermission(
      @NonNull final Activity activity,
      @NonNull final String permission,
      final int requestCode,
      @NonNull final Consumer<Boolean> onResult
  ) {
    checkSelfPermission(activity, permission, isGranted -> {
      if (isGranted) {
        onResult.accept(true);
      } else {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
      }
    });
  }
}
