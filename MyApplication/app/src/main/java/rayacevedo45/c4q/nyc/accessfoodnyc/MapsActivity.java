package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import rayacevedo45.c4q.nyc.accessfoodnyc.accounts.LoginActivity;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Coordinate;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.YelpResponse;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.ServiceGenerator;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpSearchService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener {

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private static final String LOCATION_KEY = "location-key";
    private static final String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mToolbarLayout;
    private FloatingActionButton mButtonFilter;

    private RecyclerView mRecyclerView;
    private VendorListAdapter mAdapter;

    private List<ParseObject> mVendorList;
    public static String businessId;

    private static String latLngForSearch = "40.740949, -73.932157";
    private static LatLng lastLatLng;

    public static ParseApplication sApplication;

    // Declare a variable for the cluster manager.
    ClusterManager<MarkerCluster> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        buildGoogleApiClient();
        createLocationRequest();

        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMap = mapFragment.getMap();

        initializeViews();


        //setSupportActionBar(mToolbar);
        mToolbar.setTitle("Maps");
        //mToolbar.inflateMenu(R.menu.menu_map);
        setSupportActionBar(mToolbar);

        // this is enable to back button arrow icon
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


    }

    private void initializeViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mButtonFilter = (FloatingActionButton) findViewById(R.id.button_filter);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        lm.computeHorizontalScrollExtent()
        mRecyclerView.setLayoutManager(lm);
    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MarkerCluster>(getApplicationContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);


        mClusterManager.setRenderer(new ClusterRendring(getApplicationContext(), mMap, mClusterManager));

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerCluster>() {
            @Override
            public boolean onClusterItemClick(MarkerCluster markerCluster) {

                String a = markerCluster.getTitle();
                Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
                return false;
            }

        });


    }

    protected class YelpSearchCallback implements Callback<YelpResponse> {

        public String TAG = "YelpSearchCallback";
        public HashMap < Marker, String> markerHashMap;
        @Override
        public void success(YelpResponse data, Response response) {
            Log.d(TAG, "Success");
            sApplication = ParseApplication.getInstance();
            sApplication.sYelpResponse = data;
            final List<Business> businessList = sApplication.sYelpResponse.getBusinesses();

            mAdapter.addYelpList(businessList);

            markerHashMap = new HashMap<>();
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_WEEK);

            //ParseGeoPoint point = new ParseGeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
//            query.whereNear();
            query.setLimit(50).findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    List<ParseObject> ourVendors = new ArrayList<ParseObject>();
                    for (ParseObject object : list) {
                        if (object.getString("yelpId") == null) {
                            ourVendors.add(object);
                            String today = "day" + Integer.toString(day);
                            String json = object.getString(today);
                            try {
                                JSONObject info = new JSONObject(json);
                                double latitude = info.getDouble("latitude");
                                double longitude = info.getDouble("longitude");
                                ParseGeoPoint location = new ParseGeoPoint(latitude, longitude);
                                object.put("location", location);
                                object.saveInBackground();
                                LatLng position = new LatLng(latitude, longitude);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(object.getString("name")));
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_truck_red));
                                markerHashMap.put(marker, object.getObjectId());
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }


                    }
//                    mAdapter = new VendorListAdapter(getApplicationContext(), businessList, ourVendors);
//                    mRecyclerView.setAdapter(mAdapter);
                }
            });



            int i = 1;
            for (Business business : businessList) {
                rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Location location = business.getLocation();
                Coordinate coordinate = location.getCoordinate();

                double latitude = coordinate.getLatitude();

                double longitude = coordinate.getLongitude();
                LatLng position = new LatLng(latitude, longitude);
                // create marker
//                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(business.getName());
                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(business.getName())); //...

//
//                MarkerCluster mc = new MarkerCluster(latitude, longitude, business.getName(),business.getId());
//                mClusterManager.addItem(mc);
                // Changing marker icon
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_truck_red));


                markerHashMap.put(marker, business.getId());
//                mMap.addMarker(marker);
            }
//            generateClusterManager(mClusterManager);
//            mClusterManager.cluster();

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String businessId = markerHashMap.get(marker);

                    Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_OBJECT_ID, businessId);
                    startActivity(intent);
                }
            });


        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(TAG, error.getMessage());
        }

    }

    protected ClusterManager<MarkerCluster> generateClusterManager(ClusterManager<MarkerCluster> mClusterManager){
        return mClusterManager;
    }

        @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListener(true);
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    public void setUpListener(boolean isResumed) {
        if (isResumed) {
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
                    Object object = mAdapter.getItem(position);
                    if (object instanceof Business) {
                        Business business = (Business) mAdapter.getItem(position);
                        businessId = business.getId();
                        intent.putExtra(Constants.EXTRA_KEY_IS_YELP, true);
                        intent.putExtra(Constants.EXTRA_KEY_OBJECT_ID, businessId);
                    } else {
                        ParseObject vendor = (ParseObject) object;
                        intent.putExtra(Constants.EXTRA_KEY_IS_YELP, false);
                        intent.putExtra(Constants.EXTRA_KEY_OBJECT_ID, vendor.getObjectId());
                    }
                    startActivity(intent);
                }
            })
            );

        } else {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListener(false);
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                logOut();
                break;
            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnCameraChangeListener(this);

    }

    @Override
    public void onConnected(Bundle bundle) {

        LatLng defaultLatLng = new LatLng(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        mAdapter = new VendorListAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


        ParseGeoPoint point = new ParseGeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<ParseObject> ourVendors = new ArrayList<ParseObject>();
                for (ParseObject object : list) {
                    if (object.getString("yelpId") == null) {
                        ourVendors.add(object);
//                        String today = "day" + Integer.toString(day);
//                        String json = object.getString(today);
//                        try {
//                            JSONObject info = new JSONObject(json);
//                            double latitude = info.getDouble("latitude");
//                            double longitude = info.getDouble("longitude");
//                            ParseGeoPoint location = new ParseGeoPoint(latitude, longitude);
//                            object.put("location", location);
//                            object.saveInBackground();
//                            LatLng position = new LatLng(latitude, longitude);
//                            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(object.getString("name")));
//                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_truck_red));
//                            markerHashMap.put(marker, object.getObjectId());
//                        } catch (JSONException e1) {
//                            e1.printStackTrace();
//                        }
                    }


                }
                mAdapter.addList(ourVendors);
            }
        });

//        setUpClusterer();

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();

            YelpSearchService yelpService = ServiceGenerator.createYelpSearchService();
//        yelpService.searchFoodCarts(String.valueOf(lastLatLng), new YelpSearchCallback());
            yelpService.searchFoodCarts(address + " " + postalCode, new YelpSearchCallback());
//        yelpService.searchFoodCarts("3100 47th Ave 11101", new YelpSearchCallback());

        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    protected void startLocationUpdates() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            }
        };
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, locationListener);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        outState.putParcelable(LOCATION_KEY, mCurrentLocation);
        outState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    private void getQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
    }

    private void cleanUpMarkers(Set<String> markersToKeep) {
//        for (String objId : new HashSet<String>(mapMarkers.keySet())) {
//            if (!markersToKeep.contains(objId)) {
//                Marker marker = mapMarkers.get(objId);
//                marker.remove();
//                mapMarkers.get(objId).remove();
//                mapMarkers.remove(objId);
//            }
//        }
    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        ParseUser.logOut();
        Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}