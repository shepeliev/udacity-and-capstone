package com.familycircleapp.ui.details;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.familycircleapp.repository.DeviceLocation;

public class LocationHistoryItem {

  private final String mAddress;
  private final double mLatitude;
  private final double mLongitude;
  private final double mAccuracy;
  private final long mStartTime;
  private final long mEndTime;

  public LocationHistoryItem(
      final String address,
      final double latitude,
      final double longitude,
      final double accuracy,
      final long startTime,
      final long endTime
  ) {
    mAddress = address;
    mLatitude = latitude;
    mLongitude = longitude;
    mAccuracy = accuracy;
    mStartTime = startTime;
    mEndTime = endTime;
  }

  public static LocationHistoryItem fromDeviceLocation(@NonNull final DeviceLocation location) {
    return new LocationHistoryItem(
        TextUtils.isEmpty(location.getAddress()) ? "" : location.getAddress(),
        location.getLatitude(),
        location.getLongitude(),
        location.getAccuracy(),
        location.getTime(),
        location.getTime()
    );
  }

  public String getAddress() {
    return mAddress;
  }

  public double getLatitude() {
    return mLatitude;
  }

  public double getLongitude() {
    return mLongitude;
  }

  public double getAccuracy() {
    return mAccuracy;
  }

  public long getStartTime() {
    return mStartTime;
  }

  public long getEndTime() {
    return mEndTime;
  }

  public int getDurationSec() {
    return (int) ((mEndTime - mStartTime) / 1000);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final LocationHistoryItem that = (LocationHistoryItem) o;

    if (Double.compare(that.mLatitude, mLatitude) != 0) return false;
    if (Double.compare(that.mLongitude, mLongitude) != 0) return false;
    if (Double.compare(that.mAccuracy, mAccuracy) != 0) return false;
    if (mStartTime != that.mStartTime) return false;
    if (mEndTime != that.mEndTime) return false;
    return mAddress != null ? mAddress.equals(that.mAddress) : that.mAddress == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = mAddress != null ? mAddress.hashCode() : 0;
    temp = Double.doubleToLongBits(mLatitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(mLongitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(mAccuracy);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (int) (mStartTime ^ (mStartTime >>> 32));
    result = 31 * result + (int) (mEndTime ^ (mEndTime >>> 32));
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("LocationHistoryItem{");
    sb.append("mAddress='").append(mAddress).append('\'');
    sb.append(", mLatitude=").append(mLatitude);
    sb.append(", mLongitude=").append(mLongitude);
    sb.append(", mAccuracy=").append(mAccuracy);
    sb.append(", mStartTime=").append(mStartTime);
    sb.append(", mEndTime=").append(mEndTime);
    sb.append('}');
    return sb.toString();
  }
}
