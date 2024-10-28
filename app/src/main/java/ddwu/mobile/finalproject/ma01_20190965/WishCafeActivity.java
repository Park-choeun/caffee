package ddwu.mobile.finalproject.ma01_20190965;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class WishCafeActivity extends AppCompatActivity {

    private GoogleMap mGoogleMap;
    private LatLng currentLoc;
    private Marker marker;
    Cafe cafe;

    TextView tvName;
    TextView tvAddress;
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishcafe);

        cafe = (Cafe) getIntent().getSerializableExtra("cafe");
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        tvName = findViewById(R.id.tvWishName);
        tvAddress = findViewById(R.id.tvWishAddress);
        tvPhone = findViewById(R.id.tvWishPhone);

        tvName.setText(cafe.getName());
        tvAddress.setText("주소 : " + cafe.getAddress());
        tvPhone.setText("전화번호 : " + cafe.getPhone());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWishMy:
                Intent addIntent = new Intent(WishCafeActivity.this, AddMyListActivity.class);
                addIntent.putExtra("cafe", cafe);
                startActivity(addIntent);
                break;
            case R.id.btnClose:
                finish();
                break;
        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;

            currentLoc = new LatLng(cafe.getLat(), cafe.getLng());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions options = new MarkerOptions();
            options.position(currentLoc);
            options.title(cafe.getName());
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            marker = mGoogleMap.addMarker(options);
            marker.showInfoWindow();
        }
    };
}
