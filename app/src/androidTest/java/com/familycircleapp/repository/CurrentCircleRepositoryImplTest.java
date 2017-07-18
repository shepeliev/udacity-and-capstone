package com.familycircleapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrentCircleRepositoryImplTest {

  @Rule
  public UiThreadTestRule uiThread = new UiThreadTestRule();

  @Mock UserRepository mockUserRepository;
  @Mock CircleRepository mockCircleRepository;
  @InjectMocks CurrentCircleRepositoryImpl mCurrentCircleRepository;

  private MutableLiveData<Circle> mCircleLiveData;
  private MutableLiveData<User> mUserLiveData;
  private MutableLiveData<String> mCurrentCircleIdLiveData;

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

    mCurrentCircleIdLiveData = new MutableLiveData<>();
    mCurrentCircleIdLiveData.postValue("circle_1");

    when(mockUserRepository.getCurrentCircleIdLiveData("user_1"))
        .thenReturn(mCurrentCircleIdLiveData);
    when(mockCircleRepository.getCircleLiveData("circle_1")).thenReturn(mCircleLiveData);
  }

  @Test
  public void testGetCurrentCircle() throws Throwable {
    final LiveData<Circle> currentCircle = mCurrentCircleRepository.getCurrentCircle("user_1");

    final Circle[] actualCircle = {null};
    uiThread.runOnUiThread(() -> actualCircle[0] = LiveDataUtil.getValue(currentCircle));

    final Circle expectedCircle = new Circle();
    expectedCircle.setId("circle_1");
    expectedCircle.setName("Circle circle_1");
    expectedCircle.setMembers(F.mapOf(Collections.singleton(F.mapEntry("user_1", true))));
    assertEquals(expectedCircle, actualCircle[0]);
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
