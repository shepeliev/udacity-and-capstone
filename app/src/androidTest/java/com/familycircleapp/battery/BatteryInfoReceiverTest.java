package com.familycircleapp.battery;

import android.content.Intent;
import android.os.BatteryManager;
import android.support.test.runner.AndroidJUnit4;

import com.familycircleapp.TestApp;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BatteryInfoReceiverTest {

  @Inject CurrentUser mockCurrentUser;
  @Inject UserRepository mockUserRepository;

  private BatteryInfoReceiver mBatteryInfoReceiver;

  @Before
  public void setUp() throws Exception {
    TestApp.getComponent().inject(this);
    mBatteryInfoReceiver = new BatteryInfoReceiver();
    Mockito.reset(mockCurrentUser, mockUserRepository);
  }

  @Test
  public void onReceive_shouldSaveBatteryInfo() throws Exception {
    when(mockCurrentUser.isAuthenticated()).thenReturn(true);
    when(mockCurrentUser.getId()).thenReturn("user_1");
    final Intent intent = new Intent(Intent.ACTION_BATTERY_CHANGED);
    intent.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_DISCHARGING);
    intent.putExtra(BatteryManager.EXTRA_LEVEL, 50);
    intent.putExtra(BatteryManager.EXTRA_SCALE, 100);

    mBatteryInfoReceiver.onReceive(null, intent);

    verify(mockUserRepository).saveBatteryInfo("user_1", new BatteryInfo(0.5, "discharging"));
  }

  @Test
  public void onReceive_userNotAuthenticated_shouldNotSaveBatteryInfo() throws Exception {
    when(mockCurrentUser.isAuthenticated()).thenReturn(false);
    when(mockCurrentUser.getId()).thenReturn(null);
    final Intent intent = new Intent(Intent.ACTION_BATTERY_CHANGED);
    intent.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_DISCHARGING);
    intent.putExtra(BatteryManager.EXTRA_LEVEL, 50);
    intent.putExtra(BatteryManager.EXTRA_SCALE, 100);

    mBatteryInfoReceiver.onReceive(null, intent);

    verify(mockUserRepository, never()).saveBatteryInfo(anyString(), any(BatteryInfo.class));
  }
}
