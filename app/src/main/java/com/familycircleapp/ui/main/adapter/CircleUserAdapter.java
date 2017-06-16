package com.familycircleapp.ui.main.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.familycircleapp.ui.main.CircleUser;

import java.util.Collections;
import java.util.List;

public final class CircleUserAdapter extends RecyclerView.Adapter<CircleUserViewHolder> {

  private final LifecycleOwner mLifecycleOwner;
  private List<LiveData<CircleUser>> mUsers = Collections.emptyList();

  public CircleUserAdapter(final LifecycleOwner lifecycleOwner) {
    mLifecycleOwner = lifecycleOwner;
  }

  @Override
  public CircleUserViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
    return new CircleUserViewHolder(mLifecycleOwner, parent);
  }

  @Override
  public void onBindViewHolder(final CircleUserViewHolder holder, final int position) {
    holder.onBind(mUsers.get(position));
  }

  @Override
  public void onViewRecycled(final CircleUserViewHolder holder) {
    holder.onRecycled();
  }

  @Override
  public int getItemCount() {
    return mUsers.size();
  }

  public void setData(final List<LiveData<CircleUser>> users) {
    mUsers = users;
    notifyDataSetChanged();
  }
}
