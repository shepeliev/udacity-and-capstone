package com.familycircleapp.repository;

import android.support.annotation.NonNull;

public final class Invite implements HasId {

  private String mId;
  private String mCircleId;
  private long mExpiration;

  public Invite() {
  }

  public Invite(final String id, final String circleId, final long expiration) {
    mId = id;
    mCircleId = circleId;
    mExpiration = expiration;
  }

  @Override
  public String getId() {
    return mId;
  }

  @Override
  public void setId(@NonNull final String id) {
    mId = id;
  }

  public String getCircleId() {
    return mCircleId;
  }

  public void setCircleId(final String circleId) {
    mCircleId = circleId;
  }

  public long getExpiration() {
    return mExpiration;
  }

  public void setExpiration(final long expiration) {
    mExpiration = expiration;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Invite invite = (Invite) o;

    if (mExpiration != invite.mExpiration) return false;
    if (mId != null ? !mId.equals(invite.mId) : invite.mId != null) return false;
    return mCircleId != null ? mCircleId.equals(invite.mCircleId) : invite.mCircleId == null;
  }

  @Override
  public int hashCode() {
    int result = mId != null ? mId.hashCode() : 0;
    result = 31 * result + (mCircleId != null ? mCircleId.hashCode() : 0);
    result = 31 * result + (int) (mExpiration ^ (mExpiration >>> 32));
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Invite{");
    sb.append("mId='").append(mId).append('\'');
    sb.append(", mCircleId='").append(mCircleId).append('\'');
    sb.append(", mExpiration=").append(mExpiration);
    sb.append('}');
    return sb.toString();
  }
}
