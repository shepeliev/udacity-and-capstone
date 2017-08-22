package com.familycircleapp;

import com.familycircleapp.battery.BatteryModule;
import com.familycircleapp.datasource.FirebaseModule;
import com.familycircleapp.location.LocationModule;
import com.familycircleapp.repository.RepositoryModule;
import com.familycircleapp.ui.ViewModelModule;
import com.familycircleapp.ui.map.MapModule;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {
    AndroidModule.class,
    JobModule.class,
    FirebaseModule.class,
    RepositoryModule.class,
    ViewModelModule.class,
    BatteryModule.class,
    LocationModule.class,
    MapModule.class
})
interface AppComponent extends Component {

}
