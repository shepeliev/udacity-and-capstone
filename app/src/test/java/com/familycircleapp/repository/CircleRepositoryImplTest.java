package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CircleRepositoryImplTest {

  @Mock private FirebaseDatabase mockFirebaseDatabase;
  @Mock private DatabaseReference mockDatabaseReference;
  @Mock private DatabaseReference mockCirclesReference;
  private CircleRepositoryImpl mCircleRepository;

  @Before
  public void setUp() throws Exception {
    when(mockFirebaseDatabase.getReference()).thenReturn(mockDatabaseReference);
    when(mockFirebaseDatabase.getReference("circles")).thenReturn(mockCirclesReference);

    mCircleRepository = new CircleRepositoryImpl(mockFirebaseDatabase);
  }

  @Test
  public void getCircle_shouldCreateLiveDataOnCircleReference() throws Exception {
    final LiveData<Circle> circleLiveData = mCircleRepository.getCircleLiveData("circle_1");

    assertNotNull(circleLiveData);
    verify(mockCirclesReference).child("circle_1");
  }

  @Test
  public void createNewCircle_shouldCreateNewCircle_updateCurrentCircleKey() throws Exception {
    final DatabaseReference mockNewCircleRef = mock(DatabaseReference.class);
    when(mockCirclesReference.push()).thenReturn(mockNewCircleRef);
    when(mockNewCircleRef.getKey()).thenReturn("circle_1");

    mCircleRepository.createNewCircle("user_1", "Family").subscribe();

    final Map<String, Object> expectedUpdate = new HashMap<String, Object>(){{
      put("/circles/circle_1/name", "Family");
      put("/circles/circle_1/members/user_1", true);
      put("/users/user_1/currentCircle", "circle_1");
    }};
    verify(mockDatabaseReference).updateChildren(eq(expectedUpdate), any());
  }
}
