package com.familycircleapp.ui.map;

public class UserModel {
  private final String mId;
  private final String mDisplayName;

  public UserModel(final String id, final String displayName) {
    mId = id;
    mDisplayName = displayName;
  }

  public String getId() {
    return mId;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final UserModel user = (UserModel) o;

    if (mId != null ? !mId.equals(user.mId) : user.mId != null) return false;
    return mDisplayName != null ? mDisplayName.equals(user.mDisplayName) : user.mDisplayName == null;
  }

  @Override
  public int hashCode() {
    int result = mId != null ? mId.hashCode() : 0;
    result = 31 * result + (mDisplayName != null ? mDisplayName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("User{");
    sb.append("mId='").append(mId).append('\'');
    sb.append(", mDisplayName='").append(mDisplayName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
