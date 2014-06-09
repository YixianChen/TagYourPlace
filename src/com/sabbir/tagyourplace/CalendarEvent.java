package com.sabbir.tagyourplace;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CalendarEvent extends Activity {
    /*********************************************************************
     * UI part*/
	
	private TextView c_text_event;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.calactivity);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   
            findViewById(R.id.backBtn).setOnClickListener(b);
               TextEvent();
                  this.getEvents();
                 
    }
      
	 private OnClickListener b = new OnClickListener() {

			public void onClick(View v) {
				
	         finish();
					
			}
		};
	

    public void TextEvent() {
    	c_text_event = (TextView) findViewById(R.id.text_event);
    	
                             }
        
    /*********************************************************************
     * Get a list of events*/
    public void getEvents() {
    	  Context context = getApplicationContext();    
          ContentResolver contentResolver = context.getContentResolver();
          Uri l_eventUri;
     	  l_eventUri = Uri.parse("content://com.android.calendar/events");
    	  String[] l_projection = new String[]{"title","eventLocation","dtstart" ,"description"};
       	  Cursor l_managedCursor = contentResolver.query(l_eventUri, l_projection,"calendar_id=1", null, null);
    	      	  
          	if (l_managedCursor.moveToFirst()) {
    		
    		String l_title;
    		String l_eventLocation;
    		String l_begin;
    		String l_end;
    		String l_description ;
    		
    		StringBuilder l_displayText = new StringBuilder();
    		int l_colTitle = l_managedCursor.getColumnIndex(l_projection[0]);
    		int l_coleventLocation = l_managedCursor.getColumnIndex(l_projection[1]);
    		int l_colBegin = l_managedCursor.getColumnIndex(l_projection[2]);
    		int l_colEnd = l_managedCursor.getColumnIndex(l_projection[2]);
    		int l_colDescription = l_managedCursor.getColumnIndex(l_projection[3]);
    		
    		do {
    			l_title = l_managedCursor.getString(l_colTitle);
    			l_eventLocation = l_managedCursor.getString(l_coleventLocation);
    			l_begin = getDateTimeStr(l_managedCursor.getString(l_colBegin));
    			l_end = getDateTimeStr(l_managedCursor.getString(l_colEnd));
    			l_description = l_managedCursor.getString(l_colDescription );
    			
    			l_displayText.append(l_title +"\n" + l_eventLocation + "\n" + l_begin + "\n" + l_end +"\n" + l_description + "\n---------------------\n");
    			
    		} while (l_managedCursor.moveToNext() );
    		  c_text_event.setText(l_displayText.toString());
    		
          	
          	}else{
          		c_text_event.setText("Oopps! no calendar available");
          		c_text_event.setTextColor(Color.parseColor("#FF0000"));
          		c_text_event.setTextSize(30);
    	            }
                       
    }
    
    
    /*********************************************************************
     * Utility part*/
     
    private static final String DATE_TIME_FORMAT = "yyyy MMM dd, HH:mm:ss";
    public static String getDateTimeStr(int p_delay_min) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		if (p_delay_min == 0) {
			return sdf.format(cal.getTime());
		} else {
			Date l_time = cal.getTime();
			l_time.setMinutes(l_time.getMinutes() + p_delay_min);
			return sdf.format(l_time);
		}
	}
    public static String getDateTimeStr(String p_time_in_millis) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
    	Date l_time = new Date(Long.parseLong(p_time_in_millis));
    	return sdf.format(l_time);
    }
   
}