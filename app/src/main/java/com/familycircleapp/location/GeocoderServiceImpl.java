package com.familycircleapp.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.familycircleapp.R;

import java.io.IOException;
import java.util.List;

final class GeocoderServiceImpl implements GeocoderService {

  private static final int MAX_RESULTS = 1;

  private final Context mContext;
  private final Geocoder mGeocoder;

  GeocoderServiceImpl(final Context context, final Geocoder geocoder) {
    mContext = context;
    mGeocoder = geocoder;
  }

  @Override
  public String fetchAddress(
      final double latitude, final double longitude, final int maxAddressLines
  ) {
    final String naText = mContext.getString(R.string.na);
    try {
      final List<Address> addresses = mGeocoder.getFromLocation(latitude, longitude, MAX_RESULTS);
      return getAddress(addresses, maxAddressLines, naText);
    } catch (IOException e) {
      return naText;
    }
  }

  private String getAddress(
      final List<Address> addresses, final int maxAddressLines, final String defaultValue
  ) {
    if (addresses == null || addresses.isEmpty()) {
      return defaultValue;
    }

    final Address address = addresses.get(0);
    final int maxIndex = Math.min(address.getMaxAddressLineIndex(), maxAddressLines - 1);
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i <= maxIndex; i++) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(address.getAddressLine(i));
    }

    if (sb.length() == 0) {
      sb.append(defaultValue);
    }

    return sb.toString();
  }
}
