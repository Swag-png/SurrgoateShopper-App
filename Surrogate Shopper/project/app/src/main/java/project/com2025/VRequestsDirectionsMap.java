package project.com2025;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class VRequestsDirectionsMap extends AppCompatActivity implements OnMapReadyCallback {

    private Button btnShowDirections;
    private GoogleMap mMap;
    private boolean mapLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);  // Enable edge-to-edge UI
        setContentView(R.layout.activity_vrequests_directions_map); // NOTE: ensure this name matches the XML file exactly

        // Adjust padding for system bars for the main container view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnShowDirections = findViewById(R.id.btnShowDirections);
        FrameLayout mapContainer = findViewById(R.id.mapContainer);

        // Handle button click
        btnShowDirections.setOnClickListener(v -> {
            if (!mapLoaded) {
                mapContainer.setVisibility(View.VISIBLE); // Show the map container
                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mapContainer, mapFragment)
                        .commit();
                mapFragment.getMapAsync(this);
                mapLoaded = true;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // The Tyrwhitt, Rosebank, Johannesburg
        LatLng destination = new LatLng(-26.1466, 28.0412);
        mMap.addMarker(new MarkerOptions().position(destination).title("The Tyrwhitt, Rosebank"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
    }

}

