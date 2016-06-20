package um.feri.mihael.wi_finder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.identity.intents.AddressConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Bundle extras;
    private Resources res;
    private final int initialZoom = 15;
    private Button saveBtn;
    private ApplicationMy app;

    private String password;
    float markerColor;
    boolean markerDraggable = false;

    private Marker marker;

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

        saveBtn = (Button) findViewById(R.id.buttonEditLocation);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editLocationIntent = new Intent();
                editLocationIntent.putExtra(Utilities.RETURN_HOTSPOT_LONGITUDE, marker.getPosition().longitude);
                editLocationIntent.putExtra(Utilities.RETURN_HOTSPOT_LATITUDE, marker.getPosition().latitude);
                setResult(Activity.RESULT_OK, editLocationIntent);

                finish();
            }
        });

        if(extras != null) {
            HotSpot.Accessibility accessibility = HotSpot.Accessibility.valueOf(extras.getString(Utilities.EXTRA_HOTSPOT_ACCESS));

            password = res.getString(R.string.password) + ": " + extras.getString(Utilities.EXTRA_HOTSPOT_SEC_KEY);

            if (extras.containsKey(Utilities.INTENT_LOCATION_CHANGE)) {
                markerDraggable = extras.getBoolean(Utilities.INTENT_LOCATION_CHANGE);
            }
            else
            {
                saveBtn.setVisibility(View.GONE);
            }

            if (accessibility == HotSpot.Accessibility.PRIVATE) {
                markerColor = BitmapDescriptorFactory.HUE_BLUE;
            } else if (accessibility == HotSpot.Accessibility.PUBLIC) {
                markerColor = BitmapDescriptorFactory.HUE_GREEN;
                password = res.getString(R.string.noPassWordRequired);
            } else if (accessibility == HotSpot.Accessibility.SECURE) {
                markerColor = BitmapDescriptorFactory.HUE_YELLOW;
            } else {
                markerColor = BitmapDescriptorFactory.HUE_RED;
                password = res.getString(R.string.passwordInaccessible);
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

        LatLng hotSpotPos = new LatLng(
                extras.getDouble(Utilities.EXTRA_HOTSPOT_LATITUDE), extras.getDouble(Utilities.EXTRA_HOTSPOT_LONGITUDE));

        marker = mMap.addMarker(new MarkerOptions().position(hotSpotPos)
                .title(res.getString(R.string.ssid) + ": " + extras.getString(Utilities.EXTRA_HOTSPOT_SSID)));

        marker.setDraggable(markerDraggable);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(markerColor));
        marker.setSnippet(password);
        marker.showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(hotSpotPos)
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
}
