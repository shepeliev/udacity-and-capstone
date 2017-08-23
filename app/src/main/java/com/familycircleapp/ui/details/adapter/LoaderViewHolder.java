package com.familycircleapp.ui.details.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.familycircleapp.R;

public class LoaderViewHolder extends RecyclerView.ViewHolder {

  public LoaderViewHolder(@NonNull final ViewGroup parent) {
    super(
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.loader_item, parent, false)
    );
  }
}
