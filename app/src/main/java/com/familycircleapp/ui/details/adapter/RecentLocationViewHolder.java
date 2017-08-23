package com.familycircleapp.ui.details.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.familycircleapp.R;
import com.familycircleapp.repository.DeviceLocation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentLocationViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.tw_location_description) TextView mLocationDescription;
  @BindView(R.id.tw_time_range) TextView mTimeRange;

  public RecentLocationViewHolder(@NonNull final ViewGroup parent) {
    super(
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.recent_location_item, parent, false)
    );
    ButterKnife.bind(this, itemView);
  }

  public void bind(@NonNull final DeviceLocation recentLocation) {
    final String description =
        itemView.getContext().getString(R.string.user_status_near, recentLocation.getAddress());
    mLocationDescription.setText(description);
    mTimeRange.setText(DateUtils.getRelativeTimeSpanString(recentLocation.getTime()));
  }
}
