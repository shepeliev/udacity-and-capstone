package com.familycircleapp.repository;

import java.util.Map;

public final class Circle {

  private String mId;
  private String mName;
  private Map<String, Boolean> mMembers;

  public String getId() {
    return mId;
  }

  public Circle setId(final String id) {
    mId = id;
    return this;
  }

  public String getName() {
    return mName;
  }

  public Circle setName(final String name) {
    mName = name;
    return this;
  }

  public Map<String, Boolean> getMembers() {
    return mMembers;
  }

  public Circle setMembers(final Map<String, Boolean> members) {
    mMembers = members;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Circle circle = (Circle) o;

    if (mId != null ? !mId.equals(circle.mId) : circle.mId != null) return false;
    if (mName != null ? !mName.equals(circle.mName) : circle.mName != null) return false;
    return mMembers != null ? mMembers.equals(circle.mMembers) : circle.mMembers == null;
  }

  @Override
  public int hashCode() {
    int result = mId != null ? mId.hashCode() : 0;
    result = 31 * result + (mName != null ? mName.hashCode() : 0);
    result = 31 * result + (mMembers != null ? mMembers.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Circle{");
    sb.append("mId='").append(mId).append('\'');
    sb.append(", mName='").append(mName).append('\'');
    sb.append(", mMembers=").append(mMembers);
    sb.append('}');
    return sb.toString();
  }
}
