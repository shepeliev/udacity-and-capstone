package com.familycircleapp.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.LiveDataTestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class DatabaseReferenceLiveDataTest {

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
    final DatabaseReferenceLiveData<String> liveData =
        new DatabaseReferenceLiveData<>(mockReference, String.class);
    final DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
    when(mockDataSnapshot.getValue(String.class)).thenReturn("value");
    liveData.onActive();

    final String[] actualValue = {null};
    uiThreadRule.runOnUiThread(() -> {
      liveData.onDataChange(mockDataSnapshot);
      actualValue[0] = LiveDataTestUtil.getValue(liveData);
    });

    assertEquals("value", actualValue[0]);
  }
}
