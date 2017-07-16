package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CircleRepositoryImplTest {

  @Mock private FirebaseDatabase mockFirebaseDatabase;
  @InjectMocks private CircleRepositoryImpl mCircleRepository;

  @Test
  public void getCircle_shouldCreateLiveDataOnCircleReference() throws Exception {
    final DatabaseReference mockCirclesReference = mock(DatabaseReference.class);
    when(mockFirebaseDatabase.getReference("circles")).thenReturn(mockCirclesReference);
    final DatabaseReference mockCircle1Reference = mock(DatabaseReference.class);
    when(mockCirclesReference.child("circle_1")).thenReturn(mockCircle1Reference);

    final LiveData<Circle> circleLiveData = mCircleRepository.getCircle("circle_1");

    assertNotNull(circleLiveData);
    verify(mockFirebaseDatabase).getReference("circles");
    verify(mockCirclesReference).child("circle_1");
    assertEquals(mockCircle1Reference,
        ((DatabaseReferenceLiveData<Circle>) circleLiveData).mDatabaseReference);
  }

  @Test
  public void createNewCircle_shouldCreateNewCircle_updateCurrentCircleKey() throws Exception {
    final DatabaseReference mockDatabaseRef = mock(DatabaseReference.class);
    when(mockFirebaseDatabase.getReference()).thenReturn(mockDatabaseRef);
    final DatabaseReference mockCirclesRef = mock(DatabaseReference.class);
    when(mockDatabaseRef.child("circles")).thenReturn(mockCirclesRef);
    final DatabaseReference mockNewCircleRef = mock(DatabaseReference.class);
    when(mockCirclesRef.push()).thenReturn(mockNewCircleRef);
    when(mockNewCircleRef.getKey()).thenReturn("circle_1");

    mCircleRepository.createNewCircle("user_1", "Family", null);

    final Map<String, Object> expectedUpdate = new HashMap<String, Object>(){{
      put("/circles/circle_1/name", "Family");
      put("/circles/circle_1/members/user_1", true);
      put("/users/user_1/currentCircle", "circle_1");
    }};
    verify(mockDatabaseRef).updateChildren(eq(expectedUpdate), any());
  }
}
