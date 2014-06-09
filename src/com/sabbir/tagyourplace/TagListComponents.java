package com.sabbir.tagyourplace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.android.gms.maps.model.LatLng;

import android.os.Environment;

public class TagListComponents {
	//private variables
    long _id;
    String _name;
    String _date;
    List<LatLng>ll = new ArrayList<LatLng>();
    
    // Empty constructor
    public TagListComponents(){
         
    }
    // constructor
    public TagListComponents(int id, String name, String _date, List<LatLng>ll){
        this._id = id;
        this._name = name;
        this._date =_date;
        this.ll = ll;
        
    }
     
 
    // getting ID
    public long getID(){
        return this._id;
    }
     
    // setting id
    public void setID(long id){
        this._id = id;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
    // getting date
    public String getDate(){
        return this._date;
    }
     
    // setting date
    public void setDate(String _date){
        this._date=_date;
    }
    // getting Coordinates
    public List<LatLng> getCoordinates(){
        return this.ll;
    }
     
    // setting phone number
    public void setCoordinates(List<LatLng> _ll){
    	this.ll = _ll;
    }
}
