package com.familycircleapp.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import timber.log.Timber;

abstract public class BackgroundTaskViewModel<T> extends ViewModel {

  private final MutableLiveData<Boolean> mRunning = new MutableLiveData<>();
  private final ObservableBoolean mIsRunning = new ObservableBoolean(false);
  private final MutableLiveData<String> mError = new MutableLiveData<>();
  private final ObservableField<String> mErrorText = new ObservableField<>("");
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
    mIsRunning.set(true);
    mRunning.postValue(true);
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

  public MutableLiveData<Boolean> getRunningState() {
    return mRunning;
  }

  public ObservableField<String> getErrorText() {
    return mErrorText;
  }

  protected final void fail(@NonNull final Throwable error) {
    Timber.e(error);
    mIsRunning.set(false);
    mRunning.postValue(false);
    final String errorText = mErrorTextResolver.getErrorText(error);
    mErrorText.set(errorText);
    mError.postValue(errorText);
  }

  protected final void success(final T result) {
    mIsRunning.set(false);
    mRunning.postValue(false);
    mResult.postValue(result);
  }

  protected abstract void startTask();

  public interface ErrorTextResolver {

    String getErrorText(@NonNull final Throwable error);
  }
}
