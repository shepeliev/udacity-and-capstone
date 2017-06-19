package com.familycircleapp.ui.main.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.familycircleapp.R;
import com.familycircleapp.databinding.UserListItemBinding;
import com.familycircleapp.ui.main.CircleUser;
import com.familycircleapp.ui.main.CircleUserViewModel;

public final class CircleUserViewHolder extends RecyclerView.ViewHolder {

  private final LifecycleOwner mLifecycleOwner;
  private final CircleUserViewModel mCircleUserViewModel;
  private final UserListItemBinding mBinding;
  private LiveData<CircleUser> mUserLiveData;

  public CircleUserViewHolder(
      @NonNull final ViewGroup parent,
      @NonNull final LifecycleOwner lifecycleOwner,
      @NonNull final CircleUserViewModel circleUserViewModel
  ) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false));
    mLifecycleOwner = lifecycleOwner;
    mCircleUserViewModel = circleUserViewModel;
    mBinding = UserListItemBinding.bind(itemView);
  }

  public void onBind(final String userId) {
    if (mUserLiveData != null) {
      throw new IllegalStateException("Previous live data observer has not been removed");
    }

    mUserLiveData = mCircleUserViewModel.getCircleUser(userId);
    mUserLiveData.observe(mLifecycleOwner, mBinding::setUserViewModel);
  }

  public void onRecycled() {
    mUserLiveData.removeObservers(mLifecycleOwner);
    mUserLiveData = null;
  }
}
