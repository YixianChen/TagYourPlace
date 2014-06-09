package com.yixian.loginbyfacebook;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class UploadService extends Service {

	private boolean uploadRData = false;

	public class MyBinder extends Binder {
		public UploadService getService() {
			return UploadService.this;
		}
	}

	private MyBinder mBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	   @Override
       public void onCreate() {
		   
		   
		   
	   }

	   @Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			
			return START_STICKY;
		}
}
