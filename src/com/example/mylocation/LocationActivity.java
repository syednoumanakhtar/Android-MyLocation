package com.example.mylocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity implements LocationListener {
  private TextView latituteField;
  private TextView longitudeField;
  private TextView locaddress;
  private LocationManager locationManager;
  private String provider;
  float lat;
  float lng;

  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.location);
    latituteField = (TextView) findViewById(R.id.TextView02);
    longitudeField = (TextView) findViewById(R.id.TextView04);
    locaddress = (TextView) findViewById(R.id.TextView06);
    
    // Get the location manager
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Define the criteria how to select the locatioin provider -> use
    // default
    Criteria criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    Location location = locationManager.getLastKnownLocation(provider);

    // Initialize the location fields
    if (location != null) {
      System.out.println("Provider " + provider + " has been selected.");
      onLocationChanged(location);
    } else {
      latituteField.setText("Location not available");
      longitudeField.setText("Location not available");
    }
  }

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(provider, 400, 1, this);
  }

  /* Remove the location listener updates when Activity is paused */
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  @Override
  public void onLocationChanged(Location location) {
	lat = (float) (location.getLatitude());
    lng = (float) (location.getLongitude());
    latituteField.setText(String.valueOf(lat));
    longitudeField.setText(String.valueOf(lng));
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub

  }
  
  public void getaddress(View view) {
      // Do something in response to button
	  String s=getAddress(lat,lng);
	  locaddress.setText(s);
  }
  
  private String getAddress(double latitude, double longitude) {
      StringBuilder result = new StringBuilder();
      try {
          Geocoder geocoder = new Geocoder(this, Locale.getDefault());
          List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
          if (addresses.size() > 0) {
        	  Address returnedAddress = addresses.get(0);
              for (int i = 1; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                  result.append(returnedAddress.getAddressLine(i)).append("\n");
              }
          }
      } catch (IOException e) {
          Log.e("tag", e.getMessage());
      }     
      return result.toString();
  }

  @Override
  public void onProviderEnabled(String provider) {
    Toast.makeText(this, "Enabled new provider " + provider,
        Toast.LENGTH_SHORT).show();

  }

  @Override
  public void onProviderDisabled(String provider) {
    Toast.makeText(this, "Disabled provider " + provider,
        Toast.LENGTH_SHORT).show();
  }
} 