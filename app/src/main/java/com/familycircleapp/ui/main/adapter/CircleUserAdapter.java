package com.familycircleapp.ui.main.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.familycircleapp.ui.main.CircleUserViewModel;

import java.util.Collections;
import java.util.List;

public final class CircleUserAdapter extends RecyclerView.Adapter<CircleUserViewHolder> {

  private final LifecycleOwner mLifecycleOwner;
  private final CircleUserViewModel mCircleUserViewModel;
  private List<String> mUserIds = Collections.emptyList();

  public CircleUserAdapter(
      @NonNull final LifecycleOwner lifecycleOwner,
      @NonNull final CircleUserViewModel circleUserViewModel
  ) {
    mLifecycleOwner = lifecycleOwner;
    mCircleUserViewModel = circleUserViewModel;
  }

  @Override
  public CircleUserViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
    return new CircleUserViewHolder(parent, mLifecycleOwner, mCircleUserViewModel);
  }

  @Override
  public void onBindViewHolder(final CircleUserViewHolder holder, final int position) {
    holder.onBind(mUserIds.get(position));
  }

  @Override
  public void onViewRecycled(final CircleUserViewHolder holder) {
    holder.onRecycled();
  }

  @Override
  public int getItemCount() {
    return mUserIds.size();
  }

  public void setData(final List<String> users) {
    mUserIds = users;
    notifyDataSetChanged();
  }
}
