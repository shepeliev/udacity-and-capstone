package com.familycircleapp;

import com.familycircleapp.battery.BatteryModule;
import com.familycircleapp.datasource.FirebaseModule;
import com.familycircleapp.repository.RepositoryModule;
import com.familycircleapp.location.LocationModule;
import com.familycircleapp.ui.ViewModelModule;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {
    AndroidModule.class,
    FirebaseModule.class,
    RepositoryModule.class,
    ViewModelModule.class,
    BatteryModule.class,
    LocationModule.class
})
interface AppComponent extends Component {

}
