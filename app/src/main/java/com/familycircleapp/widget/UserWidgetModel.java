package com.familycircleapp.widget;

import android.support.annotation.NonNull;

import com.familycircleapp.repository.User;

class UserWidgetModel {

  private final String mId;
  private final String mDisplayName;
  private final int mBatteryLevel;
  private final String mBatteryStatus;
  private final String mStatusText;

  private UserWidgetModel(
      final String id,
      final String displayName,
      final int batteryLevel,
      final String batteryStatus,
      final String statusText
  ) {
    mId = id;
    mDisplayName = displayName;
    mBatteryLevel = batteryLevel;
    mBatteryStatus = batteryStatus;
    mStatusText = statusText;
  }

  public static UserWidgetModel fromUser(@NonNull final User user) {
    return new UserWidgetModel(
        user.getId(),
        user.getDisplayName(),
        (int) (user.getBatteryLevel() * 1000 / 10),
        user.getBatteryStatus(),
        user.getCurrentAddress()
    );
  }

  public String getId() {
    return mId;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public int getBatteryLevel() {
    return mBatteryLevel;
  }

  public String getBatteryStatus() {
    return mBatteryStatus;
  }

  public String getStatusText() {
    return mStatusText;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final UserWidgetModel that = (UserWidgetModel) o;

    if (mBatteryLevel != that.mBatteryLevel) return false;
    if (mId != null ? !mId.equals(that.mId) : that.mId != null) return false;
    if (mDisplayName != null ? !mDisplayName.equals(that.mDisplayName) : that.mDisplayName != null)
      return false;
    if (mBatteryStatus != null ? !mBatteryStatus.equals(that.mBatteryStatus) : that.mBatteryStatus != null)
      return false;
    return mStatusText != null ? mStatusText.equals(that.mStatusText) : that.mStatusText == null;
  }

  @Override
  public int hashCode() {
    int result = mId != null ? mId.hashCode() : 0;
    result = 31 * result + (mDisplayName != null ? mDisplayName.hashCode() : 0);
    result = 31 * result + mBatteryLevel;
    result = 31 * result + (mBatteryStatus != null ? mBatteryStatus.hashCode() : 0);
    result = 31 * result + (mStatusText != null ? mStatusText.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("UserWidgetModel{");
    sb.append("mId='").append(mId).append('\'');
    sb.append(", mDisplayName='").append(mDisplayName).append('\'');
    sb.append(", mBatteryLevel=").append(mBatteryLevel);
    sb.append(", mBatteryStatus='").append(mBatteryStatus).append('\'');
    sb.append(", mStatusText='").append(mStatusText).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
