package com.sabbir.tagyourplace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//import java.io.FileOutputStream;
import org.xmlpull.v1.XmlSerializer;

import com.google.android.gms.maps.model.LatLng;

import android.app.AlertDialog;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class XmlFileCreator 
{
	boolean isNew = false;
	private static final int XML_INSERT_OFFSET = 18;
	static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	/*public void create(String filelocation,String tagName,List<LatLng>LL)
	{
		if (APPEND)
		{
			File newxmlfile = new File(filelocation,"TAG.xml");
			 //we have to bind the new file with a FileOutputStream
	        FileOutputStream fileos = null;       	
	        try
	        {
	        	fileos = new FileOutputStream(newxmlfile,APPEND);
	        }
	        catch(FileNotFoundException e)
	        {
	        	Log.e("FileNotFoundException", "can't create FileOutputStream");
	        }
			
	      //we create a XmlSerializer in order to write xml data
	        XmlSerializer serializer = Xml.newSerializer();
	        try {
	        	//we set the FileOutputStream as output for the serializer, using UTF-8 encoding
				serializer.setOutput(fileos, "UTF-8");
				//Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null) 
				//serializer.startDocument(null, Boolean.valueOf(true)); 
				//set indentation option
				//serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true); 
				//start a tag called "tag"
				serializer.startTag(null, "tag"); 
				//i indent code just to have a view similar to xml-tree
					serializer.startTag(null, "name");
					serializer.text(tagName);
					serializer.endTag(null, "name");
					
					serializer.startTag(null, "date");
					//set an attribute called "attribute" with a "value" for <date>
					//serializer.attribute(null, "attribute", "value");
					Date date = new Date();
					serializer.text(date.toString());
					serializer.endTag(null, "date");
				
					serializer.startTag(null, "time");
					//write some text inside <time>
					serializer.text("some text inside time");
					serializer.endTag(null, "time");
					
					serializer.startTag(null, "coordinates");
					//write some text inside <time>
					//serializer.text("some text inside LatLng");
					for(int i=0;i<LL.size();i++)
					{
						serializer.startTag(null, "Lat");
						serializer.text(""+LL.get(i).latitude);
						serializer.endTag(null, "Lat");
						serializer.startTag(null, "Lon");
						serializer.text(""+LL.get(i).longitude);
						serializer.endTag(null, "Lon");
						
					}
						
					serializer.endTag(null, "coordinates");
					
					
				serializer.endTag(null, "tag");
				//serializer.endDocument();
				//write xml data into the FileOutputStream
				serializer.flush();
				//finally we close the file stream
				fileos.close();
				
		        //TextView tv = (TextView)this.findViewById(R.id.result);
				//tv.setText("file has been created on SD card");
			} 
			catch (Exception e) 
			{
				Log.e("Exception","error occurred while creating xml file");
			}
		}
		else 
		{
			//create a new file called "new.xml" in the SD card
		    File newxmlfile = new File(filelocation,"TAG.xml");
		    try 
		    {
				newxmlfile.createNewFile();
			} 
		    catch (IOException e) 
		    {
			
				//e.printStackTrace();
				Log.e("IOException", "exception in createNewFile() method");
			}
		  //we have to bind the new file with a FileOutputStream
	        FileOutputStream fileos = null;       	
	        try
	        {
	        	fileos = new FileOutputStream(newxmlfile);
	        }
	        catch(FileNotFoundException e)
	        {
	        	Log.e("FileNotFoundException", "can't create FileOutputStream");
	        }
	      //we create a XmlSerializer in order to write xml data
	        XmlSerializer serializer = Xml.newSerializer();
	        try {
	        	//we set the FileOutputStream as output for the serializer, using UTF-8 encoding
				serializer.setOutput(fileos, "UTF-8");
				//Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null) 
				serializer.startDocument(null, Boolean.valueOf(true)); 
				//set indentation option
				serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true); 
				//start a tag called "tag"
				serializer.startTag(null, "tag"); 
				//i indent code just to have a view similar to xml-tree
					serializer.startTag(null, "name");
					serializer.text(tagName);
					serializer.endTag(null, "name");
					
					serializer.startTag(null, "date");
					//set an attribute called "attribute" with a "value" for <date>
					//serializer.attribute(null, "attribute", "value");
					Date date = new Date();
					serializer.text(date.toString());
					serializer.endTag(null, "date");
				
					serializer.startTag(null, "time");
					//write some text inside <time>
					serializer.text("some text inside time");
					serializer.endTag(null, "time");
					
					serializer.startTag(null, "coordinates");
					//write some text inside <time>
					//serializer.text("some text inside LatLng");
					for(int i=0;i<LL.size();i++)
					{
						serializer.startTag(null, "Lat");
						serializer.text(""+LL.get(i).latitude);
						serializer.endTag(null, "Lat");
						serializer.startTag(null, "Lon");
						serializer.text(""+LL.get(i).longitude);
						serializer.endTag(null, "Lon");
						
					}
						
					serializer.endTag(null, "coordinates");
					
					
				serializer.endTag(null, "tag");
				serializer.endDocument();
				//write xml data into the FileOutputStream
				serializer.flush();
				//finally we close the file stream
				fileos.close();
				
		        //TextView tv = (TextView)this.findViewById(R.id.result);
				//tv.setText("file has been created on SD card");
			} 
			catch (Exception e) 
			{
				Log.e("Exception","error occurred while creating xml file");
			}
			
		}
	
	}
	*/
	
	
	
	void Create2(String Path,LatLng LL)
	{
		
		 //Create Folder
	    File folder = new File(Path);
	    if (!folder.exists()) //if Folder doesn't exist
	    {
	    	//make a folder
	    	folder.mkdir();  
	    	isNew = true;
	    }
		try {
			
		//Create File
	    File file= new File(folder.getPath(),"track.xml");
		
		if(!file.exists()) //if File doesn't exist
		{
			//make a new file
			file.createNewFile();
			isNew = true;
		}
		if(isNew)
		{
			FileOutputStream initialWriter = new FileOutputStream(file,true);
			
			String xml = "<?xml version=\"1.0\"?>" + 
						 "\n"+  "<xml xmlns=\"http://www.ltu.se/org/srt?l=en\">" +
						 "\n"+ 	"<Document>" + "</Document>" +
						 "\n"+  "</xml>";
			
			initialWriter.write(xml.getBytes());
			initialWriter.flush();
			initialWriter.close();
		}
			
		
		String dateString = FORMATTER.format(new Date());
		//fileData contains all data that need to be written in the file
		String fileData = "\n"+          "<tag>" + 
							
							"\n\t"+				"<date>" + dateString + "</date>" + 
							"\n\t"+				"<coordinates>" ;
		

			fileData =fileData + "\n\t\t"+	"<Latitude>" + LL.latitude + "</Latitude>" + "\t"+"<Longitude>" + LL.longitude + "</Longitude>" ;
		
		fileData =fileData +"\n\t"+ "</coordinates>" + 
					"\n"+	"</tag>" + 
					"\n"+ "</Document>" + 
					"\n"+ "</xml>";
			
		
		
		RandomAccessFile fileAccess = new RandomAccessFile(file, "rw");
		FileLock lock = fileAccess.getChannel().lock();
		
		fileAccess.seek((file.length() - XML_INSERT_OFFSET));
		fileAccess.write(fileData.getBytes());
		
		lock.release();
		fileAccess.close();
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	
	//Create File from Tag data at the given path
	void Create(String Path,String tagName,List<LatLng>LL)
	{
		
		 //Create Folder
	    File folder = new File(Path);
	    if (!folder.exists()) //if Folder doesn't exist
	    {
	    	//make a folder
	    	folder.mkdir();  
	    	isNew = true;
	    }
		try {
			
		//Create File
	    File file= new File(folder.getPath(),"TAG.xml");
		
		if(!file.exists()) //if File doesn't exist
		{
			//make a new file
			file.createNewFile();
			isNew = true;
		}
		if(isNew)
		{
			FileOutputStream initialWriter = new FileOutputStream(file,true);
			
			String xml = "<?xml version=\"1.0\"?>" + 
						 "\n"+  "<xml xmlns=\"http://www.ltu.se/org/srt?l=en\">" +
						 "\n"+ 	"<Document>" + "</Document>" +
						 "\n"+  "</xml>";
			
			initialWriter.write(xml.getBytes());
			initialWriter.flush();
			initialWriter.close();
		}
			
		
		String dateString = FORMATTER.format(new Date());
		//fileData contains all data that need to be written in the file
		String fileData = "\n"+          "<tag>" + 
							"\n\t"+				"<name>" + tagName + "</name>" +
							"\n\t"+				"<date>" + dateString + "</date>" + 
							"\n\t"+				"<coordinates>" ;
		for(int i=0;i<LL.size();i++)
		{

			fileData =fileData + "\n\t\t"+	"<Latitude>" + LL.get(i).latitude + "</Latitude>" + "\t"+"<Longitude>" + LL.get(i).longitude + "</Longitude>" ;
		}
		fileData =fileData +"\n\t"+ "</coordinates>" + 
					"\n"+	"</tag>" + 
					"\n"+ "</Document>" + 
					"\n"+ "</xml>";
			
		
		
		RandomAccessFile fileAccess = new RandomAccessFile(file, "rw");
		FileLock lock = fileAccess.getChannel().lock();
		
		fileAccess.seek((file.length() - XML_INSERT_OFFSET));
		fileAccess.write(fileData.getBytes());
		
		lock.release();
		fileAccess.close();
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	boolean Delete(String Path)
	{
		//Log.w("Xml file ","I am in boolean Delete");
		//File folder = new File(Path);
		File file= new File(Path+"/TAG.xml");
	
		if(file.exists()) //if File exist
		{
			//Log.w("Xml file ","Exists");
			//delete the file
			try {
				file.delete();
				Log.w("Xml file ","deleted");
			} catch (Exception ee) {
				// TODO: handle exception
				ee.printStackTrace();
				Log.e(ee.toString()," Xml file detele operation failed");
			}
			
			
			return true;
		}
		else
		{
			Log.w("Xml file ","Not Exists");
			return false;
		}
		
	}
	// Get the information of External Storage privilege, to decide whether file writing is possible.... 
	boolean ExtStorageInfo()
	{
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) 
		{
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} 
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) 
		{
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} 
		else 
		{
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return (mExternalStorageAvailable && mExternalStorageWriteable);
	}
    
}

