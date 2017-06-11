package com.familycircleapp.matchers;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class ToastMatcher extends TypeSafeMatcher<Root> {

  @Override
  protected boolean matchesSafely(final Root root) {
    final int type = root.getWindowLayoutParams().get().type;
    if (type == WindowManager.LayoutParams.TYPE_TOAST) {
      final IBinder windowToken = root.getDecorView().getWindowToken();
      final IBinder appToken = root.getDecorView().getApplicationWindowToken();
      return appToken == windowToken;
    }

    return false;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("is toast");
  }
}
