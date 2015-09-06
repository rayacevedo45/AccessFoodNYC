package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.ServiceGenerator;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpBusinessSearchService;
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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_user_favorite);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

//        mAdapter = new VendorListAdapter(getApplicationContext());
//        mRecyclerView.setAdapter(mAdapter);

        ParseUser user = ParseUser.getCurrentUser();
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
                                //mAdapter.addYelpItem(business);
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
}
