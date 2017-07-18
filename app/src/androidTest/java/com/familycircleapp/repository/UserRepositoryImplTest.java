package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.lifecycle.LiveData;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.battery.BatteryInfo;
import com.familycircleapp.utils.F;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import io.reactivex.Single;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserRepositoryImplTest {

  @Rule
  public UiThreadTestRule mUiThread = new UiThreadTestRule();

  @Mock private FirebaseDatabase mockFirebaseDatabase;
  @Mock private DatabaseReference mockUsersReference;
  private UserRepositoryImpl mUserRepository;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(mockFirebaseDatabase.getReference("users")).thenReturn(mockUsersReference);
    mUserRepository = new UserRepositoryImpl(mockFirebaseDatabase);
  }

  @Test
  public void testGetUserLiveData() throws Throwable {
    final LiveData<User> userLiveData = mUserRepository.getUserLiveData("user_1");

    assertNotNull(userLiveData);
    verify(mockUsersReference).child("user_1");
  }

  @Test
  public void testGetUser() throws Exception {
    final Single<User> user = mUserRepository.getUser("user_1");

    assertNotNull(user);
    verify(mockUsersReference).child("user_1");
  }

  @Test
  public void testGetCurrentCircleId() throws Exception {
    final DatabaseReference mockUser1Ref = mock(DatabaseReference.class);
    when(mockUsersReference.child("user_1")).thenReturn(mockUser1Ref);

    final LiveData<String> circleIdLiveData = mUserRepository.getCurrentCircleIdLiveData("user_1");

    assertNotNull(circleIdLiveData);
    verify(mockUsersReference).child("user_1");
    verify(mockUser1Ref).child("currentCircle");
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

  @Test
  public void testSaveDisplayName() throws Exception {
    final DatabaseReference mockUserDisplayNameRef = mock(DatabaseReference.class);
    final DatabaseReference mockUser1Ref = mock(DatabaseReference.class);
    when(mockUser1Ref.child(UserRepositoryImpl.DISPLAY_NAME_KEY))
        .thenReturn(mockUserDisplayNameRef);
    when(mockUsersReference.child("user_1")).thenReturn(mockUser1Ref);

    mUserRepository.saveDisplayName("user_1", "John");

    verify(mockUserDisplayNameRef).setValue("John");
  }
}
