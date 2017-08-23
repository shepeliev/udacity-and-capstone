package com.familycircleapp.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.DeviceLocation;
import com.familycircleapp.repository.LastKnownLocationRepository;
import com.familycircleapp.utils.Rx;

public class LastLocationViewModel extends ViewModel {

  private final LastKnownLocationRepository mLastKnownLocationRepository;
  private LiveData<DeviceLocation> mLastLocation;

  public LastLocationViewModel(final LastKnownLocationRepository lastKnownLocationRepository) {
    mLastKnownLocationRepository = lastKnownLocationRepository;
  }

  public LiveData<DeviceLocation> getLastLocation(@NonNull final String userId) {
    if (mLastLocation == null) {
      mLastLocation = Rx.liveData(mLastKnownLocationRepository.observeLastLocation(userId));
    }

    return mLastLocation;
  }
}
