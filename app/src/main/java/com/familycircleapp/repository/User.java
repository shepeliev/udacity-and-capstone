package com.familycircleapp.repository;

import java.util.Map;

public final class User implements HasId {

  private String mId;
  private String mDisplayName;
  private double mBatteryLevel;
  private String mBatteryStatus;
  private String mCurrentAddress;
  private String mCurrentCircle;
  private Map<String, Boolean> mCircles;

  public User() {
  }

  public User(final String id) {
    mId = id;
  }

  @Override
  public String getId() {
    return mId;
  }

  @Override
  public void setId(final String id) {
    mId = id;
  }

  public String getDisplayName() {
    return mDisplayName;
  }


  public void setDisplayName(final String displayName) {
    mDisplayName = displayName;
  }

  public double getBatteryLevel() {
    return mBatteryLevel;
  }

  public void setBatteryLevel(final double batteryLevel) {
    mBatteryLevel = batteryLevel;
  }

  public String getBatteryStatus() {
    return mBatteryStatus;
  }

  public void setBatteryStatus(final String batteryStatus) {
    mBatteryStatus = batteryStatus;
  }

  public String getCurrentAddress() {
    return mCurrentAddress;
  }

  public void setCurrentAddress(final String currentAddress) {
    mCurrentAddress = currentAddress;
  }

  public String getCurrentCircle() {
    return mCurrentCircle;
  }

  public void setCurrentCircle(final String currentCircle) {
    mCurrentCircle = currentCircle;
  }

  public Map<String, Boolean> getCircles() {
    return mCircles;
  }

  public void setCircles(final Map<String, Boolean> circles) {
    mCircles = circles;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final User user = (User) o;

    if (Double.compare(user.mBatteryLevel, mBatteryLevel) != 0) return false;
    if (mId != null ? !mId.equals(user.mId) : user.mId != null) return false;
    if (mDisplayName != null ? !mDisplayName.equals(user.mDisplayName) : user.mDisplayName != null)
      return false;
    if (mBatteryStatus != null ? !mBatteryStatus.equals(user.mBatteryStatus) : user.mBatteryStatus != null)
      return false;
    if (mCurrentAddress != null ? !mCurrentAddress.equals(user.mCurrentAddress) : user.mCurrentAddress != null)
      return false;
    if (mCurrentCircle != null ? !mCurrentCircle.equals(user.mCurrentCircle) : user.mCurrentCircle != null)
      return false;
    return mCircles != null ? mCircles.equals(user.mCircles) : user.mCircles == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = mId != null ? mId.hashCode() : 0;
    result = 31 * result + (mDisplayName != null ? mDisplayName.hashCode() : 0);
    temp = Double.doubleToLongBits(mBatteryLevel);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (mBatteryStatus != null ? mBatteryStatus.hashCode() : 0);
    result = 31 * result + (mCurrentAddress != null ? mCurrentAddress.hashCode() : 0);
    result = 31 * result + (mCurrentCircle != null ? mCurrentCircle.hashCode() : 0);
    result = 31 * result + (mCircles != null ? mCircles.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("User{");
    sb.append("mId='").append(mId).append('\'');
    sb.append(", mDisplayName='").append(mDisplayName).append('\'');
    sb.append(", mBatteryLevel=").append(mBatteryLevel);
    sb.append(", mBatteryStatus='").append(mBatteryStatus).append('\'');
    sb.append(", mCurrentAddress='").append(mCurrentAddress).append('\'');
    sb.append(", mCurrentCircle='").append(mCurrentCircle).append('\'');
    sb.append(", mCircles=").append(mCircles);
    sb.append('}');
    return sb.toString();
  }
}
