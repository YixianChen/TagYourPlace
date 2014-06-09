package com.sabbir.tagyourplace;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.OpenableColumns;

public class DatabaseCreator  extends SQLiteOpenHelper{
	

		// the Activity or Application that is creating an object from this class.
		//Context context;
		// a reference to the database used by this application/object
		//private SQLiteDatabase db;
	 
		// These constants are specific to the database.  They should be 
		// changed to suit your needs.
		private static final String DB_NAME = "tagdb";
		private static final int DB_VERSION = 1;
		// These constants are specific to the database table.  They should be
		// changed to suit your needs.
		//private final SQLiteOpenHelper helper;
		private final String TABLE_NAME = "tbPlace";
		private final String TABLE_ROW_ID = "id";
		final static String TABLE_ROW_NAME = "name";
		//private static final String MODE_PRIVATE = null;
		private final String TABLE_ROW_DATE = "date";
		private final String TABLE_ROW_LATITUDE = "Latitude";
		private final String TABLE_ROW_LONGITUDE = "Longitude";
		private String[] colNames = new String[]{TABLE_ROW_DATE};
		

		public DatabaseCreator(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		}
         
		
		
		void CreateDatabase(String Path, String Date)
		{
			//Create or Open a Database
			SQLiteDatabase db = null;
			db.openOrCreateDatabase(Path+"/ad", null);
			//openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);
			//Create a SQL table if it isn't exists
			//db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+"id INTEGER, date VARCHAR, name VARCHAR, Latitude FLOAT, Longitude FLOAT);");
			//Insert data into database
			//db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (1, '123Dec', 'LTYU', 12.3, 13.4);");
			//Cursor c= db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
			//c.moveToFirst();
			
			db.close();
			
		}
		


		@Override
		public void onCreate(SQLiteDatabase db) {
			  String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
		                 + TABLE_ROW_NAME + " TEXT,"
		                + TABLE_ROW_DATE + " INTEGER ," 
		                + TABLE_ROW_LATITUDE + " FLOAT" 
		                + TABLE_ROW_LONGITUDE + " FLOAT" +")";
		        db.execSQL(CREATE_CONTACTS_TABLE);
			
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	 
	        // Create tables again
	        onCreate(db);
			
		}
		
	    // Adding new Tag
	public void addTag(TagListComponents TagList) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    for(int i=0;i<TagList.getCoordinates().size();i++)
	    {
	    	ContentValues values = new ContentValues();
	    	values.put(TABLE_ROW_NAME, TagList.getName()); // Tag Name
	    	values.put(TABLE_ROW_DATE, TagList.getDate()); // Tag Date
	    	values.put(TABLE_ROW_LATITUDE, TagList.getCoordinates().get(i).latitude); // Latitude
	    	values.put(TABLE_ROW_LONGITUDE, TagList.getCoordinates().get(i).longitude); //Longitude
	    	// Inserting Row
	    	db.insert(TABLE_NAME, null, values);
	    }
	    db.close(); // Closing database connection
	}
	
	   // Getting All Tags
	 public List<TagListComponents> getAllContacts() {
	    List<TagListComponents> TagLists = new ArrayList<TagListComponents>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    //if (cursor.moveToFirst()) {
	    if (cursor!=null) {
	        do {
	            TagListComponents TList = new TagListComponents();
	            TList.setName(cursor.getString(0));
	            TList.setDate(cursor.getString(1));
	            //TList.setPhoneNumber(cursor.getString(2));
	            // Adding contact to list
	            TagLists.add(TList);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return TagLists;
	}
	// Getting TagLists Count
	    public int getContactsCount() {
	        String countQuery = "SELECT  * FROM " + TABLE_NAME;
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(countQuery, null);
	        cursor.close();
	 
	        // return count
	        return cursor.getCount();
	    }
	    // Updating single Tag
	    public int updateTag(TagListComponents Tag) {
	        SQLiteDatabase db = this.getWritableDatabase();
	     
	        ContentValues values = new ContentValues();
	        values.put(TABLE_ROW_NAME, Tag.getName());
	        values.put(TABLE_ROW_DATE, Tag.getDate());
	     
	        // updating row
	      //  return db.update(TABLE_ values, KEY_ID + " = ?",
	        //        new String[] { String.valueOf(contact.getID()) });
	        return 1;
	    }

	    public Cursor getQueryCursor(long from, long to) {
	    	SQLiteDatabase db = this.getReadableDatabase();
			return db.query(DB_NAME, colNames, "TIME > ? AND TIME <= ?",new String[]{""+from, ""+to}, null, null, "TIME DESC");
		}

		public Cursor getQueryCursorStraight(long from, long to) {
			SQLiteDatabase db = this.getReadableDatabase();
			return db.query(DB_NAME, colNames, "TIME > ? AND TIME <= ?",new String[]{""+from, ""+to}, null, null, "TIME ASC");
		}
}
