package com.sabbir.tagyourplace;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class TagHandler{
private PointInsidePolygon PIP=new PointInsidePolygon();
private Context context;
public TagHandler(Context context)
{
	this.context = context;
}
boolean getNameFromPosition(LatLng currentposition,List <LatLng> polygon)
{
		//List <LatLng> polygon = new ArrayList<LatLng>();
		if (PIP.isTRUE(currentposition, polygon)) return true;
		else return false;
}

void getNameforPosition(LatLng currentposition)
{
	TagDataSource TDS= new TagDataSource(this.context);
	List<String> tags=TDS.getAllTagsName();
	for(int i=0;i<tags.size();i++)
	{
		if(getNameFromPosition(currentposition, TDS.getPolygon(tags.get(i)))) 
		{
			Log.w("TagHandeler","Point is inside polygon");
			i=tags.size(); // which will break the for loop
		}
	}
}
}
