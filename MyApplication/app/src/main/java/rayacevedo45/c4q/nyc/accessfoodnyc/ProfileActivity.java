package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
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
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.ServiceGenerator;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpBusinessSearchService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static rayacevedo45.c4q.nyc.accessfoodnyc.MapsActivity.businessId;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mImageViewProfile;
    private TextView first;
    private TextView last;

    private Toolbar mToolbar;

    //private Button maps;
    private Button mButtonLogOut;
    private Button mButtonFindFriends;
    private Button mButtonFriends;

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

        getSupportActionBar().setTitle(me.getString("first_name") + " " + me.getString("last_name") + "'s Profile");


        if (objectId != null) {

            final ParseRelation<ParseUser> relation = me.getRelation("friends");

            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereEqualTo(Constants.EXTRA_KEY_OBJECT_ID, objectId);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    final ParseUser friend = list.get(0);
                    relation.add(friend);
                    me.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Accepted!", Toast.LENGTH_SHORT).show();

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
        first = (TextView) findViewById(R.id.first_name);
        last = (TextView) findViewById(R.id.last_name);
        mButtonFindFriends = (Button) findViewById(R.id.find_friends);
        mButtonFriends = (Button) findViewById(R.id.button_friends_list);
        mButtonLogOut = (Button) findViewById(R.id.log_out);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_profile_favorite);
//        mRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager lm = new LinearLayoutManager(this);
//        lm.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(lm);


        ParseUser user = ParseUser.getCurrentUser();
//        ParseRelation<ParseObject> relation = user.getRelation("favorite");
//        relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                if (list != null) {
//                    mOurVendorList = new ArrayList<ParseObject>();
//                    List<ParseObject> yelpList = new ArrayList<ParseObject>();
//                    for (ParseObject vendor : list) {
//                        if (vendor.getString("yelpId") == null) {
//                            mOurVendorList.add(vendor);
//                        } else {
//                            yelpList.add(vendor);
//                        }
//                    }
//                    new SearchAllYelpTask().execute(yelpList);
//                }
//            }
//        });

        first.setText(user.getString("first_name"));
        last.setText(user.getString("last_name"));
        Picasso.with(getApplicationContext()).load(user.getString("profile_url")).centerCrop().resize(400, 400).into(mImageViewProfile);
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
            mButtonFindFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), FindFriendsActivity.class);
                    startActivity(intent);
                }
            });
            mButtonLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOut();
                }
            });
            mButtonFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            mButtonFindFriends.setOnClickListener(null);
            mButtonLogOut.setOnClickListener(null);
            mButtonFriends.setOnClickListener(null);
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
}