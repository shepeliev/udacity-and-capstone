package com.familycircleapp.repository;

public final class DeviceLocation {

  private long mTime;
  private double mLatitude;
  private double mLongitude;
  private double mAccuracy;
  private String mAddress;
  private String mGeocoderError;

  public DeviceLocation() {
  }

  private DeviceLocation(final Builder builder) {
    mTime = builder.mTime;
    mLatitude = builder.mLatitude;
    mLongitude = builder.mLongitude;
    mAccuracy = builder.mAccuracy;
    mAddress = builder.mAddress;
    mGeocoderError = builder.mGeocoderError;
  }

  public long getTime() {
    return mTime;
  }

  public DeviceLocation setTime(final long time) {
    mTime = time;
    return this;
  }

  public double getLatitude() {
    return mLatitude;
  }

  public DeviceLocation setLatitude(final double latitude) {
    mLatitude = latitude;
    return this;
  }

  public double getLongitude() {
    return mLongitude;
  }

  public DeviceLocation setLongitude(final double longitude) {
    mLongitude = longitude;
    return this;
  }

  public double getAccuracy() {
    return mAccuracy;
  }

  public DeviceLocation setAccuracy(final double accuracy) {
    mAccuracy = accuracy;
    return this;
  }

  public String getAddress() {
    return mAddress;
  }

  public DeviceLocation setAddress(final String address) {
    mAddress = address;
    return this;
  }

  public String getGeocoderError() {
    return mGeocoderError;
  }

  public DeviceLocation setGeocoderError(final String geocoderError) {
    mGeocoderError = geocoderError;
    return this;
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
    if (mAddress != null ? !mAddress.equals(that.mAddress) : that.mAddress != null) return false;
    return mGeocoderError != null ? mGeocoderError.equals(that.mGeocoderError) : that.mGeocoderError == null;
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
    result = 31 * result + (mGeocoderError != null ? mGeocoderError.hashCode() : 0);
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
    sb.append(", mGeocoderError='").append(mGeocoderError).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private long mTime;
    private double mLatitude;
    private double mLongitude;
    private double mAccuracy;
    private String mAddress;
    private String mGeocoderError;

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

    public Builder setGeocoderError(final String geocoderError) {
      mGeocoderError = geocoderError;
      return this;
    }

    public DeviceLocation build() {
      return new DeviceLocation(this);
    }
  }
}
