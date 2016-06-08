package um.feri.mihael.wi_finder;

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

    private String name;
    private String email;
    private int points;

    public User(String name, String email)
    {
        this.name = name;
        this.email = email;
        this.points = 0;
    }

    public User(String name, String email, int points)
    {
        this.name = name;
        this.email = email;
        this.points = points;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", points='" + points +'\'' +
                '}';
    }
}
