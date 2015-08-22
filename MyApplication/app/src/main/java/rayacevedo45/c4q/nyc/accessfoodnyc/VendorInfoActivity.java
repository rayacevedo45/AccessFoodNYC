package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.ServiceGenerator;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.service.YelpBusinessSearchService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class VendorInfoActivity extends FragmentActivity implements ActionBar.TabListener {

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
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        YelpSearchService yelpService = ServiceGenerator.createYelpSearchService();
//        yelpService.searchFoodCarts("40.686406, -73.981440", new MapsActivity.YelpSearchCallback());

    YelpBusinessSearchService yelpBizService = ServiceGenerator.createYelpBusinessSearchService();
        yelpBizService.searchBusiness(MapsActivity.businessId, new YelpBusinessSearchCallback());


        setContentView(R.layout.activity_vendor_info);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);




        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });


        for (String tab_name : TABS) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

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
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    mCurrentDetailsFragment = new DetailsFragment();
                    // Top Rated fragment activity
                    return mCurrentDetailsFragment;
                case 1:
                    // Menu fragment activity
                    return new MenuFragment();
                case 2:
                    // Reviews fragment activity
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


//    protected class YelpSearchCallback implements Callback<YelpResponse> {
//
//        public String TAG = "YelpSearchCallback";
//
//        @Override
//        public void success(YelpResponse data, Response response) {
//            Log.d(TAG, "Success");
//            sApplication = ParseApplication.getInstance();
//            sApplication.sYelpResponse = data;
//            List<Business> businessList = sApplication.sYelpResponse.getBusinesses();
//            if (businessList != null && businessList.size() > 0) {
//                if (mCurrentDetailsFragment != null) {
//                    mCurrentDetailsFragment.onYelpData(businessList.get(0));
//                } else {
//                    Log.d("YelpDataGenerator", "mCurrentDetailsFragment was null!!!!");
//                }
//            }
//        }
//
//        @Override
//        public void failure(RetrofitError error) {
//            Log.e(TAG, error.getMessage());
//        }
//    }

    protected class YelpBusinessSearchCallback implements Callback<Business> {

        public String TAG = "YelpBusinessSearchCallback";

        @Override
        public void success(Business business, Response response) {
            Log.d(TAG, "Success");
//            sApplication = ParseApplication.getInstance();
//            sApplication.sYelpResponse = business;
//            business = sApplication.sYelpResponse;
            if (business != null ) {
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
