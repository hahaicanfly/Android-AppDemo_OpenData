package akira.com.opendata_1;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Bundle bundle;
    private LocationManager mLocationManager;
    private Context context;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bundle = getIntent().getExtras();

        mLocationManager = (LocationManager) getSystemService(context.LOCATION_SERVICE);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //(自定義)效果
        double X = Double.parseDouble((String) bundle.get("X"));
        double Y = Double.parseDouble((String) bundle.get("Y"));
        String title = (String) bundle.get("title");
        String address = (String) bundle.get("address");

        Log.i("XYY", X + "  " + Y);
        LatLng place = new LatLng(Y, X);
        moveMap(place);

        addMarker(place, title, address);

    }


    private void moveMap(LatLng place) {
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addMarker(LatLng place, String title, String content) {

        BitmapDescriptor icon =
                BitmapDescriptorFactory.fromResource(R.drawable.pin);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title(title)
                .snippet(content)
                .icon(icon);
        mMap.addMarker(markerOptions);
    }


}
