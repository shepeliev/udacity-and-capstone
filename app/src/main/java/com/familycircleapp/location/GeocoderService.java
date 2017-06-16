package com.familycircleapp.location;

public interface GeocoderService {

  String fetchAddress(final double latitude, final double longitude) throws GeocoderServiceException;
}
