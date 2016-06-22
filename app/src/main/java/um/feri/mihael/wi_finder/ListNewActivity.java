package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListNewActivity extends AppCompatActivity {

    private Resources res;
    private ApplicationMy app;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private AdapterNewHotSpot adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        res = getResources();
        app = (ApplicationMy) getApplication();
        DataAll all = app.getAll();

        setContentView(R.layout.activity_list_new_activity);

        recyclerView = (RecyclerView) findViewById(R.id.listNewRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = (FloatingActionButton) findViewById(R.id.listNewFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!app.saveData())
                {
                    Toast.makeText(app, res.getString(R.string.saveFailure), Toast.LENGTH_LONG).show();
                }

                Intent doneAddingIntent = new Intent();
                setResult(Activity.RESULT_OK, doneAddingIntent);

                finish();
            }
        });

        if(!app.loadData())
        {
            Toast.makeText(app, res.getString(R.string.loadFailure), Toast.LENGTH_LONG).show();
        }

        List<ScanResult> wifiNetworks = ((WifiManager) app.getSystemService(Context.WIFI_SERVICE)).getScanResults();
        HashMap<String, ScanResult> uniqueNetworks = new HashMap<>();

        for(int j=0; j < wifiNetworks.size(); j++)
        {
            ScanResult current = wifiNetworks.get(j);
            if ((all.getHostSpotBySSID(current.SSID) == null) && (!uniqueNetworks.containsKey(current.SSID)))
            {
                uniqueNetworks.put(current.SSID, current);
            }
        }

        ArrayList<ScanResult> scanResults = new ArrayList<>();
        for(ScanResult scanResult : uniqueNetworks.values())
        {
            scanResults.add(scanResult);
        }

        adapter = new AdapterNewHotSpot(scanResults, this);
        recyclerView.setAdapter(adapter);
    }

    protected void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == Utilities.REQ_ADD_ITEM)
            {
                User finder = app.getAll().getUserById(app.getSignInResult().getSignInAccount().getId());
                Bundle extras = data.getExtras();

                    HotSpot.Accessibility accessibility = (HotSpot.Accessibility.valueOf(extras.getString(Utilities.RETURN_HOTSPOT_ACCESS)));

                    if (accessibility == HotSpot.Accessibility.SECURE) {
                        finder.addToPoints(Utilities.POINTS_FOR_SECURE);
                    } else if (accessibility == HotSpot.Accessibility.PRIVATE) {
                        finder.addToPoints(Utilities.POINTS_FOR_PRIVATE);
                    } else {
                        finder.addToPoints(Utilities.POINTS_FOR_OTHERWISE);
                    }

                    app.getAll().addHotSpot(new HotSpot(extras.getString(Utilities.RETURN_HOTSPOT_SSID),
                            extras.getString(Utilities.RETURN_HOTSPOT_SEC_KEY),
                            extras.getDouble(Utilities.RETURN_HOTSPOT_LATITUDE),
                            extras.getDouble(Utilities.RETURN_HOTSPOT_LONGITUDE), finder, accessibility
                    ));

                    adapter.removeItem(extras.getInt(Utilities.RETURN_HOTSPOT_POS));
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }
}