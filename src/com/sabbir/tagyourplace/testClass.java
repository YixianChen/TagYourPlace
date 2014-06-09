//This class is responsible to test points inside a polygon
package com.sabbir.tagyourplace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
public class testClass {

	private PointInsidePolygon PIP=new PointInsidePolygon();
	boolean isNew = false;
	private static final int XML_INSERT_OFFSET = 18;
	static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	void test(String Path)
	{
		List<LatLng>poly= new ArrayList<LatLng>();
		//poly.add(new LatLng(65.61747870593815,22.12701067328453));
		//poly.add(new LatLng(65.61749697617084,22.129703275859356));
		//poly.add(new LatLng(65.61604279154105,22.129986248910427));
		//poly.add(new LatLng(65.61583585497631,22.12714310735464));
		
		/*poly.add(new LatLng(65.617383,22.13557));
		poly.add(new LatLng(65.617831,22.138389));
		poly.add(new LatLng(65.616604,22.139545));
		poly.add(new LatLng(65.616204,22.136664));*/
		//poly.add(new LatLng(35.675147,-0.351562));
		poly.add(new LatLng(65.61738901551855,22.13548745959997));
		poly.add(new LatLng(65.61785144382324,22.13852036744356));
		poly.add(new LatLng(65.61654344663022,22.139537259936333));
		poly.add(new LatLng(65.6160261813091,22.13702604174614));
		//poly.add(new LatLng(65.61738901551855,22.13548745959997));
		//poly.add(new LatLng(35.675147,-0.351562));
		
		List<LatLng>points= new ArrayList<LatLng>();
		//points.add(new LatLng(65.61720561979197,22.128184139728546));
		//points.add(new LatLng(65.61719343950162,22.12880775332451));
		//points.add(new LatLng(65.61699564739627,22.12931603193283));
		//points.add(new LatLng(65.61620474074594,22.129431031644344));
		//points.add(new LatLng(65.61686789144284,22.12909508496523));
		//points.add(new LatLng(65.61809822574745,22.12695937603712));
		//points.add(new LatLng(65.61808300090696,22.127967216074467));
		points.add(new LatLng(65.617453,22.137432));
		points.add(new LatLng(65.6174499,22.1374638));
		points.add(new LatLng(65.6174535,22.1374559));
		points.add(new LatLng(65.6174518,22.1374638));
		points.add(new LatLng(65.6174519,22.1374641));
		points.add(new LatLng(65.6174523,22.1374622));
		points.add(new LatLng(65.6174526,22.1374596));
	
	/*	points.add(new LatLng(29.075375,19.335938));
		points.add(new LatLng(27.683528,19.248047));
		points.add(new LatLng(24.926295,22.587891));
		points.add(new LatLng(29.382175,22.851563));
		
		points.add(new LatLng(31.728167,0.087891));
		points.add(new LatLng(27.683528,1.933594));
		points.add(new LatLng(28.921631,5.888672));
		points.add(new LatLng(32.842674,5.537109));
		points.add(new LatLng(31.428663,3.603516));*/
		String txt="";
		for (int i=0;i<points.size();i++)
		{
			
			if(ispointinsidepolygon(points.get(i), poly))
				//if(contains(poly, points.get(i)))
				//if(PIP.isTRUE(points.get(i), poly))
			{
				Log.w("Points "+i+" ","Y");
				txt = "Points "+i+" "+"Y "+txt;
				//Toast.makeText(context, "Points "+i+" ","Y", Toast.LENGTH_SHORT).show();
			}
			else
			{
				txt = "Points "+i+" "+"N "+txt;
			}
				
				//CreateLog(Path,"Points "+i+" "+"N");
				//Toast.makeText(context, "Points "+i+" ","N", Toast.LENGTH_SHORT).show();
		}
		CreateLog(Path,txt);
		
		
	}
	
	
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
	                if (rayCrossesSegment(point, a, b)) 
	                {
	                    crossings++;
	                }
	            }

	            // odd number of crossings?
	            return (crossings % 2 == 1);

	}
	
	
	

    boolean rayCrossesSegment(LatLng point, LatLng a, LatLng b) 
    {
        double px = point.longitude;
        double py = point.latitude;
        double ax = a.longitude;
        double ay = a.latitude;
        double bx = b.longitude;
        double by = b.latitude;
        if (ay > by) {
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
	
	
	
	
	
	
	
	
	public boolean contains(List<LatLng> poly,LatLng point)
    {
    	//List<LatLng> poly = new ArrayList<LatLng>();
    	//LatLng point = new LatLng(0,0);
        boolean oddTransitions = false;
        int polySides=poly.size();
        for( int i = 0, j = polySides -1; i < polySides; j = i++ )
        {
            if( ( poly.get(i).latitude < point.longitude && poly.get(j).longitude  >= point.longitude ) || ( poly.get(j).longitude < point.longitude && poly.get(i).longitude >= point.longitude ) )
            {
                if( poly.get(i).latitude  + ( point.longitude - poly.get(i).longitude ) / ( poly.get(j).longitude - poly.get(i).longitude ) * ( poly.get(j).latitude  - poly.get(i).latitude  ) < point.latitude )
                {
                    oddTransitions = !oddTransitions;          
                }
            }
        }
        return oddTransitions;
    }
	
	
	
	
	
	void CreateLog(String Path,String txt)
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
	    File file= new File(folder.getPath(),"traceponts.xml");
		
		if(!file.exists()) //if File doesn't exist
		{
			//make a new file
			file.createNewFile();
			isNew = true;
		}
		if(isNew)
		{
			FileOutputStream initialWriter = new FileOutputStream(file,true);
			
			String xml = " ";
			
			initialWriter.write(xml.getBytes());
			initialWriter.flush();
			initialWriter.close();
		}
			
		
		String dateString = FORMATTER.format(new Date());
		//fileData contains all data that need to be written in the file
		String fileData = "\n"+txt  ;
			
		
		
		RandomAccessFile fileAccess = new RandomAccessFile(file, "rw");
		FileLock lock = fileAccess.getChannel().lock();
		
		//fileAccess.seek((file.length() - XML_INSERT_OFFSET));
		fileAccess.write(fileData.getBytes());
		
		lock.release();
		fileAccess.close();
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
}
