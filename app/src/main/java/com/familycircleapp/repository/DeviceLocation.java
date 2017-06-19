package com.familycircleapp.repository;

public final class DeviceLocation {

  private long mTime;
  private double mLatitude;
  private double mLongitude;
  private double mAccuracy;
  private String mAddress;

  public DeviceLocation() {
  }

  private DeviceLocation(final Builder builder) {
    mTime = builder.mTime;
    mLatitude = builder.mLatitude;
    mLongitude = builder.mLongitude;
    mAccuracy = builder.mAccuracy;
    mAddress = builder.mAddress;
  }

  public long getTime() {
    return mTime;
  }

  public void setTime(final long time) {
    mTime = time;
  }

  public double getLatitude() {
    return mLatitude;
  }

  public void setLatitude(final double latitude) {
    mLatitude = latitude;
  }

  public double getLongitude() {
    return mLongitude;
  }

  public void setLongitude(final double longitude) {
    mLongitude = longitude;
  }

  public double getAccuracy() {
    return mAccuracy;
  }

  public void setAccuracy(final double accuracy) {
    mAccuracy = accuracy;
  }

  public String getAddress() {
    return mAddress;
  }

  public void setAddress(final String address) {
    mAddress = address;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final DeviceLocation that = (DeviceLocation) o;

    if (mTime != that.mTime) return false;
    if (Double.compare(that.mLatitude, mLatitude) != 0) return false;
    if (Double.compare(that.mLongitude, mLongitude) != 0) return false;
    if (Double.compare(that.mAccuracy, mAccuracy) != 0) return false;
    return mAddress != null ? mAddress.equals(that.mAddress) : that.mAddress == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = (int) (mTime ^ (mTime >>> 32));
    temp = Double.doubleToLongBits(mLatitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(mLongitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(mAccuracy);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (mAddress != null ? mAddress.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Location{");
    sb.append("mTime=").append(mTime);
    sb.append(", mLatitude=").append(mLatitude);
    sb.append(", mLongitude=").append(mLongitude);
    sb.append(", mAccuracy=").append(mAccuracy);
    sb.append(", mAddress='").append(mAddress).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private long mTime;
    private double mLatitude;
    private double mLongitude;
    private double mAccuracy;
    private String mAddress;

    public Builder setTime(final long time) {
      mTime = time;
      return this;
    }

    public Builder setLatitude(final double latitude) {
      mLatitude = latitude;
      return this;
    }

    public Builder setLongitude(final double longitude) {
      mLongitude = longitude;
      return this;
    }

    public Builder setAccuracy(final double accuracy) {
      mAccuracy = accuracy;
      return this;
    }

    public Builder setAddress(final String address) {
      mAddress = address;
      return this;
    }

    public DeviceLocation build() {
      return new DeviceLocation(this);
    }
  }
}
