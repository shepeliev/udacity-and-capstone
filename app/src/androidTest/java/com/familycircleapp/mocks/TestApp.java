package com.familycircleapp.mocks;

import com.familycircleapp.App;

public class TestApp extends App {

  private static MockComponent sMockComponent;

  public static MockComponent getComponent() {
    return (MockComponent) App.getComponent();
  }

  @Override
  public MockComponent buildAppComponent() {
    return DaggerMockComponent.builder().build();
  }
}
