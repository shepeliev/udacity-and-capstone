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

public class UserRepositoryImplTest {

  @Test
  public void getCircle_shouldCreateLiveDataOnCircleReference() throws Exception {
    final FirebaseDatabase mockFirebaseDatabase = mock(FirebaseDatabase.class);
    final DatabaseReference mockUsersReference = mock(DatabaseReference.class);
    when(mockFirebaseDatabase.getReference("users")).thenReturn(mockUsersReference);
    final DatabaseReference mockUser1Reference = mock(DatabaseReference.class);
    when(mockUsersReference.child("user_1")).thenReturn(mockUser1Reference);
    final UserRepository repository = new UserRepositoryImpl(mockFirebaseDatabase);

    final LiveData<User> circleLiveData = repository.getUser("user_1");

    assertNotNull(circleLiveData);
    verify(mockFirebaseDatabase).getReference("users");
    verify(mockUsersReference).child("user_1");
    assertEquals(mockUser1Reference,
        ((DatabaseReferenceLiveData<User>) circleLiveData).mDatabaseReference);
  }
}
