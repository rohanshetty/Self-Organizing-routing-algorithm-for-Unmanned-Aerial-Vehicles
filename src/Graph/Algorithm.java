/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Graph;

import Business.Drone;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Rohan
 */
public class Algorithm {

    Stack stack;
    private int first=0;
    private int second=0;
    private int secondCnt=0;
    Random r;
    private int cnt=0;
    int flag = 0;
    public Algorithm() {
        r  = new Random();
        stack = new Stack();
        first = r.nextInt(50);
        second = 50-first;
        
    }
    
    
    
     public double getAngleBtwPoints(double lat1, double lon1, double lat2, double lon2){
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1); 
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
            * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);
//        System.out.println("1 Angle (Redundant):"+brng);
        brng = (brng + 360) % 360;
        brng = returnCorrectionAngle(brng);
        
//        brng = (brng+180)%360;
//        System.out.println("2nd Angle :"+brng);
//        brng=360-brng;
//        System.out.println("3rd Angle :"+brng + "  After (360-a)");
        
        
        return brng;
    }
     
    public double returnCorrectionAngle(double oldAngle){
        if(oldAngle>0 && oldAngle<=90){
            return (90-oldAngle);
        }
        else if(oldAngle>90 && oldAngle<=180){
            double x = 180 - oldAngle;
            return (x+270);
        }
        else if(oldAngle>180 && oldAngle<=270){
            double x = 270-oldAngle;
            return (x+180);
        }
        else if(oldAngle>270 && oldAngle<=360){
            double x = 360-oldAngle;
            return (x+90);
        }
        return -1;
    }
    
    
    public double[] returnNextPoint(double lat1, double lon1, double angle, double fixed){
        double newPts[] = new double[2];
        angle = Math.toRadians(angle);
        double s = Math.sin(angle);
        double dx = (Math.cos(angle))*fixed;
        double dy = (Math.sin(angle))*fixed;
        newPts[0] = lat1+dy;
        newPts[1] = lon1+dx;
        return newPts;
    }
    
    public boolean isWeatherGood(double lat, double lon, Drone d) throws MalformedURLException, IOException,ParseException{
        
//         URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon);
////        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=42.333079&lon=-71.096621");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//        String line;
//        JSONObject jsonObject = null;
//        JSONParser jsonParser = new JSONParser();
//
//        while ((line = reader.readLine()) != null) {
//            
//            jsonObject = (JSONObject) jsonParser.parse(line);
//            
//            System.out.println(jsonObject);   
//        }
//        
//        JSONObject mainObj = (JSONObject)jsonObject.get("main");
//        double temp = (double)mainObj.get("temp");
//        long humidity = (long)mainObj.get("humidity");
//        long pressure = (long)mainObj.get("pressure");
//        JSONObject windObj = (JSONObject)jsonObject.get("wind");
//        double windSpeed = (double)windObj.get("speed");
//        double gust = (double)windObj.get("gust");
//        
//        reader.close();
//        
//        if(d.getTemperature()<temp && 
//                d.getTurbulence() < gust &&
//                d.getWindSpeed() < windSpeed 
////                && d.getHumidity() < humidity &&
////                d.getPressure() < pressure
//                ){
//            return false;
//        }
//        else{
//            return true;
//        } 
//        

        int i = r.nextInt(10);
        if(i>5){
            return false;   
        }
        return true;   
    }
    
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
    double earthRadius = 3958.75;
    double dLat = Math.toRadians(lat2-lat1);
    double dLng = Math.toRadians(lng2-lng1);
    double sindLat = Math.sin(dLat / 2);
    double sindLng = Math.sin(dLng / 2);
    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    double dist = earthRadius * c;

    return dist;
    }
    
    public void implement(double lat1, double lon1, double lat2, double lon2, Drone drone) throws IOException, MalformedURLException, ParseException{
//        double fixed = .00001;
        double fixed = .00001;
//        double lat1 = 42.339347;
//        double lon1 = -71.089714;
//        
//        double lat2 = 42.331844;
//        double lon2 = -71.111644;

        Algorithm m = new Algorithm();
//        Stack stack = new Stack();

        System.out.println("Begin --");
        double angle = m.getAngleBtwPoints(lat1, lon1, lat2, lon2);
        System.out.println(angle);
          
        double newPts [] = new double[2];
        newPts[0] = lat1;
        newPts[1] = lon1;
        System.out.println("Start point ("+newPts[0]+","+newPts[1]+")");
        System.out.println("End point ("+lat2+","+lon2+")");
        
        Link l = new Link(lat1, lon1);
        stack.insert(l);
//        double tempPts[] = new double[2];
        
//        while((m.distFrom(newPts[0], newPts[1], lat2, lon2)>.008)){
//            Link p = stack.seek();
//            int z = p.getLastNonNullLink();
//            if(z<9){
//                
//            }
//        }
        boolean flag = false;
        
        while(true){
            if(stack.isEmpty()){
                System.out.println("No Such Route Found.");
                break;
            }
            Link p = stack.seek();
//                        if((.008>=m.distFrom(p.getLatitude(),p.getLongitude(), lat2, lon2))){//                System.out.println("execution done");
            if((.008>=m.distFrom(p.getLatitude(),p.getLongitude(), lat2, lon2))){//                System.out.println("execution done");
                break;
            }
                
                angle = m.getAngleBtwPoints(p.getLatitude(),p.getLongitude(),lat2, lon2);
//                System.out.println("Link :"+ p);
                while(!p.allChildrenVisited()){
                        flag=false;
                        System.out.println("*");
                        int index = p.getNextEmptyChildIndex();
                        double arb = m.getArbitaryAngle(index);
                        newPts = m.returnNextPoint(newPts[0], newPts[1], angle+arb, fixed);
                        Link a = new Link(newPts[0],newPts[1]);
//                        System.out.println("Child of "+p+" : "+a+ "Found  at angle" +arb);
                        p.insertNextChild(a);
                        if(m.isWeatherGood(newPts[0],newPts[1],drone)){
//                            System.out.println(a+"Accepted");
                            stack.insert(a);
                            flag=true;
                            break;
                        }
                        else{
//                            System.out.println(a+"Rejected");
                        }
                }
                if(flag==false){
                    stack.remove();
//                    System.out.println("Link Removed from stack");
                }
            }
             
//        
//        
        System.out.println("************************");
        double dist = m.distFrom(lat1, lon1,lat2,lon2);
        System.out.println(" Final Distance : "+dist);
        
        System.out.println("***");
        
        stack.displayListFromBottom();
        System.out.println("("+ lat2 + ","+lon2+")");
    }
    
     public int getArbitaryAngle(int index){
        switch(index){
            case 0 : return 0;
            case 1: return 20;
            case 2: return -20;
            case 3: return 40;
            case 4: return -40;
            case 5: return 60;
            case 6: return -60;
            case 7: return 90;
            case 8: return -90; 
            default : return -1;    
    }
 
}

    public Stack getStack() {
        return stack;
    }

     
}