package Graph;

import Business.Drone;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import org.json.simple.parser.ParseException;

/**
 * Implementation of the routing algorithm
 * @author Rohan
 */
public class Algorithm {

    Stack stack;
    private int first = 0;
    private int second = 0;
    private int secondCnt = 0;
    Random r;
    private int cnt = 0;
    int flag = 0;

    public Algorithm() {
        r = new Random();
        stack = new Stack();
        first = r.nextInt(50);
        second = 50 - first;
    }

    /**
     * Calculate the angle between two geo-cordinates based on haversine formula
     *
     * @param lat1 Latitude for the first geo-cordinate
     * @param lon1 Longitude for the first geo-cordinate
     * @param lat2 Latitude for the second geo-cordinate
     * @param lon2 Longitude for the second geo-cordinate
     * @return double This returns the angle between the geo-cordinates
     */
    public double getAngleBtwPoints(double lat1, double lon1, double lat2, double lon2) {
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
        brng = (brng + 360) % 360;
        brng = returnCorrectionAngle(brng);
        return brng;
    }

    /**
     * Calculate and return the corrected angle
     *
     * @param oldAngle Angle derived from haversine formula
     * @return returns an angle to be used for further calculations
     */
    public double returnCorrectionAngle(double oldAngle) {
        if (oldAngle > 0 && oldAngle <= 90) {
            return (90 - oldAngle);
        } else if (oldAngle > 90 && oldAngle <= 180) {
            double x = 180 - oldAngle;
            return (x + 270);
        } else if (oldAngle > 180 && oldAngle <= 270) {
            double x = 270 - oldAngle;
            return (x + 180);
        } else if (oldAngle > 270 && oldAngle <= 360) {
            double x = 360 - oldAngle;
            return (x + 90);
        }
        return -1;
    }

    /**
     * Calculate the next arbitrary point based on the given angle and the
     * previous geo-cordinates
     *
     * @param lat1 Latitude for the previous geo-cordinate
     * @param lon1 Longitude for the previous geo-cordinate
     * @param angle Desired angle between the two points
     * @param fixed Inter geo-cordinate distance
     * @return double[] returns latitude and longitude of the new points
     */
    public double[] returnNextPoint(double lat1, double lon1, double angle, double fixed) {
        double newPts[] = new double[2];
        angle = Math.toRadians(angle);
        double s = Math.sin(angle);
        double dx = (Math.cos(angle)) * fixed;
        double dy = (Math.sin(angle)) * fixed;
        newPts[0] = lat1 + dy;
        newPts[1] = lon1 + dx;
        return newPts;
    }

    /**
     * Determine if the weather condition at a geo-cordinate is good for a drone
     * based on the drones threshold.Openweathermap API is consumed for
     * retrieving real-time weather conditions.
     *
     * @param lat Latitude for the geo-cordinate
     * @param lon Longitude for the geo-cordinate
     * @param drone Drone in consideration from the pool of drones in the system
     * @return boolean returns true if the drone can sustain the weather
     */
    public boolean isWeatherGood(double lat, double lon, Drone drone) throws MalformedURLException, IOException, ParseException {
        /*
         URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon);
         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
         String line;
         JSONObject jsonObject = null;
         JSONParser jsonParser = new JSONParser();
         while ((line = reader.readLine()) != null) {
         jsonObject = (JSONObject) jsonParser.parse(line);
         }
        
         JSONObject mainObj = (JSONObject)jsonObject.get("main");
         double temp = (double)mainObj.get("temp");
         long humidity = (long)mainObj.get("humidity");
         long pressure = (long)mainObj.get("pressure");
         JSONObject windObj = (JSONObject)jsonObject.get("wind");
         double windSpeed = (double)windObj.get("speed");
         double gust = (double)windObj.get("gust");
         reader.close();
        
         if(d.getTemperature()<temp && d.getTurbulence() < gust &&
         d.getWindSpeed() < windSpeed && d.getHumidity() < humidity &&
         d.getPressure() < pressure){
         return false;
         }
         else{
         return true;
         } 
         */
        
        /*
        Demo code added for randomization of weather conditions(used for 
        demonstration purposes when the api isn't called)
        */
        int i = r.nextInt(10);
        if (i > 5) {
            return false;
        }
        return true;
    }

    /**
     * Calculate the distance between two geo-cordinates based on haversine
     * formula
     *
     * @param lat1 Latitude for the first geo-cordinate
     * @param lon1 Longitude for the first geo-cordinate
     * @param lat2 Latitude for the second geo-cordinate
     * @param lon2 Longitude for the second geo-cordinate
     * @return double This returns the distance between the geo-cordinates
     */
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return dist;
    }

    /**
     * Each node on the graph will have 8 angle variations it can take to determine 
     * the next geo-cordinate.The angle varies as and when the next node at a 
     * particular angle isn't safe for the drone to fly.All nodes which qualify 
     * to be safe are added to the routing path.If all angle variations are 
     * declared unsafe then the algorithm backtracks to the previous node and 
     * tries a variation of its present angle until a safe node is found.
     *
     * @param lat1 Latitude of the drones source
     * @param lon1 Longitude for the drones source
     * @param lat2 Latitude for the drones destination
     * @param lon2 Longitude for the drones destination
     * @param drone The drone in consideration from the pool of drones in the
     * system
     * @return double This returns the distance between the geo-cordinates
     */
    public void implement(double lat1, double lon1, double lat2, double lon2, Drone drone) throws IOException, MalformedURLException, ParseException {
        double fixed = .00001; // Distance between two geo-cordinates
        Algorithm m = new Algorithm();
        double angle = m.getAngleBtwPoints(lat1, lon1, lat2, lon2);
        double newPts[] = new double[2];
        newPts[0] = lat1;
        newPts[1] = lon1;
        Link l = new Link(lat1, lon1);
        stack.insert(l);
        boolean flag = false;
        while (true) {
            if (stack.isEmpty()) {
                System.out.println("No Such Route Found.");
                break;
            }
            Link p = stack.seek(); // Get the most recent geo-cordinates to calculate the next one
            if ((.008 >= m.distFrom(p.getLatitude(), p.getLongitude(), lat2, lon2))) {
                break;
            }
            angle = m.getAngleBtwPoints(p.getLatitude(), p.getLongitude(), lat2, lon2); // Calculate the angle between the geo-cordinates
            while (!p.allChildrenVisited()) { // 
                flag = false;
                int index = p.getNextEmptyChildIndex();
                double arb = m.getArbitraryAngle(index);
                newPts = m.returnNextPoint(newPts[0], newPts[1], angle + arb, fixed);
                Link a = new Link(newPts[0], newPts[1]);
                p.insertNextChild(a);
                if (m.isWeatherGood(newPts[0], newPts[1], drone)) {
                    stack.insert(a);
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                stack.remove();
            }
        }
        double dist = m.distFrom(lat1, lon1, lat2, lon2);
        stack.displayListFromBottom();
    }

    /**
     * Returns an arbitrary number that is used as a variation angle.The values
     * can be changed based on the use case.
     *
     * @return int The variation angle in determining the next safe point
     */
    public int getArbitraryAngle(int index) {
        switch (index) {
            case 0:
                return 0;
            case 1:
                return 20;
            case 2:
                return -20;
            case 3:
                return 40;
            case 4:
                return -40;
            case 5:
                return 60;
            case 6:
                return -60;
            case 7:
                return 90;
            case 8:
                return -90;
            default:
                return -1;
        }

    }

    /**
     * Returns all the points of the route from source to destination
     *
     * @return stack This is a stack of all the points in the route of the drone
     */
    public Stack getStack() {
        return stack;
    }
}
