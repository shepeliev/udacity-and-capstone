package com.familycircleapp;


import com.familycircleapp.battery.BatteryInfoReceiver;
import com.familycircleapp.location.UpdatedLocationIntentService;
import com.familycircleapp.ui.main.MainActivity;

public interface Component {

  void inject(final EntryPointActivity target);

  void inject(final MainActivity target);

  void inject(final BatteryInfoReceiver target);

  void inject(final UpdatedLocationIntentService target);
}
