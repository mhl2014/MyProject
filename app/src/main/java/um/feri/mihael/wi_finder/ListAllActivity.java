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
                Intent addNewIntent = new Intent(ListAllActivity.this, AddNewActivity.class);
                startActivityForResult(addNewIntent, Utilities.REQ_ADD_ITEM);
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

        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

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
            else if(requestCode == Utilities.REQ_ADD_ITEM)
            {
                User finder = app.getAll().getUserById(app.getSignInResult().getSignInAccount().getId());

                HotSpot.Accessibility accessibility = (HotSpot.Accessibility.valueOf(extras.getString(Utilities.RETURN_HOTSPOT_ACCESS)));

                if(accessibility == HotSpot.Accessibility.SECURE) {
                    finder.addToPoints(Utilities.POINTS_FOR_SECURE);
                }
                else if(accessibility == HotSpot.Accessibility.PRIVATE) {
                    finder.addToPoints(Utilities.POINTS_FOR_PRIVATE);
                }
                else {
                    finder.addToPoints(Utilities.POINTS_FOR_OTHERWISE);
                }

                adapter.addItem(new HotSpot(extras.getString(Utilities.RETURN_HOTSPOT_SSID),
                        extras.getString(Utilities.RETURN_HOTSPOT_SEC_KEY),
                        extras.getDouble(Utilities.RETURN_HOTSPOT_LATITUDE),
                        extras.getDouble(Utilities.RETURN_HOTSPOT_LONGITUDE), finder, accessibility
                        ));
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

        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
}
