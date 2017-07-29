package com.familycircleapp.ui.main;

import com.google.android.gms.maps.SupportMapFragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;

import com.familycircleapp.App;
import com.familycircleapp.EntryPointActivity;
import com.familycircleapp.PermissionManager;
import com.familycircleapp.R;
import com.familycircleapp.battery.BatteryInfoListener;
import com.familycircleapp.location.LocationUpdatesManager;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.ui.AppCompatLifecycleActivity;
import com.familycircleapp.ui.main.adapter.CircleUserAdapter;
import com.familycircleapp.ui.map.GoogleMapService;
import com.familycircleapp.utils.Ctx;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public final class MainActivity extends AppCompatLifecycleActivity {

  static final int RC_LOCATION_PERMISSION = 1;

  @Inject PermissionManager mPermissionManager;
  @Inject CurrentUser mCurrentUser;
  @Inject ViewModelProvider.Factory mViewModelFactory;
  @Inject BatteryInfoListener mBatteryInfoListener;
  @Inject LocationUpdatesManager mLocationUpdatesManager;
  @Inject GoogleMapService mGoogleMapService;

  @BindView(R.id.root_layout) CoordinatorLayout mRootLayout;
  @BindView(R.id.app_bar_layout) AppBarLayout mAppBarLayout;
  @BindView(R.id.loader_screen) View mLoaderScreen;
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

  private CircleUserAdapter mCircleUserAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    App.getComponent().inject(this);
    ButterKnife.bind(this);

    mBatteryInfoListener.setLifecycleOwner(this);
    mGoogleMapService.setLifecycleOwner(this);

    // hack to avoid NPE in tests
    if (mBatteryInfoListener.getClass().getPackage() != null) {
      getLifecycle().addObserver(mBatteryInfoListener);
    }
    if (mBatteryInfoListener.getClass().getPackage() != null) {
      getLifecycle().addObserver(mGoogleMapService);
    }

    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
        .getMapAsync(mGoogleMapService);

    if (mCurrentUser.isAuthenticated()) {
      final CircleUserViewModel circleUserViewModel = ViewModelProviders
          .of(this, mViewModelFactory)
          .get(CircleUserViewModel.class);
      mCircleUserAdapter = new CircleUserAdapter(this, circleUserViewModel);
      mRecyclerView.setAdapter(mCircleUserAdapter);

      mBatteryInfoListener.enable();
      mPermissionManager.requestPermission(
          this,
          Manifest.permission.ACCESS_FINE_LOCATION,
          RC_LOCATION_PERMISSION,
          () -> mLocationUpdatesManager.startLocationUpdates(this)
      );

      ViewModelProviders
          .of(this, mViewModelFactory)
          .get(CurrentCircleUserIdsViewModel.class)
          .getUserIds()
          .observe(this, this::onUserIdsLoaded);
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

  @Override
  protected void onStart() {
    super.onStart();
    adjustMapHeight();
    fixMovingMap();
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.action_menu, menu);
    return true;
  }

  private void onUserIdsLoaded(final List<String> userIds) {
    mLoaderScreen.setVisibility(View.GONE);
    mCircleUserAdapter.setData(userIds);
    mGoogleMapService.putUsersOnMap(userIds);
    if (mCurrentUser.getId() != null) {
      final float zoom = getResources().getInteger(R.integer.initial_map_zoom);
      mGoogleMapService.moveCameraToUser(mCurrentUser.getId(), zoom);
    }
  }

  private void adjustMapHeight() {
    mRootLayout.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {

          @Override
          public void onGlobalLayout() {
            final float height = mRootLayout.getHeight() -
                getResources().getDimension(R.dimen.recycler_view_visible_top);
            mAppBarLayout.getLayoutParams().height = (int) height;
            mRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        }
    );
  }

  private void fixMovingMap() {
    final AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
    behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
      @Override
      public boolean canDrag(@NonNull final AppBarLayout appBarLayout) {
        return false;
      }
    });

    ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).setBehavior(behavior);
  }
}
