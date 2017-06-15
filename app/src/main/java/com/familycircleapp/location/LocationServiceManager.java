package com.familycircleapp.location;

import android.app.Activity;
import android.support.annotation.NonNull;

public interface LocationServiceManager {

  void startLocationUpdates();

  void startLocationUpdates(@NonNull final Activity activity);

  void stopLocationUpdates(@NonNull final Activity activity);
}
