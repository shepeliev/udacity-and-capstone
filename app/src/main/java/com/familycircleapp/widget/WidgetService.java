package com.familycircleapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.familycircleapp.App;
import com.familycircleapp.repository.UserRepository;

import javax.inject.Inject;

public class WidgetService extends RemoteViewsService {

  @Inject UserRepository mUserRepository;

  @Override
  public void onCreate() {
    super.onCreate();
    App.getComponent().inject(this);
  }

  @Override
  public RemoteViewsFactory onGetViewFactory(final Intent intent) {
    return new WidgetRemoteViewFactory(getApplicationContext(), mUserRepository, intent);
  }
}
