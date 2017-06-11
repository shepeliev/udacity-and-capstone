package com.familycircleapp;

import com.familycircleapp.repository.RepositoryModule;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {
    AndroidModule.class,
    FirebaseModule.class,
    RepositoryModule.class
})
public interface AppComponent extends Component {

}
