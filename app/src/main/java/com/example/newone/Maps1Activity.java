package com.example.newone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newone.Controller.DatabaseHandler;
import com.example.newone.Model.Place;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.newone.DirectionFinder;
import com.example.newone.DirectionFinderListener;
import com.example.newone.Route;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class Maps1Activity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener,GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    Location currenLocation;
    DatabaseHandler db;
    private String namo;
    private static final int REQUEST_CODE=10001;
    private Button btnFindPath,next;
    private Button buExit;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SearchView searchView;
    List<Marker> markerListDB;
    private LatLng checkForDB=new LatLng(32.847038, 35.209176);
    private Bundle extras;
    private Switch aSwitch;


    // lm3se bdeka mol bmkbel m3 almekom
    private LocationManager locationManager;
    // read location
    private LocationListener locationListener;



    //
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable=ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Initialize variable for find nearby places
    Spinner spType;
    Button btFind,go;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat=0,currentLong=0;
    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);

        //keshor ll client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // return to the last location - before exit
        fetchLastLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);


        // switch for traffic mode
        aSwitch =(Switch) findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){

                    mMap.setTrafficEnabled(true);
                }
                else{
                    mMap.setTrafficEnabled(false);
                }
            }
        });

        //Assign variables for nearby places spinner
        spType=findViewById(R.id.sp_type);
        btFind=findViewById(R.id.bt_find);
        supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        //initialize array of places type
        final String[] placeTypeList={"park","gym","ATM","bakery","bank","hospital","cafe","police","restaurant"};
        //initialize array of place name
        String[] placeNameList={"Park","GYM","ATM","Bakery","Bank","Hospital","Cafe","Police Station","Restaurant"};

        extras= getIntent().getExtras();


        if(extras != null){



           String.valueOf(currenLocation);
            //etOrigin.setText(String.valueOf(currentLat)+" "+String.valueOf(currentLong));
           // etOrigin.setText(String.valueOf(currenLocation));
            etOrigin.setText("Haifa");
            etDestination.setText(extras.getString("title"));



            sendRequest();
        }
        // for the search
        searchView=findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String location=searchView.getQuery().toString();
                List<Address> addressList=null;
                if(location !=null || !location.equals("")){
                    Geocoder geocoder=new Geocoder(Maps1Activity.this);
                    try {
                        addressList=geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address= addressList.get(0);
                   // mMap.clear();
                    LatLng  latLng=new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        spType.setAdapter(new ArrayAdapter<>(Maps1Activity.this
                ,android.R.layout.simple_spinner_dropdown_item,placeNameList));
        //Initialize fused location provider client
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        //check permission sptype
        if(ActivityCompat.checkSelfPermission(Maps1Activity.this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //when permession granted
            //call method

            getCurrentLocation();
        }else {
            //when permission denied
            //request permission
            ActivityCompat.requestPermissions(Maps1Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        // find nearby places

        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get selected position of spinner
                int i=spType.getSelectedItemPosition();
                check=spType.getSelectedItem().toString();
                //initialize url
                String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json"+//url
                        "?location="+currentLat+","+currentLong+//location
                        "&radius=4000"+
                        "&types="+placeTypeList[i]+
                        "&sensor=true"+
                        "&key="+getResources().getString(R.string.map_key);

                //execute place task method to download json data
                new Maps1Activity.PlaceTask().execute(url);
            }
        });




        // search for route
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        buExit=(Button)findViewById(R.id.buttonExit);
        buExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Maps1Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCurrentLocation() {
        //Initialize task location
        Task<Location> task= fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when success
                if(location !=null){
                    //when location is not equal to null
                    //get current latitude and longidtude
                    currentLat=location.getLatitude();
                    currentLong=location.getLongitude();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //when map is ready
                            mMap=googleMap;
                            //zoom current location on map
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat,currentLong),10
                            ));

                        }
                    });

                }
            }
        });

    }


    private void fetchLastLocation() {


        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currenLocation=location;
                Toast.makeText(getApplicationContext(),currenLocation.getLatitude()
                        +""+currenLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(Maps1Activity.this);
            }
        });

    }


    private void sendRequest() {
        // send request to build the route
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void enableUserLocation(){

        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        db=new DatabaseHandler(this);

        mMap.setIndoorEnabled(true);
        mMap.isIndoorEnabled();
        LatLng tamra = new LatLng(32.847481, 35.208637);
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tamra, 14));
       // originMarkers.add(mMap.addMarker(new MarkerOptions()
           //     .title("Hello")
          //      .position(tamra)));



        // for database__________________________________________________DB
        markerListDB=new ArrayList<>();
        List<Place> placeList=db.getAllPlaces();

        for(Place p: placeList){
            String myInfo="ID: "+p.getId()+"Latitude: "+p.getPlatitude()+"Longitude: "+p.getpLongitude()+"Title: "+p.getTitle();
            Log.d("myInfo",myInfo);

            markerListDB.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(p.getPlatitude())
                            ,Double.parseDouble(p.getpLongitude()))).title(p.getTitle()).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ttt))));
        }



        for(Marker m: markerListDB){

            LatLng checkDB=new LatLng(m.getPosition().latitude,m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(checkDB).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ttt)));
        }

        //----------------GPS---------------------
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("My Location",location.toString());
                String myFullAdress="";



                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);


                    if(addressList !=null && addressList.size()>0){
                        Log.d("Address",addressList.get(0).toString());

                        if(addressList.get(0).getAddressLine(0) != null){
                            myFullAdress+= addressList.get(0).getCountryName() +" ";
                        }
                        Toast.makeText(Maps1Activity.this,myFullAdress,Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("Address","Address not fount");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMap.clear();
                LatLng start=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(start).title(myFullAdress));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            enableUserLocation();

        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                //we can show user location
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            }
        }
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
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait",
                "Finding direction...", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }



    // after get all the data for build route
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        // show the route on the map
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 11));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            // add markers on map
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(20);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){

            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                enableUserLocation();
            }else{
                //error in location

            }
        }
        if(requestCode==44){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //when permiss granted
                //call method
                getCurrentLocation();
            }

        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker((new MarkerOptions().position(latLng).title(latLng.toString())
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ttt))));
        Intent intent=new Intent(Maps1Activity.this,AddActivity.class);
        intent.putExtra("latitude",latLng.latitude);
        intent.putExtra("longitude",latLng.longitude);

        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this,marker.getTitle(),Toast.LENGTH_SHORT).show();

        // open new page
        Intent intent=new Intent(Maps1Activity.this,ShowActivity.class);
        // send variables to the new page
        intent.putExtra("latitude",marker.getPosition().latitude);
        //2
        intent.putExtra("longitude",marker.getPosition().longitude);
        //3
        intent.putExtra("title",marker.getTitle());
        // start the activity
        startActivity(intent);
        return false;
    }


    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data=null;
            //initialize data
            try {
                data=downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //execute parser task
            new Maps1Activity.ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException{
        //initialize url
        URL url=new URL(string);
        //initialize connection
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        //connect connection
        connection.connect();
        //initialize input stream
        InputStream stream=connection.getInputStream();
        //initialize buffer reader
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
        //initialize string builder
        StringBuilder builder=new StringBuilder();
        //initialize string variable
        String line="";
        //use while loop
        while((line=reader.readLine())!=null){
            //append line
            builder.append(line);
        }
        //get append data
        String data=builder.toString();
        //close reader
        reader.close();
        //return data
        return  data;


    }
    private class ParserTask extends  AsyncTask<String,Integer,List<HashMap<String,String>>>{
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            //create json parser class
            JsonParser jsonParser=new JsonParser();
            //initialize hash map list
            List<HashMap<String,String>> mapList=null;
            //initialize json object
            JSONObject object=null;
            try {
                object=new JSONObject(strings[0]);
                //parse json object
                mapList=jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // return all the places that URL found
            return mapList;
        }


        // for the nearby places
        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //clear map
           mMap.clear();
            //user for loop
            for(int i=0;i<hashMaps.size();i++){
                //initialize hash map
                HashMap<String,String> hashMapList=hashMaps.get(i);
                //get latitud and longitude
                double lat= Double.parseDouble(hashMapList.get("lat"));
                double lng=Double.parseDouble(hashMapList.get("lng"));
                String name="haifa "+hashMapList.get("name");
                //concat latitude and longitude
                LatLng latLng=new LatLng(lat,lng);

                if(check.equals("Restaurant")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_burger));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("Cafe")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_coffee_cup));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("GYM")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_gym));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("Park")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_park));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("ATM")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_atm));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("Bakery")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_bread));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("Bank")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_banking));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("Hospital")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_hospital));
                    //add marker on map
                    mMap.addMarker(options);
                }
                if(check.equals("Police Station")){
                    //initialize marker options
                    MarkerOptions options=new MarkerOptions();
                    //mMap.addMarker(new MarkerOptions().position(latLng));
                    //set position
                    options.position(latLng);
                    //set title
                    options.title(name);
                    options.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_police));
                    //add marker on map
                    mMap.addMarker(options);
                }




            }

        }
    }

}
