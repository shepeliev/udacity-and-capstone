package com.familycircleapp.matchers;

public final class CustomMatchers {

  private CustomMatchers() {
    throw new UnsupportedOperationException();
  }

  public static ToastMatcher toast() {
    return new ToastMatcher();
  }
}
