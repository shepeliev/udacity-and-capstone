package com.familycircleapp.ui.main.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.familycircleapp.R;
import com.familycircleapp.databinding.UserListItemBinding;
import com.familycircleapp.ui.main.CircleUser;

public final class CircleUserViewHolder extends RecyclerView.ViewHolder
    implements Observer<CircleUser> {

  private final LifecycleOwner mLifecycleOwner;
  private final UserListItemBinding mBinding;
  private LiveData<CircleUser> mUserLiveData;

  public CircleUserViewHolder(final LifecycleOwner lifecycleOwner, final ViewGroup parent) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false));
    mLifecycleOwner = lifecycleOwner;
    mBinding = UserListItemBinding.bind(itemView);
  }

  public void onBind(final LiveData<CircleUser> userLiveData) {
    if (mUserLiveData != null) {
      throw new IllegalStateException("Previous live data observer has not been removed");
    }

    mUserLiveData = userLiveData;
    mUserLiveData.observe(mLifecycleOwner, this);
  }

  public void onRecycled() {
    mUserLiveData.removeObserver(this);
    mUserLiveData = null;
  }

  @Override
  public void onChanged(@Nullable final CircleUser circleUser) {
    mBinding.setUserViewModel(circleUser);
  }
}
