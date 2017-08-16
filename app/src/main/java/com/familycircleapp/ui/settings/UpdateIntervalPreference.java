package com.familycircleapp.ui.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import com.familycircleapp.R;

public class UpdateIntervalPreference extends DialogPreference {

  private static final int DEFAULT_VALUE = 0;
  private static final int MINUTES_IN_HOUR = 60;

  public UpdateIntervalPreference(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    setDialogLayoutResource(R.layout.dialog_update_interval);
    setPositiveButtonText(android.R.string.ok);
    setNegativeButtonText(android.R.string.cancel);
  }

  public static String getSummaryText(@NonNull final Context context, final int intervalMinutes) {
    final int hours = intervalMinutes / MINUTES_IN_HOUR;
    final String interval = intervalMinutes < MINUTES_IN_HOUR ?
        context.getString(R.string.settings_interval_minutes, intervalMinutes) :
        context.getResources().getQuantityString(R.plurals.hour_plurals, hours, hours);

    return context.getString(R.string.settings_update_interval_summary, interval);
  }

  public int getIntervalMinutes() {
    return getPersistedInt(DEFAULT_VALUE);
  }

  public void setIntervalMinutes(final int intervalMinutes) {
    persistInt(intervalMinutes);
    setSummary(getSummaryText(getContext(), intervalMinutes));
  }

  @Override
  protected void onSetInitialValue(final boolean restorePersistedValue, final Object defaultValue) {
    if (!restorePersistedValue) {
      final int initValue = defaultValue != null ? (int) defaultValue : DEFAULT_VALUE;
      setIntervalMinutes(initValue);
    }

    setSummary(getSummaryText(getContext(), getIntervalMinutes()));
  }
}
