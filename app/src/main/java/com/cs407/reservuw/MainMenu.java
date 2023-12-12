package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainMenu extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {



    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1; // could've been any number!

    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap mMap;

    private Location lastKnownLocation;
    private CameraPosition cameraPosition;


    // The entry point to the Places API.
    private PlacesClient placesClient;


    //grant permission
    private boolean locationPermissionGranted;

    private final LatLng defaultLocation = new LatLng(43.075404393142115, -89.40341145630344);


    //saving the map state
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Intent intent= getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), "AIzaSyDYnEriKHXVSo50g7c_XJ7QlN3TjooL0QM");
        placesClient = Places.createClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //binding = new ActivityMainMenuBinding(findViewById(R.id.bottomNavigationView));
        BottomNavigationView binding = findViewById(R.id.bottomNavigationView);
        binding.setSelectedItemId(R.id.logout);
        binding.setSelectedItemId(R.id.reserves);
        binding.setSelectedItemId(R.id.favorites);

        // Perform item selected listener
        binding.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.favorites) {
                    goToFav();
                }else if(item.getItemId() == R.id.reserves){

                }else if(item.getItemId() == R.id.logout){
                    goToLogin();
                    overridePendingTransition(0, 0);
                }
                        return true;

            }
        });



    }

    public void goToFav() {
        Intent intent2 = new Intent(this, favorite.class);
        startActivity(intent2);
    }

    public void goToMyres() {
        Intent intent3 = new Intent(this, myReserve.class);
        startActivity(intent3);
    }

    public void goToLogin(){
        //TODO: erase user session

        startActivity(new Intent(getApplicationContext(), loginActivity.class));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap= googleMap;


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //places API
        showCurrentPlace();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()))
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_user_location_marker)));

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 15));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, 15));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    //Places API implementation for buildings marker and info
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }


        // Define the Places ID
        Resources res = getResources();
        final String[] placesID = res.getStringArray(R.array.buildingPlacesID);


        //make a arr of placeFields
        // Specify the fields to return.
        List<List<Place.Field>> placeFields = new ArrayList<List<Place.Field>>(placesID.length);
        for(int i=0; i< placesID.length; i++){
            placeFields.add(Arrays.asList(Place.Field.LAT_LNG , Place.Field.NAME));
            //placeFields.set(i, Arrays.asList(Place.Field.LAT_LNG , Place.Field.NAME));
        }


        //make a arr of requests
        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest[] requests = new FetchPlaceRequest[placesID.length];
        for(int i=0; i< placesID.length; i++){
            requests[i]= FetchPlaceRequest.newInstance(placesID[i], placeFields.get(i));
        }



        //get our places and place marker

        //make marker of array. used to set our markers tag= placeID
        Marker marker[] = new Marker[placesID.length];
        for(int i=0; i< placesID.length; i++){
            int finalI = i;
            placesClient.fetchPlace(requests[i]).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                Log.i(TAG, "Place found: " + place.getName());

                //add marker to building
                marker[finalI] = mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_building_location))
                        .title(place.getName()));
                marker[finalI].setTag(placesID[finalI]);

                mMap.setOnMarkerClickListener(this);


            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();

                    //NOTE: here Handle error with given status code.
                }
            });
        }
    }



    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
        String placeID = (String) marker.getTag();
        Log.i(TAG, "Place clicked: " + placeID);
        // if the thing clicked wasnt a building (the user)
        if(placeID== null) return false;


        //TODO: go to Building view. choose building via buildings places ID
        //Intent intent= new Intent(this, BuildingView.class);
        //intent.putExtra("ID", placeID);
        //startActivity(intent);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }



    //save the map state when the activity pauses
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }




    //To make marker logo different
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
