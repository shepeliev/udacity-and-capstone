package com.familycircleapp.ui.main;

import android.Manifest;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.familycircleapp.App;
import com.familycircleapp.EntryPointActivity;
import com.familycircleapp.PermissionManager;
import com.familycircleapp.R;
import com.familycircleapp.battery.BatteryInfoListener;
import com.familycircleapp.location.LocationUpdatesManager;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.ui.main.adapter.CircleUserAdapter;
import com.familycircleapp.utils.Ctx;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends LifecycleActivity {

  static final int RC_LOCATION_PERMISSION = 1;

  @Inject PermissionManager mPermissionManager;
  @Inject CurrentUser mCurrentUser;
  @Inject ViewModelProvider.Factory mViewModelFactory;
  @Inject BatteryInfoListener mBatteryInfoListener;
  @Inject LocationUpdatesManager mLocationUpdatesManager;

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
      mPermissionManager.requestPermission(
          this,
          Manifest.permission.ACCESS_FINE_LOCATION,
          RC_LOCATION_PERMISSION,
          () ->  mLocationUpdatesManager.startLocationUpdates(this)
      );


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

  @Override
  public void onRequestPermissionsResult(
      final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode != RC_LOCATION_PERMISSION) {
      return;
    }

    final int idx = Arrays.asList(permissions).indexOf(Manifest.permission.ACCESS_FINE_LOCATION);
    if (idx > -1 && grantResults[idx] == PackageManager.PERMISSION_GRANTED) {
      mLocationUpdatesManager.startLocationUpdates(this);
    } else {
      Ctx.toast(this, R.string.error_location_permission_not_granted);
      Timber.w(Manifest.permission.ACCESS_FINE_LOCATION + " has not been granted");
    }
  }

  private void onUsersLoaded(final List<LiveData<CircleUser>> users) {
    mLoaderScreen.setVisibility(View.GONE);
    mCircleUserAdapter.setData(users);
  }
}
