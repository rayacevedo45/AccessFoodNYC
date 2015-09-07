package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Coordinate;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.ServiceGenerator;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpBusinessSearchService;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpSearchService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserFavoriteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private VendorListAdapter mAdapter;

    private List<ParseObject> yelpList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favorite);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        final String today = "day" + Integer.toString(day);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_user_favorite);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

        final ParseUser user = ParseUser.getCurrentUser();
        ParseGeoPoint point = user.getParseGeoPoint("location");

        mAdapter = new VendorListAdapter(getApplicationContext(), point);
        mRecyclerView.setAdapter(mAdapter);

        ParseQuery<ParseObject> favorites = ParseQuery.getQuery("Favorite");
        favorites.include("vendor");
        favorites.whereEqualTo("follower", user).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject favorite : list) {
                    final ParseObject vendor = favorite.getParseObject("vendor");
                    if (vendor.getParseGeoPoint("location") == null) {

                        ParseRelation<ParseUser> friends = user.getRelation("friends");
                        friends.getQuery().findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {

                                ParseQuery<ParseObject> favorites = ParseQuery.getQuery("Favorite");
                                favorites.include("follower");
                                favorites.whereEqualTo("vendor", vendor).whereContainedIn("follower", list);
                                favorites.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(final List<ParseObject> list, ParseException e) {
                                        YelpBusinessSearchService yelpBizService = ServiceGenerator.createYelpBusinessSearchService();
                                        yelpBizService.searchBusiness(vendor.getString("yelpId"), new Callback<Business>() {
                                            @Override
                                            public void success(Business business, Response response) {
                                                Coordinate coordinate = business.getLocation().getCoordinate();
                                                ParseGeoPoint location = new ParseGeoPoint(coordinate.getLatitude(), coordinate.getLongitude());
                                                Vendor truck = new Vendor.Builder(business.getId())
                                                        .isYelp(true).isLiked(true).setName(business.getName())
                                                        .setAddress(DetailsFragment.addressGenerator(business).get(0))
                                                        .setPicture(business.getImageUrl()).setRating(business.getRating())
                                                        .setLocation(location).setFriends(list).build();
                                                mAdapter.addVendor(truck);
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                            }
                                        });
                                    }
                                });

                            }
                        });

                    } else {

                        final String json = vendor.getString(today);
                        ParseRelation<ParseUser> friends = user.getRelation("friends");
                        friends.getQuery().findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> friends, ParseException e) {

                                ParseQuery<ParseObject> favorites = ParseQuery.getQuery("Favorite");
                                favorites.include("follower");
                                favorites.whereEqualTo("vendor", vendor).whereContainedIn("follower", friends);
                                favorites.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(final List<ParseObject> list, ParseException e) {

                                        Vendor truck = new Vendor.Builder(vendor.getObjectId())
                                                .setName(vendor.getString("name")).setAddress(vendor.getString("address"))
                                                .isYelp(false).setCategory(vendor.getString("category"))
                                                .setFriends(list).setLocation(vendor.getParseGeoPoint("location")).setHours(json)
                                                .setPicture(vendor.getString("profile_url")).setRating(vendor.getDouble("rating"))
                                                .isLiked(true).build();
                                        mAdapter.addVendor(truck);

                                    }
                                });

                            }
                        });

                    }

                }
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        goToVendorInfoAcvitity(position);
                    }
                })
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToVendorInfoAcvitity(int position) {
        Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
        Vendor vendor = mAdapter.getItem(position);
        intent.putExtra(Constants.EXTRA_KEY_OBJECT_ID, vendor.getId());
        if (vendor.isYelp()) {
            intent.putExtra(Constants.EXTRA_KEY_IS_YELP, true);
        } else {
            intent.putExtra(Constants.EXTRA_KEY_IS_YELP, false);
        }
        startActivity(intent);
    }
}
