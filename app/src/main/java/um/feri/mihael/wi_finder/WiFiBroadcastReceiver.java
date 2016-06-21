package um.feri.mihael.wi_finder;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mihael on 21. 06. 2016.
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver {

    private ApplicationMy app;
    private Resources res;

    WiFiBroadcastReceiver(ApplicationMy app)
    {
        this.app = app;
        res = app.getResources();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        if(intentAction.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        {
            DataAll all = app.getAll();
            List<ScanResult> wifiNetworks = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getScanResults();

            int newNetworks = 0;
            int recognizedNetworks = 0;
            for(int j=0; j<wifiNetworks.size(); j++) {

                if (all.getHostSpotBySSID(wifiNetworks.get(j).SSID) == null)
                {
                    newNetworks++;
                }
                else
                {
                    recognizedNetworks++;
                }
            }

            if (newNetworks != 0 || recognizedNetworks != 0)
            {
                //Toast.makeText(context, "found networks" +  newNetworks + " " + recognizedNetworks, Toast.LENGTH_SHORT).show();

                String appTitle = res.getString(R.string.app_name);
                String notificationAdditional = "Najdenih " + newNetworks + " novih in " + recognizedNetworks + " znanih omrežij!";

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
                notification.setSmallIcon(R.drawable.ic_perm_scan_wifi_white_24dp);
                notification.setContentTitle(appTitle);
                notification.setContentText(notificationAdditional);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(Utilities.NETWORK_SCAN_NOTIFICATION, notification.build());
            }

        }
    }
}
