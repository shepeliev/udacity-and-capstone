package com.familycircleapp.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.familycircleapp.repository.DeviceLocation;
import com.familycircleapp.repository.DeviceLocationRepository;
import com.familycircleapp.utils.F;
import com.familycircleapp.utils.Rx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocationHistoryViewModel extends ViewModel {

  private static final float DISTANCE_THRESHOLD = 300;
  private static final int ZONE_OFFSET = Calendar.getInstance().get(Calendar.ZONE_OFFSET);
  private final DeviceLocationRepository mDeviceLocationRepository;
  private LiveData<Map<Long, List<LocationHistoryItem>>> mLocationHistory;

  public LocationHistoryViewModel(final DeviceLocationRepository deviceLocationRepository) {
    mDeviceLocationRepository = deviceLocationRepository;
  }

  public LiveData<Map<Long, List<LocationHistoryItem>>> getLocationHistory(@NonNull final String userId) {
    if (mLocationHistory == null) {
      mLocationHistory = loadLocationHistory(userId);
    }
    return mLocationHistory;
  }

  private LiveData<Map<Long, List<LocationHistoryItem>>> loadLocationHistory(final String userId) {
    return Rx.liveData(
        mDeviceLocationRepository.getAllLocations(userId).map(this::mapToHistory).toObservable()
    );
  }

  private Map<Long, List<LocationHistoryItem>> mapToHistory(final List<DeviceLocation> locations) {
    final List<DeviceLocation> sortedLocations = new ArrayList<>(locations);
    Collections.sort(sortedLocations, (o1, o2) -> Long.compare(o1.getTime(), o2.getTime()));

    final List<LocationHistoryItem> history = F.fold(
        sortedLocations, new ArrayList<>(), this::locationReducer
    );

    final List<LocationHistoryItem> filteredHistory =
        F.filter(history, item -> item.getDurationSec() > 0);

    final Map<Long, List<LocationHistoryItem>> groupedHistory = F.groupBy(
        filteredHistory,
        item -> {
          final long localTime = item.getEndTime() + ZONE_OFFSET;
          return localTime - localTime % DateUtils.DAY_IN_MILLIS;
        });

    final List<Long> sortedKeys = new ArrayList<>(groupedHistory.keySet());
    Collections.sort(sortedKeys, (o1, o2) -> o2.compareTo(o1));
    F.foreach(
        sortedKeys,
        key -> Collections.sort(
            groupedHistory.get(key),
            (o1, o2) -> Long.compare(o2.getEndTime(), o1.getEndTime()))
    );

    final LinkedHashMap<Long, List<LocationHistoryItem>> sortedHistory = new LinkedHashMap<>();
    F.foreach(sortedKeys, key -> {
      final LocationHistoryItem endItem = groupedHistory.get(key).get(0);
      final long normKey = endItem.getEndTime() - endItem.getEndTime() % DateUtils.DAY_IN_MILLIS;
      sortedHistory.put(normKey, groupedHistory.get(key));
    });

    return sortedHistory;
  }

  private List<LocationHistoryItem> locationReducer(
      final List<LocationHistoryItem> acc,
      final DeviceLocation location
  ) {
    if (acc.isEmpty()) {
      acc.add(LocationHistoryItem.fromDeviceLocation(location));
      return acc;
    }

    final LocationHistoryItem prevItem = acc.get(acc.size() - 1);
    float[] distanceResult = new float[1];
    Location.distanceBetween(
        prevItem.getLatitude(),
        prevItem.getLongitude(),
        location.getLatitude(),
        location.getLongitude(),
        distanceResult
    );
    float distance = distanceResult[0];

    if (distance > location.getAccuracy() && distance > DISTANCE_THRESHOLD) {
      acc.add(LocationHistoryItem.fromDeviceLocation(location));
    } else {
      acc.remove(prevItem);
      LocationHistoryItem newItem = new LocationHistoryItem(
          prevItem.getAddress(),
          prevItem.getLatitude(),
          prevItem.getLongitude(),
          prevItem.getAccuracy(),
          prevItem.getStartTime(),
          location.getTime()
      );
      acc.add(newItem);
    }

    return acc;
  }
}
