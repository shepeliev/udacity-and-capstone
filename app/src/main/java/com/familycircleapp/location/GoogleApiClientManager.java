package com.familycircleapp.location;

import com.google.android.gms.common.api.GoogleApiClient;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

public interface GoogleApiClientManager {

  void connect(@NonNull final OnGoogleApiConnectedListener onGoogleApiConnectedListener);

  void connect(
      @NonNull final FragmentActivity activity,
      @NonNull final OnGoogleApiConnectedListener onGoogleApiConnectedListener
  );

  GoogleApiClient getGoogleApiClient();

  void disconnect();

  interface OnGoogleApiConnectedListener {
    void onConnected();
  }
}
