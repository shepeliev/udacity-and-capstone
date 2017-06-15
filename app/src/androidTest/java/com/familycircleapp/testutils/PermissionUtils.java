package com.familycircleapp.testutils;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import timber.log.Timber;

public final class PermissionUtils {

  private PermissionUtils() {
    throw new UnsupportedOperationException();
  }

  public static void allowPermissionsIfNeeded() {
    if (Build.VERSION.SDK_INT >= 23) {
      UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
      UiObject allowPermissions = device.findObject(new UiSelector().text("ALLOW"));
      if (allowPermissions.exists()) {
        try {
          allowPermissions.click();
        } catch (UiObjectNotFoundException e) {
          Timber.e(e, "There is no permissions dialog to interact with.");
        }
      } else {
        Timber.w("There is no ALLOW button on the screen.");
      }
    }
  }
}
