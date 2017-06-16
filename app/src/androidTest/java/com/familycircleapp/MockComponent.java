package com.familycircleapp;

import com.familycircleapp.battery.BatteryInfoReceiverTest;
import com.familycircleapp.battery.MockBatteryModule;
import com.familycircleapp.location.MockLocationModule;
import com.familycircleapp.location.UpdatedLocationIntentServiceTest;
import com.familycircleapp.map.MockMapModule;
import com.familycircleapp.repository.MockRepositoryModule;
import com.familycircleapp.ui.MockViewModelModule;
import com.familycircleapp.ui.main.MainActivityTest;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {
    MockAndroidModule.class,
    MockFirebaseModule.class,
    MockRepositoryModule.class,
    MockViewModelModule.class,
    MockBatteryModule.class,
    MockLocationModule.class,
    MockMapModule.class
})
public interface MockComponent extends Component {

  void inject(EntryPointActivityTest target);

  void inject(MainActivityTest target);

  void inject(BatteryInfoReceiverTest target);

  void inject(UpdatedLocationIntentServiceTest target);
}
