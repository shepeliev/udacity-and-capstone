package com.familycircleapp;

public final  class TestApp extends App {

  private static MockComponent sMockComponent;

  public static MockComponent getComponent() {
    return (MockComponent) App.getComponent();
  }

  @Override
  public MockComponent buildAppComponent() {
    return DaggerMockComponent.builder().build();
  }
}
