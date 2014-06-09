//This class contains algorithm to detect a given point is inside a polygon or not.......
package com.sabbir.tagyourplace;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
public class PointInsidePolygon {
	
	
	boolean ispointinsidepolygon( LatLng point,List<LatLng>poly)
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
	
	
	
	
	
	
	//Another algorithm to detect a given point is inside a polygon or not?
	//But this has a very low accuracy.......
	boolean isTRUE(LatLng point, List<LatLng>polygon)
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
	}
}

