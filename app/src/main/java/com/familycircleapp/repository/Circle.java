package com.familycircleapp.repository;

import java.util.Map;

public final class Circle implements HasId {

  private String mId;
  private String mName;
  private String mInviteCode;
  private Map<String, Boolean> mMembers;

  @Override
  public String getId() {
    return mId;
  }

  @Override
  public void setId(final String id) {
    mId = id;
  }

  public String getName() {
    return mName;
  }

  public void setName(final String name) {
    mName = name;
  }

  public String getInviteCode() {
    return mInviteCode;
  }

  public void setInviteCode(final String inviteCode) {
    mInviteCode = inviteCode;
  }

  public Map<String, Boolean> getMembers() {
    return mMembers;
  }

  public void setMembers(final Map<String, Boolean> members) {
    mMembers = members;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Circle circle = (Circle) o;

    if (mId != null ? !mId.equals(circle.mId) : circle.mId != null) return false;
    if (mName != null ? !mName.equals(circle.mName) : circle.mName != null) return false;
    if (mInviteCode != null ? !mInviteCode.equals(circle.mInviteCode) : circle.mInviteCode != null)
      return false;
    return mMembers != null ? mMembers.equals(circle.mMembers) : circle.mMembers == null;
  }

  @Override
  public int hashCode() {
    int result = mId != null ? mId.hashCode() : 0;
    result = 31 * result + (mName != null ? mName.hashCode() : 0);
    result = 31 * result + (mInviteCode != null ? mInviteCode.hashCode() : 0);
    result = 31 * result + (mMembers != null ? mMembers.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Circle{");
    sb.append("mId='").append(mId).append('\'');
    sb.append(", mName='").append(mName).append('\'');
    sb.append(", mInviteCode='").append(mInviteCode).append('\'');
    sb.append(", mMembers=").append(mMembers);
    sb.append('}');
    return sb.toString();
  }
}
