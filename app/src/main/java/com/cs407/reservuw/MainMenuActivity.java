package com.cs407.reservuw;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
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



public class MainMenuActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {


    private boolean locationPermissionGranted;

    //the chosen request id to check/request fine location permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    //this class provides us the ability to obtain users location
    private FusedLocationProviderClient fusedLocationClient;

    private Location lastKnownLocation;

    //failsafe location in the case of a rare location fetch error becuase of the system
    private final LatLng defaultLocation = new LatLng(43.075404393142115, -89.40341145630344);

    //non-null instance of the GoogleMap class that we use to update and customize the map
    private GoogleMap mMap;

    // The entry point to the Places API
    private PlacesClient placesClient;




    //TODO: need? fix?
    private CameraPosition cameraPosition;
    //saving the map state
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";




    @Override
    protected void onCreate(Bundle savedInstanceState){
        //gets login uid to make shared preference
        Intent intent= getIntent();

        //TODO: why do we make userID catch a intent when we could fetch it the shared preference??
        int userID= intent.getIntExtra("uid", -1);
        Log.i(TAG, userID+ "");

        SharedPreferences sharedPreferences = getSharedPreferences ("com.cs407.reservuw", MODE_PRIVATE);

        if(sharedPreferences.getInt ( "uid", -1) == -1){
            sharedPreferences.edit().putInt("uid", userID).apply();
        }



        //renders main Menu
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // TODO: need? delete or fix?
        //Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }



        // Construct a PlacesClient, using our API key from resources, in order to get our select
        // uw madison buildings real time information
        Places.initialize(getApplicationContext(), String.valueOf(R.string.PlacesClient_Key).toString());
        placesClient = Places.createClient(this);

        //TODO: setting map and nav can be their own functions

        // Sets GoogleMap fragment, and navigation
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView binding = findViewById(R.id.bottomNavigationView);
        binding.setSelectedItemId(R.id.logout);
        binding.setSelectedItemId(R.id.reserves);
        binding.setSelectedItemId(R.id.favorites);


        // sets navigation onClick listeners
        binding.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.favorites) {
                    goToFav();
                }else if(item.getItemId() == R.id.reserves){
                    goToMyRes();
                }else if(item.getItemId() == R.id.logout){
                    goToLogin();
                    overridePendingTransition(0, 0);
                }
                        return true;

            }
        });


    }

    //navigation "favorites" button onClick function
    public void goToFav() {
        startActivity(new Intent(getApplicationContext(), FavoritesActivity.class));
    }

    //navigation "reserves" button onClick function
    public void goToMyRes() {
        startActivity(new Intent(getApplicationContext(), myReserveActivity.class));
    }

    //navigation "logout" button onClick function
    public void goToLogin(){
        //erase user session
        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }



    //when the map is ready to receive user input.
    // It provides a non-null instance of the GoogleMap class
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap= googleMap;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Gets the current location of the device and sets the users position on the map
        getDeviceLocation();

        // uses PlacesAPI to display our reservable buildings location marker
        showCurrentPlace();
    }


    /**
     * sets the location controls on the map. If the user has granted location permission,
     * enable the My Location layer and the related control on the map,
     * otherwise disable the layer and the control, and set the current location to null,
     * and request for user location permission
     */
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



    private void getLocationPermission() {
        /*
         * Request location permission.
         * The result of the permission request is handled by a callback, onRequestPermissionsResult().
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationPermissionGranted = true;

            //TODO: issue: the first time permission is granted, the camera doesnt go to the users
            // location
            // fix: getDeviceLocation() should be called here?


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    //Callback function. Called after getLocationPermission() function gets a return back from
    // the system permission request call
    //TODO: cant i remove this? it seems redundent since locationPermissionGranted can be changed
    // in getLocationPermission.
    // this seems more useful if after a request, we do additional things in the background depending on the
    // result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If grantResults array is empty, its because the request was cancelled
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
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

                            //if location fetch was successful, we add a marker representing the user and move camera to it
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

        Marker marker[] = new Marker[placesID.length]; //used to set our markers tag= placeID,
                                                        // in order for its onClick function to take user to
                                                        // that specific building Activity
        for(int i=0; i< placesID.length; i++){
            int finalI = i;
            placesClient.fetchPlace(requests[i]).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                Log.i(TAG, "Place found: " + place.getName());

                //make and assign a marker to a building
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

                }
            });
        }
    }



    /** Called when the user clicks a building marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the buildings placeAPI's "placeID"
        String placeID = (String) marker.getTag();

        // if the marker clicked was the user, leave
        if(placeID== null) return false;


        Intent intent= new Intent(this, BuildingActivity.class);
        intent.putExtra("ID", placeID);
        startActivity(intent);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open.)
        return false;
    }



    //TODO: works?? check
    /**save the map state when the activity pauses*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }




    /**Helper function to make custom marker logo. Used for buildings markers*/
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}



