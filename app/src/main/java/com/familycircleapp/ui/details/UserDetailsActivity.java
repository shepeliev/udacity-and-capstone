package com.familycircleapp.ui.details;

import com.google.android.gms.maps.SupportMapFragment;

import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewTreeObserver;

import com.familycircleapp.App;
import com.familycircleapp.BuildConfig;
import com.familycircleapp.R;
import com.familycircleapp.ui.AppCompatLifecycleActivity;
import com.familycircleapp.ui.details.adapter.LocationHistoryRecyclerViewAdapter;
import com.familycircleapp.ui.map.GoogleMapService;
import com.familycircleapp.ui.map.UserModel;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class UserDetailsActivity extends AppCompatLifecycleActivity {

  public static final String EXTRA_USER_ID = BuildConfig.APPLICATION_ID + ".USER_ID";

  @Inject ViewModelProvider.Factory mViewModelFactory;
  @Inject GoogleMapService mGoogleMapService;

  @BindView(R.id.root_layout_2) CoordinatorLayout mRootLayout;
  @BindView(R.id.app_bar_layout_2) AppBarLayout mAppBarLayout;
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

  private LocationHistoryRecyclerViewAdapter mRecyclerViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_details);
    App.getComponent().inject(this);
    ButterKnife.bind(this);

    initRecyclerView();
    initMap();

    final String userId = getUserId();
    final ViewModelProvider modelProvider = ViewModelProviders.of(this, mViewModelFactory);
    initUserModel(modelProvider, userId);
    initLastLocationModel(modelProvider, userId);
    initLocationHistoryViewModel(modelProvider, userId);
  }

  private void initUserModel(final ViewModelProvider modelProvider, final String userId) {
    Transformations.map(
        modelProvider.get(UserViewModel.class).getUser(userId),
        user -> new UserModel(userId, user.getDisplayName())
    )
        .observe(this, this::showUserOnMap);
  }

  private void initLocationHistoryViewModel(
      final ViewModelProvider modelProvider, final String userId
  ) {
    modelProvider.get(LocationHistoryViewModel.class)
        .getLocationHistory(userId)
        .observe(this, mRecyclerViewAdapter::setLocationHistory);
  }

  private void initLastLocationModel(final ViewModelProvider modelProvider, final String userId) {
    modelProvider.get(LastLocationViewModel.class)
        .getLastLocation(userId)
        .observe(this, mRecyclerViewAdapter::setRecentLocation);
  }

  private void initRecyclerView() {
    mRecyclerViewAdapter = new LocationHistoryRecyclerViewAdapter();
    mRecyclerView.setAdapter(mRecyclerViewAdapter);
    final DividerItemDecoration decoration =
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
    mRecyclerView.addItemDecoration(decoration);
  }

  private void showUserOnMap(final UserModel user) {
    mGoogleMapService.putUsersOnMap(Collections.singletonList(user));
    final float zoom = getResources().getInteger(R.integer.initial_map_zoom);
    mGoogleMapService.moveCameraToUser(user, zoom);
  }

  private void initMap() {
    mGoogleMapService.setLifecycleOwner(this);
    getLifecycle().addObserver(mGoogleMapService);
  }

  @NonNull
  private String getUserId() {
    final String userId = getIntent().getStringExtra(EXTRA_USER_ID);
    if (TextUtils.isEmpty(userId)) {
      throw new IllegalStateException(EXTRA_USER_ID + " must contain the user ID");
    }
    return userId;
  }

  @Override
  protected void onStart() {
    super.onStart();
    ((SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map_2))
        .getMapAsync(mGoogleMapService);

    fixMovingMap();
    adjustMapHeight();
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
