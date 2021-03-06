package Graph;

/**
 * This class defines all attributes associated with a geo-cordinate
 * @author Rohan
 */
public class Link {

    double latitude;
    double longitude;
    double windspeed;
    double windAngle;
    public Link next;
    public Link previous; 
    private Link children[];
    
    public Link(double lat, double lon) {
        latitude = lat;
        longitude = lon;
        children = new Link[9];
    }

    public int getLastNonNullLink() {
        for (int i = 8; i >= 0; i--) {
            if (children[i] != null) {
                return i;
            }
        }
        return -1;
    }

    public boolean allChildrenVisited() {
        for (int i = 0; i < 9; i++) {
            if (children[i] == null) {
                return false;
            }
        }
        return true;
    }

    public void insertNextChild(Link n) {
        int i;
        for (i = 0; i < 9; i++) {
            if (children[i] == null) {
                break;
            }
        }
        children[i] = n;
    }

    public int getNextEmptyChildIndex() {
        for (int i = 0; i < 9; i++) {
            if (children[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return ("(" + latitude + " , " + longitude + ")");
    }
}
