package com.familycircleapp.ui;

import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.ViewTreeObserver;

import com.familycircleapp.R;
import com.familycircleapp.ui.map.GoogleMapService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class MapWithRecyclerViewActivity extends AppCompatLifecycleActivity {

  @Inject GoogleMapService mGoogleMapService;

  @BindView(R.id.root_layout) CoordinatorLayout mRootLayout;
  @BindView(R.id.app_bar_layout) AppBarLayout mAppBarLayout;

  @Override
  @CallSuper
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    inject();
    ButterKnife.bind(this);

    mGoogleMapService.setLifecycleOwner(this);
    getLifecycle().addObserver(mGoogleMapService);

    ((SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map))
        .getMapAsync(mGoogleMapService);
  }

  @Override
  protected void onStart() {
    super.onStart();
    adjustMapHeight();
    fixMovingMap();
  }

  protected abstract void inject();

  @LayoutRes
  protected abstract int getLayoutId();


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
