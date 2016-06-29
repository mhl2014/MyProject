package um.feri.mihael.wi_finder;

/**
 * Created by Mihael on 6. 06. 2016.
 */

public class Utilities {

    public static final String SERVICE_CHECK_NETWORK = "um.feri.mihael.wi_finfer.CHECKNETWORKSERVICE";

    public static final int NETWORK_SCAN_NOTIFICATION = 10;

    public static final int REQ_EDIT_OR_DEL_ITEM = 0;
    public static final int REQ_ADD_ITEM = 1;
    public static final int REQ_ADD_ITEMS_TO_SET = 2;
    public static final int REQ_SIGN_IN = 9001;
    public static final int REQ_LOCATION_CHANGE = 5;

    public static final int POINTS_FOR_SECURE = 100;
    public static final int POINTS_FOR_PRIVATE = 0;
    public static final int POINTS_FOR_OTHERWISE = 50;

    public static final int PERMISSION_ACCESS_FINE_LOCATION = 4;

    public static final String REQ_SCAN = "com.google.zxing.client.android.SCAN";

    public static final String EXTRA_HOTSPOT_POS = "um.feri.mihael.wi_finfer.POS";
    public static final String EXTRA_HOTSPOT_LATITUDE = "um.feri.mihael.wi_finfer.LATITUDE";
    public static final String EXTRA_HOTSPOT_LONGITUDE = "um.feri.mihael.wi_finfer.LONGITUDE";
    public static final String EXTRA_HOTSPOT_SSID = "um.feri.mihael.wi_finder.SSID";
    public static final String EXTRA_HOTSPOT_ACCESS = "um.feri.mihael.wi_finder.ACCESS";
    public static final String EXTRA_HOTSPOT_SEC_KEY = "um.feri.mihael.wi_finder.SECKEY";
    public static final String EXTRA_USER_NAME = "um.feri.mihael.wi_finder.USERNAME";
    public static final String EXTRA_USER_EMAIL = "um.feri.mihael.wi_finder.EMAIL";
    public static final String EXTRA_USER_ID = "um.feri.mihael.wi_finder.ID";
    public static final String INTENT_LOCATION_CHANGE = "um.feri.mihael.wi_finder.LOCATIONCHANGE";

    public static final int ACTION_SAVE = 0;
    public static final int ACTION_DELETE = 1;
    public static final int ACTION_ADD = 2; //unused for now
    public static final int ACTION_SCAN = 3;

    public static final String RETURN_EDIT_ACTION = "um.feri.mihael.wi_finfer.RETN_ACT";
    public static final String RETURN_HOTSPOT_POS = "um.feri.mihael.wi_finfer.RETN_POS";
    public static final String RETURN_HOTSPOT_LATITUDE = "um.feri.mihael.wi_finfer.RETN_LATITUDE";
    public static final String RETURN_HOTSPOT_LONGITUDE = "um.feri.mihael.wi_finfer.RETN_LONGITUDE";
    public static final String RETURN_HOTSPOT_SSID = "um.feri.mihael.wi_finder.RETN_SSID";
    public static final String RETURN_HOTSPOT_SEC_KEY = "um.feri.mihael.wi_finder.RETN_SECKEY";
    public static final String RETURN_USER_NAME = "um.feri.mihael.wi_finder.RETN_USERNAME";
    public static final String RETURN_USER_EMAIL = "um.feri.mihael.wi_finder.RETN_EMAIL";

    public static final String SCAN_MODE = "SCAN_MODE";
    public static final String QR_CODE_MODE = "QR_CODE_MODE";
    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String RETURN_HOTSPOT_ACCESS = "um.feri.mihael.wi_finder.RETN_ACCESS";



    /*
    public static final String ACCESS_PUBLIC = "HOTSPOT_PUBLIC";
    public static final String ACCESS_SECURE = "HOTSPOT_SECURE";
    public static final String ACCESS_LOGIN  = "HOTSPOT_LOGIN";
    public static final String ACCESS_INACCESSIBLE = "HOTSPOT_INACCESSIBLE";

    public static final String ERR_FILE_NOT_FOUND = "um.feri.mihael.wi_finder.FILENOTFOUND";
    */

}
