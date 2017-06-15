package com.familycircleapp.location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.familycircleapp.R;

import timber.log.Timber;

final class GoogleApiClientManagerImpl implements GoogleApiClientManager,
    GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

  private final Context mContext;
  private GoogleApiClient mGoogleApiClient;

  private FragmentActivity mActivity;
  private OnGoogleApiConnectedListener mOnGoogleApiConnectedListener;

  GoogleApiClientManagerImpl(final Context context) {
    mContext = context;
  }

  @Override
  public void connect(
      @NonNull final OnGoogleApiConnectedListener onGoogleApiConnectedListener
  ) {
    mOnGoogleApiConnectedListener = onGoogleApiConnectedListener;
    mGoogleApiClient = new GoogleApiClient.Builder(mContext)
        .addOnConnectionFailedListener(this)
        .addConnectionCallbacks(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override
  public void connect(
      @NonNull final FragmentActivity activity,
      @NonNull final OnGoogleApiConnectedListener onGoogleApiConnectedListener
  ) {
    mActivity = activity;
    mOnGoogleApiConnectedListener = onGoogleApiConnectedListener;
    mGoogleApiClient = new GoogleApiClient.Builder(mContext)
        .enableAutoManage(mActivity, this)
        .addConnectionCallbacks(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override
  public GoogleApiClient getGoogleApiClient() {
    return mGoogleApiClient;
  }

  @Override
  public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
    Timber.e("Connection failed code: {}, {}",
        connectionResult.getErrorCode(), connectionResult.getErrorMessage());
  }

  @Override
  public void onConnected(@Nullable final Bundle connectionHint) {
    mOnGoogleApiConnectedListener.onConnected();
  }

  @Override
  public void onConnectionSuspended(final int cause) {
    Timber.e(getCauseText(cause));
  }

  @Override
  public void disconnect() {
    if (mActivity != null) {
      mGoogleApiClient.stopAutoManage(mActivity);
    }
    mGoogleApiClient.disconnect();
    mGoogleApiClient = null;
    mActivity = null;
  }

  private String getCauseText(final int cause) {
    String causeString;
    switch (cause) {
      case GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST:
        causeString = mContext.getString(R.string.error_google_services_connection_lost);
        break;
      case GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED:
        causeString = mContext.getString(R.string.error_google_services_service_disconnected);
        break;
      default:
        causeString = mContext.getString(R.string.error_google_services_unknown);
    }

    return mContext.getString(R.string.error_google_services_connection_suspended, causeString);
  }
}
