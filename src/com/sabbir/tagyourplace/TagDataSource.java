package com.sabbir.tagyourplace;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

//import org.w3c.dom.Comment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.widget.Toast;

public class TagDataSource {

  // Database fields
  //private SQLiteDatabase database;
 // private MySQLiteHelper dbHelper;
  private Context context;
  public static final String TABLE_TAG = "tags";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_LATITUDE = "lat";
  public static final String COLUMN_LONGITUDE = "lon";
  //private static final String DATABASE_NAME = "tag.db";
  //private String[] allColumns = { COLUMN_ID, COLUMN_NAME,COLUMN_DATE,COLUMN_LATITUDE,COLUMN_LONGITUDE };

  public TagDataSource(Context context) {
	this.context = context;
    //dbHelper = new MySQLiteHelper(context);
  }

//  public void open() throws SQLException {
//    database = dbHelper.getWritableDatabase();
//  }

/*  public void close() {
    dbHelper.close();
  }*/
  public Cursor query()
  {
	  return context.getContentResolver().query(StatusProvider.CONTENT_URI, null, null, null, null);
  }

  public void createTag(String tagname,String date, List<LatLng> llList) 
  {
    for(int i=0;i<llList.size();i++)
    {
    	ContentValues values = new ContentValues();
    	values.put(COLUMN_NAME, tagname);
    	values.put(COLUMN_DATE, date);
    	values.put(COLUMN_LATITUDE, llList.get(i).latitude);
    	values.put(COLUMN_LONGITUDE, llList.get(i).longitude);
    	/*long insertId = database.insert(MySQLiteHelper.TABLE_TAG, null,
        values);
    	Cursor cursor = database.query(MySQLiteHelper.TABLE_TAG,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    	cursor.moveToFirst();
    	TagListComponents tag = cursorToTList(cursor);
    	cursor.close();*/
    	context.getContentResolver().insert(StatusProvider.CONTENT_URI, values);
    }
    //return tag;
  }

 /* public void deleteTag(TagListComponents tag) {
    long id = tag.getID();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_TAG, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }*/

  public void deleteAllTag() {
	    //long id = tag.getID();
	    //System.out.println("Comment deleted with id: " + id);
	   // database.delete(MySQLiteHelper.TABLE_TAG, null, null);
	    context.getContentResolver().delete(StatusProvider.CONTENT_URI, null, null);
	    
	  }
  public void deleteTag(String TagName) {
	  	String Selection = COLUMN_NAME+"=?";
	  	String[] SelectionArgs = {TagName};
	    context.getContentResolver().delete(StatusProvider.CONTENT_URI, Selection, SelectionArgs);
	    
	  }
  
//  public List<TagListComponents> getAllTags() {
//    List<TagListComponents> TLists = new ArrayList<TagListComponents>();
//
//    Cursor cursor = database.query(MySQLiteHelper.TABLE_TAG,
//        allColumns, null, null, null, null, null);
//
//    cursor.moveToFirst();
//    while (!cursor.isAfterLast()) {
//      TagListComponents Tag = cursorToTList(cursor);
//      TLists.add(Tag);
//      cursor.moveToNext();
//    }
//    // Make sure to close the cursor
//    cursor.close();
//    return TLists;
//  }

/*  private TagListComponents cursorToTList(Cursor cursor) 
  {
	TagListComponents TList = new TagListComponents();
	TList.setID(cursor.getLong(0));
	TList.setName(cursor.getString(1));
	TList.setDate(cursor.getString(2));
    return TList;
  }*/
  public List<LatLng> getPolygon(String tag_Name)
  {
	  List<LatLng> polygon = new ArrayList<LatLng>();
	  //String sql="select "+MySQLiteHelper.COLUMN_LATITUDE+","+MySQLiteHelper.COLUMN_LONGITUDE+" from "+ MySQLiteHelper.TABLE_TAG+ " where "+MySQLiteHelper.COLUMN_NAME+" = '"+tag_Name+"' ;";
	  String[] Projection ={COLUMN_LATITUDE,COLUMN_LONGITUDE};
	  String Selection = COLUMN_NAME+"=?";
	  String[] SelectionArgs = {tag_Name};
	  
	  Cursor cursor = context.getContentResolver().query(StatusProvider.CONTENT_URI,Projection,Selection,SelectionArgs,null);
	  
	  cursor.moveToFirst();
	  while (!cursor.isAfterLast()) {
	      LatLng clickedll = new LatLng(cursor.getFloat(0), cursor.getFloat(1));
	      polygon.add(clickedll);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	  return polygon;
  }
  public List<String > getAllTagsName()
  {
	  List<String> tagNames= new ArrayList<String>();
	  //String sql="select distinct "+MySQLiteHelper.COLUMN_NAME+" from "+MySQLiteHelper.TABLE_TAG+";";
	  String[] Projections={"distinct "+COLUMN_NAME};
	  Cursor cursor = context.getContentResolver().query(StatusProvider.CONTENT_URI,Projections,null,null,null);
	  cursor.moveToFirst();
	  while (!cursor.isAfterLast()) 
	  {
	      String tagname = cursor.getString(0);
	      tagNames.add(tagname);
	      cursor.moveToNext();
	  }
	    // Make sure to close the cursor
	    cursor.close();
	  return tagNames;
  }
} 