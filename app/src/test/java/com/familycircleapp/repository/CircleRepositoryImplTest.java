package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CircleRepositoryImplTest {

  @Test
  public void getCircle_shouldCreateLiveDataOnCircleReference() throws Exception {
    final FirebaseDatabase mockFirebaseDatabase = mock(FirebaseDatabase.class);
    final DatabaseReference mockCirclesReference = mock(DatabaseReference.class);
    when(mockFirebaseDatabase.getReference("circles")).thenReturn(mockCirclesReference);
    final DatabaseReference mockCircle1Reference = mock(DatabaseReference.class);
    when(mockCirclesReference.child("circle_1")).thenReturn(mockCircle1Reference);
    final CircleRepository repository = new CircleRepositoryImpl(mockFirebaseDatabase);

    final LiveData<Circle> circleLiveData = repository.getCircle("circle_1");

    assertNotNull(circleLiveData);
    verify(mockFirebaseDatabase).getReference("circles");
    verify(mockCirclesReference).child("circle_1");
    assertEquals(mockCircle1Reference,
        ((DatabaseReferenceLiveData<Circle>) circleLiveData).mDatabaseReference);
  }
}
