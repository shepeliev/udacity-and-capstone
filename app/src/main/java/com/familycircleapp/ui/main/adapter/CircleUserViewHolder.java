package com.familycircleapp.ui.main.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.familycircleapp.R;
import com.familycircleapp.ui.BindingAdapters;
import com.familycircleapp.ui.details.UserDetailsActivity;
import com.familycircleapp.ui.main.CircleUser;
import com.familycircleapp.ui.main.CircleUserViewModel;
import com.familycircleapp.utils.Ctx;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public final class CircleUserViewHolder extends RecyclerView.ViewHolder {

  private final LifecycleOwner mLifecycleOwner;
  private final CircleUserViewModel mCircleUserViewModel;

  @BindView(R.id.user_list_item) View mUserListItem;
  @BindView(R.id.iw_avatar) ImageView mAvatar;
  @BindView(R.id.tw_user_displayed_name) TextView mDisplayName;
  @BindView(R.id.tw_user_status) TextView mUserStatus;
  @BindView(R.id.tw_battery_status) ImageView mBatteryStatus;
  @BindView(R.id.tw_battery_level) TextView mBatteryLevel;

  private LiveData<CircleUser> mUserLiveData;
  private Disposable mDisposable;

  public CircleUserViewHolder(
      @NonNull final ViewGroup parent,
      @NonNull final LifecycleOwner lifecycleOwner,
      @NonNull final CircleUserViewModel circleUserViewModel
  ) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false));
    mLifecycleOwner = lifecycleOwner;
    mCircleUserViewModel = circleUserViewModel;
    ButterKnife.bind(this, itemView);
  }

  public void onBind(final String userId) {
    if (mUserLiveData != null) {
      throw new IllegalStateException("Previous live data observer has not been removed");
    }

    mUserLiveData = mCircleUserViewModel.getCircleUser(userId);
    mUserLiveData.observe(mLifecycleOwner, this::bind);
  }

  private void bind(final CircleUser circleUser) {
    final Context context = itemView.getContext();
    BindingAdapters.bindAvatar(mAvatar, circleUser.getDisplayName());
    mDisplayName.setText(circleUser.getDisplayName());
    mUserStatus.setText(
        context.getString(circleUser.getStatusTemplate(), circleUser.getStatusText())
    );
    BindingAdapters.bindBatteryInfo(
        mBatteryStatus,
        circleUser.getBatteryStatus(),
        circleUser.getBatteryLevel()
    );
    mBatteryLevel.setText(
        context.getString(R.string.battery_level, circleUser.getBatteryLevel())
    );
    mBatteryLevel.setVisibility(circleUser.getBatteryLevel() > -1 ? View.VISIBLE : View.GONE);

    final Map<String, String> args = new HashMap<String, String>() {{
      put(UserDetailsActivity.EXTRA_USER_ID, circleUser.getId());
    }};
    mDisposable = RxView.clicks(mUserListItem).subscribe(aVoid -> {
          Timber.d("Click on user ID: %s", circleUser.getId());
          Ctx.startActivity(context, args, UserDetailsActivity.class);
        }
    );
  }

  public void onRecycled() {
    if (mUserLiveData != null) {
      mUserLiveData.removeObservers(mLifecycleOwner);
      mUserLiveData = null;
    }
    if (mDisposable != null) {
      mDisposable.dispose();
      mDisposable = null;
    }
  }
}
