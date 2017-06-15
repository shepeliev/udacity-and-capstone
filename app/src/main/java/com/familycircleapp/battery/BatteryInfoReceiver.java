package com.familycircleapp.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.familycircleapp.App;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;

import javax.inject.Inject;

import timber.log.Timber;

public final class BatteryInfoReceiver extends BroadcastReceiver {

  @Inject CurrentUser mCurrentUser;
  @Inject UserRepository mUserRepository;

  private BatteryInfo mPreviousBatteryInfo;

  @Override
  public void onReceive(final Context context, final Intent intent) {
    App.getComponent().inject(this);

    final String userId = mCurrentUser.getId();
    if (userId == null) {
      Timber.i("User is not authenticated. Can't save battery info.");
      return;
    }

    final BatteryInfo batteryInfo = BatteryInfo.fromIntent(intent);

    if (!batteryInfo.equals(mPreviousBatteryInfo)) {
      Timber.i(batteryInfo.toString());
      mUserRepository.saveBatteryInfo(userId, batteryInfo);
      mPreviousBatteryInfo = batteryInfo;
    }
  }
}
