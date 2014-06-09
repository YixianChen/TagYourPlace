package com.sabbir.tagyourplace;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;

public class PlaceTagManager {
	public static final String COLUMN_NAME = "name";
	public static final String AUTHORITY="com.sabbir.tagyourplace.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);
	private static final String COLUMN_LATITUDE = "lat";
	private static final String COLUMN_LONGITUDE = "lon";
	private Context tagContext;
	private LatLng point;
	// Contructor
	public PlaceTagManager(Context context,LatLng point) {
		this.tagContext = context;
		this.point = point;
		
	}
	public String getPositionName()
	{
		List<String> TagNames = getTagNames();
		//int i=0;
		String Tag=null;
		List<String> TagNamesList=new ArrayList<String>();
		for (int i=0;i<TagNames.size();i++)
		{
			
				List<LatLng> polygon = getPolygon(TagNames.get(i));
				if(PositionIsInsidePolygon(point, polygon))
				{
					Tag=TagNames.get(i);
					TagNamesList.add(Tag);
				}
				
		}
	
		
		if(Tag==null) 
		{
			Tag="Unknown";
			return Tag;
		}
		else if(TagNamesList.size()>1)
		{	
			if(TagNamesList.size()==2)
			{
				return PolyInsidePoly(TagNamesList.get(0), TagNamesList.get(1));
			}
			else
			{
				String Temp=PolyInsidePoly(TagNamesList.get(0), TagNamesList.get(1));
				for(int k=2;k<TagNamesList.size();k++)
				{
					Temp = PolyInsidePoly(Temp, TagNamesList.get(k));
					//Temp=Temp+"#"+TagNamesList.get(k);
					
				}
				return Temp;
			}
			
			
		}
			
		else
			return Tag;
			
	}
 	List<String> getTagNames()
	{
 		String[] Projections={"distinct "+COLUMN_NAME};
		Cursor cursor = tagContext.getContentResolver().query(CONTENT_URI, Projections, null, null, null);
		
		cursor.moveToFirst();
		List<String> tagNames= new ArrayList<String>();
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
 	//Polygon is inside polygon
 	String PolyInsidePoly(String polyName1,String polyName2)
 	{
 		if (polyName1==null)
 		return polyName2;
 		
 		else
 		{
 			List<LatLng>polygon1=getPolygon(polyName1);
 			List<LatLng>polygon2=getPolygon(polyName2);
 			boolean flag=true;
 			for(int i=0;i<polygon1.size();i++)
 			{
 				flag=( PositionIsInsidePolygon(polygon1.get(i),polygon2 )&& flag );
 			}
 			if(flag==true) //polygon1 is inside polygon2
 				return polyName1;
 			else           //polygon1 is not inside polygon2
 			{
 				boolean flag2=true;
 	 			for(int t=0;t<polygon2.size();t++)
 	 			{
 	 				flag2=(PositionIsInsidePolygon(polygon2.get(t),polygon1 )&& flag2);
 	 			}
 	 			if(flag2==true)  //polygon2 is inside polygon1
 	 				return polyName2;
 	 			
 	 			else // None of them are belongs to each other so decision will be based on smaller parameter of the polygon
 	 			{
 	 				if (perimeterOfPolygon(polygon1)> perimeterOfPolygon(polygon2))
 	 					return polyName2;
 	 				else 
 	 					return polyName1;
 	 				
 	 			}
 			}
 		}
 			
 	}
 	
 	//Return the perimeter of a polygon
 	double perimeterOfPolygon(List<LatLng>polygon)
 	{
 		double perimeter=0;
 		for(int i=0;i<polygon.size()-1;i++)
 		{
 			perimeter=perimeter+distance(polygon.get(i).latitude, polygon.get(i).longitude, polygon.get(i+1).latitude, polygon.get(i+1).longitude);
 		}
 		return perimeter;
 	}
 	//Return Distance between two points(LatLons)
 	double distance(double lat1, double lon1, double lat2, double lon2)
 	{
 		//Haversine formula
 		double EARTH_RADIUS = 6372.8; // In kilometers
 	    
 	        double dLat = Math.toRadians(lat2 - lat1);
 	        double dLon = Math.toRadians(lon2 - lon1);
 	        lat1 = Math.toRadians(lat1);
 	        lat2 = Math.toRadians(lat2);
 	 
 	        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
 	        double c = 2 * Math.asin(Math.sqrt(a));
 	        return EARTH_RADIUS * c;
 		
 	}
 	boolean PositionIsInsidePolygon( LatLng point,List<LatLng>poly)
	{
	    int crossings = 0;
	            
	            // for each edge
	            for (int i = 0; i < poly.size(); i++) 
	            {
	                LatLng a = poly.get(i);
	                int j = i + 1;
	                if (j >= poly.size()) 
	                {
	                    j = 0;
	                }
	                LatLng b = poly.get(j);
	                //point : the point from which the ray starts
	                //a : the end-point of the segment with the smallest longitudes (a must be "below" b)
	                //b : the end-point of the segment with the greatest longitudes  (b must be "above" a)
	                if (rayCrossesSegment(point, a, b)) 
	                {
	                    crossings++;
	                }
	            }
	            // odd number of crossings?
	            return (crossings % 2 == 1);
	}
	
	//Ray-casting algorithm
    boolean rayCrossesSegment(LatLng point, LatLng a, LatLng b) 
    {
        double px = point.longitude;
        double py = point.latitude;
        double ax = a.longitude;
        double ay = a.latitude;
        double bx = b.longitude;
        double by = b.latitude;
        
        if (ay > by) 
        {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
        
        if (py == ay || py == by) py += 0.00000001;
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) return false;
        if (px < Math.min(ax, bx)) return true;

        double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
        double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
        return (blue >= red);
    }
	
 	
 	
/*	boolean PositionIsInsidePolygon(LatLng point, List<LatLng>polygon)
	{

	  boolean result = false;
	  for (int i=1; i < polygon.size(); i++)
	  {
	      
	        if (!(polygon.get(i-1).latitude > point.latitude && polygon.get(i).latitude > point.latitude)
	             && (      (polygon.get(i-1).longitude < point.longitude && polygon.get(i).longitude > point.longitude)  ||    
	                       (polygon.get(i-1).longitude > point.longitude && polygon.get(i).longitude < point.longitude)  ||
	                       (polygon.get(i-1).longitude <= point.longitude && polygon.get(i).longitude > point.longitude) ||
	                       (polygon.get(i-1).longitude >= point.longitude && polygon.get(i).longitude < point.longitude)
	                )
	            )
	              { result = !(result); }
	  }
	  return result;
	}*/
	
	List<LatLng> getPolygon(String tag_Name)
	  {
		  List<LatLng> polygon = new ArrayList<LatLng>();
		  String[] Projection ={COLUMN_LATITUDE,COLUMN_LONGITUDE};
		  String Selection = COLUMN_NAME+"=?";
		  String[] SelectionArgs = {tag_Name};
		  
		  Cursor cursor = tagContext.getContentResolver().query(CONTENT_URI,Projection,Selection,SelectionArgs,null);
		  
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
}
