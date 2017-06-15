package com.familycircleapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.familycircleapp.utils.Consumer;

class PermissionManagerImpl implements PermissionManager {

  private final Context mContext;

  PermissionManagerImpl(final Context context) {
    mContext = context;
  }

  @Override
  public void checkSelfPermission(
      @NonNull final String permission, @NonNull final Consumer<Boolean> onResult
  ) {
    final int selfPermission = ContextCompat.checkSelfPermission(mContext, permission);
    onResult.accept(selfPermission == PackageManager.PERMISSION_GRANTED);
  }

  @Override
  public void requestPermission(
      @NonNull final Activity activity,
      @NonNull final String permission,
      final int requestCode,
      @NonNull final Runnable onGranted
  ) {
    checkSelfPermission(permission, isGranted -> {
      if (isGranted) {
        onGranted.run();
      } else {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
      }
    });
  }
}
