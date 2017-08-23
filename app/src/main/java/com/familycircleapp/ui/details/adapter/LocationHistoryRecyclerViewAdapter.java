package com.familycircleapp.ui.details.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.familycircleapp.repository.DeviceLocation;
import com.familycircleapp.ui.details.LocationHistoryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationHistoryRecyclerViewAdapter
    extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

  private static final int RECENT_LOCATION_VIEW_TYPE = 2;
  private static final int LOADER_VIEW_TYPE = 3;
  private static final int LOCATION_VIEW_TYPE = 4;

  private Map<Long, List<LocationHistoryItem>> mLocationHistory;
  private DeviceLocation mRecentLocation;

  public void setLocationHistory(final Map<Long, List<LocationHistoryItem>> locationHistory) {
    mLocationHistory = locationHistory;
    notifyDataSetChanged();
  }

  public void setRecentLocation(final DeviceLocation recentLocation) {
    mRecentLocation = recentLocation;
    notifyItemChanged(0);
  }

  @Override
  protected int getItemCount(final int section) {
    if (section == 0) {
      return 0;
    } else if (mLocationHistory != null) {
      return new ArrayList<>(mLocationHistory.values()).get(section - 1).size();
    }

    return 0;
  }

  @Override
  protected int getSectionCount() {
    if (mLocationHistory == null) {
      return 2;
    } else {
      return mLocationHistory.size() + 1;
    }
  }

  @Override
  protected int getSectionViewType(final int section) {
    if (section == 0 && mRecentLocation != null) {
      return RECENT_LOCATION_VIEW_TYPE;
    } else if (section == 1 && mLocationHistory == null) {
      return LOADER_VIEW_TYPE;
    }

    return super.getSectionViewType(section);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
    switch (viewType) {
      case SectionedRecyclerViewAdapter.SECTION_HEADER_VIEW_TYPE:
        return new SectionViewHolder(viewGroup);
      case RECENT_LOCATION_VIEW_TYPE:
        return new RecentLocationViewHolder(viewGroup);
      case LOADER_VIEW_TYPE:
        return new LoaderViewHolder(viewGroup);
      default:
        return new LocationViewHolder(viewGroup);
    }
  }

  @Override
  protected void onBindSectionHeaderViewHolder(
      final RecyclerView.ViewHolder holder, final int section
  ) {
    switch (section) {
      case 0:
        if (holder instanceof RecentLocationViewHolder) {
          ((RecentLocationViewHolder) holder).bind(mRecentLocation);
        }
        break;
      default:
        if (mLocationHistory != null) {
          ((SectionViewHolder) holder)
              .bind(new ArrayList<>(mLocationHistory.keySet()).get(section - 1));
        }
    }
  }

  @Override
  protected void onBindViewHolder(
      final RecyclerView.ViewHolder holder,
      final int section,
      final int sectionPosition,
      final int absolutePosition
  ) {
    final List<LocationHistoryItem> sectionData =
        new ArrayList<>(mLocationHistory.values()).get(section - 1);
    ((LocationViewHolder) holder).bind(sectionData.get(sectionPosition));
  }
}
