package com.familycircleapp;

import com.familycircleapp.battery.BatteryInfoReceiver;
import com.familycircleapp.battery.BatteryModule;
import com.familycircleapp.datasource.FirebaseModule;
import com.familycircleapp.location.LocationModule;
import com.familycircleapp.location.UpdatedLocationIntentService;
import com.familycircleapp.repository.RepositoryModule;
import com.familycircleapp.ui.JoinCircleActivity;
import com.familycircleapp.ui.NewCircleActivity;
import com.familycircleapp.ui.NewUserActivity;
import com.familycircleapp.ui.SwitchCircleDialog;
import com.familycircleapp.ui.ViewModelModule;
import com.familycircleapp.ui.details.UserDetailsActivity;
import com.familycircleapp.ui.invite.InviteActivity;
import com.familycircleapp.ui.main.MainActivity;
import com.familycircleapp.ui.map.MapModule;
import com.familycircleapp.ui.settings.DeleteAccountDialog;
import com.familycircleapp.ui.settings.SettingsFragment;
import com.familycircleapp.ui.settings.UsernamePreference;
import com.familycircleapp.widget.WidgetProvider;
import com.familycircleapp.widget.WidgetService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    AndroidModule.class,
    JobModule.class,
    FirebaseModule.class,
    RepositoryModule.class,
    ViewModelModule.class,
    BatteryModule.class,
    LocationModule.class,
    MapModule.class
})
public interface AppComponent {

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
