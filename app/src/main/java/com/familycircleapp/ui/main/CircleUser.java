package com.familycircleapp.ui.main;

import com.familycircleapp.R;
import com.familycircleapp.repository.User;

public class CircleUser {

  private final String mDisplayName;
  private final int mBatteryLevel;
  private final String mBatteryStatus;
  private final int mStatusTemplate;
  private final String mStatusText;

  public CircleUser(
      final String displayName,
      final int batteryLevel,
      final String batteryStatus,
      final int statusTemplate,
      final String statusText
  ) {
    this.mDisplayName = displayName;
    this.mBatteryLevel = batteryLevel;
    this.mBatteryStatus = batteryStatus;
    this.mStatusTemplate = statusTemplate;
    this.mStatusText = statusText;
  }

  public static CircleUser fromUser(final User user) {
    return new CircleUser(
        user.getDisplayName(),
        (int) (user.getBatteryLevel() * 1000 / 10),
        user.getBatteryStatus(),
        R.string.user_status_near,
        user.getCurrentAddress()
    );
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

  public int getStatusTemplate() {
    return mStatusTemplate;
  }

  public String getStatusText() {
    return mStatusText;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final CircleUser that = (CircleUser) o;

    if (mBatteryLevel != that.mBatteryLevel) return false;
    if (mStatusTemplate != that.mStatusTemplate) return false;
    if (mDisplayName != null ? !mDisplayName.equals(that.mDisplayName) : that.mDisplayName != null)
      return false;
    if (mBatteryStatus != null ? !mBatteryStatus.equals(that.mBatteryStatus) : that.mBatteryStatus != null)
      return false;
    return mStatusText != null ? mStatusText.equals(that.mStatusText) : that.mStatusText == null;
  }

  @Override
  public int hashCode() {
    int result = mDisplayName != null ? mDisplayName.hashCode() : 0;
    result = 31 * result + mBatteryLevel;
    result = 31 * result + (mBatteryStatus != null ? mBatteryStatus.hashCode() : 0);
    result = 31 * result + mStatusTemplate;
    result = 31 * result + (mStatusText != null ? mStatusText.hashCode() : 0);
    return result;
  }

  @Override
  public String
  toString() {
    final StringBuffer sb = new StringBuffer("UserViewModel{");
    sb.append("mDisplayName='").append(mDisplayName).append('\'');
    sb.append(", mBatteryLevel=").append(mBatteryLevel);
    sb.append(", mBatteryStatus='").append(mBatteryStatus).append('\'');
    sb.append(", mStatusTemplate=").append(mStatusTemplate);
    sb.append(", mStatusText='").append(mStatusText).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
