package zunair.syed.trackyourones.util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.location.LocationListener;

public class LocationFinder implements LocationListener{

	Context context;

	private LocationManager locationManager;
	private String provider;
	private boolean locationNotAvailable;


	private String fullAddress;
	private String city; 

	public LocationFinder(Context context){
		this.context = context;

		// Get the location manager
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		// Define the criteria how to select the locatioin provider -> use default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			onLocationChanged(location);
		} else {
			locationNotAvailable = true;
		}
	}

	public String getFullAddress(){
		return fullAddress;
	}
	
	public String getCity(){
		return city;
	}
	
	public boolean getLocationNotAvailable(){
		return locationNotAvailable;
	}

	public String getLastKnownLocationCity(){
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		

		// Initialize the location fields
		if (location != null) {
			onLocationChanged(location);
		} else {
			locationNotAvailable = true;
		}
		return city;
	}
	
	@Override
	public void onLocationChanged(Location location) {

		locationNotAvailable = false;

		//Get Lat and Long
		double lat = (location.getLatitude());
		double lng =  (location.getLongitude());

		//Get Address and city from full address
		fullAddress = getLocationAddress(lat,lng);
		city = fullAddress.split("_")[1].toString();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public String getLocationAddress(double latitude, double longitude){
		Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

		try {
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

			if(addresses != null) {
				Address returnedAddress = addresses.get(0);
				String locality = returnedAddress.getLocality();
				StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
				for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				return strReturnedAddress.toString() + "_"+ locality;
			}
			else{
				return "No Address returned!";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Can not get Address!" + e.getMessage();
		}	  

	}


	public void getRequestLocationUpdates(){
		locationManager.requestLocationUpdates(provider, 400, 100, this);
	}

	public void getRequestRemoveUpdates(){
		locationManager.removeUpdates(this);
	}

}
