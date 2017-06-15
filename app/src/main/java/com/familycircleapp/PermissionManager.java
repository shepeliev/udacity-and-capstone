package com.familycircleapp;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.familycircleapp.utils.Consumer;

public interface PermissionManager {

  void checkSelfPermission(
      @NonNull final String permission, @NonNull final Consumer<Boolean> onResult
  );

  void requestPermission(
      @NonNull final Activity activity,
      @NonNull final String permission,
      final int requestCode,
      @NonNull final Runnable onGranted
  );
}
