package MyServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonWriter;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonArray;

/**
 * Servlet implementation class MyServer
 */
public class MyServer extends HttpServlet {
	private ArrayList<LocInfo> Info;
	private static final double EARTH_RADIUS = 6378137;
	private static final long serialVersionUID = 1L;

	private int radius=6;
    /**
     * Default constructor.
     */
    public MyServer() {
        // TODO Auto-generated constructor stub
    	Info=new ArrayList<LocInfo>();
    	System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String longitude = request.getParameter("longitude");
		String latitude = request.getParameter("latitude");
		String id = request.getParameter("nrPois");
		System.out.println("longitude"+longitude);
		System.out.println("latitude"+latitude);
		System.out.println("nrPois"+id);

    ArrayList<JsonObject> locs= getlocations_granularity(Double.parseDouble(longitude),Double.parseDouble(latitude));
		boolean found=false;
		int mysize=0;
    /*
		for (int i=0;i<Info.size();i++) {
			if(Info.get(i).getid().equals(id)) {
				for (int j=0;j<locs.size();j++) {
					if(!Info.get(i).getloc().contains(locs.get(j))) {
						double power=locs.get(j)-1;
						int inter=(int)Math.pow(2, power);
						mysize=mysize+inter*1000;

					}
				}
				Info.get(i).setloc(locs);
				found=true;
				break;
			}
		}
		if(!found) {
			LocInfo newuser=new LocInfo(id,locs);
			Info.add(newuser);
			mysize=newuser.totalsize();
		}
		System.out.println(mysize+"^^^^^^^^^^^^^^^^^");
		if(mysize>0) {

			File temp=new File("/home/temp.txt");
			create(temp,mysize);
			InputStream in = new FileInputStream(temp);
			int b;
			while((b=in.read())!= -1)
			{
				out.write(b);
			}
	        in.close();
		}
		System.out.print("detected locs: ");
		for(int i=0;i<locs.size();i++) {
			System.out.print(locs.get(i)+" ");
		}
		System.out.println("");
		out.println("");
		out.flush();
    */
    JsonWriter wrtr = Json.createWriter(out);
    JsonArray pois = Json.createArrayBuilder().build();
    for(JsonObject poi : locs) {
      pois.add(poi);
    }
    wrtr.write(pois);
    wrtr.close();
		out.close();
	}
	private ArrayList<JsonObject> getlocations(double longitude, double latitude) {
		int mysize=0;

    File locations = new File("locations.json");
    ArrayList<JsonObject> temp = new ArrayList<JsonObject>();
    try {
      InputStream is = new FileInputStream(locations);
      JsonReader rdr = Json.createReader(is);
      JsonObject obj = rdr.readObject();
      is.close();
      JsonArray results = obj.getJsonArray("data");
      for (JsonObject result : results.getValuesAs(JsonObject.class)) {
        double lg = Double.parseDouble(result.getString("longitude"));
        double la = Double.parseDouble(result.getString("latitude"));
        if (this.GetDistance(longitude, latitude, lg, la)<radius) {
          temp.add(result);
        }
      }
      JsonArray wcresults = obj.getJsonArray("wcdata");
      for (JsonObject result : wcresults.getValuesAs(JsonObject.class)) {
        double lg = Double.parseDouble(result.getString("longitude"));
        double la = Double.parseDouble(result.getString("latitude"));
        if (this.GetDistance(longitude, latitude, lg, la)<30) {
          temp.add(result);
        }
      }
      rdr.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return temp;

	}
	public static void create(File file, long length) throws IOException{
        long start = System.currentTimeMillis();
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "rw");
            r.setLength(length);
        } finally{
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
	  private double rad(double d)
	  {
	     return d * Math.PI / 180.0;
	  }
	  public double GetDistance(double lon1,double lat1,double lon2, double lat2) {
		    double radLat1 = rad(lat1);
		    double radLat2 = rad(lat2);
		    double a = radLat1 - radLat2;
		    double b = rad(lon1) - rad(lon2);
		    double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		    s = s * EARTH_RADIUS;
		   return s;//单位米
	  }
	  private ArrayList<JsonObject> getlocations_granularity(double longitude, double latitude) {
			int mysize=0;
      File locations = new File("locations.json");
      ArrayList<JsonObject> temp = new ArrayList<JsonObject>();
      try {
        InputStream is = new FileInputStream(locations);
        JsonReader rdr = Json.createReader(is);
        JsonObject obj = rdr.readObject();
        JsonArray results = obj.getJsonArray("gldata");
        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
          double lg = Double.parseDouble(result.getString("longitude"));
          double la = Double.parseDouble(result.getString("latitude"));
          if (this.GetDistance(longitude, latitude, lg, la)<radius) {
            temp.add(result);
          }
        }
        rdr.close();
        is.close();

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
		  return temp;
    }

}
