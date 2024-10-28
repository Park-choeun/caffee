package ddwu.mobile.finalproject.ma01_20190965;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class SearchActivity extends AppCompatActivity {

    final static String TAG = "SearchActivity";
    final static int PERMISSION_REQ_CODE = 100;
    final static int GPS_ENABLE_REQUEST_CODE = 2001;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    EditText etLocation;
    private GoogleMap mGoogleMap;
    private PlacesClient placesClient;
    private Geocoder geocoder;
    private LatLng currentLoc;
    private MarkerOptions markerOptions;
    private GpsTracker gpsTracker;
    CafeDBManager cafeDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
        gpsTracker = new GpsTracker(SearchActivity.this);
        double lat = gpsTracker.getLatitude();
        double lng = gpsTracker.getLongitude();
        currentLoc = new LatLng(lat, lng);

        etLocation = findViewById(R.id.etLocation);
        mapLoad();

        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        placesClient = Places.createClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        cafeDBManager = new CafeDBManager(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                String Searchlocation = etLocation.getText().toString();
                Log.d(TAG, "Search Location :: " + Searchlocation);
                List<LatLng> address = getLatLng(Searchlocation);
                Log.d(TAG, "Search address :: " + address);

                double latitude = address.get(0).latitude;
                double longitude = address.get(0).longitude;
                LatLng searchLoc = new LatLng(latitude, longitude);;
                searchStart(latitude, longitude);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchLoc, 17));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public void onMenuItemClick(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.GoWishList:
                intent = new Intent(this, WishListActivity.class);
                break;
            case R.id.GoMyList:
                intent = new Intent(this, MyListActivity.class);
                break;
            case R.id.exit:
                builder.setTitle("앱 종료")
                        .setMessage("앱을 종료하시겠습니까?")
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.finishAffinity(SearchActivity.this);
                                System.runFinalization();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
        }
        if (intent != null) startActivity(intent);
    }

    private void searchStart(double latitude, double longitude) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.api_key))
                .latlng(latitude, longitude)
                .radius(300)
                .type(PlaceType.CAFE)
                .build()
                .execute();
    }

    private void getPlaceDetail(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.PHONE_NUMBER, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse response) {
                        Place place = response.getPlace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);

                        Log.d(TAG, "Place ID :: " + place.getId());
                        Log.d(TAG, "Place found :: " + place.getName());
                        Log.d(TAG, "Place PhoneNumber :: " + place.getPhoneNumber());  //전화번호가 없을때에는 null로 나옴
                        Log.d(TAG, "Place Address :: " + place.getAddress());
                        Log.d(TAG, "Place Photo :: " + place.getLatLng());

                        String cafeAddress = place.getAddress().substring(5);
                        String cafePhone;
                        if (place.getPhoneNumber() == null) {
                            cafePhone = "없음";
                        } else {
                            cafePhone = place.getPhoneNumber();
                        }
                        String cafeInfo = "<주소>\n" + cafeAddress + "\n\n<전화번호>\n" + cafePhone;
                        Cafe cafe = new Cafe(place.getName(), cafeAddress, cafePhone, null, null, "w", null, place.getLatLng().latitude, place.getLatLng().longitude, place.getId());

                        builder.setTitle(place.getName())
                                .setMessage(cafeInfo)
                                .setPositiveButton("WishList", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        boolean result = cafeDBManager.addNewCafe(cafe);

                                        if (result) {
                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(SearchActivity.this);
                                            builder2.setTitle("WishList에 " + place.getName() + "이(가) 추가되었습니다.")
                                                    .setMessage("WishList로 이동하시겠습니까?")
                                                    .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent wintent = new Intent(SearchActivity.this, WishListActivity.class);
                                                            startActivity(wintent);
                                                        }
                                                    })
                                                    .setNegativeButton("더 검색하기", null)
                                                    .setCancelable(false)
                                                    .show();
                                        } else {
                                            Toast.makeText(SearchActivity.this, "추가가 실패되었습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("MyList", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent addIntent = new Intent(SearchActivity.this, AddMyListActivity.class);
                                        addIntent.putExtra("cafe", cafe);
                                        startActivity(addIntent);
                                    }
                                })
                                .setCancelable(true)
                                .show();
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e(TAG, "Place not found :: " + statusCode + " " + e.getMessage());
                        }
                    }
                }
        );
    }

    PlacesListener placesListener = new PlacesListener() {
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places) {
                        markerOptions.title(place.getName());
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                        ));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());
                        Log.d(TAG, place.getName() + " :: " + place.getPlaceId());
                    }
                }
            });
        }

        @Override
        public void onPlacesFailure(PlacesException e) { }

        @Override
        public void onPlacesStart() { }

        @Override
        public void onPlacesFinished() { }
    };

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            markerOptions = new MarkerOptions();
            Log.d(TAG, "Map ready");

            if (checkPermission()) {
                mGoogleMap.setMyLocationEnabled(true);
            }

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            searchStart(currentLoc.latitude, currentLoc.longitude);

            mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Toast.makeText(SearchActivity.this, "현재위치", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    List<String> straddress = getAddress(location.getLatitude(), location.getLongitude());
                    Toast.makeText(SearchActivity.this,
                            "현재위치 : " + straddress,
                            Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String placeId = marker.getTag().toString();
                    getPlaceDetail(placeId);
                }
            });
        }
    };

    private void mapLoad() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);
    }

    private List<String> getAddress(double latitude, double longitude) {

        List<Address> addresses = null;
        ArrayList<String> addressFragments = null;

//        위도/경도에 해당하는 주소 정보를 Geocoder 에게 요청
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses == null || addresses.size()  == 0) { //주소를 찾을 수 없을 때
            return null;
        } else {
            Address addressList = addresses.get(0);
            addressFragments = new ArrayList<String>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                addressFragments.add(addressList.getAddressLine(i));
            }
        }

        return addressFragments;
    }

    private List<LatLng> getLatLng(String targetLocation) {

        List<Address> addresses = null;
        ArrayList<LatLng> addressFragments = null;

//        주소에 해당하는 위도/경도 정보를 Geocoder 에게 요청
        try {
            addresses = geocoder.getFromLocationName(targetLocation, 1);
        } catch (IOException e) { // Catch network or other I/O problems.
            e.printStackTrace();
        } catch (IllegalArgumentException e) { // Catch invalid address values.
            e.printStackTrace();
        }

        if (addresses == null || addresses.size()  == 0) {
            return null;
        } else {
            Address addressList = addresses.get(0);
            addressFragments = new ArrayList<LatLng>();

            for(int i = 0; i <= addressList.getMaxAddressLineIndex(); i++) {
                LatLng latLng = new LatLng(addressList.getLatitude(), addressList.getLongitude());
                addressFragments.add(latLng);
            }
        }

        return addressFragments;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    void checkRunTimePermission(){

        if (!checkPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SearchActivity.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(SearchActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(SearchActivity.this, REQUIRED_PERMISSIONS, PERMISSION_REQ_CODE);
            } else {
                ActivityCompat.requestPermissions(SearchActivity.this, REQUIRED_PERMISSIONS, PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
