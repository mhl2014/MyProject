package um.feri.mihael.wi_finder;


import java.util.ArrayList;

public class DataAll {
    private ArrayList<HotSpot> hotSpots;
    private ArrayList<User> users;

    public DataAll()
    {
        hotSpots = new ArrayList<HotSpot>();
        users = new ArrayList<User>();
    }

    public int hotSpotSize()
    {
        return hotSpots.size();
    }

    public void dodaj(User u){
        users.add(u);
    }
    public void dodaj(HotSpot h){
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

    public HotSpot getHostSpotBySSID(String ssid){

        HotSpot found = null;
        for(int i=0; i<hotSpots.size(); i++)
        {
            if(hotSpots.get(i).getSsid() == ssid)
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

    public void updateHotSpot(int pos, String newSSID, String newSecKey)
    {
        hotSpots.get(pos).setSsid(newSSID);
        hotSpots.get(pos).setSecurityKey(newSecKey);
    }

    public ArrayList<HotSpot> find(String key) {
        ArrayList<HotSpot> found = new ArrayList<>();

        for (int i=0; i<hotSpots.size(); i++) {
            if (hotSpots.get(i).isSelected(key)) found.add(hotSpots.get(i));
        }

        return found;
    }


    public static DataAll getScenarij1Data() {
        DataAll all = new DataAll();
        all.dodaj(new User("Matej Črepinšek", "matej@najdi.si"));
        all.dodaj(new User("Maja Kos", "maja.kos@najdi.si"));
        all.dodaj(new User("Aleš Stroka", "as123@najdi.uk.si"));
        all.dodaj(new User("Janez Novak", "janez.novak@najdi.si"));
        all.dodaj(new User("Marjetka Kos Cerar", "nedela@naj12233 di.si"));
        all.dodaj(new HotSpot("154ea2", "MyPassword", 46.47817490000001, 15.715625300000056, all.users.get(0)));
        all.dodaj(new HotSpot("Stanovanje 7", "stanovanje 007", 46.47817360000003, 15.726625300000444, all.users.get(1)));
        all.dodaj(new HotSpot("Space Marines", "smGeneral", 46.3622743, 15.110658199999989, all.users.get(1)));
        all.dodaj(new HotSpot("SitecomA35798", "ivBf686tM9", 46.3569675, 15.129486600000064, all.users.get(2)));
        return all;

    }


}
