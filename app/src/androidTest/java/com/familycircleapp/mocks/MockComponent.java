package com.familycircleapp.mocks;

import com.familycircleapp.Component;
import com.familycircleapp.EntryPointActivityTest;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {
    MockFirebaseModule.class,
    MockRepositoryModule.class
})
public interface MockComponent extends Component {
  void inject(EntryPointActivityTest target);
}
