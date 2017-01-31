package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
        app = (ApplicationMy) getApplication();

        setContentView(R.layout.activity_list_all);

        addFab = (FloatingActionButton) findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addToSetIntent = new Intent(ListAllActivity.this, ListNewActivity.class);
                startActivityForResult(addToSetIntent, Utilities.REQ_ADD_ITEMS_TO_SET);
            }
        });

        if(!app.isUserLoggedIn())
        {
            addFab.setVisibility(View.GONE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.listAllView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(!app.loadData())
        {
            Toast.makeText(this, res.getString(R.string.loadFailure), Toast.LENGTH_SHORT).show();
        }

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

            if(requestCode == Utilities.REQ_EDIT_OR_DEL_ITEM)
            {
                if (extras.getInt(Utilities.RETURN_EDIT_ACTION, -1) == Utilities.ACTION_DELETE)
                {
                    adapter.deleteItem(extras.getInt(Utilities.RETURN_HOTSPOT_POS));
                }
                else if (extras.getInt(Utilities.RETURN_EDIT_ACTION, -1) == Utilities.ACTION_SAVE)
                {
                    adapter.updateItem(extras.getInt(Utilities.RETURN_HOTSPOT_POS),
                            extras.getString(Utilities.RETURN_HOTSPOT_SSID),
                            extras.getString(Utilities.RETURN_HOTSPOT_SEC_KEY),
                            extras.getDouble(Utilities.RETURN_HOTSPOT_LATITUDE),
                            extras.getDouble(Utilities.RETURN_HOTSPOT_LONGITUDE),
                            HotSpot.Accessibility.valueOf(extras.getString(Utilities.RETURN_HOTSPOT_ACCESS)));
                }
            }
            else if(requestCode == Utilities.REQ_ADD_ITEMS_TO_SET)
            {
                if(!app.loadData())
                {
                    Toast.makeText(this, res.getString(R.string.loadFailure), Toast.LENGTH_SHORT).show();
                }

                adapter.setDataSet(app.getAll());
            }
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if(!app.saveData())
        {
            Toast.makeText(this, res.getString(R.string.saveFailure), Toast.LENGTH_SHORT).show();
        }
    }
}
