package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.ServiceGenerator;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpBusinessSearchService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static rayacevedo45.c4q.nyc.accessfoodnyc.MapsActivity.businessId;


public class VendorInfoActivity extends AppCompatActivity implements ActionBar.TabListener {

    // Tab titles
    private static final String[] TABS = { "Details", "Menu", "Reviews" };

    public static ParseApplication sApplication;

    private DetailsFragment mCurrentDetailsFragment;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    ViewPagerAdapter mViewPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    // Tab titles
    private String[] tabs = { "Details", "Menu", "Reviews" };
    String vendorName;
    private String objectId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_vendor_info);

        objectId = getIntent().getStringExtra(Constants.EXTRA_KEY_OBJECT_ID);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        //mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        mToolbar = (Toolbar) findViewById(R.id.toolbar_vendor);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);


        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                mToolbar.setSelectedNavigationItem(position);
            }
        });


        for (String tab_name : TABS) {

            mToolbar.addTab(mToolbar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        YelpBusinessSearchService yelpBizService = ServiceGenerator.createYelpBusinessSearchService();
        yelpBizService.searchBusiness(businessId, new YelpBusinessSearchCallback());

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.accent_material_light)), "CAT");
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.ripple_material_light)), "DOG");
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.button_material_dark)), "MOUSE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    mCurrentDetailsFragment = new DetailsFragment();
                    // Detail fragment activity

                    return mCurrentDetailsFragment;
                case 1:
                    // Menu fragment activity
                    return new MenuFragment();
                case 2:

                    fragment = new ReviewsFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                    fragment.setArguments(bundle);
                    return new ReviewsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }



    protected class YelpBusinessSearchCallback implements Callback<Business> {

        public String TAG = "YelpBusinessSearchCallback";

        @Override
        public void success(Business business, Response response) {
            Log.d(TAG, "Success");

            if (business != null) {
                mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

                mViewPager.setAdapter(mViewPagerAdapter);
                mViewPager.setOffscreenPageLimit(3);

                mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between different app sections, select the corresponding tab.
                        // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                        // Tab.
                        mToolbar.setSelectedNavigationItem(position);
                    }
                });
//                mCurrentDetailsFragment.onYelpData(business);

                if (mCurrentDetailsFragment != null) {

                    mCurrentDetailsFragment.onYelpData(business);
                } else {
                    Log.d("YelpDataGenerator", "mCurrentDetailsFragment was null!!!!");
                }
            }

        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(TAG, error.getMessage());
        }
    }


}
