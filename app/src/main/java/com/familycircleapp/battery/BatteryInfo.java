package com.familycircleapp.battery;

import android.content.Intent;
import android.os.BatteryManager;
import android.support.annotation.NonNull;

public class BatteryInfo {

  private static final String STATUS_CHARGING = "charging";
  private static final String STATUS_DISCHARGING = "discharging";
  private static final String STATUS_FULL = "full";
  private static final String STATUS_NOT_CHARGING = "not_charging";
  private static final String STATUS_UNKNOWN = "unknown";

  private final double mBatteryLevel;
  private final String mBatteryStatus;

  BatteryInfo(final double batteryLevel, final String batteryStatus) {
    mBatteryLevel = batteryLevel;
    mBatteryStatus = batteryStatus;
  }

  public static BatteryInfo fromIntent(@NonNull final Intent intent) {
    return new BatteryInfo(getBatteryLevel(intent), getBatteryStatus(intent));
  }

  public static double getBatteryLevel(final Intent intent) {
    final double level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    final double scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    return level / scale;
  }

  public static String getBatteryStatus(final Intent intent) {
    final int status = intent.getIntExtra(
        BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN
    );

    switch (status) {
      case BatteryManager.BATTERY_STATUS_CHARGING:
        return STATUS_CHARGING;
      case BatteryManager.BATTERY_STATUS_DISCHARGING:
        return STATUS_DISCHARGING;
      case BatteryManager.BATTERY_STATUS_FULL:
        return STATUS_FULL;
      case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
        return STATUS_NOT_CHARGING;
      case BatteryManager.BATTERY_STATUS_UNKNOWN:
        return STATUS_UNKNOWN;
      default:
        return STATUS_UNKNOWN;
    }
  }


  public double getBatteryLevel() {
    return mBatteryLevel;
  }

  public String getBatteryStatus() {
    return mBatteryStatus;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final BatteryInfo that = (BatteryInfo) o;

    if (Double.compare(that.mBatteryLevel, mBatteryLevel) != 0) return false;
    return mBatteryStatus != null ? mBatteryStatus.equals(that.mBatteryStatus) : that.mBatteryStatus == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(mBatteryLevel);
    result = (int) (temp ^ (temp >>> 32));
    result = 31 * result + (mBatteryStatus != null ? mBatteryStatus.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("BatteryInfo{");
    sb.append("mBatteryLevel=").append(mBatteryLevel);
    sb.append(", mBatteryStatus='").append(mBatteryStatus).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
