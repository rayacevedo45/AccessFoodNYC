package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.accounts.LoginActivity;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageViewProfile;
    private TextView first;
    private TextView last;

    private Toolbar mToolbar;

    //private Button maps;
    private LinearLayout mButtonFriends;
    private LinearLayout mButtonReviews;
    private LinearLayout mButtonFavorite;
    private TextView mTextViewFriends;
    private TextView mTextViewReviews;
    private TextView mTextViewFavorite;

    private RecyclerView mRecyclerView;
    private VendorListAdapter mAdapter;

    private String mFavoriteBizName;
    private TextView mName;
    private List<ParseObject> mOurVendorList;
    private List<Business> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String objectId = getIntent().getStringExtra(Constants.EXTRA_KEY_OBJECT_ID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ParseUser me = ParseUser.getCurrentUser();
        String name = me.getString("first_name") + " " + me.getString("last_name");

        getSupportActionBar().setTitle(name);


        if (objectId != null) {

            final ParseRelation<ParseUser> relation = me.getRelation("friends");

            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereEqualTo(Constants.EXTRA_KEY_OBJECT_ID, objectId);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    final ParseUser friend = list.get(0);
                    relation.add(friend);
                    Toast.makeText(getApplicationContext(), "Accepted!", Toast.LENGTH_SHORT).show();

                    ParseRelation<ParseUser> pendingFriends = me.getRelation("friend_requests");
                    pendingFriends.remove(friend);
                    me.saveInBackground();

                    ParseUser user = ParseUser.getCurrentUser();
                    String name = user.get("first_name") + " " + user.get("last_name");
                    try {
                        JSONObject data = new JSONObject("{\"alert\": \"" + name + " accepted your friend request!" + "\"," +
                                "\"accepted\": \"" + user.getObjectId() + "\"}");
                        ParseQuery query = ParseInstallation.getQuery();
                        query.whereEqualTo("user", friend);
                        ParsePush push = new ParsePush();
                        push.setQuery(query);
                        push.setData(data);
                        push.sendInBackground();

                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            });
        }

        mImageViewProfile = (ImageView) findViewById(R.id.imageView_profile);
        mButtonFriends = (LinearLayout) findViewById(R.id.button_friends_list);
        mButtonReviews = (LinearLayout) findViewById(R.id.button_user_reviews);
        mButtonFavorite = (LinearLayout) findViewById(R.id.button_profile_favorite);

        mTextViewFavorite = (TextView) findViewById(R.id.profile_number_favorite);
        mTextViewFriends = (TextView) findViewById(R.id.profile_number_friends);
        mTextViewReviews = (TextView) findViewById(R.id.profile_number_reviews);

        ParseRelation<ParseUser> friendRelation = me.getRelation("friends");
        friendRelation.getQuery().countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                mTextViewFriends.setText(i + "");
            }
        });

        ParseQuery<ParseObject> favorites = ParseQuery.getQuery("Favorite");
        favorites.whereEqualTo("follower", me).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mTextViewFavorite.setText(list.size() + "");
            }
        });
        

        ParseQuery<ParseObject> reviewQuery = ParseQuery.getQuery("Review");
        reviewQuery.whereEqualTo("writer", me).countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                mTextViewReviews.setText(i + "");
            }
        });




        //first.setText(name);
        Picasso.with(getApplicationContext()).load(me.getString("profile_url")).into(mImageViewProfile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListeners(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListeners(false);
    }

    private void setUpListeners(boolean isResumed) {
        if (isResumed) {
            mButtonFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                    startActivity(intent);
                }
            });
            mButtonReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserReviewActivity.class);
                    startActivity(intent);
                }
            });
            mButtonFavorite.setOnClickListener(this);
        } else {
            //mButtonFindFriends.setOnClickListener(null);
            //mButtonLogOut.setOnClickListener(null);
            mButtonFriends.setOnClickListener(null);
            mButtonReviews.setOnClickListener(null);
            mButtonFavorite.setOnClickListener(null);
        }
    }


    private void logOut() {
        LoginManager.getInstance().logOut(); // facebook logout
        ParseUser.logOut();
        Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_profile_favorite:
                Intent intent = new Intent(getApplicationContext(), UserFavoriteActivity.class);
                startActivity(intent);
                break;
        }
    }


    //    public class SearchAllYelpTask extends AsyncTask<List<ParseObject>, Void, List<Business>> {
//        @Override
//        protected List<Business> doInBackground(List<ParseObject>... params) {
//
//            //final List<Business> result = new ArrayList<>();
//            List<ParseObject> vendors = params[0];
//            for (ParseObject vendor : vendors) {
//                String yelpId = vendor.getString("yelpId");
//                YelpBusinessSearchService yelpBizService = ServiceGenerator.createYelpBusinessSearchService();
//                yelpBizService.searchBusiness(yelpId, new Callback<Business>() {
//                    @Override
//                    public void success(Business business, Response response) {
//                        result.add(business);
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        LinearLayout layout;
//
//                    }
//                });
//
//            }
//            int size = result.size();
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Business> businesses) {
//            mAdapter = new VendorListAdapter(getApplicationContext(), businesses, mOurVendorList);
//            mRecyclerView.setAdapter(mAdapter);
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_find_friends:
                Intent intent = new Intent(getApplicationContext(), FindFriendsActivity.class);
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
}