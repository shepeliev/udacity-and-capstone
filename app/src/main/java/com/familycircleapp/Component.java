package com.familycircleapp;


import com.familycircleapp.battery.BatteryInfoReceiver;
import com.familycircleapp.location.UpdatedLocationIntentService;
import com.familycircleapp.ui.JoinCircleActivity;
import com.familycircleapp.ui.NewCircleActivity;
import com.familycircleapp.ui.NewUserActivity;
import com.familycircleapp.ui.SwitchCircleDialog;
import com.familycircleapp.ui.details.UserDetailsActivity;
import com.familycircleapp.ui.invite.InviteActivity;
import com.familycircleapp.ui.main.MainActivity;
import com.familycircleapp.ui.settings.DeleteAccountDialog;
import com.familycircleapp.ui.settings.SettingsFragment;
import com.familycircleapp.ui.settings.UsernamePreference;
import com.familycircleapp.widget.WidgetProvider;
import com.familycircleapp.widget.WidgetService;

public interface Component {

  void inject(final EntryPointActivity target);

  void inject(final MainActivity target);

  void inject(final BatteryInfoReceiver target);

  void inject(final UpdatedLocationIntentService target);

  void inject(NewUserActivity target);

  void inject(InviteActivity target);

  void inject(NewCircleActivity target);

  void inject(SwitchCircleDialog target);

  void inject(JoinCircleActivity target);

  void inject(SettingsFragment target);

  void inject(DeleteAccountDialog target);

  void inject(UsernamePreference target);

  void inject(App target);

  void inject(UserDetailsActivity target);

  void inject(WidgetProvider target);

  void inject(WidgetService target);
}
