package com.familycircleapp.ui.details.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.familycircleapp.R;
import com.familycircleapp.ui.details.LocationHistoryItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.tw_location_description) TextView mLocationDescription;
  @BindView(R.id.tw_time_range) TextView mTimeRange;

  public LocationViewHolder(@NonNull final ViewGroup parent) {
    super(
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.location_item, parent, false)
    );
    ButterKnife.bind(this, itemView);
  }

  public void bind(final LocationHistoryItem locationHistoryItem) {
    final Context context = itemView.getContext();
    final String description = context.getString(
        R.string.user_status_near, locationHistoryItem.getAddress()
    );
    mLocationDescription.setText(description);
    mTimeRange.setText(
        DateUtils.formatDateRange(
            context,
            locationHistoryItem.getStartTime(),
            locationHistoryItem.getEndTime(),
            DateUtils.FORMAT_SHOW_TIME
        )
    );
  }
}
