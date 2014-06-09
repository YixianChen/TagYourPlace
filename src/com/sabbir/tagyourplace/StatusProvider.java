package com.sabbir.tagyourplace;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StatusProvider extends ContentProvider
 {
	
	private MySQLiteHelper dbHelper;
	private SQLiteDatabase db; 
	//AUTHORITY  & URI both identifies the ContentProvider
	public static final String AUTHORITY="com.sabbir.tagyourplace.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);
		
	@Override
	public boolean onCreate() 
	{
		dbHelper = new MySQLiteHelper(getContext());
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		if(uri.getLastPathSegment()==null)
			// return "vendor-specific" MIME types for multiple rows
			return "vnd.android.cursor.dir/vnd.sabbir.tagyourplace.status";
		
		else
			// return "vendor-specific" MIME types for single row
			return "vnd.android.cursor.item/vnd.sabbir.tagyourplace.status";
		
	}
	

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db=dbHelper.getWritableDatabase();
		long id = db.insertWithOnConflict(dbHelper.TABLE_TAG, null,values, SQLiteDatabase.CONFLICT_IGNORE);
		if (id!=-1)
			uri=Uri.withAppendedPath(uri, Long.toString(id));
		return uri;
	}  
	
	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		db.delete(dbHelper.TABLE_TAG, selection, selectionArgs);
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,	String sortOrder) 
	{
		db=dbHelper.getReadableDatabase();
		Cursor cursor = db.query(MySQLiteHelper.TABLE_TAG, projection, selection, selectionArgs, null, null, sortOrder);
		return cursor;
	}



}
