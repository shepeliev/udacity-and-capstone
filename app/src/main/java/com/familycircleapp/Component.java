package com.familycircleapp;


import com.familycircleapp.battery.BatteryInfoReceiver;
import com.familycircleapp.location.UpdatedLocationIntentService;
import com.familycircleapp.ui.main.MainActivity;
import com.familycircleapp.ui.newuser.NewUserActivity;

public interface Component {

  void inject(final EntryPointActivity target);

  void inject(final MainActivity target);

  void inject(final BatteryInfoReceiver target);

  void inject(final UpdatedLocationIntentService target);

  void inject(NewUserActivity target);
}
