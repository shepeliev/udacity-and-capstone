package com.familycircleapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeviceLocationRepositoryImplTest {

  private static final double LATITUDE = 34;
  private static final double LONGITUDE = 43;
  private static final double ACCURACY = 20;

  @Mock FirebaseDatabase mockFirebaseDatabase;
  @Mock DatabaseReference mockDatabaseReference;
  @Mock DatabaseReference mockLocationsReference;
  @Mock DatabaseReference mockUser1LocationsReference;
  @Mock DatabaseReference mockNewLocationReference;
  DeviceLocationRepositoryImpl mDeviceLocationRepository;

  @Before
  public void setUp() throws Exception {
    when(mockFirebaseDatabase.getReference()).thenReturn(mockDatabaseReference);
    when(mockFirebaseDatabase.getReference("locations")).thenReturn(mockLocationsReference);

    when(mockLocationsReference.child("user_1")).thenReturn(mockUser1LocationsReference);
    when(mockUser1LocationsReference.push()).thenReturn(mockNewLocationReference);
    when(mockNewLocationReference.getKey()).thenReturn("new_location_key");

    mDeviceLocationRepository = new DeviceLocationRepositoryImpl(mockFirebaseDatabase);
  }

  @Test
  public void saveDeviceLocation_shouldSaveDeviceLocation() throws Exception {
    final DeviceLocation deviceLocation = getDeviceLocation();

    mDeviceLocationRepository.saveDeviceLocation("user_1", deviceLocation);

    final HashMap<String, Object> expectedUpdate = new HashMap<String, Object>() {{
      put("/locations/user_1/new_location_key", deviceLocation);
      put("/users/user_1/currentAddress", deviceLocation.getAddress());
      put("/users/user_1/lastKnownLocation", deviceLocation);
    }};
    verify(mockDatabaseReference).updateChildren(eq(expectedUpdate), any());
  }

  public DeviceLocation getDeviceLocation() {
    return new DeviceLocation.Builder()
        .setTime(System.currentTimeMillis())
        .setLatitude(LATITUDE)
        .setLongitude(LONGITUDE)
        .setAccuracy(ACCURACY)
        .setAddress("address")
        .build();
  }
}
