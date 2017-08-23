package com.familycircleapp.ui.details.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.familycircleapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.tw_section_header) TextView mHeader;

  public SectionViewHolder(@NonNull final ViewGroup parent) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.section_header, parent, false));
    ButterKnife.bind(this, itemView);
  }

  public void bind(final long time) {
    if (DateUtils.isToday(time)) {
      mHeader.setText(R.string.today);
    } else if (DateUtils.isToday(time + DateUtils.DAY_IN_MILLIS)) {
      mHeader.setText(DateUtils.getRelativeTimeSpanString(time));
    } else {
      mHeader.setText(
          DateUtils.getRelativeTimeSpanString(
              time,
              System.currentTimeMillis() + DateUtils.WEEK_IN_MILLIS,
              DateUtils.MINUTE_IN_MILLIS,
              DateUtils.FORMAT_SHOW_DATE
          )
      );
    }
  }
}
