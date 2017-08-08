package com.familycircleapp.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.utils.F;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SwitchCircleDialog extends DialogFragment {

  private static final String ARG_CIRCLE_LIST = "circleList";

  @Inject CurrentUser mCurrentUser;

  public SwitchCircleDialog() {
    App.getComponent().inject(this);
  }

  public static SwitchCircleDialog getInstance(@NonNull final List<Circle> circles) {
    final SwitchCircleDialog dialog = new SwitchCircleDialog();
    dialog.fillArguments(circles);
    return dialog;
  }

  private void fillArguments(final List<Circle> circles) {
    final Bundle args = new Bundle();
    args.putParcelableArrayList(
        ARG_CIRCLE_LIST,
        new ArrayList<>(F.map(circles, circle -> new Item(circle.getId(), circle.getName())))
    );
    setArguments(args);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(final Bundle savedInstanceState) {
    final ArrayList<Item> items = getArguments().getParcelableArrayList(ARG_CIRCLE_LIST);
    if (items == null) {
      throw new NullPointerException("circle list should not be null");
    }

    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.switch_circle_dialog_title)
        .setItems(
            F.map(items, Item::getName).toArray(new String[items.size()]),
            (dialog, which) -> mCurrentUser.joinCircle(items.get(which).mId).subscribe()
        );

    return builder.create();
  }

  private static class Item implements Parcelable {

    public static final Creator<Item> CREATOR = new Creator<Item>() {
      @Override
      public Item createFromParcel(Parcel in) {
        return new Item(in);
      }

      @Override
      public Item[] newArray(int size) {
        return new Item[size];
      }
    };

    private final String mId;
    private final String mName;

    Item(final String id, final String name) {
      mId = id;
      mName = name;
    }

    Item(Parcel in) {
      mId = in.readString();
      mName = in.readString();
    }

    public String getId() {
      return mId;
    }

    public String getName() {
      return mName;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
      dest.writeString(mId);
      dest.writeString(mName);
    }
  }
}
