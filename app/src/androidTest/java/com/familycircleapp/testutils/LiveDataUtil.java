package com.familycircleapp.testutils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class LiveDataUtil {

  private LiveDataUtil() {
    throw new UnsupportedOperationException();
  }

  public static <T> T getValue(final LiveData<T> liveData) {
    final Object[] data = new Object[1];
    final CountDownLatch latch = new CountDownLatch(1);
    final Observer<T> observer = new Observer<T>() {
      @Override
      public void onChanged(@Nullable T o) {
        data[0] = o;
        latch.countDown();
        liveData.removeObserver(this);
      }
    };
    liveData.observeForever(observer);
    try {
      latch.await(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    //noinspection unchecked
    return (T) data[0];
  }
}
