package ddwu.mobile.finalproject.ma01_20190965;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class UpdateMyListActivity extends AppCompatActivity {

    private GoogleMap mGoogleMap;
    private LatLng currentLoc;
    private Marker marker;
    Cafe cafe;
    CafeDBManager cafeDBManager;

    TextView tvName;
    TextView tvAddress;
    TextView tvPhone;
    EditText etReview;
    RatingBar etRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mylist);

        cafe = (Cafe) getIntent().getSerializableExtra("cafe");
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);
        cafeDBManager = new CafeDBManager(this);

        tvName = findViewById(R.id.tvWishName);
        tvAddress = findViewById(R.id.tvWishAddress);
        tvPhone = findViewById(R.id.tvWishPhone);
        etReview = findViewById(R.id.etReview);
        etRating = findViewById(R.id.etRating);

        tvName.setText(cafe.getName());
        tvAddress.setText("주소 : " + cafe.getAddress());
        tvPhone.setText("전화번호 : " + cafe.getPhone());
        etReview.setText(cafe.getReview());
        etRating.setRating(Float.parseFloat(cafe.getRating()));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateMy:
                cafe.setReview(etReview.getText().toString());
                cafe.setRating(Float.toString(etRating.getRating()));

                if (cafeDBManager.modifyCafe(cafe)) {
                    Toast.makeText(UpdateMyListActivity.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(UpdateMyListActivity.this, "수정이 실패되었습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                }
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
