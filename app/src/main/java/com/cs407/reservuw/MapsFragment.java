//package com.cs407.reservuw;
//
//import static android.content.Intent.getIntent;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Looper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//
//
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//public class MapsFragment extends Fragment {
//
//    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1; // could've been any number!
//
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//
//    private GoogleMap mMap;
//
//    private final LatLng uwMadisonLocation = new LatLng(43.075404393142115, -89.40341145630344);
//
//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        /**
//         * Manipulates the map once available.
//         * This callback is triggered when the map is ready to be used.
//         * This is where we can add markers or lines, add listeners or move the camera.
//         * In this case, we just add a marker near Sydney, Australia.
//         * If Google Play services is not installed on the device, the user will be prompted to
//         * install it inside the SupportMapFragment. This method will only be triggered once the
//         * user has installed Google Play services and returned to the app.
//         */
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//
//            //TODO: getIntent() is depricated. How do i get my 'putExtra' value?
//            //determine whether to: displaying recent reserved location, or
//            //current location showing all available buildings
//            //Intent intent= getIntent();
//            //String str= intent.getStringExtra("result");
//            //if(){
//
//            //}else{}
//
//
//
//            mMap= googleMap;
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
//
//            displayMyLocation();
//
//
//            //TODO: used to track the user in real time
//            locationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    if (locationResult == null) {
//                        //TODO: put something here?
//                        return;
//                    }
//
//                    //TODO: doing this correct?
//                    for (Location location : locationResult.getLocations()) {
//                        //permission and location marker displayed here
//                        displayMyLocation();
//                    }
//                }
//            };
//
//
//            //TODO: IMPROTANT QUESTION: should i make a marker or use google map API 'places'
//            // https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
//            //TODO: make markers through database. loop and make them all.
//            //get database:
//            // -LatLng
//            // -title
//            //
//            // -set icon
//            //
//            // -opt: set infoWindow to display building photo
//
//
//            //TODO: use a hashMap. Key= the building address/name.
//            // Get arr of buildings via room database
//            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(@NonNull Marker marker) {
//                    String building= marker.getTitle();
//
//                    //make intent  to go to building view activity
//
//                    //putExtra= title, aka, buildings name
//
//                    return false;
//                }
//            });
//
//
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_maps, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
//    }
//
//
//
//
//    private void displayMyLocation() {
//        // Check if permission granted
//        int permission = ActivityCompat.checkSelfPermission(requireContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION);
//
//
//        if (permission == PackageManager.PERMISSION_DENIED) {
//
//
//            //Where we use PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//
//
//
//
//        } else {
//
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                //TODO: what should i put here
//                                // Logic to handle location object
//                            }
//                            LatLng currentLocation= new LatLng(location.getLatitude(), location.getLongitude());
//                            mMap.addMarker(new MarkerOptions().position(currentLocation));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//
//
//                        }
//                    });
//        }
//    }
//
//    //TODO: fix first: google real time track not working:
//    // https://developer.android.com/develop/sensors-and-location/location/request-updates?authuser=1#java
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //Notice that the above code snippet refers to a boolean flag, requestingLocationUpdates,
//        // used to track whether the user has turned location updates on or off.
//        // If users have turned location updates off, you can inform them of
//        // your app's location requirement.
//        if (requestingLocationUpdates) {
//            startLocationUpdates();
//        }
//    }
//
//    private void startLocationUpdates() {
//        fusedLocationClient.requestLocationUpdates(locationRequest,
//                locationCallback,
//                Looper.getMainLooper());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//    }
//
//    private void stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
//
//}