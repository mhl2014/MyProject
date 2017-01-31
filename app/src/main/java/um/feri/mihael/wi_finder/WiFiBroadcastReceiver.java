package um.feri.mihael.wi_finder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
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
            HashMap<String, ScanResult> uniqueNetworks = new HashMap<>();

            int newNetworks = 0;
            for(int j=0; j < wifiNetworks.size(); j++)
            {
                ScanResult current = wifiNetworks.get(j);

                if ((all.getHostSpotBySSID(current.SSID) == null) && (!uniqueNetworks.containsKey(current.SSID)))
                {
                    newNetworks++;
                    uniqueNetworks.put(current.SSID, current);
                }
            }

            if (newNetworks != 0)
            {
                String appTitle = res.getString(R.string.app_name);
                String notificationAdditional = "Najdenih " + newNetworks + " novih omrežij!";

                Intent addDiscoveredIntent = new Intent(app, ListNewActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(app);
                stackBuilder.addParentStack(ListNewActivity.class);
                stackBuilder.addNextIntent(addDiscoveredIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
                notification.setSmallIcon(R.drawable.ic_perm_scan_wifi_white_24dp);
                notification.setContentTitle(appTitle);
                notification.setContentText(notificationAdditional);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notification.setContentIntent(pendingIntent);
                notificationManager.notify(Utilities.NETWORK_SCAN_NOTIFICATION, notification.build());
            }
        }
    }
}
