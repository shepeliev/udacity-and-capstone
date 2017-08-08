package com.familycircleapp;


import com.familycircleapp.battery.BatteryInfoReceiver;
import com.familycircleapp.location.UpdatedLocationIntentService;
import com.familycircleapp.ui.NewCircleActivity;
import com.familycircleapp.ui.SwitchCircleDialog;
import com.familycircleapp.ui.invite.InviteActivity;
import com.familycircleapp.ui.main.MainActivity;
import com.familycircleapp.ui.NewUserActivity;

public interface Component {

  void inject(final EntryPointActivity target);

  void inject(final MainActivity target);

  void inject(final BatteryInfoReceiver target);

  void inject(final UpdatedLocationIntentService target);

  void inject(NewUserActivity target);

  void inject(InviteActivity target);

  void inject(NewCircleActivity target);

  void inject(SwitchCircleDialog target);
}
