package com.familycircleapp.ui.main;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.familycircleapp.App;
import com.familycircleapp.EntryPointActivity;
import com.familycircleapp.R;
import com.familycircleapp.battery.BatteryInfoListener;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.ui.main.adapter.CircleUserAdapter;
import com.familycircleapp.utils.Ctx;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LifecycleActivity {

  @Inject CurrentUser mCurrentUser;
  @Inject ViewModelProvider.Factory mViewModelFactory;
  @Inject BatteryInfoListener mBatteryInfoListener;

  @BindView(R.id.loader_screen) View mLoaderScreen;
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

  private CircleUserAdapter mCircleUserAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    App.getComponent().inject(this);
    ButterKnife.bind(this);

    if (mCurrentUser.isAuthenticated()) {
      mCircleUserAdapter = new CircleUserAdapter(this);
      mRecyclerView.setAdapter(mCircleUserAdapter);

      mBatteryInfoListener.start(getLifecycle());

      ViewModelProviders
          .of(this, mViewModelFactory)
          .get(CurrentCircleUsersViewModel.class)
          .getUsers()
          .observe(this, this::onUsersLoaded);
    } else {
      Ctx.startActivity(this, EntryPointActivity.class);
      finish();
    }
  }

  private void onUsersLoaded(final List<LiveData<CircleUser>> users) {
    mLoaderScreen.setVisibility(View.GONE);
    mCircleUserAdapter.setData(users);
  }
}
