package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

public class ListAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterHotSpot adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApplicationMy app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);

        recyclerView = (RecyclerView) findViewById(R.id.listAllView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        app = new ApplicationMy();
        app.setApp(getApplication());

        String toastText;
        if(!app.loadData())
        {
            //Ce ne uspemo dobiti podatkov iz json datoteke nastavimo "dummy data"
            app.setAll(DataAll.getScenarij1Data());

            toastText = "Could not load hotspots!";
        }
        else
        {
            toastText = "Successfully loaded hotspots!";
        }

        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();

        adapter = new AdapterHotSpot(app.getAll(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == AdapterHotSpot.EDIT_DATA_REQUEST)
            {
                Bundle extras = data.getExtras();

                if (extras.getInt(EditHotSpotActivity.RETURN_EDIT_ACTION, 2) == EditHotSpotActivity.ACTION_DELETE)
                {
                    adapter.deleteItem(extras.getInt(EditHotSpotActivity.RETURN_HOTSPOT_POS));
                }
                else if (extras.getInt(EditHotSpotActivity.RETURN_EDIT_ACTION, 2) == EditHotSpotActivity.ACTION_SAVE)
                {
                    //String mySSID = extras.getString(EditHotSpotActivity.RETURN_HOTSPOT_SSID);
                    //String myPass = extras.getString(EditHotSpotActivity.RETURN_HOTSPOT_SEC_KEY);

                    adapter.updateItem(extras.getInt(EditHotSpotActivity.RETURN_HOTSPOT_POS),
                            extras.getString(EditHotSpotActivity.RETURN_HOTSPOT_SSID),
                            extras.getString(EditHotSpotActivity.RETURN_HOTSPOT_SEC_KEY));
                }
            }
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        String toastText;
        if(app.saveData())
        {
            toastText = "Hotspots saved succesfully!";
        }
        else
        {
            toastText = "Error saving Hotspots!";
        }

        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }
}
