package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class ListAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterHotSpot adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApplicationMy app;
    private FloatingActionButton addFab;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        res = getResources();

        setContentView(R.layout.activity_list_all);

        addFab = (FloatingActionButton) findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewIntent = new Intent(ListAllActivity.this, AddNewActivity.class);
                startActivityForResult(addNewIntent, CallCodes.REQ_ADD_ITEM);
            }
        });

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

            toastText = res.getString(R.string.loadFailure);
        }
        else
        {
            toastText = res.getString(R.string.loadSuccess);
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

            Bundle extras = data.getExtras();

            if(requestCode == CallCodes.REQ_EDIT_OR_DEL_ITEM)
            {
                if (extras.getInt(CallCodes.RETURN_EDIT_ACTION, -1) == CallCodes.ACTION_DELETE)
                {
                    adapter.deleteItem(extras.getInt(CallCodes.RETURN_HOTSPOT_POS));
                }
                else if (extras.getInt(CallCodes.RETURN_EDIT_ACTION, -1) == CallCodes.ACTION_SAVE)
                {
                    //String mySSID = extras.getString(EditHotSpotActivity.RETURN_HOTSPOT_SSID);
                    //String myPass = extras.getString(EditHotSpotActivity.RETURN_HOTSPOT_SEC_KEY);

                    adapter.updateItem(extras.getInt(CallCodes.RETURN_HOTSPOT_POS),
                            extras.getString(CallCodes.RETURN_HOTSPOT_SSID),
                            extras.getString(CallCodes.RETURN_HOTSPOT_SEC_KEY));
                }
            }
            else if(requestCode == CallCodes.REQ_ADD_ITEM)
            {
                String access = extras.getString(CallCodes.RETURN_HOTSPOT_ACCESS);
                //String array[] = res.getStringArray(R.array.accessibility);

                //if(access.equals(R.array.accessibility))

                adapter.addItem(new HotSpot(extras.getString(CallCodes.RETURN_HOTSPOT_SSID),
                        extras.getString(CallCodes.RETURN_HOTSPOT_SEC_KEY),
                        0.0, 0.0, new User("testIme", "test@test.com"),));
            }
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        String toastText;
        if(!app.saveData())
        {
            toastText = res.getString(R.string.saveFailure);
        }
        else
        {
            toastText = res.getString(R.string.saveSuccess);
        }

        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }
}
