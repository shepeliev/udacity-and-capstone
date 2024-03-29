package com.familycircleapp.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.preference.Preference;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.battery.UpdateBatteryInfoJob;
import com.familycircleapp.location.LocationUpdatesManager;
import com.firebase.ui.auth.AuthUI;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;

public class SettingsFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  private static final String FRAGMENT_DIALOG_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";

  @Inject LocationUpdatesManager mLocationUpdatesManager;
  @Inject SharedPreferences mSharedPreferences;

  @BindString(R.string.pref_update_interval) String mUpdateIntervalPrefKey;

  @Override
  public void onCreatePreferencesFix(
      @Nullable final Bundle savedInstanceState, final String rootKey
  ) {
    App.getComponent().inject(this);
    setPreferencesFromResource(R.xml.preferences, rootKey);
    ButterKnife.bind(this, getActivity());

    findPreference(getString(R.string.pref_logout)).setOnPreferenceClickListener(
        preference -> {
          AuthUI.getInstance().signOut(getActivity()).addOnSuccessListener(
              aVoid -> {
                mLocationUpdatesManager.stopLocationUpdates(getActivity());
                NavUtils.navigateUpFromSameTask(getActivity());
              }
          );
          return true;
        }
    );

    findPreference(getString(R.string.pref_delete_account)).setOnPreferenceClickListener(
        preference -> {
          new DeleteAccountDialog().show(getFragmentManager(), null);
          return true;
        }
    );

    mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    super.onDestroy();
  }

  @Override
  public void onDisplayPreferenceDialog(final Preference preference) {
    final FragmentManager fragmentManager = getFragmentManager();
    if (fragmentManager.findFragmentByTag(FRAGMENT_DIALOG_TAG) != null) {
      return;
    }

    if (preference instanceof UpdateIntervalPreference) {
      final UpdateIntervalDialog dialog = UpdateIntervalDialog.getInstance(preference.getKey());
      dialog.setTargetFragment(this, 0);
      dialog.show(fragmentManager, FRAGMENT_DIALOG_TAG);
    } else {
      super.onDisplayPreferenceDialog(preference);
    }
  }

  @Override
  public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
    if (mUpdateIntervalPrefKey.equals(key)) {
      mLocationUpdatesManager.startLocationUpdates(getActivity());
      UpdateBatteryInfoJob.cancelJob();

      final int intervalMinutes = mSharedPreferences.getInt(
          getString(R.string.pref_update_interval),
          getResources().getInteger(R.integer.default_update_interval_minutes)
      );
      UpdateBatteryInfoJob.startJob(TimeUnit.MINUTES.toMillis(intervalMinutes));
    }
  }
}
