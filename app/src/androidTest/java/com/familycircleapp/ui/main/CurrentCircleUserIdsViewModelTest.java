package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.testutils.LiveDataUtil;
import com.familycircleapp.utils.F;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrentCircleUserIdsViewModelTest {

  @Rule
  public UiThreadTestRule uiThread = new UiThreadTestRule();

  @Mock CurrentUser mockCurrentUser;
  @Mock CurrentCircleRepository mockCurrentCircleRepository;
  @InjectMocks CurrentCircleUserIdsViewModel mCurrentCircleUserIdsViewModel;

  private MutableLiveData<Circle> mCircleLiveData;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    final Map<String, Boolean> members = F.mapOf(asList(
        F.mapEntry("user_1", true),
        F.mapEntry("user_2", true)
    ));
    mCircleLiveData = new MutableLiveData<>();
    mCircleLiveData.postValue(getCircle("circle_1", members));

    when(mockCurrentUser.getId()).thenReturn("user_1");
    when(mockCurrentCircleRepository.getCurrentCircle("user_1")).thenReturn(mCircleLiveData);
  }

  @Test
  public void getUsers_returnsUserIdsLiveData() throws Throwable {
    final List<String> actualUserIds = new ArrayList<>();

    uiThread.runOnUiThread(() -> {
      final LiveData<List<String>> users = mCurrentCircleUserIdsViewModel.getUserIds();
      actualUserIds.addAll(LiveDataUtil.getValue(users));
    });

    assertEquals(asList("user_1", "user_2"), actualUserIds);
  }

  private Circle getCircle(final String id, final Map<String, Boolean> members) {
    final Circle circle = new Circle();
    circle.setId(id);
    circle.setName("Circle " + id);
    circle.setMembers(members);
    return circle;
  }
}
