package com.yixian.loginbyfacebook;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.sabbir.tagyourplace.DatabaseCreator;
import com.sabbir.tagyourplace.PlaceTagManager;


public class DataUploader implements Runnable {

	public static final String TAG = "Uploader";

	// public static final String SHARED_PREFS = "AffectiveHealth";
	private static final String API_URL = "http://newaffectivehealth.mobilelifecentre.org/api.php";
    
	
	public static final int STATE_IDLE = 0;
	public static final int STATE_UPLOAD = 1;
	public static final int STATE_DONE = 2;
	private String PREFS_NAME="Tagyourplace";

	
	public interface Listener {
		void uploaderStopped(String reason);
		void uploaderIssue(String msg);
	}
	
	private Listener mListener = null;
	
	long sent_start,sent_end;
	int mState = STATE_IDLE;
	int status_toupload = 0;
	int status_progress = 0;
	
	DatabaseCreator mTable;
	private boolean mStream = false;

	private Thread mThread;
	private boolean mRun = true;
	private Context mContext;
	private String mUserName;
	private long mTimeLastSampleSent = 0;
    
	private class Sample {
		long time;
		float longitude;
		float latitude;
		String tagName;

		Sample() {
			// nothing
		}
		Sample(Cursor c) {
			// indexes:
			//   0: time
			//   1: acc	(xyz combined and filtered)
			//   2: gsr
			time = c.getLong(0);
			longitude = c.getFloat(1);
			latitude = c.getFloat(2);
			tagName = c.getString(4);
		}

		public String toString() {
			return "{\"t\":"+time+",\"longitude\":"+longitude+",\"latitude\":"+latitude+",\"tag\":"+tagName+"}";
		}
	}

	public DataUploader(Context ctx, DatabaseCreator table, boolean stream, String username) {
		mContext = ctx;
		this.mTable = table;
		mStream = stream;
		mUserName = username;

			// load these from sharedpreferences
		SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, 0);
		sent_start = prefs.getLong("sent_start", 0);
		sent_end = prefs.getLong("sent_end", 0);
		
		mTimeLastSampleSent = sent_end;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
      
	}

}
