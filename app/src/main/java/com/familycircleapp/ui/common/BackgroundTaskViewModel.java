package com.familycircleapp.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import timber.log.Timber;

abstract public class BackgroundTaskViewModel<T> extends ViewModel {

  private final MutableLiveData<Boolean> mRunning = new MutableLiveData<>();
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final MutableLiveData<T> mResult = new MutableLiveData<>();
  private final ErrorTextResolver mErrorTextResolver;

  public BackgroundTaskViewModel() {
    this(Throwable::getLocalizedMessage);
  }

  protected BackgroundTaskViewModel(final ErrorTextResolver errorTextResolver) {
    mErrorTextResolver = errorTextResolver;
    mRunning.postValue(false);
  }

  public void start() {
    mRunning.postValue(true);
    startTask();
  }

  public LiveData<T> getResult() {
    return mResult;
  }

  public LiveData<String> getError() {
    return mError;
  }

  public MutableLiveData<Boolean> getRunningState() {
    return mRunning;
  }

  protected final void fail(@NonNull final Throwable error) {
    Timber.e(error);
    mRunning.postValue(false);
    final String errorText = mErrorTextResolver.getErrorText(error);
    mError.postValue(errorText);
  }

  protected final void success(final T result) {
    mRunning.postValue(false);
    mResult.postValue(result);
  }

  protected abstract void startTask();

  public interface ErrorTextResolver {

    String getErrorText(@NonNull final Throwable error);
  }
}
