package ddwu.mobile.finalproject.ma01_20190965;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddMyListActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add_mylist);

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
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddMy:
                boolean result = cafeDBManager.addNewCafe(new Cafe(cafe.getName(), cafe.getAddress(), cafe.getPhone(), etReview.getText().toString(), Float.toString(etRating.getRating()), "m", null, cafe.getLat(), cafe.getLng(), cafe.getPlaceId()));

                if (result) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMyListActivity.this);
                    builder.setTitle("MyList에 " + cafe.getName() +"이(가) 추가되었습니다.")
                            .setMessage("MyList로 이동하시겠습니까?")
                            .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(AddMyListActivity.this, MyListActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("더 살펴보기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    Toast.makeText(AddMyListActivity.this, "추가가 실패되었습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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
