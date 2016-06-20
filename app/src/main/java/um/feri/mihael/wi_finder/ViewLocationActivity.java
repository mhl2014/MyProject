package um.feri.mihael.wi_finder;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.identity.intents.AddressConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Bundle extras;
    private Resources res;
    private final int initialZoom = 15;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(extras != null)
        {
            LatLng hotSpotPos = new LatLng(
                    extras.getDouble(Utilities.EXTRA_HOTSPOT_LATITUDE), extras.getDouble(Utilities.EXTRA_HOTSPOT_LONGITUDE));

            Marker marker = mMap.addMarker(new MarkerOptions().position(hotSpotPos)
                    .title(res.getString(R.string.ssid) + ": " + extras.getString(Utilities.EXTRA_HOTSPOT_SSID)));

            marker.setSnippet(res.getString(R.string.password) + ": " + extras.getString(Utilities.EXTRA_HOTSPOT_SEC_KEY));
            marker.showInfoWindow();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(hotSpotPos)
                    .zoom(initialZoom)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else
        {
            Toast.makeText(this, res.getString(R.string.internalError), Toast.LENGTH_SHORT).show();
        }
    }
}
