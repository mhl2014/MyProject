package um.feri.mihael.wi_finder;

/*
 * Created by Mihael on 30. 05. 2016.
 */
public class HotSpot {

    public enum Accessibility { PUBLIC, SECURE, LOGIN, INACCESSIBLE, PRIVATE }

    public static final int greatRatingScore = 4;
    public static final int goodRatingScore = 3;
    public static final int okRatingScore = 2;
    public static final int badRatingScore = 1;
    public static final int noAccessRatingScore = 0;

    public static final String NOACCESS_RATING_ARFF = "no_access";
    public static final String BAD_RATING_ARFF = "bad";
    public static final String OK_RATING_ARFF = "OK";
    public static final String GOOD_RATING_ARFF = "good";
    public static final String GREAT_RATING_ARFF = "great";

    public static final String NO_PUBLIC_ACCESS_ARFF = "closed_for_public";
    public static final String PUBLIC_ACCESS_ARFF = "open_for_public";
    public static final String PASSWORD_ACCESS_ARFF = "need_password";
    public static final String LOGIN_ACCESS_ARFF = "login_required";
    public static final String PRIVATE_ACCESS_ARFF = "private";

    private String ssid;
    private String securityKey;
    private double latitude;
    private double longitude;
    private double averageRating;
    private int visitsCounter;
    private User user;
    private String classifierRating; // Will be assigned by the classifier

    public Accessibility getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Accessibility accessLevel) {
        this.accessLevel = accessLevel;
    }

    private Accessibility accessLevel;

    public HotSpot(String ssid, String securityKey, double latitude, double longitude, User user, Accessibility accessLevel, double rating)
    {
        this.ssid = ssid;
        this.securityKey = securityKey;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.accessLevel = accessLevel;
        visitsCounter = 1;
        averageRating = rating; // Prvega ni potrebno racunati
        classifierRating = "none";
    }

    @Override
    public String toString() {
        return "HotSpot{" +
                "ssid='" + ssid + '\'' +
                ", securityKey='" + securityKey + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", user='" + user + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", visitsCounter='" + visitsCounter + '\'' +
                ", averageRating='" + averageRating + '\'' +
                ", classifierRating='" + classifierRating + '\'' +
                '}';
    }

    // Sluzi za racunanje novega povprecja potem, ko drugi uporabniki ocenijo HotSpot
    public void addRatingToAverage(String rating)
    {
        int ratingScore;
        if(rating.equals(BAD_RATING_ARFF))
            ratingScore = badRatingScore;
        else if(rating.equals(OK_RATING_ARFF))
            ratingScore = okRatingScore;
        else if(rating.equals(GOOD_RATING_ARFF))
            ratingScore = goodRatingScore;
        else if(rating.equals(GREAT_RATING_ARFF))
            ratingScore = greatRatingScore;
        else
            ratingScore = noAccessRatingScore;

        averageRating = (averageRating + ratingScore) / (visitsCounter + 1);
        visitsCounter++;
    }

    public boolean isSelected(String key) {
        return ssid.equals(key);
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

    public double getAverageRating(){ return averageRating; }

    public int getVisitsCounter() { return  visitsCounter; }

    public void setUser(User user) {
        this.user = user;
    }

    public String getClassifierRating() { return classifierRating; }

    public void setClassifierRating(String rating) { classifierRating = rating; }

    public float getClassifierRatingAsNumber()
    {
        int ratingScore;

        if(classifierRating.equals("BAD_RATING_ARFF"))
            ratingScore = badRatingScore;
        else if(classifierRating.equals("OK_RATING_ARFF"))
            ratingScore = okRatingScore;
        else if(classifierRating.equals("GOOD_RATING_ARFF"))
            ratingScore = goodRatingScore;
        else if(classifierRating.equals("GREAT_RATING_ARFF"))
            ratingScore = greatRatingScore;
        else
            ratingScore = noAccessRatingScore;

        return ratingScore;
    }
}

