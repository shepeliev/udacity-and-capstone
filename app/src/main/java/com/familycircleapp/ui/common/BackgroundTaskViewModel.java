package com.familycircleapp.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

abstract public class BackgroundTaskViewModel<T> extends ViewModel {

  private final ObservableBoolean mIsRunning = new ObservableBoolean(false);
  private final ObservableField<String> mErrorText = new ObservableField<>("");
  private final MutableLiveData<T> mResult = new MutableLiveData<>();
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final ErrorTextResolver mErrorTextResolver;

  public BackgroundTaskViewModel() {
    this(Throwable::getLocalizedMessage);
  }

  protected BackgroundTaskViewModel(final ErrorTextResolver errorTextResolver) {
    mErrorTextResolver = errorTextResolver;
  }

  public void start() {
    mIsRunning.set(true);
    mErrorText.set("");
    startTask();
  }

  public LiveData<T> getResult() {
    return mResult;
  }

  public LiveData<String> getError() {
    return mError;
  }

  public ObservableBoolean isRunning() {
    return mIsRunning;
  }

  public ObservableField<String> getErrorText() {
    return mErrorText;
  }

  protected final void fail(@NonNull final Throwable error) {
    mIsRunning.set(false);
    final String errorText = mErrorTextResolver.getErrorText(error);
    mErrorText.set(errorText);
    mError.postValue(errorText);
  }

  protected final void success(final T result) {
    mIsRunning.set(false);
    mResult.postValue(result);
  }

  protected abstract void startTask();

  public interface ErrorTextResolver {

    String getErrorText(@NonNull final Throwable error);
  }
}