package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

//// TODO: 8. 06. 2016 add onclick listeners for buttons and location view 
public class EditHotSpotActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView textSSID;
    private TextView textSecKey;
    private TextView textUserName;
    private TextView textEmail;
    private TextView textAddress;
    private Spinner spinnerAccessibility;

    private Button saveBtn;
    private Button deleteBtn;
    private Button changeLocationBtn;

    private Bundle extras;

    private Resources res;

    private int position;
    private double posLatitude;
    private double posLongitude;
    private String accessLevel;
    private String userID;

    private Geocoder geocoder;

    private ApplicationMy app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (ApplicationMy) getApplication();
        res = getResources();

        setContentView(R.layout.activity_edit_hot_spot);
        textSSID = (TextView) findViewById(R.id.editTextSSID);
        textSecKey = (TextView) findViewById(R.id.editTextSecKey);
        textUserName = (TextView) findViewById(R.id.editTextUserName);
        textEmail = (TextView) findViewById(R.id.editTextUserEmail);
        textAddress = (TextView) findViewById(R.id.textViewAddress);

        extras = getIntent().getExtras();

        if(extras != null)
        {
            userID = extras.getString(Utilities.EXTRA_USER_ID);
            position = extras.getInt(Utilities.EXTRA_HOTSPOT_POS);
            accessLevel = extras.getString(Utilities.EXTRA_HOTSPOT_ACCESS);
            posLatitude = extras.getDouble(Utilities.EXTRA_HOTSPOT_LATITUDE);
            posLongitude = extras.getDouble(Utilities.EXTRA_HOTSPOT_LONGITUDE);
            textSSID.setText(extras.getString(Utilities.EXTRA_HOTSPOT_SSID));
            textSecKey.setText(extras.getString(Utilities.EXTRA_HOTSPOT_SEC_KEY));
            textUserName.setText(extras.getString(Utilities.EXTRA_USER_NAME));
            textEmail.setText(extras.getString(Utilities.EXTRA_USER_EMAIL));

        }

        saveBtn = (Button) findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent saveIntent = new Intent();

                saveIntent.putExtra(Utilities.RETURN_EDIT_ACTION, Utilities.ACTION_SAVE);
                saveIntent.putExtra(Utilities.RETURN_HOTSPOT_POS, position);
                saveIntent.putExtra(Utilities.RETURN_HOTSPOT_LATITUDE, posLatitude);
                saveIntent.putExtra(Utilities.RETURN_HOTSPOT_LONGITUDE, posLongitude);
                saveIntent.putExtra(Utilities.RETURN_HOTSPOT_SSID, textSSID.getText().toString());
                saveIntent.putExtra(Utilities.RETURN_HOTSPOT_SEC_KEY, textSecKey.getText().toString());
                saveIntent.putExtra(Utilities.RETURN_HOTSPOT_ACCESS, accessLevel);
                setResult(Activity.RESULT_OK, saveIntent);

                finish();
            }
        });

        deleteBtn = (Button) findViewById(R.id.buttonDelete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteIntent = new Intent();

                deleteIntent.putExtra(Utilities.RETURN_EDIT_ACTION, Utilities.ACTION_DELETE);
                deleteIntent.putExtra(Utilities.RETURN_HOTSPOT_POS, position);
                setResult(Activity.RESULT_OK, deleteIntent);

                finish();
            }
        });

        changeLocationBtn = (Button) findViewById(R.id.buttonEditLocation);
        changeLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeLocationIntent = new Intent(EditHotSpotActivity.this, ViewLocationActivity.class);
                changeLocationIntent.putExtra(Utilities.EXTRA_HOTSPOT_LONGITUDE, posLongitude);
                changeLocationIntent.putExtra(Utilities.EXTRA_HOTSPOT_LATITUDE, posLatitude);
                changeLocationIntent.putExtra(Utilities.EXTRA_HOTSPOT_SSID, textSSID.getText().toString());
                changeLocationIntent.putExtra(Utilities.EXTRA_HOTSPOT_SEC_KEY, textSecKey.getText().toString());
                changeLocationIntent.putExtra(Utilities.EXTRA_HOTSPOT_ACCESS, accessLevel);
                changeLocationIntent.putExtra(Utilities.INTENT_LOCATION_CHANGE, true);

                startActivityForResult(changeLocationIntent, Utilities.REQ_LOCATION_CHANGE);
            }
        });

        spinnerAccessibility = (Spinner) findViewById(R.id.editHotSpot_AccessibilitySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accessibility, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        boolean disableEditAndDelete = false;
        if (!app.isUserLoggedIn())
        {
            disableEditAndDelete = true;
        }
        else if (!app.getSignInResult().getSignInAccount().getId().equals(userID))
        {
            disableEditAndDelete = true;
        }

        if(disableEditAndDelete)
        {
            saveBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            changeLocationBtn.setVisibility(View.GONE);

            spinnerAccessibility.setEnabled(false);
            spinnerAccessibility.setClickable(false);

            textSSID.setEnabled(false);
            textSSID.setKeyListener(null);
            textSSID.setCursorVisible(false);

            textSecKey.setEnabled(false);
            textSecKey.setKeyListener(null);
            textSecKey.setCursorVisible(false);

            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                textSSID.setTextAppearance(android.R.style.TextAppearance_Widget_TextView);
                textSecKey.setTextAppearance(android.R.style.TextAppearance_Widget_TextView);

            }
            else
            {
                textSSID.setTextAppearance(this, android.R.style.TextAppearance_Widget_TextView);
                textSecKey.setTextAppearance(this, android.R.style.TextAppearance_Widget_TextView);
            }
            */

            textSSID.setBackgroundColor(Color.TRANSPARENT);
            textSecKey.setBackgroundColor(Color.TRANSPARENT);
        }

        spinnerAccessibility.setAdapter(adapter);

        //set the initial accessibility as is
        Toast.makeText(this, "slected item is " + accessLevel, Toast.LENGTH_LONG).show();
        spinnerAccessibility.setSelection(findItemPosByAccessibilityName(accessLevel));
        spinnerAccessibility.setOnItemSelectedListener(this);

        geocoder = new Geocoder(this);

        try
        {
            List<Address> addresses = geocoder.getFromLocation(posLatitude, posLongitude, 1);
            String addressString;

            if(addresses.size() > 0) {
                addressString = addresses.get(0).getCountryName() + " " + addresses.get(0).getLocality();
            }
            else
            {
                addressString = "No address found";
            }

            textAddress.setText(addressString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String selectedItem = adapterView.getSelectedItem().toString();

        if(selectedItem.equals(res.getString(R.string.publicAccess)))
        {
            accessLevel = HotSpot.Accessibility.PUBLIC.name();
        }
        else if(selectedItem.equals(res.getString(R.string.loginAccess)))
        {
            accessLevel = HotSpot.Accessibility.LOGIN.name();
        }
        else if(selectedItem.equals(res.getString(R.string.secureAccess)))
        {
            accessLevel = HotSpot.Accessibility.SECURE.name();
        }
        else if(selectedItem.equals(res.getString(R.string.inaccessibleAccess)))
        {
            accessLevel = HotSpot.Accessibility.INACCESSIBLE.name();
        }
        else if(selectedItem.equals(res.getString(R.string.privateAccess)))
        {
            accessLevel = HotSpot.Accessibility.PRIVATE.name();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //gets the position of the item according to the name passed by the enum
    private int findItemPosByAccessibilityName(String access)
    {
        int count = spinnerAccessibility.getAdapter().getCount();
        int ind = -1;

        String stringValueOfItem = "";

        if(access.equals(HotSpot.Accessibility.INACCESSIBLE.name()))
        {
            stringValueOfItem = getString(R.string.inaccessibleAccess);
        }
        else if(access.equals(HotSpot.Accessibility.LOGIN.name()))
        {
            stringValueOfItem = getString(R.string.loginAccess);
        }
        else if(access.equals(HotSpot.Accessibility.SECURE.name()))
        {
            stringValueOfItem = getString(R.string.secureAccess);
        }
        else if(access.equals(HotSpot.Accessibility.PUBLIC.name()))
        {
            stringValueOfItem = getString(R.string.publicAccess);
        }

        for(int i=0; i<count; i++)
        {
            if((spinnerAccessibility.getItemAtPosition(i)).equals(stringValueOfItem))
            {
                ind = i;
                break;
            }
        }

        return ind;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == Utilities.REQ_LOCATION_CHANGE)
            {
                Bundle extras = data.getExtras();
                posLongitude = extras.getDouble(Utilities.RETURN_HOTSPOT_LONGITUDE);
                posLatitude = extras.getDouble(Utilities.RETURN_HOTSPOT_LATITUDE);
            }
        }
    }


}
