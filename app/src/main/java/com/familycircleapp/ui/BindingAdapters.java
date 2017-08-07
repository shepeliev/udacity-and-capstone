package com.familycircleapp.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.familycircleapp.R;

public final class BindingAdapters {

  private static final int[] BATTERY_DRAWABLE_RESOURCES = {
      R.drawable.battery_0,
      R.drawable.battery_10,
      R.drawable.battery_20,
      R.drawable.battery_30,
      R.drawable.battery_40,
      R.drawable.battery_50,
      R.drawable.battery_60,
      R.drawable.battery_70,
      R.drawable.battery_80,
      R.drawable.battery_90,
      R.drawable.battery_100
  };

  private BindingAdapters() {
    throw new UnsupportedOperationException();
  }

  public static void bindTemplateString(final TextView view, final int template, final Object arg) {
    if (template == 0) {
      return;
    }

    final Context context = view.getContext();
    view.setText(context.getString(template, arg));
  }

  @DrawableRes
  public static int batteryImageResource(final String batteryStatus, final int batteryLevel) {
    switch (batteryStatus) {
      case "charging":
        return R.drawable.battery_charging;
      default:
        final int index = Math.round(batteryLevel / 10f);
        return BATTERY_DRAWABLE_RESOURCES[index];
    }
  }

  public static void bindBatteryInfo(
      final ImageView view, final String batteryStatus, final int batteryLevel
  ) {
    if (batteryStatus == null) {
      return;
    }

    final Context context = view.getContext();

    if (batteryLevel >= 0) {
      view.setContentDescription(
          context.getString(R.string.content_description_battery_status, batteryStatus)
      );
      view.setImageResource(batteryImageResource(batteryStatus, batteryLevel));
    }
  }

  public static void bindAvatar(final ImageView view, final String name) {
    final TextDrawable drawable = drawableAvatar(name);
    view.setImageDrawable(drawable);
  }

  public static TextDrawable drawableAvatar(final String name) {
    final String initial = TextUtils.isEmpty(name) ? "-" : name.substring(0, 1);
    final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    final int color = colorGenerator.getColor(name != null ? name : "-");
    return TextDrawable.builder().buildRound(initial, color);
  }
}
