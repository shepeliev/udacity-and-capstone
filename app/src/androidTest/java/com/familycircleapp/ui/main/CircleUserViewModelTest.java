package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.R;
import com.familycircleapp.repository.User;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.testutils.LiveDataUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CircleUserViewModelTest {

  @Rule
  public UiThreadTestRule mUiThread = new UiThreadTestRule();

  @Mock UserRepository mockUserRepository;
  @InjectMocks CircleUserViewModel mCircleUserViewModel;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    when(mockUserRepository.getUser("user_1")).thenReturn(userLiveData);
    final User user = new User();
    user.setDisplayName("User");
    user.setBatteryLevel(0.77);
    user.setBatteryStatus("charging");
    user.setCurrentAddress("address");
    userLiveData.postValue(user);
  }

  @Test
  public void testGetCircleUser() throws Throwable {
    final CircleUser[] circleUser = {null};
    mUiThread.runOnUiThread(() -> {
      final LiveData<CircleUser> circleUserLiveData = mCircleUserViewModel.getCircleUser("user_1");
      circleUser[0] = LiveDataUtil.getValue(circleUserLiveData);
    });

    final CircleUser expectedCircleUser = new CircleUser(
        "User",
        77,
        "charging",
        R.string.user_status_near,
        "address"
    );
    assertEquals(expectedCircleUser, circleUser[0]);
  }
}
