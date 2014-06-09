package com.sabbir.tagyourplace;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_TAG = "tags";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_LATITUDE = "lat";
  public static final String COLUMN_LONGITUDE = "lon";
  private static final String DATABASE_NAME = "tag.db";
  private static final int DATABASE_VERSION = 3;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_TAG + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_NAME + " varchar, "
      + COLUMN_DATE + " varchar, "
      + COLUMN_LATITUDE + " float, "
      + COLUMN_LONGITUDE + " float "
      + " );";

  public MySQLiteHelper(final Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
    onCreate(db);
  }
  

}

class DatabaseContext extends ContextWrapper {

private static final String DEBUG_CONTEXT = "DatabaseContext";

public DatabaseContext(Context base) {
    super(base);
}

@Override
public File getDatabasePath(String name) 
{
    File sdcard = Environment.getExternalStorageDirectory();    
    String dbfile = sdcard.toString() + File.separator+ "databases" + File.separator + name;
    if (!dbfile.endsWith(".db"))
    {
        dbfile += ".db" ;
    }

    File result = new File(dbfile);

    if (!result.getParentFile().exists())
    {
        result.getParentFile().mkdirs();
    }

    if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN))
    {
        Log.w(DEBUG_CONTEXT,
                "getDatabasePath(" + name + ") = " + result.getAbsolutePath());
    }

    return result;
}

@Override
public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) 
{
    SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    // SQLiteDatabase result = super.openOrCreateDatabase(name, mode, factory);
    if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN))
    {
        Log.w(DEBUG_CONTEXT,
                "openOrCreateDatabase(" + name + ",,) = " + result.getPath());
    }
    return result;
}
}