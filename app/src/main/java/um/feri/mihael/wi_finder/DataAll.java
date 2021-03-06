package um.feri.mihael.wi_finder;


import java.util.ArrayList;
import java.util.List;

public class DataAll {
    private ArrayList<HotSpot> hotSpots;
    private ArrayList<User> users;

    public DataAll()
    {
        hotSpots = new ArrayList<>();
        users = new ArrayList<>();
    }

    public int hotSpotSize()
    {
        return hotSpots.size();
    }
    public int userSize(){ return users.size(); }

    public void addUser(User u){
        users.add(u);
    }
    public void addHotSpot(HotSpot h){
        hotSpots.add(h);
    }

    @Override
    public String toString() {
        return "DataAll{" +
                "users=" + users +
                ", hotSpots=" + hotSpots +
                '}';
    }

    public HotSpot getHotSpot(int i) {
        return hotSpots.get(i);
    }
    public User getUser(int i) { return users.get(i); }

    public HotSpot getHostSpotBySSID(String ssid){

        HotSpot found = null;
        for(int i=0; i< hotSpots.size(); i++)
        {
            if(hotSpots.get(i).getSsid().equals(ssid))
            {
                found = hotSpots.get(i);
            }
        }

        return  found;
    }

    public void deleteHotSpot(int pos)
    {
        hotSpots.remove(pos);
    }

    public void updateHotSpot(int pos, String newSSID, String newSecKey, double latitude, double longitude, HotSpot.Accessibility accessibility)
    {
        hotSpots.get(pos).setSsid(newSSID);
        hotSpots.get(pos).setSecurityKey(newSecKey);
        hotSpots.get(pos).setLatitude(latitude);
        hotSpots.get(pos).setLongitude(longitude);
        hotSpots.get(pos).setAccessLevel(accessibility);
    }

    public ArrayList<HotSpot> find(String key) {
        ArrayList<HotSpot> found = new ArrayList<>();

        for (int i=0; i<hotSpots.size(); i++) {
            if (hotSpots.get(i).isSelected(key)) found.add(hotSpots.get(i));
        }

        return found;
    }

    public User getUserById(String id)
    {
        User user = null;

        for(int i=0; i<users.size(); i++)
        {
            if(users.get(i).getId().equals(id))
            {
                user = users.get(i);
            }
        }

        return user;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public List<HotSpot> getHotSpots()
    {
        return hotSpots;
    }

    public static DataAll getScenarij1Data() {
        DataAll all = new DataAll();
        all.addUser(new User ("Mihael Kvar", "mihael.kvar@gmail.com", "0"));
        all.addUser(new User("Matej Kotnik", "matej@najdi.si", "1"));
        all.addUser(new User("Maja Kos", "maja.kos@najdi.si", "2"));

        HotSpot hs1 = new HotSpot("154ea2", "MyPassword", 46.47817490000001, 15.715625300000056, all.users.get(0), HotSpot.Accessibility.SECURE, 4);
        hs1.setClassifierRating("GOOD_RATING_ARFF");

        HotSpot hs2 = new HotSpot("Stanovanje 7", "stanovanje 007", 46.47817360000003, 15.726625300000444, all.users.get(1), HotSpot.Accessibility.SECURE, 2);
        hs2.setClassifierRating("GREAT_RATING_ARFF");

        HotSpot hs3 = new HotSpot("Space Marines", "smGeneral", 46.3622743, 15.110658199999989, all.users.get(1), HotSpot.Accessibility.SECURE, 4);
        hs3.setClassifierRating("OK_RATING_ARFF");

        HotSpot hs4 = new HotSpot("SitecomA35798", "ivBf686tM9", 46.3569675, 15.129486600000064, all.users.get(2), HotSpot.Accessibility.SECURE, 1);
        hs4.setClassifierRating("BAD_RATING_ARFF");

        all.addHotSpot(hs1);
        all.addHotSpot(hs2);
        all.addHotSpot(hs3);
        all.addHotSpot(hs4);
        return all;
    }
}
