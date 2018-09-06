package com.example.aleksandra.backpack.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aleksandra.backpack.Models.PlaceModel;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.adapters.ProfileNewPlaceAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.HashMap;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private GoogleMap mMap;
    String provider;
    double lat;
    double lng;
    Marker marker;
    private LocationManager lm;

    private ArrayList<User> temp=new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        // setSupportActionBar(toolbar);
         lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     //   provider = lm.getBestProvider(new Criteria(), true);

      //  provider="gps";
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });**/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
     //  Location location = lm.getLastKnownLocation(provider);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private  static HashMap<Marker,Integer> markerUserIDMap;
    private  static HashMap<Marker,Integer> markerPlaceIDMap;


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        } else {

            LatLng sydney = new LatLng(lat, lng);

            mMap.setMyLocationEnabled(true);
         //   marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location is here...!!! "));

           addUsersMarkers();
            addPlaceMarkers();
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent i = new Intent(GoogleMapActivity.this, PlacesActivity.class);
                    int a =Integer.valueOf(marker.getTitle());
                    i.putExtra("position", a);
                    startActivity(i);

                 //   Toast.makeText(GoogleMapActivity.this,marker.getPosition().latitude+" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }

    }
    public   void addUsersMarkers()
    {

        //ovde treba skloniti
        ArrayList<PlaceModel> places = FirebaseAccess.getInstance().getPlaces();
        markerUserIDMap = new HashMap<Marker, Integer>((int) ((double) places.size() * 1.2));
        for (int i = 0; i < places.size(); i++) {
            PlaceModel place = places.get(i);
            String lat = place.getLatitude();
            String lon = place.getLongitute();
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(loc);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_eye_open));
            markerOptions.title(String.valueOf(i));
            Marker marker = mMap.addMarker(markerOptions);
            markerUserIDMap.put(marker, i);
        }
    }
    public   void addPlaceMarkers()
    {

        //ovde treba skloniti
        ArrayList<PlaceModel> places = FirebaseAccess.getInstance().getPlaces();
        markerPlaceIDMap = new HashMap<Marker, Integer>((int) ((double) places.size() * 1.2));
        for (int i = 0; i < places.size(); i++) {
            PlaceModel place = places.get(i);
            String lat = place.getLatitude();
            String lon = place.getLongitute();
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(loc);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_eye_open));
            markerOptions.title(String.valueOf(i));

            Marker marker = mMap.addMarker(markerOptions);
            markerPlaceIDMap.put(marker, i);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider callingf
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
         //   return;
        }
     /*   lm.requestLocationUpdates(provider, 400, 1, this);
        lat = location.getLatitude();
        lng = location.getLongitude();


        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));
        Marker marker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));

        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser userF=auth.getCurrentUser();
      //
          String email=userF.getEmail();

        User u=new User();
        int index=0;
        for(int i = 0; i< FirebaseAccess.getInstance().getUsers().size(); i++)
        {
            if(email== FirebaseAccess.getInstance().getUsers().get(i).getEmail() ) { u= FirebaseAccess.getInstance().getUsers().get(i);
            index=i;
            }
        }

        u= FirebaseAccess.getInstance().getUsers().get(index);
        u.setLatitude(String.valueOf(lat));
        u.setLongitude(String.valueOf(lng));
        u.UserKey= FirebaseAccess.getInstance().getUser(index).UserKey;
        //FirebaseAccess.getInstance().updateUser(index,u.getFirst_name(),u.getLast_name(),u.getEmail(),u.getPassword(),u.getGender(),u.getUsername(),u.getLatitude(),u.getLongitude());
       addUsersMarkers();
        Toast.makeText(this,lat+" "+lng,Toast.LENGTH_SHORT).show();*/

    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//       lm.requestLocationUpdates(provider, 400, 1, this);

    }

    @SuppressLint("MissingPermision")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                }
                return;
            }

        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_rank) {
            Intent mainIntent = new Intent(GoogleMapActivity.this,RankActivity.class);
            startActivity(mainIntent);
            finish();
        } else if (id == R.id.nav_radius) {

        } else if (id == R.id.nav_profile) {
            Intent mainIntent = new Intent(GoogleMapActivity.this,ProfileActivity.class);
            startActivity(mainIntent);
            finish();

        } else if (id == R.id.nav_settings) {


        } else if (id == R.id.nav_logout) {
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.signOut();
            Intent mainIntent = new Intent(GoogleMapActivity.this, SigninActivity.class);
            startActivity(mainIntent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
