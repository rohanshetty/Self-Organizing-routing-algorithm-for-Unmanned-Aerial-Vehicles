/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Geocoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Rohan
 */
public class newGeocode {
     private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json"; 
    public double[] getLatLng(String address) throws ParseException, UnsupportedEncodingException, IOException{
        newGeocode g = new newGeocode();
        String s = g.getJSONByGoogle(address);
        
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(s);
        System.out.println(obj);
        JSONArray arr  = (JSONArray)obj.get("results");
        
        JSONObject addObj = (JSONObject)arr.get(0);
        JSONObject geomObj = (JSONObject)addObj.get("geometry");
        JSONObject locObj = (JSONObject) geomObj.get("location");
        System.out.println("************");

        double lat = (double)locObj.get("lat");
        double lon = (double)locObj.get("lng");
        System.out.println("Lat :"+lat+",  Lon : "+lon);
        
        double[] dlist = new double[2];
        dlist[0]=lat;
        dlist[1]=lon;
        return dlist;
    }
    
    public String getJSONByGoogle(String fullAddress) throws MalformedURLException, UnsupportedEncodingException, IOException {
        
    URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&sensor=false");

    // Open the Connection 
    URLConnection conn = url.openConnection();

    //This is Simple a byte array output stream that we will use to keep the output data from google. 
    ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

    // copying the output data from Google which will be either in JSON or XML depending on your request URL that in which format you have requested.
    IOUtils.copy(conn.getInputStream(), output);

    //close the byte array output stream now.
    output.close();

    return output.toString(); // This returned String is JSON string from which you can retrieve all key value pair and can save it in POJO.
    }

    
}
