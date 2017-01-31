package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ViewLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Bundle extras;
    private Resources res;
    private final int initialZoom = 15;
    private Button saveBtn;
    private ApplicationMy app;
    private String userSelectedSsid;
    private LatLng userSeletedPosition;
    private double displayDistance = 500; // V metrih

    private String userSelectedPassword;
    float markerColor;
    boolean markerDraggable = false;

    // Matker katerega je izbral uporabnik
    private Marker userSelectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        extras = getIntent().getExtras();
        res = getResources();
        app = (ApplicationMy) getApplication();

        userSelectedSsid = extras.getString(Utilities.EXTRA_HOTSPOT_SSID);

        saveBtn = (Button) findViewById(R.id.buttonEditLocation);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editLocationIntent = new Intent();
                editLocationIntent.putExtra(Utilities.RETURN_HOTSPOT_LONGITUDE, userSelectedMarker.getPosition().longitude);
                editLocationIntent.putExtra(Utilities.RETURN_HOTSPOT_LATITUDE, userSelectedMarker.getPosition().latitude);
                setResult(Activity.RESULT_OK, editLocationIntent);

                finish();
            }
        });

        if(extras != null) {
            HotSpot.Accessibility accessibility = HotSpot.Accessibility.valueOf(extras.getString(Utilities.EXTRA_HOTSPOT_ACCESS));

            userSelectedPassword = getPasswordString(extras.getString(Utilities.EXTRA_HOTSPOT_SEC_KEY), accessibility);
            markerColor = getMarkerHue(accessibility);

            if (extras.containsKey(Utilities.INTENT_LOCATION_CHANGE)) {
                markerDraggable = extras.getBoolean(Utilities.INTENT_LOCATION_CHANGE);
            }
            else
            {
                saveBtn.setVisibility(View.GONE);
            }
        }
        else
        {
            Toast.makeText(this, res.getString(R.string.internalError), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        userSeletedPosition = new LatLng(
                extras.getDouble(Utilities.EXTRA_HOTSPOT_LATITUDE), extras.getDouble(Utilities.EXTRA_HOTSPOT_LONGITUDE));

        userSelectedMarker = mMap.addMarker(new MarkerOptions().position(userSeletedPosition)
                .title(res.getString(R.string.ssid) + ": " + extras.getString(Utilities.EXTRA_HOTSPOT_SSID)));

        userSelectedMarker.setDraggable(markerDraggable);
        userSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(markerColor));
        userSelectedMarker.setSnippet(userSelectedPassword);
        userSelectedMarker.showInfoWindow();

        // Dodamo markerje za hotspote, ki se nahajajo blizu originalnega
        // Pridobimo stevilo vseh HotSpotov
        int numOfHotSpots = app.getAll().hotSpotSize();
        List<HotSpot> hotSpots = app.getAll().getHotSpots();

        for(int i=0; i<numOfHotSpots; i++)
        {
            // HotSpot ni isti torej zracunamo oddaljenost od njega
            if(!hotSpots.get(i).getSsid().equals(userSelectedSsid))
            {
                // Ce je razdalja manjsa ali enaka nastavljeni prikazemo marker
                if(calculateHotSpotDistance(hotSpots.get(i)) <= displayDistance)
                {
                   Marker closeMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(hotSpots.get(i).getLatitude(), hotSpots.get(i).getLongitude()))
                            .title(hotSpots.get(i).getSsid()));

                    closeMarker.setDraggable(false);
                    closeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(getMarkerHue(hotSpots.get(i).getAccessLevel())));
                    closeMarker.setSnippet(hotSpots.get(i).getSecurityKey());
                }
            }
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userSeletedPosition)
                .zoom(initialZoom)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });
    }

    private double calculateHotSpotDistance(HotSpot other)
    {
        double longitudeOriginal = userSeletedPosition.longitude;
        double latitudeOriginal = userSeletedPosition.latitude;

        double longitudeOther = other.getLongitude();
        double latitudeOther = other.getLatitude();

        // Zracunamo oddaljenost glede na sfericni zakon kosinusov
        return (Math.acos(Math.sin(latitudeOriginal * Math.PI/180) * Math.sin(latitudeOther * Math.PI/180)
                + Math.cos(latitudeOriginal * Math.PI/180) * Math.cos(latitudeOther * Math.PI/180) * Math.cos(longitudeOther * Math.PI/180 - longitudeOriginal * Math.PI/180) * 6371000) * 6371000);
    }

    private String getPasswordString(String SecKey, HotSpot.Accessibility access)
    {
        String password;

        if (access == HotSpot.Accessibility.PUBLIC) {
            password = res.getString(R.string.noPassWordRequired);
        }
        else if(access == HotSpot.Accessibility.INACCESSIBLE)
        {
            password = res.getString(R.string.passwordInaccessible);
        }
        else
        {
            password = res.getString(R.string.password) + ": " + SecKey;
        }

        return password;
    }

    private float getMarkerHue(HotSpot.Accessibility access){

        float hue;

        if (access == HotSpot.Accessibility.PRIVATE) {
            hue = BitmapDescriptorFactory.HUE_BLUE;
        } else if (access == HotSpot.Accessibility.PUBLIC) {
            hue = BitmapDescriptorFactory.HUE_GREEN;
        } else if (access == HotSpot.Accessibility.SECURE) {
            hue = BitmapDescriptorFactory.HUE_YELLOW;
        } else {
            hue = BitmapDescriptorFactory.HUE_RED;
        }

        return hue;
    }
}
