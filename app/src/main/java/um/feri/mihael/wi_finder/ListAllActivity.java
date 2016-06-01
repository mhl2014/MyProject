package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

        adapter = new AdapterHotSpot(DataAll.getScenarij1Data(), this);
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
}
