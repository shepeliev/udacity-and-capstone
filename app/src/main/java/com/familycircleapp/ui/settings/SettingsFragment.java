package com.familycircleapp.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.preference.Preference;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.location.LocationUpdatesManager;
import com.firebase.ui.auth.AuthUI;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat {

  private static final String FRAGMENT_DIALOG_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";
  @Inject LocationUpdatesManager mLocationUpdatesManager;

  @Override
  public void onCreatePreferencesFix(
      @Nullable final Bundle savedInstanceState, final String rootKey
  ) {
    App.getComponent().inject(this);
    setPreferencesFromResource(R.xml.preferences, rootKey);

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
}
