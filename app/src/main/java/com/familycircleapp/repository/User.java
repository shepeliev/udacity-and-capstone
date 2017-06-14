package com.familycircleapp.repository;

public final class User {

  private String mId;
  private String mDisplayName;
  private double mBatteryLevel;
  private String mBatteryStatus;
  private String mCurrentAddress;
  private String mCurrentCircle;

  public String getId() {
    return mId;
  }

  public User setId(final String id) {
    mId = id;
    return this;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public User setDisplayName(final String displayName) {
    mDisplayName = displayName;
    return this;
  }

  public double getBatteryLevel() {
    return mBatteryLevel;
  }

  public User setBatteryLevel(final double batteryLevel) {
    mBatteryLevel = batteryLevel;
    return this;
  }

  public String getBatteryStatus() {
    return mBatteryStatus;
  }

  public User setBatteryStatus(final String batteryStatus) {
    mBatteryStatus = batteryStatus;
    return this;
  }

  public String getCurrentAddress() {
    return mCurrentAddress;
  }

  public User setCurrentAddress(final String currentAddress) {
    mCurrentAddress = currentAddress;
    return this;
  }

  public String getCurrentCircle() {
    return mCurrentCircle;
  }

  public User setCurrentCircle(final String currentCircle) {
    mCurrentCircle = currentCircle;
    return this;
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
    return mCurrentCircle != null ? mCurrentCircle.equals(user.mCurrentCircle) : user.mCurrentCircle == null;
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
    sb.append('}');
    return sb.toString();
  }
}