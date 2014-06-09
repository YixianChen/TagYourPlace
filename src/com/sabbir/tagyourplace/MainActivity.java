package com.sabbir.tagyourplace;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Text;


import com.yixian.loginbyfacebook.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.text.Html.TagHandler;
import android.text.InputType;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends FragmentActivity implements OnMarkerClickListener  {
	private GoogleMap googleMap;
	private  LocationManager locationManager;
	private LocationListener locationListener;
	private static final int ZERO_MINUTE = 1000*60*1;
	private List<LatLng>LLarray = new ArrayList<LatLng>();
	String tagName=null;
	static final int STATE_START = 1;
	static final int STATE_TAG = 2;
	private PolylineOptions pLine = new PolylineOptions().width(3).color(Color.GREEN);
	private TagDataSource TDS;
	private int which_id=-1;
	static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	static String CALENDER_EVENT="com.ltu.calendar";
	private EditText SearchBox;
	private String POSITION_STATUS_NAME="Last Known Location";
	private DatabaseCreator dbHelper;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Set the Screen Rotation Off; kept PORTRAIT
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TDS= new TagDataSource(MainActivity.this);
    	//TDS.open();
		//final Button Btn_Tag = (Button)findViewById(R.id.btn_st_tag);
		//final Button Btn_Tag_Done = (Button)findViewById(R.id.btn_dn_tag);
		
		findViewById(R.id.btn_st_tag).setOnClickListener(mStartTagOnclick);
		findViewById(R.id.btn_dn_tag).setOnClickListener(mDoneTagOnclick);
		findViewById(R.id.undo_btn).setOnClickListener(mUndoTagOnclick);
		findViewById(R.id.tagDelete).setOnClickListener(mDeleteTagOnclick);
		findViewById(R.id.tagLists).setOnClickListener(mListTagOnclick);
		findViewById(R.id.btn_Pos).setOnClickListener(mBackToPositioning);
		findViewById(R.id.btn_Calender).setOnClickListener(mShowCalenderData);
		findViewById(R.id.btn_SearchMap).setOnClickListener(mSearchMapOnclick);
		SearchBox= (EditText)findViewById(R.id.et_MapSearchBox);
		SearchBox.setInputType(InputType.TYPE_CLASS_TEXT );
		SearchBox.setBackgroundColor(Color.parseColor("#c0c0c0"));
		TextView tv = (TextView)findViewById(R.id.tvPosStat);
		tv.setTextColor(Color.RED);
		tv.setText("Unknown");
		
	
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		setButtonState(STATE_START);
		//.....Test class....
		testClass tc = new testClass();
		tc.test(getPATH());
		//end
	    // Showing status
	    if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

	        int requestCode = 10;
	        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	        dialog.show();

	    }
	    else 
	    { // Google Play Services are available

	        // Getting reference to the SupportMapFragment of activity_main.xml
	        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	        
	        // Getting GoogleMap object from the fragment
	        googleMap = fm.getMap();
	        //Set the google Map view to Satellite view
	        googleMap.setMapType(googleMap.MAP_TYPE_SATELLITE);
	        // Enabling MyLocation Layer of Google Map
	       googleMap.setMyLocationEnabled(true);
	        
	        googleMap.setOnMarkerClickListener(this);
	        
/*	        googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				
				@Override
				public void onMyLocationChange(Location location) {
					// TODO Auto-generated method stub
					TextView tvv =(TextView)findViewById(R.id.tvPosStat);
			           PlaceTagManager PTM= new PlaceTagManager(getApplicationContext(), new LatLng(location.getLatitude(), location.getLongitude()));
			           String postionName = PTM.getPositionName();
			           tvv.setText(postionName +" #"+location.getAccuracy()+" "+location.getProvider());
			           googleMap.clear();
			           LatLng currentPosition = new LatLng(location.getLatitude(),
			   				location.getLongitude());
			           //move the camera to the position
						//googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
				   		// Zoom in the Google Map
				   		//googleMap.animateCamera(CameraUpdateFactory.zoomTo(1));
			   				//add a marker to a map
			   				googleMap.addMarker(new MarkerOptions()
			   				.position(currentPosition)
			   				.snippet("Lat:" + location.getLatitude() + " Long:"+ location.getLongitude())
			   				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon21))
			   				.title("I am Here!!!"));
			   				ContentValues values = new ContentValues(); 
			   				values.put("name",postionName);
			   				values.put("data",System.currentTimeMillis()/1000);
			   				values.put("Latitude",location.getLatitude());
			   				values.put("Longitude",location.getLongitude());
			   				
			   				dbHelper = new DatabaseCreator(MainActivity.this);
			   				db= dbHelper.getWritableDatabase();
			   				db.insert("tbPlace", null, values);
			   				db.close();
			   				XmlFileCreator newXML =new XmlFileCreator();
		                	//newXML.UpdateExtStroageInfo(PATH);
		                	newXML.Create2(getPATH(),  new LatLng(location.getLatitude(), location.getLongitude()));
		                	Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_SHORT).show();
					
				}
			});*/
	        
	        
	        // Getting LocationManager object from System Service LOCATION_SERVICE
	        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	        // Creating a criteria object to retrieve provider
	        Criteria criteria = new Criteria();

	        // Getting the name of the best provider
	        String provider = locationManager.getBestProvider(criteria, true);

	        // Getting a fast fix with the last known location
	        Location location = locationManager.getLastKnownLocation(provider);

	        locationListener = new LocationListener() {
	          public void onLocationChanged(Location location) {
	          // redraw the marker when get location update.
	          Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_SHORT).show();
	          drawMarker(location,"I am Here!!!");
	          TextView tvv =(TextView)findViewById(R.id.tvPosStat);
	          PlaceTagManager PTM= new PlaceTagManager(getApplicationContext(), new LatLng(location.getLatitude(), location.getLongitude()));
	          String postionName = PTM.getPositionName();
	          tvv.setText(postionName+"  #"+location.getAccuracy()+" "+location.getProvider());
	    
	           LatLng currentPosition = new LatLng(location.getLatitude(),
	   				location.getLongitude());
	           //move the camera to the position
				//googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
		   		// Zoom in the Google Map
		   		//googleMap.animateCamera(CameraUpdateFactory.zoomTo(1));
	   				//add a marker to a map
	   				googleMap.addMarker(new MarkerOptions()
	   				.position(currentPosition)
	   				.snippet("Lat:" + location.getLatitude() + " Long:"+ location.getLongitude())
	   				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon21))
	   				.title("I am Here!!!"));
	   				ContentValues values = new ContentValues(); 
	   				values.put("name",postionName);
	   				//values.put("date",System.currentTimeMillis()/1000);
	   				values.put("date",System.currentTimeMillis());
	   				values.put("Latitude",location.getLatitude());
	   				values.put("Longitude",location.getLongitude());
	   				
	   				dbHelper = new DatabaseCreator(MainActivity.this);
	   				db= dbHelper.getWritableDatabase();
	   				db.insert("tbPlace", null, values);
	   				db.close();
	   				XmlFileCreator newXML =new XmlFileCreator();
               	//newXML.UpdateExtStroageInfo(PATH);
               	newXML.Create2(getPATH(),  new LatLng(location.getLatitude(), location.getLongitude()));
               	Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_SHORT).show();
	          
	        }

			@Override
			public void onProviderDisabled(String arg0) {
				//print "Currently GPS is Disabled"; 
				Toast.makeText(getApplicationContext(), "Currently GPS is Disabled", Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				//print "GPS got Enabled"; 
				Toast.makeText(getApplicationContext(), "GPS got Enabled", Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}};

	        if(location!=null){
	           //PLACE THE INITIAL MARKER  
	        	POSITION_STATUS_NAME="I am Here!!!";
	          drawMarker(location,POSITION_STATUS_NAME);
	          
	          
	           TextView tvv =(TextView)findViewById(R.id.tvPosStat);
	           tvv.setText(""+String.format("%.8f",location.getLatitude())+" "+String.format("%.8f",location.getLongitude()));
	          PlaceTagManager PTM= new PlaceTagManager(getApplicationContext(), new LatLng(location.getLatitude(), location.getLongitude()));
	          tvv.setText(PTM.getPositionName()+" "+location.getAccuracy()+" "+location.getProvider());
	         
	        }
	        
	        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
	    }
	    
	    
	    SearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (	actionId == EditorInfo.IME_ACTION_SEARCH ||
						actionId == EditorInfo.IME_ACTION_DONE ||
						actionId == EditorInfo.IME_ACTION_GO ||
						event.getAction() == KeyEvent.ACTION_DOWN &&
	                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
	                    event != null) {
					ImageButton searchBtn=(ImageButton)findViewById(R.id.btn_SearchMap);
					searchBtn.setVisibility(View.GONE);
					//Log.w("Kabira","hehe");
	                // hide virtual keyboard
					//Toast.makeText(getApplicationContext(),"Kabira!!!", Toast.LENGTH_SHORT).show();
					
	                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                imm.hideSoftInputFromWindow(SearchBox.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	                
	                
	                MapSearchcontroller MC= new MapSearchcontroller(searchBtn,SearchBox.getText().toString().replace("\n"," ").replace(" ", "%20"),googleMap,getApplicationContext());
	                MC.execute();
	                
	          /*      final String[] anArray;
	    			anArray = new String[5];
	                anArray[0]="A";
	                anArray[1]="BB";
	                anArray[2]="CCC";
	                anArray[3]="DDDD";
	                anArray[4]="EEEEE";
	                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	                builder.setTitle("Your Search Results")
	                       .setItems(anArray, new DialogInterface.OnClickListener() {
	                           public void onClick(DialogInterface dialog, int which) {
	                           // The 'which' argument contains the index position
	                           // of the selected item
	                        	   Toast.makeText(getApplicationContext(),anArray[which], Toast.LENGTH_SHORT).show();
	                        	   findViewById(R.id.btn_SearchMap).setVisibility(View.VISIBLE);
	                       }
	                });
	                builder.create().show();*/
	                
	                //new MapSearchController(getApplicationContext(),googleMap,SearchBox.getText().toString()).doInBackground();
	                SearchBox.setText("", TextView.BufferType.EDITABLE);
	                SearchBox.setVisibility(View.GONE);
	                findViewById(R.id.btn_SearchMap).setVisibility(View.GONE);
	                return true;
	            }
				return false;
			}
		});
		
	}
	
	private OnClickListener mSearchMapOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//ImageButton Btn_SearchMap=(ImageButton)findViewById(R.id.btn_SearchMap);
			ImageButton searchBtn=(ImageButton)findViewById(R.id.btn_SearchMap);
			searchBtn.setVisibility(View.GONE);
			SearchBox.setVisibility(View.VISIBLE);
			
		}
	};
	private OnClickListener mBackToPositioning = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Android refresh current activity to get back to Start Screen
		    Intent intent = getIntent();
		    finish();
		    startActivity(intent);
			
		}
	};
	private OnClickListener mShowCalenderData = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(MainActivity.this,CalendarEvent.class);
			startActivity(i);
			
			
		}
	};
	private OnClickListener mUndoTagOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pLine = new PolylineOptions().width(3).color(Color.GREEN);
			Log.v("LLarray",LLarray.size()+"");
			if (LLarray.size()>0)
			{
				//Log.v("Lat",LLarray.get(LLarray.size()-1).longitude+"");
				LatLng tmp =LLarray.get(LLarray.size()-1);
				LLarray.remove(tmp);
				//Log.v("LLarray after",LLarray.size()+"");
				googleMap.clear();
				drawMarkerRemainingClickedPoints();
				googleMap.addPolyline(pLine.addAll(LLarray));
				
			}
			else
			{
				//nothing to do
				
			}
			
			
		}
	};
	private OnClickListener mDeleteTagOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			deleteShape();
		}
	};
	private OnClickListener mStartTagOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Log.w("tagbtn", "clicked");
			  //Deciding when to stop listening for updates
			//locationManager.removeGpsStatusListener((Listener) locationListener);
	        locationManager.removeUpdates(locationListener);
	        //locationListener=null;
	        googleMap.setMyLocationEnabled(false);
	        //Btn_Tag.setVisibility(View.GONE);
	        //Btn_Tag_Done.setVisibility(View.VISIBLE);
	        //locationManager.clearTestProviderEnabled(LOCATION_SERVICE);
	        //locationManager.notify();				
	        setButtonState(STATE_TAG);
	        //googleMap.clear();
	        //deleleShapeEnable();
	        
	        
		}
	};
