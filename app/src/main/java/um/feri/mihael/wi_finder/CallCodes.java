package um.feri.mihael.wi_finder;

/**
 * Created by Mihael on 6. 06. 2016.
 */
public class CallCodes {

    public static final int REQ_EDIT_OR_DEL_ITEM = 0;
    public static final int REQ_ADD_ITEM = 1;
    public static final int REQ_SIGN_IN = 9001;

    public static final String REQ_SCAN = "com.google.zxing.client.android.SCAN";

    public static final String EXTRA_HOTSPOT_POS = "um.feri.mihael.wi_finfer.POS";
    public static final String EXTRA_HOTSPOT_SSID = "um.feri.mihael.wi_finder.SSID";
    public static final String EXTRA_HOTSPOT_SEC_KEY = "um.feri.mihael.wi_finder.SECKEY";
    public static final String EXTRA_USER_NAME = "um.feri.mihael.wi_finder.USERNAME";
    public static final String EXTRA_USER_EMAIL = "um.feri.mihael.wi_finder.EMAIL";

    public static final int ACTION_SAVE = 0;
    public static final int ACTION_DELETE = 1;
    public static final int ACTION_ADD = 2; //unused for now
    public static final int ACTION_SCAN = 3;

    public static final String RETURN_EDIT_ACTION = "um.feri.mihael.wi_finfer.RETACT";
    public static final String RETURN_HOTSPOT_POS = "um.feri.mihael.wi_finfer.RETNPOS";
    public static final String RETURN_HOTSPOT_SSID = "um.feri.mihael.wi_finder.RETNSSID";
    public static final String RETURN_HOTSPOT_SEC_KEY = "um.feri.mihael.wi_finder.RETNSECKEY";
    public static final String RETURN_USER_NAME = "um.feri.mihael.wi_finder.RETNUSERNAME";
    public static final String RETURN_USER_EMAIL = "um.feri.mihael.wi_finder.RETNEMAIL";

    public static final String SCAN_MODE = "SCAN_MODE";
    public static final String QR_CODE_MODE = "QR_CODE_MODE";
    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String RETURN_HOTSPOT_ACCESS = "um.feri.mihael.wi_finder.RETACCESS";

    public static final String ACCESS_PUBLIC = "HOTSPOT_PUBLIC";
    public static final String ACCESS_SECURE = "HOTSPOT_SECURE";
    public static final String ACCESS_LOGIN  = "HOTSPOT_LOGIN";
    public static final String ACCESS_INACCESSIBLE = "HOTSPOT_INACCESSIBLE";

    public static final String ERR_FILE_NOT_FOUND = "um.feri.mihael.wi_finder.FILENOTFOUND";

}
