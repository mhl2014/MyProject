package um.feri.mihael.wi_finder;

import android.net.Uri;

/**
 * Created by Mihael on 30. 05. 2016.
 */
public class User {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addToPoints(int extraPoints)
    {
        this.points += extraPoints;
    }

    public String getUserPhotoUri() {
        return userPhotoUri;
    }

    public void setUserPhotoUri(String userPhotoUri) {
        this.userPhotoUri = userPhotoUri;
    }

    private String name;
    private String email;
    private String id;

    private String userPhotoUri;
    private int points;

    public User(String name, String email, String id)
    {
        this.name = name;
        this.email = email;
        this.id = id;
        this.points = 0;
        this.userPhotoUri = null;
    }

    public User(String name, String email, String id, String userPhotoUri)
    {
        this.name = name;
        this.email = email;
        this.id = id;
        this.points = 0;
        this.userPhotoUri = userPhotoUri;
    }



    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", points='" + points +'\'' +
                ", id='" + id +'\'' +
                ", uri='" + userPhotoUri +'\'' +
                '}';
    }
}
