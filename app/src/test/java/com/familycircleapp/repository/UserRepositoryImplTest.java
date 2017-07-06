package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;

import com.familycircleapp.battery.BatteryInfo;
import com.familycircleapp.utils.F;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryImplTest {

  @Mock FirebaseDatabase mockFirebaseDatabase;
  @Mock DatabaseReference mockUsersReference;
  @InjectMocks UserRepositoryImpl mUserRepository;

  @Before
  public void setUp() throws Exception {
    when(mockFirebaseDatabase.getReference("users")).thenReturn(mockUsersReference);
  }

  @Test
  public void testGetUser() throws Exception {
    final DatabaseReference mockUser1Reference = mock(DatabaseReference.class);
    when(mockUsersReference.child("user_1")).thenReturn(mockUser1Reference);

    final LiveData<User> userLiveData = mUserRepository.getUser("user_1");

    assertNotNull(userLiveData);
    verify(mockFirebaseDatabase).getReference("users");
    verify(mockUsersReference).child("user_1");
    assertEquals(mockUser1Reference,
        ((DatabaseReferenceLiveData<User>) userLiveData).mDatabaseReference);
  }

  @Test
  public void testGetCurrentCircleId() throws Exception {
    final DatabaseReference mockCurrentCircleRef = mock(DatabaseReference.class);
    final DatabaseReference mockUser1Ref = mock(DatabaseReference.class);
    when(mockUser1Ref.child(UserRepositoryImpl.CURRENT_CIRCLE_KEY))
        .thenReturn(mockCurrentCircleRef);
    when(mockUsersReference.child("user_1")).thenReturn(mockUser1Ref);

    final LiveData<String> circleIdLiveData = mUserRepository.getCurrentCircleId("user_1");

    assertNotNull(circleIdLiveData);
    assertEquals(
        mockCurrentCircleRef,
        ((DatabaseReferenceLiveData<String>) circleIdLiveData).mDatabaseReference
    );
  }

  @Test
  public void testSaveBatteryInfo() throws Exception {
    final BatteryInfo batteryInfo = new BatteryInfo(1, "charging");
    final DatabaseReference mockUser1Reference = mock(DatabaseReference.class);
    when(mockUsersReference.child("user_1")).thenReturn(mockUser1Reference);
    final Map<String, Object> expectedUpdateMap = F.mapOf(asList(
        F.mapEntry("batteryLevel", 1.0),
        F.mapEntry("batteryStatus", "charging")
    ));

    mUserRepository.saveBatteryInfo("user_1", batteryInfo);

    verify(mockUser1Reference).updateChildren(expectedUpdateMap);
  }
}