private OnClickListener mListTagOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//SQLiteDatabase db= openOrCreateDatabase(getPATH()+"/TagDB", MODE_PRIVATE, null);
			//db.close();
			
			List<String> retTagNames =TDS.getAllTagsName();
			
			final String[] anArray;
			final boolean[] checkedItemState;
	        anArray = new String[retTagNames.size()];
	        checkedItemState = new boolean[retTagNames.size()];
	       for (int i=0;i<retTagNames.size();i++)
	       {
	    	   anArray[i]=retTagNames.get(i);
	    	   checkedItemState[i] = false;
	       }
	      
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		    builder.setTitle("Lists of Tags: ")
		           .setSingleChoiceItems(anArray, -1,new DialogInterface.OnClickListener() {
		        	   @Override
		        	   public void onClick(DialogInterface dialog, int which) {
		                   // The 'which' argument contains the index position
		                   // of the selected item
		        		    which_id=which;
/*		            	   List<LatLng> poly = TDS.getPolygon(anArray[which]); 
		            	googleMap.clear();
		   				// Add a polygon from return polygon
		   		        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
		   		            .addAll(poly)
		   		            .strokeColor(Color.RED)
		   		            .fillColor(Color.TRANSPARENT));
		   		        LatLng polyposition=getMidofPoly(poly);
		   		        googleMap.addMarker(new MarkerOptions()
		   		        	.position(polyposition)
		   		        	.draggable(true)
		   		        	//.snippet("Lat:" + location.getLatitude() + " Long:"+ location.getLongitude())
		   		        	.icon(BitmapDescriptorFactory.fromResource(R.drawable.man))
		   		        	.title(anArray[which]))
		   		        	.showInfoWindow();
		   		     //move the camera to first element of the polygon
		   		     googleMap.moveCamera(CameraUpdateFactory.newLatLng(poly.get(0)));
		   		     // Zoom in the Google Map
		   		     googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));*/
		            	   
		               }

				

				
		        })
		           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                   if(which_id!=-1)
	                   {
	                	   
	                   
	                	 List<LatLng> poly = TDS.getPolygon(anArray[which_id]); 
			            	googleMap.clear();
			   				// Add a polygon from return polygon
			   		        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
			   		            .addAll(poly)
			   		            .strokeColor(Color.RED)
			   		            .fillColor(Color.TRANSPARENT));
			   		        LatLng polyposition=getMidofPoly(poly);
			   		        googleMap.addMarker(new MarkerOptions()
			   		        	.position(polyposition)
			   		        	.draggable(true)
			   		        	//.snippet("Lat:" + location.getLatitude() + " Long:"+ location.getLongitude())
			   		        	.icon(BitmapDescriptorFactory.fromResource(R.drawable.man))
			   		        	.title(anArray[which_id]))
			   		        	.showInfoWindow();
			   		     //move the camera to first element of the polygon
			   		     googleMap.moveCamera(CameraUpdateFactory.newLatLng(poly.get(0)));
			   		     // Zoom in the Google Map
			   		     googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
			   		     //reset the which_id
			   		     which_id=-1;
	                   }
	                }
	            })
	            	.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	
		                	if (which_id!=-1)
		                	{
		                	TDS.deleteTag(anArray[which_id]);
		                	//reset the which_id
				   		     which_id=-1;
				   		     }
		                	
		                }
		            })
		           .setNegativeButton("Delete All", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // delete all tags from database....
	                	   TDS.deleteAllTag();
	                	   Toast.makeText(getApplicationContext(),"Database deleted!!!", Toast.LENGTH_SHORT).show();
	                	   DeleteXmlFile();
	                	   googleMap.clear();
	                   }
	               })
	               
		           ;	
		    
		    builder.create().show();
	        
		}
	};
	private OnClickListener mDoneTagOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) 
		{
			//^^^^^Execute Test points inside Test polygon^^^^^^
			//testClass tc= new testClass();
			//tc.test();
			//.......END HERE.............
			//^^^^Execute Test points inside Test polygon^^^^^^
			//TagHandler tg =new TagHandler(MainActivity.this);
			//.......END HERE.............
			if (LLarray.size()>=3)
			{
				final EditText ETxtTagName = new EditText(MainActivity.this);
				// TODO Auto-generated method stub
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder .setTitle("Store a new tag")
						.setMessage("Enter your Tag Text:")
						.setView(ETxtTagName)
				      
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    // User clicked OK
	                	//tagname.getText().toString()
	                	tagName=ETxtTagName.getText().toString();
	                	//UpdateExtStroageInfo();
	                	XmlFileCreator newXML =new XmlFileCreator();
	                	//newXML.UpdateExtStroageInfo(PATH);
	                	newXML.Create(getPATH(), tagName, LLarray);
	                	Toast.makeText(getApplicationContext(), ETxtTagName.getText().toString()+" is created!!!", Toast.LENGTH_SHORT).show();
	                	TDS.createTag(tagName,FORMATTER.format(new Date()),LLarray);
	                	
	                	
	                	
	                	// After creating the tag perform vibration
	                	Vibrator tagV =(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	                	tagV.vibrate(300);
	                	tagName=null;
	                	
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    // User cancelled the dialog
	                	
	                	
	                }
	            });

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
				
				//clear the Map to remove pins those are placed on Map
				//googleMap.clear();
				// Add a polygon from clicked points
		        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
		            .addAll(LLarray)
		            .strokeColor(Color.RED)
		            .fillColor(Color.TRANSPARENT));
		        //clear the LLarray
		        //LLarray.clear(); 
			}
			else
			{
				String msg="";
				if (LLarray.size()==0)
				msg="Nothing to Tag!!!";
				else
				msg="Minimum 3 points are needed to Tag!!!";
				
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				// 2. Chain together various setter methods to set the dialog characteristics
				builder .setTitle(msg)
				.setIcon(R.drawable.warninig)	
				.setCancelable(false)   
				.setPositiveButton("OK", null);
				
				
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
			}
	
	    
			
			
		}
	};
	public void setButtonState(int state) 
	{
		Button Btn_Tag = (Button) findViewById(R.id.btn_st_tag);
		Button Btn_Tag_Done = (Button) findViewById(R.id.btn_dn_tag);
		ImageButton Btn_Tag_Undo = (ImageButton)findViewById(R.id.undo_btn);
		ImageButton Btn_Tag_Delete =(ImageButton)findViewById(R.id.tagDelete);
		ImageButton Btn_Lists_Tag_Show = (ImageButton)findViewById(R.id.tagLists);
		ImageButton Btn_Back_To_Positioning = (ImageButton)findViewById(R.id.btn_Pos);
		ImageButton Btn_SearchMap=(ImageButton)findViewById(R.id.btn_SearchMap);
		EditText SearchBox=(EditText)findViewById(R.id.et_MapSearchBox);
		Btn_Tag.setVisibility(View.GONE);
		Btn_Tag_Done.setVisibility(View.GONE);
		Btn_Tag_Undo.setVisibility(View.GONE);
		Btn_Tag_Delete.setVisibility(View.GONE);
		Btn_Lists_Tag_Show.setVisibility(View.GONE);
		Btn_Back_To_Positioning.setVisibility(View.GONE);
		TextView tvPositioningStatus = (TextView)findViewById(R.id.tvPosStat);
		
		switch (state) {
		case STATE_TAG:
			Btn_Tag.setVisibility(View.GONE);
			Btn_Tag_Done.setVisibility(View.VISIBLE);
			Btn_Tag_Undo.setVisibility(View.VISIBLE);
			Btn_Tag_Delete.setVisibility(View.VISIBLE);
			Btn_Lists_Tag_Show.setVisibility(View.VISIBLE);
			tvPositioningStatus.setVisibility(View.GONE);
			Btn_Back_To_Positioning.setVisibility(View.VISIBLE);
			EnableListenClickOnMap();
			Btn_SearchMap.setVisibility(View.VISIBLE);
			break;
		case STATE_START:
			Btn_Tag.setVisibility(View.VISIBLE);
			Btn_Tag_Done.setVisibility(View.GONE);
			Btn_SearchMap.setVisibility(View.GONE);
			SearchBox.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
	@Override
	public void onDestroy()
	{
		//stop the location listener to disable provide updates in future.....
		try {
			locationManager.removeUpdates(locationListener);	
			} 
		catch (Exception e) 
		{
			Log.w("OnDestroy",e.toString());
		}
		super.onDestroy();
		Log.w("OnDestroy","Yes");
	}
	@Override
	public void onStop()
	{
		//stop the location listener to disable provide updates in future.....
				try {
					locationManager.removeUpdates(locationListener);	
					} 
				catch (Exception e) 
				{
					Log.w("OnDestroy",e.toString());
				}
		super.onStop();
		Log.w("OnStop","Yes");
	}
	@Override
	public void onRestart()
	{
		super.onRestart();
		Log.w("OnRestart","Yes");
		onStart();
	}
	@Override
	public void onPause()
	{
		super.onPause();
		Log.w("OnPause","Yes");
	}
	@Override
	public void onResume()
	{
		super.onResume();
		Log.w("OnResume","Yes");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		try{
			switch (item.getItemId()) {
			case R.id.uploaddata:
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			startActivity(intent);
			return true;	
			
			case R.id.action_settings:
				return true;
			default:
				
				 return super.onOptionsItemSelected(item);
			
		}
		}	catch(Exception e){
			Log.d("Tagyourplace:", "Exception",e);
			
			return true;
		}
	}
	private void drawMarker(Location location, String name){
		//clear the google map
				googleMap.clear();
				
	   		   
				//get the current position
				LatLng currentPosition = new LatLng(location.getLatitude(),
				location.getLongitude());
				//move the camera to the position
				googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
		   		// Zoom in the Google Map
		   		googleMap.animateCamera(CameraUpdateFactory.zoomTo(1));
				//add a marker to a map
				googleMap.addMarker(new MarkerOptions()
				.position(currentPosition)
				.snippet("Lat:" + location.getLatitude() + " Long:"+ location.getLongitude())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1))
				.title(name));
		}
	void drawMarkerRemainingClickedPoints()
	{
		for(int i=0;i<LLarray.size();i++)
		{
			googleMap.addMarker(new MarkerOptions()
			.position(LLarray.get(i))
			.snippet("")
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.plus))
			.title(null));
			
		}
	
	}
	private void EnableListenClickOnMap()
	{
		//Sets callback interface for when the user taps on the map
        googleMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng point) {
				// TODO Auto-generated method stub
				googleMap.addMarker(new MarkerOptions()
				.position(point)
				.snippet("")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.plus))
				
				//.icon(BitmapDescriptorFactory.defaultMarker())
				.title(null));
				
				// add the clicke points into LatLng array
				LLarray.add(point);
				
				googleMap.addPolyline(pLine.add(point));
				
				
			}
		});
	}
	
	void deleteShape()
	{
		pLine = new PolylineOptions().width(3).color(Color.GREEN);
						// TODO Auto-generated method stub
						//Toast.makeText(getApplicationContext(), "Long Press", Toast.LENGTH_SHORT).show();
						if(LLarray.size()!=0)
						{/*
							AlertDialog.Builder delShapeBuilder = new AlertDialog.Builder(MainActivity.this);
							delShapeBuilder.setMessage("Do you want to delete the current shape?")
					               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					                   public void onClick(DialogInterface dialog, int id) {*/
					                      googleMap.clear();
					                      LLarray.clear();
					  /*                 }
					               })
					               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					                   public void onClick(DialogInterface dialog, int id) {
					                       // do nothing
					                   }
					               });
					        // Create the AlertDialog object 
							delShapeBuilder.create();
							delShapeBuilder.show();*/
							
						}
						else
							googleMap.clear();
					
						
						
						
				
	}
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (LLarray.size()>=3)
		{
			googleMap.addPolyline(pLine.add(LLarray.get(0)));
			
			
		}
		return false;
	}
	//return the PATH and PATH value: /storage/sdcard0/Android/data/com.sabbir.tagyourplace/tagFiles
	String getPATH()
	{
		return Environment.getExternalStorageDirectory().toString()+"/Android/data/"+this.getApplicationContext().getPackageName()+"/tagFiles";
	}
	
	//get the mid point of the polygon
	private LatLng getMidofPoly(List<LatLng> poly) {
		// TODO Auto-generated method stub
		double lat=0,lon=0;
		for(int i=0;i<poly.size();i++)
		{
			lat=lat+poly.get(i).latitude;
			lon=lon+poly.get(i).longitude;
			
		}
		return new LatLng(lat/((double)poly.size()), lon/((double)poly.size()));
	}
	void DeleteXmlFile()
	{
		XmlFileCreator newXML =new XmlFileCreator();
    	//newXML.UpdateExtStroageInfo(PATH);
    	if (newXML.Delete(getPATH()))
    	{
    		AlertDialog.Builder delShapeBuilder = new AlertDialog.Builder(MainActivity.this);
    		delShapeBuilder.setMessage("No Flie Found!!!")
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {}
                   					 });
    		Toast.makeText(getApplicationContext(), "Log File deleted!!!", Toast.LENGTH_SHORT).show();
    		
    	}
    	
	}
	
}
