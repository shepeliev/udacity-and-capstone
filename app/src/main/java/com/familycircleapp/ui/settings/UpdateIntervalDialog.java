package com.familycircleapp.ui.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.familycircleapp.R;
import com.jakewharton.rxbinding2.widget.RxSeekBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static java.util.Arrays.asList;

public class UpdateIntervalDialog extends PreferenceDialogFragmentCompat {

  private static final List<Integer> INTERVALS = asList(15, 60, 180, 720, 1440);

  @BindView(R.id.seek_bar_update_interval) SeekBar mUpdateInterval;
  @BindView(R.id.tw_update_interval_description) TextView mUpdateIntervalDescription;

  private Disposable mDisposable;

  public static UpdateIntervalDialog getInstance(@NonNull final String key) {
    final UpdateIntervalDialog dialog = new UpdateIntervalDialog();
    final Bundle args = new Bundle();
    args.putString(ARG_KEY, key);
    dialog.setArguments(args);
    return dialog;
  }

  @Override
  public void onDialogClosed(final boolean positiveResult) {
    if (mDisposable != null) {
      mDisposable.dispose();
      mDisposable = null;
    }

    final int interval = INTERVALS.get(mUpdateInterval.getProgress());
    final UpdateIntervalPreference updateIntervalPreference = getUpdateIntervalPreference();
    if (positiveResult && updateIntervalPreference.callChangeListener(interval)) {
      updateIntervalPreference.setIntervalMinutes(interval);
    }
  }

  @Override
  protected void onBindDialogView(final View view) {
    super.onBindDialogView(view);
    ButterKnife.bind(this, view);

    mUpdateInterval.setMax(INTERVALS.size() -1);
    mUpdateInterval.setProgress(
        INTERVALS.indexOf(getUpdateIntervalPreference().getIntervalMinutes())
    );
    mDisposable = RxSeekBar.changes(mUpdateInterval).subscribe(position -> {
      final int interval = INTERVALS.get(position);
      mUpdateIntervalDescription
          .setText(UpdateIntervalPreference.getSummaryText(getContext(), interval));
    });
  }

  private UpdateIntervalPreference getUpdateIntervalPreference() {
    return (UpdateIntervalPreference) getPreference();
  }
}
