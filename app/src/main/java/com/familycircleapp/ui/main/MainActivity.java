package com.familycircleapp.ui.main;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.familycircleapp.App;
import com.familycircleapp.EntryPointActivity;
import com.familycircleapp.PermissionManager;
import com.familycircleapp.R;
import com.familycircleapp.battery.BatteryInfoListener;
import com.familycircleapp.battery.UpdateBatteryInfoJob;
import com.familycircleapp.location.LocationUpdatesManager;
import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.ui.AppCompatLifecycleActivity;
import com.familycircleapp.ui.JoinCircleActivity;
import com.familycircleapp.ui.NewCircleActivity;
import com.familycircleapp.ui.SwitchCircleDialog;
import com.familycircleapp.ui.common.CurrentCircleNameViewModel;
import com.familycircleapp.ui.invite.InviteActivity;
import com.familycircleapp.ui.main.adapter.CircleUserAdapter;
import com.familycircleapp.ui.map.GoogleMapService;
import com.familycircleapp.ui.map.UserModel;
import com.familycircleapp.ui.settings.SettingsActivity;
import com.familycircleapp.utils.Ctx;
import com.familycircleapp.utils.F;
import com.google.android.gms.maps.SupportMapFragment;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static java.util.Arrays.asList;

public final class MainActivity extends AppCompatLifecycleActivity {

  static final int RC_LOCATION_PERMISSION = 1;

  @Inject PermissionManager mPermissionManager;
  @Inject CurrentUser mCurrentUser;
  @Inject ViewModelProvider.Factory mViewModelFactory;
  @Inject BatteryInfoListener mBatteryInfoListener;
  @Inject LocationUpdatesManager mLocationUpdatesManager;
  @Inject GoogleMapService mGoogleMapService;
  @Inject SharedPreferences mSharedPreferences;

  @BindView(R.id.root_layout) CoordinatorLayout mRootLayout;
  @BindView(R.id.app_bar_layout) AppBarLayout mAppBarLayout;
  @BindView(R.id.loader_screen) View mLoaderScreen;
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
  @BindView(R.id.btn_invite) FloatingActionButton mInviteButton;

  private CircleUserAdapter mCircleUserAdapter;
  private Disposable mDisposable;
  private List<Circle> mCurrentUserCircles;
  private String mCurrentCircleId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    App.getComponent().inject(this);
    ButterKnife.bind(this);

    if (!mCurrentUser.isAuthenticated()) {
      Ctx.startActivity(this, EntryPointActivity.class);
      finish();
      return;
    }

    mPermissionManager.requestPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION,
        RC_LOCATION_PERMISSION,
        () -> mLocationUpdatesManager.startLocationUpdates(this)
    );

    final ViewModelProvider viewModelProvider = ViewModelProviders.of(this, mViewModelFactory);
    initMap();
    initBatteryInfoObserving();
    initRecyclerView(viewModelProvider);
    initCurrentCircleUserIdsViewModel(viewModelProvider);
    initCurrentCircleNameViewModel(viewModelProvider);
    initCircleListViewModel(viewModelProvider);
    observeInviteButtonClicks();
  }

  private void observeInviteButtonClicks() {
    mDisposable = RxView
        .clicks(mInviteButton)
        .subscribe(o -> Ctx.startActivity(this, InviteActivity.class));
  }

  private void initCircleListViewModel(final ViewModelProvider viewModelProvider) {
    viewModelProvider
        .get(CircleListViewModel.class)
        .getCircles()
        .observe(this, circles -> mCurrentUserCircles = circles);
  }

  private void initCurrentCircleNameViewModel(final ViewModelProvider viewModelProvider) {
    viewModelProvider
        .get(CurrentCircleNameViewModel.class)
        .getCircle()
        .observe(this, circle -> {
          if (circle != null) {
            mCurrentCircleId = circle.getId();
            setTitle(circle.getName());
          }
        });
  }

  private void initCurrentCircleUserIdsViewModel(final ViewModelProvider viewModelProvider) {
    final LiveData<List<UserModel>> liveData = Transformations.map(
            viewModelProvider.get(CurrentCircleUserIdsViewModel.class).getUserIds(),
            users -> F.map(users, user -> new UserModel(user.getId(), user.getDisplayName()))
    );
    liveData.observe(this, this::onUsersLoaded);
  }

  private void initRecyclerView(final ViewModelProvider viewModelProvider) {
    mCircleUserAdapter = new CircleUserAdapter(
        this,
        viewModelProvider.get(CircleUserViewModel.class)
    );
    mRecyclerView.setAdapter(mCircleUserAdapter);
    final RecyclerView.ItemDecoration itemDecoration =
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
    mRecyclerView.addItemDecoration(itemDecoration);
  }

  private void initBatteryInfoObserving() {
    getLifecycle().addObserver(mBatteryInfoListener);
    final int intervalMinutes = mSharedPreferences.getInt(
        getString(R.string.pref_update_interval),
        getResources().getInteger(R.integer.default_update_interval_minutes)
    );
    UpdateBatteryInfoJob.startJob(TimeUnit.MINUTES.toMillis(intervalMinutes));
  }

  private void initMap() {
    mGoogleMapService.setLifecycleOwner(this);
    getLifecycle().addObserver(mGoogleMapService);
    ((SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map))
        .getMapAsync(mGoogleMapService);
  }

  @Override
  public void onRequestPermissionsResult(
      final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode != RC_LOCATION_PERMISSION) {
      return;
    }

    final int idx = asList(permissions).indexOf(Manifest.permission.ACCESS_FINE_LOCATION);
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
  protected void onDestroy() {
    if (mDisposable != null) {
      mDisposable.dispose();
      mDisposable = null;
    }
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.action_menu, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    final boolean hasSeveralCircles = mCurrentUserCircles != null && mCurrentUserCircles.size() > 1;
    menu.findItem(R.id.action_switch_circle).setVisible(hasSeveralCircles);
    menu.findItem(R.id.action_leave_circle).setVisible(hasSeveralCircles);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_create_circle:
        Ctx.startActivity(this, NewCircleActivity.class);
        return true;

      case R.id.action_join_circle:
        Ctx.startActivity(this, JoinCircleActivity.class);
        return true;

      case R.id.action_switch_circle:
        SwitchCircleDialog
            .getInstance(mCurrentUserCircles)
            .show(getSupportFragmentManager(), null);
        return true;

      case R.id.action_leave_circle:
        final Circle newCircle = F
            .filter(mCurrentUserCircles, circle -> !circle.getId().equals(mCurrentCircleId))
            .get(0);
        mCurrentUser
            .leaveCurrentCircle(newCircle.getId())
            .subscribe((o) -> {
            }, (error) -> Ctx.toast(this, error.getLocalizedMessage()));
        return true;

      case R.id.action_settings:
        Ctx.startActivity(this, SettingsActivity.class);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void onUsersLoaded(final List<UserModel> users) {
    mLoaderScreen.setVisibility(View.GONE);
    mCircleUserAdapter.setData(F.map(users, UserModel::getId));
    mGoogleMapService.putUsersOnMap(users);
    if (mCurrentUser.getId() != null) {
      final float zoom = getResources().getInteger(R.integer.initial_map_zoom);
      final UserModel currentUser =
          F.filter(users, user -> mCurrentUser.getId().equals(user.getId())).get(0);
      mGoogleMapService.moveCameraToUser(currentUser, zoom);
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
