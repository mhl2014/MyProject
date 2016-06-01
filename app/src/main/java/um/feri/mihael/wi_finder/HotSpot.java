package um.feri.mihael.wi_finder;

/**
 * Created by Mihael on 30. 05. 2016.
 */
public class HotSpot {
    //private int identifier;
    private String ssid;
    private String securityKey;
    private double latitude;
    private double longitude;
    private User user;

    public HotSpot(/*int id, */String ssid, String securityKey, double latitude, double longitude, User user)
    {
        //this.identifier = id;
        this.ssid = ssid;
        this.securityKey = securityKey;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }

    @Override
    public String toString() {
        return "HotSpot{" +
                "ssid='" + ssid + '\'' +
                ", securityKey='" + securityKey + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    public boolean isSelected(String key) {
        if (ssid == key) return true;
        return false;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
/*
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }*/

}
