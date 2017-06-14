package com.familycircleapp.mocks;

import com.familycircleapp.Component;
import com.familycircleapp.EntryPointActivityTest;
import com.familycircleapp.ui.main.MainActivityTest;
import com.familycircleapp.ui.MockViewModelModule;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {
    MockFirebaseModule.class,
    MockRepositoryModule.class,
    MockViewModelModule.class
})
public interface MockComponent extends Component {
  void inject(EntryPointActivityTest target);

  void inject(MainActivityTest target);
}
