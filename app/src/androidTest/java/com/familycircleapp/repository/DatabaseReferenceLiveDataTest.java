package com.familycircleapp.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.testutils.LiveDataUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class DatabaseReferenceLiveDataTest {

  @Rule
  public UiThreadTestRule uiThreadRule = new UiThreadTestRule();

  @Test
  public void onActive_shouldSetValueEventListener() throws Exception {
    final DatabaseReference mockReference = mock(DatabaseReference.class);
    final DatabaseReferenceLiveData<String> liveData =
        new DatabaseReferenceLiveData<>(mockReference, String.class);

    liveData.onActive();

    verify(mockReference).addValueEventListener(liveData);
  }

  @Test
  public void onInactive_shouldRemoveValueEventListener() throws Exception {
    final DatabaseReference mockReference = mock(DatabaseReference.class);
    final DatabaseReferenceLiveData<String> liveData =
        new DatabaseReferenceLiveData<>(mockReference, String.class);
    liveData.onActive();

    liveData.onInactive();

    verify(mockReference).removeEventListener(liveData);
  }

  @Test
  public void onDataChange_shouldSetLiveDataValue() throws Throwable {
    final DatabaseReference mockReference = mock(DatabaseReference.class);
    when(mockReference.getKey()).thenReturn("foo_1");
    final DatabaseReferenceLiveData<Foo> liveData =
        new DatabaseReferenceLiveData<>(mockReference, Foo.class);
    final DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
    when(mockDataSnapshot.getValue(Foo.class)).thenReturn(new Foo(null, "value"));
    liveData.onActive();

    final Foo[] actualValue = {null};
    uiThreadRule.runOnUiThread(() -> {
      liveData.onDataChange(mockDataSnapshot);
      actualValue[0] = LiveDataUtil.getValue(liveData);
    });

    assertEquals(new Foo("foo_1", "value"), actualValue[0]);
  }

  public static class Foo implements HasId {

    private String mId;
    private String mValue;

    public Foo() {
    }

    public Foo(final String id, final String value) {
      mId = id;
      mValue = value;
    }

    @Override
    public void setId(@NonNull final String id) {
      mId = id;
    }

    @Override
    public String getId() {
      return mId;
    }

    public String getValue() {
      return mValue;
    }

    public Foo setValue(final String value) {
      mValue = value;
      return this;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final Foo foo = (Foo) o;

      if (mId != null ? !mId.equals(foo.mId) : foo.mId != null) return false;
      return mValue != null ? mValue.equals(foo.mValue) : foo.mValue == null;
    }

    @Override
    public int hashCode() {
      int result = mId != null ? mId.hashCode() : 0;
      result = 31 * result + (mValue != null ? mValue.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("Foo{");
      sb.append("mId='").append(mId).append('\'');
      sb.append(", mValue='").append(mValue).append('\'');
      sb.append('}');
      return sb.toString();
    }
  }
}
