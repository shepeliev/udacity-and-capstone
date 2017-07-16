package com.familycircleapp.ui.common;

import android.annotation.SuppressLint;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.testutils.LiveDataUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressLint("StaticFieldLeak")
@RunWith(AndroidJUnit4.class)
public class BackgroundTaskViewModelTest {

  @Rule
  public UiThreadTestRule mUiThread = new UiThreadTestRule();

  @Test
  public void onConstruct_isRunning_shouldBeFalse() throws Exception {
    final BackgroundTaskViewModel<Void> model = new BackgroundTaskViewModel<Void>() {
      @Override
      protected void startTask() {
      }
    };

    assertFalse(model.isRunning().get());
  }

  @Test
  public void onConstruct_errorText_shouldBeEmpty() throws Exception {
    final BackgroundTaskViewModel<Void> model = new BackgroundTaskViewModel<Void>() {
      @Override
      protected void startTask() {
      }
    };

    assertEquals("", model.getErrorText().get());
  }

  @Test
  public void start_shouldSetIsRunning_toTrue() throws Exception {
    final BackgroundTaskViewModel<Void> model = new BackgroundTaskViewModel<Void>() {
      @Override
      protected void startTask() {
      }
    };

    model.start();

    assertTrue(model.isRunning().get());
  }

  @Test
  public void start_shouldClearErrorText() throws Exception {
    final BackgroundTaskViewModel<Void> model = new BackgroundTaskViewModel<Void>() {
      @Override
      protected void startTask() {
      }
    };
    model.getErrorText().set("error");

    model.start();

    assertEquals("", model.getErrorText().get());
  }

  @Test
  public void fail_shouldSetIsRunning_toFalse() throws Exception {
    final BackgroundTaskViewModel<Void> model = new BackgroundTaskViewModel<Void>() {
      @Override
      protected void startTask() {
      }
    };
    model.start();

    model.fail(new RuntimeException());

    assertFalse(model.isRunning().get());
  }

  @Test
  public void fail_shouldSetErrorText() throws Exception {
    final BackgroundTaskViewModel<Void> model = new BackgroundTaskViewModel<Void>() {
      @Override
      protected void startTask() {
      }
    };
    model.start();

    model.fail(new RuntimeException("error"));

    assertEquals("error", model.getErrorText().get());
  }

  @Test
  public void fail_shouldUpdateErrorLiveData() throws Throwable {
    final BackgroundTaskViewModel<Void> model = new BackgroundTaskViewModel<Void>() {
      @Override
      protected void startTask() {
      }
    };
    model.start();

    model.fail(new RuntimeException("error"));

    final String[] errorText = {null};
    mUiThread.runOnUiThread(() -> errorText[0] = LiveDataUtil.getValue(model.getError()));
    assertEquals("error", errorText[0]);
  }

  @Test
  public void success_shouldSetIsRunning_toFalse() throws Exception {
    final BackgroundTaskViewModel<String> model = new BackgroundTaskViewModel<String>() {
      @Override
      protected void startTask() {
      }
    };
    model.start();

    model.success("success");

    assertFalse(model.isRunning().get());
  }

  @Test
  public void success_shouldUpdateResultLiveData() throws Throwable {
    final BackgroundTaskViewModel<String> model = new BackgroundTaskViewModel<String>() {
      @Override
      protected void startTask() {
      }
    };
    model.start();

    model.success("success");

    final String[] result = {null};
    mUiThread.runOnUiThread(() -> result[0] = LiveDataUtil.getValue(model.getResult()));
    assertEquals("success", result[0]);
  }
}
