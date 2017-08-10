package com.familycircleapp.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.location.LocationUpdatesManager;
import com.firebase.ui.auth.AuthUI;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat {

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
  }
}
