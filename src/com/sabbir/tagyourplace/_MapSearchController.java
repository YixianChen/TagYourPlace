package com.sabbir.tagyourplace;

import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import android.util.Log;
import android.widget.Toast;

//An asynchronous task is defined by a computation that runs on a background thread and whose result is published on the UI thread.
public class _MapSearchController {

	private String searchPlace;
	private Address addressOfPlaces;
	private Context context;
	private GoogleMap GMap;
	private static int MAXIMUM_RESULTS=1;
	//Constructor
	public _MapSearchController(Context ApplicationContext, GoogleMap googleMap, String toSearch) 
	{
		context = ApplicationContext;
		GMap = googleMap;
		searchPlace = toSearch;
	}
	
	 boolean doInBackground() 
	{
		// TODO Auto-generated method stub
		try {
				//Initialize geocoder to transform a street address or other description of a location into a (latitude, longitude) coordinate.
				Geocoder geocoder = new Geocoder(context,Locale.getDefault());
				//Get addresses that are known to describe the named location
				List<Address> results= geocoder.getFromLocationName(searchPlace, MAXIMUM_RESULTS);
				
				//If no address is found return false
				if(results.size()==0)
					return false;
				//Get the first address from addresses
				addressOfPlaces=results.get(0);
				//Convert address to LatLng
				LatLng searchedPosition= new LatLng(addressOfPlaces.getLatitude() , addressOfPlaces.getLongitude());
				//Place a pin in google map for that position
				DrawMarkerfromPositionOnGMap(searchedPosition);
				Log.w("MapSearchController.Latitude",""+addressOfPlaces.getLatitude() );
			} catch (Exception e) 
				{
					Log.e("MapSearchController.doInBackground", "Something went wrong: ", e);
					return false;
				}
		return true;
	}
	
	void DrawMarkerfromPositionOnGMap(LatLng Position )
	{
		GMap.addMarker(new MarkerOptions()
			.position(Position)
			.snippet("Lat:" + Position.latitude + " Long:"+ Position.longitude)
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
			.title("Your searched result"));
	}

}
