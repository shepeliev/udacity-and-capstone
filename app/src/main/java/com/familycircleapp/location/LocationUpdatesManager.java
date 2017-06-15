package com.familycircleapp.location;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

public interface LocationUpdatesManager {

  void startLocationUpdates();

  void startLocationUpdates(@NonNull final FragmentActivity activity);

  void stopLocationUpdates(@NonNull final FragmentActivity activity);
}
