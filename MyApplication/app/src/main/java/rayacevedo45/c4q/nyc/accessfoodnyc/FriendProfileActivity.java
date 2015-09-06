package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.ServiceGenerator;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpBusinessSearchService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FriendProfileActivity extends AppCompatActivity {

    private String objectId;
    private ImageView mImageView;
    private TextView mTextViewName;
    private Button mButtonRemove;
    private RecyclerView mRecyclerView;
    private VendorListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        if (getIntent() != null) {
            objectId = getIntent().getStringExtra(Constants.EXTRA_KEY_OBJECT_ID);
        } else {
            objectId = "";
        }

        mImageView = (ImageView) findViewById(R.id.imageView_friend_profile);
        mTextViewName = (TextView) findViewById(R.id.friend_name);
        mButtonRemove = (Button) findViewById(R.id.button_friend_remove);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_friend_favorites);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

//        mAdapter = new VendorListAdapter(getApplicationContext());
//        mRecyclerView.setAdapter(mAdapter);

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.getInBackground(objectId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                String first = user.getString("first_name");
                Picasso.with(getApplicationContext()).load(user.getString("profile_url")).resize(400, 400).centerCrop().into(mImageView);
                mTextViewName.setText(user.getString("first_name") + " " + user.getString("last_name"));

                ParseRelation<ParseObject> relation = user.getRelation("favorite");
                relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        List<ParseObject> vendors = new ArrayList<ParseObject>();
                        for (ParseObject vendor : list) {
                            if (vendor.getString("yelpId") == null) {
                                vendors.add(vendor);
                            } else {
                                String yelpId = vendor.getString("yelpId");
                                YelpBusinessSearchService yelpBizService = ServiceGenerator.createYelpBusinessSearchService();
                                yelpBizService.searchBusiness(yelpId, new Callback<Business>() {
                                    @Override
                                    public void success(Business business, Response response) {
                                        mAdapter.addYelpItem(business);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {

                                    }
                                });
                            }
                        }
                        mAdapter.addList(vendors);
                    }
                });
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
                        Object object = mAdapter.getItem(position);
                        if (object instanceof Business) {
                            Business business = (Business) mAdapter.getItem(position);
                            String businessId = business.getId();
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



    }

    @Override
    protected void onStart() {
        super.onStart();
        mButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                RemoveDialogFragment dialog = new RemoveDialogFragment();
                Bundle argument = new Bundle();
                argument.putString(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                dialog.setArguments(argument);
                dialog.show(manager, "Remove Friend");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mButtonRemove.setOnClickListener(null);
    }
}
