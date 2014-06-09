package com.sabbir.tagyourplace;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

class MapSearchcontroller extends AsyncTask<Void, Void, Void> {
	
	private static String TAG="MapSearchController2";
	private String address;
	private GoogleMap GMAP;
	private static JSONObject JsonAddressObject;
	private LatLng location=null;
	private ImageButton searchBtn;
	MediaPlayer mp = new MediaPlayer();
	private Context context;
	public MapSearchcontroller(ImageButton btn, String address,GoogleMap gmp,Context appContext)
	{
		this.address=address;
		this.GMAP = gmp;
		this.searchBtn = btn;
		this.context = appContext;
		
	}
	public static JSONObject getLocationInfo(String address) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = null;
		Log.w("GetName: ",address);
		HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" +address+"&ka&sensor=false");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
			Log.w("Something wents wrong",e);
		}

		//JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			Log.w("Something wents wrong",e);
		}

		return jsonObject;
	}
	
	public static LatLng getGeoPoint(JSONObject jsonObject) {

		Double lon = new Double(0);
		Double lat = new Double(0);
		JsonAddressObject = jsonObject;
		try {

			lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
				.getJSONObject("geometry").getJSONObject("location")
				.getDouble("lng");

			lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
				.getJSONObject("geometry").getJSONObject("location")
				.getDouble("lat");
			Log.w("LatLng: ",""+lat+lon);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new LatLng(lat , lon );

	}

	void DrawMarker_OnMap(LatLng position)
	{
		String POS_NAME="No result found!!!";
		try {
			JSONObject JsonResult_=	((JSONArray)JsonAddressObject.get("results")).getJSONObject(0);
			POS_NAME=((JSONArray)JsonResult_.get("address_components")).getJSONObject(0).getString("long_name");
					
							//.getJSONObject( "address_components").getString("long_name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.GMAP.clear();
		this.GMAP.addMarker(new MarkerOptions()
		.position(position)
		.snippet("Lat:" + position.latitude + " Long:"+ position.longitude)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon3))
		.title(POS_NAME));
		this.GMAP.moveCamera(CameraUpdateFactory.newLatLng(position));
		this.GMAP.animateCamera(CameraUpdateFactory.zoomTo(16));
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		//DrawMarker_OnMap(new LatLng(65.58388959999999,22.1531736));
		Log.w(TAG, "doInBackground");
		JSONObject Jobj=getLocationInfo(this.address);
		if(Jobj!=null)
		{
			location= getGeoPoint(Jobj);
		}
				
		return null;
	}
	
	@Override
    protected void onPostExecute(Void result) {
        Log.w(TAG, "onPostExecute");
        if(location!=null)
        {
        	DrawMarker_OnMap(location);
        	playAudio(this.context);
        	
        }
        //stop();
        this.searchBtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPreExecute() {
        Log.w(TAG, "onPreExecute");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.w(TAG, "onProgressUpdate");
        //playAudio(this.context);
    }
    void playAudio(Context context)
    {
    	
    	try {
    		stop();
            String fileName="playaudio.mp3";
			AssetFileDescriptor descriptor = context.getAssets()
                    .openFd(fileName);
            mp.setDataSource(descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mp.prepare();
            mp.setLooping(false);
            mp.start();
            mp.setVolume(3, 3);
            

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void stop()
    {
    	mp.stop();
    }

}
