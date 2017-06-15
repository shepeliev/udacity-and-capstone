package com.familycircleapp.battery;

import android.content.Intent;
import android.os.BatteryManager;
import android.support.annotation.NonNull;

import com.familycircleapp.utils.F;

import java.util.Arrays;
import java.util.Map;

public class BatteryInfo {

  private static final String STATUS_CHARGING = "charging";
  private static final String STATUS_DISCHARGING = "discharging";
  private static final String STATUS_FULL = "full";
  private static final String STATUS_NOT_CHARGING = "not_charging";
  private static final String STATUS_UNKNOWN = "unknown";

  private final double mLevel;
  private final String mStatus;

  public BatteryInfo(final double level, final String status) {
    mLevel = level;
    mStatus = status;
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


  public double getLevel() {
    return mLevel;
  }

  public String getStatus() {
    return mStatus;
  }

  public Map<String, Object> asMap() {
    return F.mapOf(Arrays.asList(
        F.mapEntry("batteryLevel", mLevel),
        F.mapEntry("batteryStatus", mStatus)
    ));
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final BatteryInfo that = (BatteryInfo) o;

    if (Double.compare(that.mLevel, mLevel) != 0) return false;
    return mStatus != null ? mStatus.equals(that.mStatus) : that.mStatus == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(mLevel);
    result = (int) (temp ^ (temp >>> 32));
    result = 31 * result + (mStatus != null ? mStatus.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("BatteryInfo{");
    sb.append("mLevel=").append(mLevel);
    sb.append(", mStatus='").append(mStatus).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
