package com.familycircleapp.ui.details.adapter;

import android.support.v7.widget.RecyclerView;

import com.familycircleapp.utils.F;

import java.util.ArrayList;
import java.util.List;

abstract public class SectionedRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {

  public static final int SECTION_HEADER_VIEW_TYPE = 1;

  @Override
  public final int getItemCount() {
    final int sectionCount = getSectionCount();
    int itemCount = 0;
    for (int i = 0; i < sectionCount; i++) {
      itemCount += getItemCount(i);
    }
    return sectionCount + itemCount;
  }

  @Override
  public final int getItemViewType(final int position) {
    final List<Integer> dataModel = getDataModel();
    final int section = getSection(dataModel, position);

    if (isSectionHeader(dataModel, position)) {
      return getSectionViewType(section);
    } else {
      final int sectionPosition = getSectionPosition(dataModel, position);
      final int absolutePosition = getAbsolutePosition(dataModel, position);
      return getItemViewType(section, sectionPosition, absolutePosition);
    }
  }

  @Override
  public final void onBindViewHolder(final VH holder, final int position) {
    final List<Integer> dataModel = getDataModel();

    final int section = getSection(dataModel, position);
    if (isSectionHeader(dataModel, position)) {
      onBindSectionHeaderViewHolder(holder, section);
    } else {
      onBindViewHolder(
          holder,
          section,
          getSectionPosition(dataModel, position),
          getAbsolutePosition(dataModel, position)
      );
    }
  }

  private List<Integer> getDataModel() {
    final List<Integer> sections = new ArrayList<>();
    for (int i = 0; i < getSectionCount(); i++) {
      sections.add(getItemCount(i));
    }

    final List<Integer> model = new ArrayList<>();
    for (int i = 0; i < sections.size(); i++) {
      model.add(-1);
      final List<Integer> sectionModel = new ArrayList<>();
      for (int j = 0; j < sections.get(i); j++) {
        sectionModel.add(i);
      }
      model.addAll(sectionModel);
    }

    return model;
  }

  private boolean isSectionHeader(final List<Integer> dataModel, final int position) {
    return dataModel.get(position) == -1;
  }

  private int getSection(final List<Integer> dataModel, final int position) {
    if (position == 0) {
      return 0;
    }

    if (!isSectionHeader(dataModel, position)) {
      return dataModel.get(position);
    }

    return getSection(dataModel, position - 1) + 1;
  }


  private int getSectionPosition(final List<Integer> dataModel, final int position) {
    return getAbsolutePosition(dataModel, position) -
        F.filter(dataModel, it -> it > -1 && it < getSection(dataModel, position)).size();
  }

  private int getAbsolutePosition(final List<Integer> dataModel, final int position) {
    return position - getSection(dataModel, position) - 1;
  }

  protected abstract int getItemCount(final int section);

  protected abstract int getSectionCount();

  protected  int getSectionViewType(final int section) {
    return SECTION_HEADER_VIEW_TYPE;
  }

  protected int getItemViewType(
      final int section,
      final int sectionPosition,
      final int absolutePosition
  ) {
    return 0;
  }

  protected abstract void onBindSectionHeaderViewHolder(final VH holder, final int section);

  protected abstract void onBindViewHolder(
      final VH holder,
      final int section,
      final int sectionPosition,
      final int absolutePosition
  );
}
