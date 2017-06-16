package com.familycircleapp.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.familycircleapp.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GeocoderServiceImplTest {

  @Mock Context mockContext;
  @Mock Geocoder mockGeocoder;
  @InjectMocks GeocoderServiceImpl mGeocoderService;

  @Before
  public void setUp() throws Exception {
    when(mockContext.getString(R.string.na)).thenReturn("N/A");
  }

  @Test
  public void fetchAddress_shouldReturnAddress() throws Exception {
    final Address mockAddress = mock(Address.class);
    when(mockAddress.getMaxAddressLineIndex()).thenReturn(4);
    when(mockAddress.getAddressLine(0)).thenReturn("address line 0");
    when(mockAddress.getAddressLine(1)).thenReturn("address line 1");
    final List<Address> addresses = Collections.singletonList(mockAddress);
    when(mockGeocoder.getFromLocation(34, 43, 1)).thenReturn(addresses);

    assertEquals(
        "address line 0, address line 1",
        mGeocoderService.fetchAddress(34, 43, 2)
    );
  }

  @Test
  public void fetchAddress_noAddressLines_shouldReturnNA() throws Exception {
    final Address mockAddress = mock(Address.class);
    when(mockAddress.getMaxAddressLineIndex()).thenReturn(-1);
    final List<Address> addresses = Collections.singletonList(mockAddress);
    when(mockGeocoder.getFromLocation(34, 43, 1)).thenReturn(addresses);

    assertEquals("N/A", mGeocoderService.fetchAddress(34, 43, 1));
  }

  @Test
  public void fetchAddress_geocoderNotAvailable_shouldReturnNA() throws Exception {
    when(mockGeocoder.getFromLocation(34, 43, 1)).thenThrow(IOException.class);

    assertEquals("N/A", mGeocoderService.fetchAddress(34, 43, 1));
  }
}
