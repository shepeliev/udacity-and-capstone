package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.R;
import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.User;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.testutils.LiveDataUtil;
import com.familycircleapp.utils.F;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrentCircleUsersViewModelTest {

  @Rule
  public UiThreadTestRule uiThread = new UiThreadTestRule();

  @Mock CurrentUser mockCurrentUser;
  @Mock CurrentCircleRepository mockCurrentCircleRepository;
  @Mock UserRepository mockUserRepository;
  @InjectMocks CurrentCircleUsersViewModel mCurrentCircleUsersViewModel;

  private MutableLiveData<Circle> mCircleLiveData;
  private MutableLiveData<User> mUserLiveData;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    final Map<String, Boolean> members = F.mapOf(Collections.singletonList(
        F.mapEntry("user_1", true)
    ));
    mCircleLiveData = new MutableLiveData<>();
    mCircleLiveData.postValue(getCircle("circle_1", members));

    mUserLiveData = new MutableLiveData<>();
    mUserLiveData.postValue(getUser("user_1", "circle_1"));

    when(mockCurrentUser.getId()).thenReturn("user_1");
    when(mockCurrentCircleRepository.getCurrentCircle("user_1")).thenReturn(mCircleLiveData);
    when(mockUserRepository.getUser("user_1")).thenReturn(mUserLiveData);
  }

  @Test
  public void getUsers_returnsUserListItemLiveData() throws Throwable {
    final CircleUser expectedCircleUser = new CircleUser(
        "User user_1",
        77,
        "charging",
        R.string.user_status_near,
        "address"
    );

    final int[] testResultListSize = {0};
    final CircleUser[] testResultListItems = new CircleUser[testResultListSize.length];

    uiThread.runOnUiThread(() -> {
      final LiveData<List<LiveData<CircleUser>>> users = mCurrentCircleUsersViewModel.getUsers();
      final List<LiveData<CircleUser>> list = LiveDataUtil.getValue(users);
      testResultListSize[0] = list.size();

      final int[] i = {0};
      list.forEach(ld -> testResultListItems[i[0]++] = LiveDataUtil.getValue(ld));
    });

    assertEquals(1, testResultListSize[0]);
    assertEquals(expectedCircleUser, testResultListItems[0]);
  }

  private Circle getCircle(final String id, final Map<String, Boolean> members) {
    final Circle circle = new Circle();
    circle.setId(id);
    circle.setName("Circle " + id);
    circle.setMembers(members);
    return circle;
  }

  private User getUser(final String id, final String currentCircle) {
    final User user = new User();
    user.setId(id);
    user.setDisplayName("User " + id);
    user.setBatteryLevel(0.77);
    user.setBatteryStatus("charging");
    user.setCurrentAddress("address");
    user.setCurrentCircle(currentCircle);
    return user;
  }
}
