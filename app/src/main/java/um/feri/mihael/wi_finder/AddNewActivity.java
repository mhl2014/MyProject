package um.feri.mihael.wi_finder;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import weka.classifiers.Classifier;
import weka.classifiers.rules.PART;
import weka.core.*;

public class AddNewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Resources res;

    private TextView addSSID;
    private TextView addSecKey;

    private Button btnScan;
    private Button btnAdd;

    private String userID;

    private Spinner spinnerAccessibility;
    private Spinner spinnerRating;

    private String accessLevel;

    private static final String trainingSetAssetName = "WEKATrainingSet";
    private static final String trainingSetFileName = "WEKATrainingSet.arff";
    private File trainingSetFile;

    private String discovererRating; // Prvi rating, ki ga postavi tisti, ki je odkril HotSpot

    private String accessStringARFF;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private Location currentLocation;
    private boolean gotCoordinates;

    private int position;
    private Bundle extras;

    ApplicationMy app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        res = getResources();
        app = (ApplicationMy) getApplication();
        gotCoordinates = false;
        currentLocation = null;

        extras = getIntent().getExtras();

        setContentView(R.layout.activity_add_new);

        String trainingSetString = "";

        StringBuilder builder = new StringBuilder();
        InputStream stream = null;
        BufferedReader bufferedReader = null;

        try {
            AssetManager assetManager = getApplicationContext().getAssets();
            stream = assetManager.open("trainingDataSet");
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            String temp;

            while((temp = bufferedReader.readLine()) != null)
            {
                builder.append(temp).append('\n');
            }

            bufferedReader.close();

            trainingSetString = builder.toString();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        InitializeFile(trainingSetFile, trainingSetString);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        addSSID = (TextView) findViewById(R.id.addEditTextSSID);
        addSecKey = (TextView) findViewById(R.id.addEditTextSecKey);

        btnScan = (Button) findViewById(R.id.buttonScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent scanIntent = new Intent(Utilities.REQ_SCAN);
                    scanIntent.putExtra(Utilities.SCAN_MODE, Utilities.QR_CODE_MODE);
                    startActivityForResult(scanIntent, Utilities.ACTION_SCAN);
                } catch (ActivityNotFoundException anfe) {
                    showDownloadDialog(AddNewActivity.this, res.getString(R.string.zxingNotFound),
                            res.getString(R.string.zxingDownload),
                            res.getString(R.string.yes),
                            res.getString(R.string.no))
                            .show();
                }
            }
        });

        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(AddNewActivity.this);
                    dialog.setTitle(res.getString(R.string.gpsRequired));
                    dialog.setMessage(res.getString(R.string.enableGps));

                    dialog.setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            AddNewActivity.this.startActivity(enableGpsIntent);
                        }
                    });

                    dialog.setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    dialog.show();
                }

                if (ContextCompat.checkSelfPermission(AddNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(AddNewActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Utilities.PERMISSION_ACCESS_FINE_LOCATION);
                }
                else if (getLocationFromGps())
                {
                    returnNewHotSpot();
                }
            }
        });

        spinnerAccessibility = (Spinner) findViewById(R.id.activityAddNewSpinnerAccess);
        ArrayAdapter<CharSequence> adapterAccessibility = ArrayAdapter.createFromResource(
                this, R.array.accessibility, android.R.layout.simple_spinner_item);
        adapterAccessibility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccessibility.setAdapter(adapterAccessibility);
        spinnerAccessibility.setOnItemSelectedListener(this);

        spinnerRating = (Spinner) findViewById(R.id.activityAddNewSpinnerRating);
        ArrayAdapter<CharSequence> adapterRating = ArrayAdapter.createFromResource(
                this, R.array.ratingsArray, android.R.layout.simple_spinner_item);
        adapterAccessibility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRating.setAdapter(adapterRating);
        spinnerRating.setOnItemSelectedListener(this);

        if(extras != null)
        {
            userID = extras.getString(Utilities.EXTRA_USER_ID);
            position = extras.getInt(Utilities.EXTRA_HOTSPOT_POS);
            addSSID.setText(extras.getString(Utilities.EXTRA_HOTSPOT_SSID));
        }

        if(ContextCompat.checkSelfPermission(AddNewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if(!getLocationFromGps())
            {
                Toast.makeText(this, res.getString(R.string.gpsFailure), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void returnNewHotSpot() {

        // Zacni klasifikacijo
        trainingSetFile = new File(getApplicationContext().getFilesDir(),trainingSetFileName);

        Instances trainingInstances = getInstances(trainingSetFile);
        if(trainingInstances!= null)
            trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
        else
            throw new RuntimeException("Could not read instances");

        Classifier klasifikator = new PART();
        ((PART)klasifikator).setNumFolds(10);

        try {
            klasifikator.buildClassifier(trainingInstances);
        } catch (Exception e) {
            e.printStackTrace();
        }

        User discoverer = null;


        Instance instanca = new DenseInstance(trainingInstances.firstInstance());
        instanca.setClassValue("?");
        instanca.setValue(0, currentLocation.getLongitude());
        instanca.setValue(1, currentLocation.getLatitude());
        instanca.setValue(2, 1); // Vedno bo en visit na zacetku
        instanca.setValue(3, accessStringARFF);
        instanca.setValue(4, app.getAll().getUserById(userID).getPoints());
        // KONCAJ



        String classifierRating = klasifikator.classifyInstance()

        if (currentLocation != null) {
            Intent addNewIntent = new Intent();
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_SSID, addSSID.getText().toString());
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_SEC_KEY, addSecKey.getText().toString());
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_ACCESS, accessLevel);
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_LATITUDE, currentLocation.getLatitude());
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_LONGITUDE, currentLocation.getLongitude());
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_AVG_RATE, discovererRating);
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_RATE_CLASS, classifierRating);
            addNewIntent.putExtra(Utilities.RETURN_HOTSPOT_POS, position);
            setResult(Activity.RESULT_OK, addNewIntent);

            finish();
        }
        else
        {
            Toast.makeText(AddNewActivity.this, res.getString(R.string.gpsSignalNotAcquired) + "!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean getLocationFromGps() {

        boolean gotCoordinates = false;

        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            gotCoordinates = true;
        }
        catch (SecurityException se)
        {
            Toast.makeText(this, res.getString(R.string.gpsRequired), Toast.LENGTH_SHORT).show();
        }

        return gotCoordinates;
    }

    private AlertDialog showDownloadDialog(final Activity ac, String title, String msg, String btnYes, String btnNo) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ac);
        dialog.setTitle(title);
        dialog.setMessage(msg);

        dialog.setPositiveButton(btnYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(res.getString(R.string.marketUri) + res.getString(R.string.zxingUri)));

                try
                {
                    ac.startActivity(viewIntent);
                }
                catch (ActivityNotFoundException afne)
                {
                    Toast.makeText(ac, res.getString(R.string.activityNotFound), Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.setNegativeButton(btnNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == Utilities.ACTION_SCAN)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                addSecKey.setText(data.getStringExtra(Utilities.SCAN_RESULT));
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        // Ugotovimo kateri spinner je sprozil dogodek
        switch (adapterView.getId()) {
            case R.id.activityAddNewSpinnerAccess: {
                String selectedItem = adapterView.getSelectedItem().toString();

                if (selectedItem.equals(res.getString(R.string.publicAccess))) {
                    accessLevel = HotSpot.Accessibility.PUBLIC.name();
                    accessStringARFF = HotSpot.PUBLIC_ACCESS_ARFF;
                }
                else if (selectedItem.equals(res.getString(R.string.loginAccess))) {
                    accessLevel = HotSpot.Accessibility.LOGIN.name();
                    accessStringARFF = HotSpot.LOGIN_ACCESS_ARFF;
                }
                else if (selectedItem.equals(res.getString(R.string.secureAccess))) {
                    accessLevel = HotSpot.Accessibility.SECURE.name();
                    accessStringARFF = HotSpot.PASSWORD_ACCESS_ARFF;
                }
                else if (selectedItem.equals(res.getString(R.string.inaccessibleAccess))) {
                    accessLevel = HotSpot.Accessibility.INACCESSIBLE.name();
                    accessStringARFF = HotSpot.NO_PUBLIC_ACCESS_ARFF;
                }
                else if (selectedItem.equals(res.getString(R.string.privateAccess))) {
                    accessLevel = HotSpot.Accessibility.PRIVATE.name();
                    accessStringARFF = HotSpot.PRIVATE_ACCESS_ARFF;
                }
                break;
            }

            case R.id.activityAddNewSpinnerRating: {
                String selectedItem = adapterView.getSelectedItem().toString();

                if (selectedItem.equals(res.getString(R.string.noAccessRating)))
                    discovererRating = HotSpot.NOACCESS_RATING_ARFF;
                else if (selectedItem.equals(res.getString(R.string.badRating)))
                    discovererRating = HotSpot.BAD_RATING_ARFF;
                else if (selectedItem.equals(res.getString(R.string.okRating)))
                    discovererRating = HotSpot.OK_RATING_ARFF;
                else if (selectedItem.equals(res.getString(R.string.goodRating)))
                    discovererRating = HotSpot.GOOD_RATING_ARFF;
                else if (selectedItem.equals(res.getString(R.string.greatRating)))
                    discovererRating = HotSpot.GREAT_RATING_ARFF;
                break;
            }

            default:
                throw new RuntimeException("Unregistered spinner");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Utilities.PERMISSION_ACCESS_FINE_LOCATION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(getLocationFromGps())
                    {
                        returnNewHotSpot();
                    }
                }
                else
                {
                    Toast.makeText(this, res.getString(R.string.gpsRequired), Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void InitializeFile(File setFile, String contents) {
        // Prvo preverimo, ce obstaja datoteka z instancami za treniranje
        try {
            if (!setFile.exists()) {
                if (!setFile.createNewFile()) {
                    throw new RuntimeException("Could not create file from set");
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(setFile);
            fileOutputStream.write(contents.getBytes());

            fileOutputStream.flush();
            fileOutputStream.close();

            Log.d("FileWrite", contents);
        } catch (IOException | RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    private Instances getInstances(File fileSet) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileSet));
            return new Instances(bufferedReader);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        return null;
    }
}