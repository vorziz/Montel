package com.montel.Montel.ui.home;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.montel.Montel.MainActivity;
import com.montel.Montel.R;
import com.montel.Montel.model.Customer;
import com.montel.Montel.model.CustomerModel;
import com.montel.Montel.ui.detailmarker.DetailActivity;
import com.montel.Montel.utils.ApplicationService;
import com.montel.Montel.utils.Constant;
import com.montel.Montel.utils.LocationAlertIntentService;
import com.montel.Montel.utils.MarkerClusterItem;
import com.montel.Montel.utils.MarkerClusterRender;
import com.montel.Montel.utils.NotificationProvider;
import com.montel.Montel.utils.RealmHelper;
import com.montel.Montel.utils.SharedPref;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.DEFAULT_SOUND;
import static androidx.core.app.NotificationCompat.DEFAULT_VIBRATE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


@SuppressLint("NeedOnRequestPermissionsResult")
@RuntimePermissions
public class HomeFragment extends Fragment implements LocationListener, ClusterManager.OnClusterClickListener<MarkerClusterItem>,
        ClusterManager.OnClusterItemClickListener<MarkerClusterItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MarkerClusterItem>{

    private HomeViewModel homeViewModel;
    Location mCurrentLoction;


    String markerInfoString;
    private long UPDATE_INTERVAL = 60000;  /* 10 secs */
    private long FASTEST_INTERVAL = 10000; /* 5 secs */

    private final static String KEY_LOCATION = "location";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    LocationManager locMan;

    double latX;

    double lonX;

    NotificationManager notMan;
    private NotificationProvider
            notificationProvider;

    int mCustomerId;
    Intent intentDB;

    boolean mNotification = false;


    Realm realm;
    RealmHelper realmHelper;
    CustomerModel customerModel;
    List<CustomerModel> customerModelList;





    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private ClusterManager<MarkerClusterItem> mClusterManager;

    private static final int LOC_PERM_REQ_CODE = 1;
    //meters
    private static final int GEOFENCE_RADIUS = 500;
    //in milli seconds
    private static final int GEOFENCE_EXPIRATION = 6000;


    private GeofencingClient geofencingClient;
    LatLng geocoder;



    Handler handler;
    SupportMapFragment supportMapFragment;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference reference;
    private ArrayList<Customer> dataCustomer;
    List<Customer> listData;

    private FirebaseAuth auth;

    protected LatLng mCenterLocation = new LatLng(-1.6076556, 103.5897767);



    MarkerClusterItem clusterItems;
    private int position;
    public static final String TAG = "HomeFragment";


//    public HomeFragment() {
//        // Required empty public constructor
//    }

    public static HomeFragment newInstance(int position) {
        Bundle args = new Bundle();
        // Pass all the parameters to your bundle
        args.putInt("pos", position);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Context context;
    View views;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        context = this.getContext();
        handler = new Handler();
        views = root;
        notificationProvider = new NotificationProvider();


        //Set up Realm
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        try {
            realm = Realm.getInstance(configuration);
        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealm(configuration);
            realm = Realm.getInstance(configuration);
        }
        realmHelper = new RealmHelper(realm);
        customerModelList = new ArrayList<>();



        if (TextUtils.isEmpty(getResources().getString(R.string.api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }

        if (savedInstanceState != null && savedInstanceState.keySet().contains(KEY_LOCATION)) {
            // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
            // is not null.
            mCurrentLoction = savedInstanceState.getParcelable(KEY_LOCATION);
        }



        initLoaction();
        locMan=(LocationManager) context.getSystemService(LOCATION_SERVICE);



        geofencingClient = LocationServices.getGeofencingClient(context);

        return root;
    }

    private void initLoaction() {
        supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg));
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);


                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            addLocationAlert(latLng.latitude, latLng.longitude);
                        }
                    });
                }
            });
        } else {
            Toast.makeText(context, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMap(GoogleMap googleMap) {

        mMap = googleMap;
        if (mMap != null) {
            // Map is ready
            Toast.makeText(context, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();



            HomeFragmentPermissionsDispatcher.getMyLocationWithPermissionCheck(this);
            HomeFragmentPermissionsDispatcher.startLocationUpdatesWithPermissionCheck(this);
            setUpClusterer(mMap);
        } else {
            Toast.makeText(context, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        switch (requestCode) {
            case LOC_PERM_REQ_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //showCurrentLocationOnMap();
                    Toast.makeText(context,
                            "Location access permission granted, you try " +
                                    "add or remove location allerts",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @SuppressWarnings({"MissingPermission"})
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getMyLocation() {
        mMap.setMyLocationEnabled(true);

        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(context);
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();

    }



    @Override
    public void onResume() {
        super.onResume();

        // Display the connection status

        if (mCurrentLoction != null) {
            Toast.makeText(context, "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(mCurrentLoction.getLatitude(), mCurrentLoction.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f);
            mMap.animateCamera(cameraUpdate);
            //setUpClusterer(mMap);
        } else {
            Toast.makeText(context, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
        HomeFragmentPermissionsDispatcher.startLocationUpdatesWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        //noinspection MissingPermission
        getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // GPS may be turned off

        if (location != null) {
            // TODO Auto-generated method stub
            doGeo(location);
        }




                    // Report to the UI that the location was updated

        mCurrentLoction = location;
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        getGeofencePendingIntent();

        if (mCurrentLoction != null) {
            markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            //setUpClusterer(mMap);
        } else {
            Toast.makeText(context, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }


    }

    private void doGeo(Location location) {


        latX = location.getLatitude();
        lonX = location.getLongitude();
        CharSequence from = "AlarmManager - Time's up!";
        CharSequence message = "This is your alert";
        //final List<Customer> listData = new ArrayList<>();

        customerModelList = realmHelper.getAllCustomer();

        Log.d("DATA PRINT", customerModelList.toString());


        if (customerModelList != null) {
            for (CustomerModel cn : customerModelList) {
                //for (int x = 0; x <= customerModelList.size(); x++) {



                    String[] latlong = cn.getCoordinat().split(",");
                    double lat = Double.parseDouble(latlong[0]);
                    double lng = Double.parseDouble(latlong[1]);

                    Location tasklocation = new Location(LocationManager.GPS_PROVIDER);
                    tasklocation.setLatitude(lat);
                    tasklocation.setLongitude(lng);
                    double dis1 = location.distanceTo(tasklocation);
                    //DecimalFormat df2 = new DecimalFormat("#.##");


                    DecimalFormat df2 = new DecimalFormat("0.00");
                    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                    dfs.setDecimalSeparator('.');
                    df2.setDecimalFormatSymbols(dfs);

                    String dis2 = df2.format(dis1);
                    double dis3 = Double.parseDouble(dis2);
                   // Toast.makeText(context,"Distance to location is: " + dis2, Toast.LENGTH_LONG).show();
                    if (dis3 < 100.00) {

                        mNotification = SharedPref.read(SharedPref.NOTIFICATION, false);
                        if (mNotification == true){

                            notificationProvider.setNotificationProvider(context);
                        }

                  }



                //}

            }
        }
    }

    private Marker locationMarker;
    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if ( mMap!=null ) {
            if ( locationMarker != null )
                locationMarker.remove();
            locationMarker = mMap.addMarker(markerOptions);
            float zoom = 18f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mMap.animateCamera(cameraUpdate);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_LOCATION, mCenterLocation);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void setUpClusterer(GoogleMap mMap) {
        // Position the map.
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.304414, 103.834006), 17));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(context, mMap);
        mClusterManager.setRenderer(new MarkerClusterRender(context, mMap, mClusterManager));

        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        mMap.setOnInfoWindowClickListener(mClusterManager); //added
        mClusterManager.setOnClusterItemInfoWindowClickListener(this); //added



        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerClusterItem>() {
            @Override
            public boolean onClusterItemClick(MarkerClusterItem item) {
                clusterItems = item;
                return false;
            }

        });




        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic
                GetData();
            }
        }, 5000);


            //GetData();


        // Listener for Info-Window Click , Parse data to next activity.
        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MarkerClusterItem>() {
            @Override
            public void onClusterItemInfoWindowClick(MarkerClusterItem myItem) {

                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("key", myItem.getKey());
                startActivity(i);
            }
        });


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


    @Override
    public boolean onClusterClick(Cluster<MarkerClusterItem> cluster) {

        if (cluster == null) return false;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerClusterItem item : cluster.getItems())
            builder.include(item.getPosition());
        LatLngBounds bounds = builder.build();
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }


    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){
        Toast.makeText(context,"Please wait....", Toast.LENGTH_LONG).show();
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference("customers");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //Inisialisasi ArrayList
                dataCustomer = new ArrayList<>();


                //for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                mClusterManager.clearItems();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    Customer customer = postSnapshot.getValue(Customer.class);

//                            keyFirebase = postSnapshot.getChildren().iterator().next().getKey();
//                            customer.setKey(keyFirebase);
                    dataCustomer.add(customer);



                }


                for (int i=0;i<dataCustomer.size();i++) {

                    final  Customer customer = dataCustomer.get(i);

                    //Toast.makeText(context,"Data"+customer, Toast.LENGTH_LONG).show();

                    String[] latlong =  dataCustomer.get(i).getCoordinat().split(",");
                    String idpel  = dataCustomer.get(i).getNoMeter();
                    String name = dataCustomer.get(i).getName();
                    String address = dataCustomer.get(i).getAddress();
                    String URLImage = dataCustomer.get(i).getImageURL1();
                    String keyF = dataCustomer.get(i).getKey();






                    double lat = Double.parseDouble(latlong[0]);
                    double lng = Double.parseDouble(latlong[1]);



                    LatLng latLng = new LatLng(lat,lng);
                    clusterItems= new MarkerClusterItem(latLng,idpel,name,address,URLImage,keyF);


                    Gson gson = new Gson();
                    markerInfoString = gson.toJson(clusterItems);

                    mClusterManager.addItem(clusterItems);

                    ///-----//

                   //Realm Simpan Data
                    customerModel = new CustomerModel();
                    customerModel.setKey(dataCustomer.get(i).key);
                    customerModel.setName(dataCustomer.get(i).getName());
                    customerModel.setCoordinat(dataCustomer.get(i).coordinat);
                    realmHelper = new RealmHelper(realm);
                    realmHelper.save(customerModel);



                }

                if (supportMapFragment!= null){
                    mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());
                    mClusterManager.cluster();
                }


               // addLocationAlert(-7.439750, 110.677034);

                Toast.makeText(context,"Data Berhasil Dimuat", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
            }


        });
    }




    @Override
    public boolean onClusterItemClick(MarkerClusterItem markerClusterItem) {
        return false;
    }



    //added with edit
    @Override
    public void onClusterItemInfoWindowClick(MarkerClusterItem myItem) {

        //Cluster item InfoWindow clicked, set title as action
//        Intent i = new Intent(context, DetailActivity.class);
//        i.setAction(myItem.getTitle());
//        startActivity(i);

    }



    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        MyCustomAdapterForItems() {

                myContentsView = inflater.inflate(R.layout.snippet_info_marker, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {




            ImageView imageView = ((ImageView) myContentsView.findViewById(R.id.ivImageMarker));

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.txtIDPEL));
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.txtName));

            Gson gson = new Gson();
            MarkerClusterItem aMarkerInfo = gson.fromJson(marker.getSnippet(),MarkerClusterItem.class);

            //Log.e("MARKER",aMarkerInfo.toString());

            //tvTitle.setText(infoMarker.getName());
            tvSnippet.setText(marker.getSnippet());
            //Picasso.get().load(infoMarker.getURLimage()).into(imageView);
            //Toast.makeText(context,"DATA IMAGE : "+ infoMarker.getURLimage(),Toast.LENGTH_SHORT).show();


            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {



            return myContentsView;
        }
    }

    private boolean isLocationAccessPermitted(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            return false;
        }
    }
    private void requestLocationAccessPermission(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_PERM_REQ_CODE);
    }

    @SuppressLint("MissingPermission")
    private void addLocationAlert(double lat, double lng){
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else  {
            String key = ""+lat+"-"+lng;
            Geofence geofence = getGeofence(lat, lng, key);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),
                    getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context,
                                        "Location alter has been added",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context,
                                        "Location alter could not be added",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void removeLocationAlert(){
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else {
            geofencingClient.removeGeofences(getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context,
                                        "Location alters have been removed",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context,
                                        "Location alters could not be removed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(context, LocationAlertIntentService.class);
        return PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();
    }
//    protected void sendnotification (String title, String message) {
//        String ns = Context.NOTIFICATION_SERVICE;
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
//
//        int icon = R.drawable.ic_pelanggaran;
//        CharSequence tickerText = message;
//        long when = System.currentTimeMillis();
//
//        Notification notification = new Notification(icon, tickerText, when);
//
//
//        CharSequence contentTitle = title;
//        CharSequence contentText = message;
//        Intent notificationIntent = new Intent(context, GoogleMapsActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
//        mNotificationManager.notify(1, notification);
//    }







}