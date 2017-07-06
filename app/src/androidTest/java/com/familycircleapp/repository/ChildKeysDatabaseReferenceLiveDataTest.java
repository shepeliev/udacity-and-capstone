package com.familycircleapp.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.testutils.LiveDataUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class ChildKeysDatabaseReferenceLiveDataTest {

  @Rule
  public UiThreadTestRule uiThreadRule = new UiThreadTestRule();

  @Test
  public void onActive_shouldSetValueEventListener() throws Exception {
    final DatabaseReference mockReference = mock(DatabaseReference.class);
    final ChildKeysDatabaseReferenceLiveData liveData =
        new ChildKeysDatabaseReferenceLiveData(mockReference);

    liveData.onActive();

    verify(mockReference).addValueEventListener(liveData);
  }

  @Test
  public void onInactive_shouldRemoveValueEventListener() throws Exception {
    final DatabaseReference mockReference = mock(DatabaseReference.class);
    final ChildKeysDatabaseReferenceLiveData liveData =
        new ChildKeysDatabaseReferenceLiveData(mockReference);
    liveData.onActive();

    liveData.onInactive();

    verify(mockReference).removeEventListener(liveData);
  }

  @Test
  public void onDataChange_shouldSetLiveDataValue() throws Throwable {
    final ChildKeysDatabaseReferenceLiveData liveData =
        new ChildKeysDatabaseReferenceLiveData(mock(DatabaseReference.class));
    final DataSnapshot mockChild1 = mock(DataSnapshot.class);
    when(mockChild1.getKey()).thenReturn("key_1");
    final DataSnapshot mockChild2 = mock(DataSnapshot.class);
    when(mockChild2.getKey()).thenReturn("key_2");
    final DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
    when(mockDataSnapshot.getChildren()).thenReturn(asList(mockChild1, mockChild2));

    final List<String> actualKeys = new ArrayList<>();
    uiThreadRule.runOnUiThread(() -> {
      liveData.onDataChange(mockDataSnapshot);
      actualKeys.addAll(LiveDataUtil.getValue(liveData));
    });

    assertEquals(asList("key_1", "key_2"), actualKeys);
  }
}
