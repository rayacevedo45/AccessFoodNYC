package rayacevedo45.c4q.nyc.accessfoodnyc;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Location;


public class DetailsFragment extends Fragment {
    private static final String TAG = DetailsFragment.class.getName();

    private TextView mVendorNameText;
    private ImageView mVendorPicImage;
    private ImageView mVendorRatingImage;
    private Button add;
    private ParseObject selectedVendor;

    private static List <String> addList;

    private TextView mCategoriesText;
    private static String mCategories;

    private TextView mPhoneText;
    private TextView mSnippetText;

    private static String mId;

    private RecyclerView mRecyclerViewReview;
    private ReviewAdapter mAdapter;

    private boolean isYelp;
    private String objectId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        add = (Button) rootView.findViewById(R.id.button_add);
        mRecyclerViewReview = (RecyclerView) rootView.findViewById(R.id.recyclerView_friends_review);
        //mRecyclerViewReview.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewReview.setLayoutManager(lm);

        objectId = getArguments().getString(Constants.EXTRA_KEY_OBJECT_ID);
        isYelp = getArguments().getBoolean(Constants.EXTRA_KEY_IS_YELP);

        ParseUser user = ParseUser.getCurrentUser();
        final ParseRelation<ParseUser> relation = user.getRelation("friends");
        if (isYelp) {
            ParseQuery<ParseObject> findVendor = ParseQuery.getQuery("Vendor");
            findVendor.whereEqualTo("yelpId", objectId).findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (list.size() != 0) {
                        final ParseObject vendor = list.get(0);
                        relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                if (list.size() != 0) {
                                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Review");
                                    query1.include("writer");
                                    query1.whereEqualTo("vendor", vendor).whereContainedIn("writer", list);
                                    query1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (list.size() != 0) {
                                                mAdapter = new ReviewAdapter(getActivity(), list);
                                                mRecyclerViewReview.setAdapter(mAdapter);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        } else {
            relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {

                    ParseQuery<ParseObject> findVendorQuery = ParseQuery.getQuery("Vendor");
                    findVendorQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject vendor, ParseException e) {
                            relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> list, ParseException e) {
                                    if (list.size() != 0) {
                                        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Review");
                                        query1.include("writer");
                                        query1.whereEqualTo("vendor", vendor).whereContainedIn("writer", list);
                                        query1.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> list, ParseException e) {
                                                if (list.size() != 0) {
                                                    mAdapter = new ReviewAdapter(getActivity(), list);
                                                    mRecyclerViewReview.setAdapter(mAdapter);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });

                }
            });
        }

        if (!isYelp) {
            mVendorPicImage = (ImageView) rootView.findViewById(R.id.vendor_pic);
            mVendorNameText = (TextView) rootView.findViewById(R.id.vendor_name);
            mSnippetText = (TextView) rootView.findViewById(R.id.snippet_text);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject vendor, ParseException e) {
                    mVendorNameText.setText(vendor.getString("name"));
                    mSnippetText.setText(vendor.getString("description"));
                    Picasso.with(getActivity()).load(vendor.getString("profile_url")).centerCrop().resize(350, 350).noFade().into(mVendorPicImage);
                }
            });
        }




        return rootView;
    }

    public void onYelpData(Business business) {
        Log.d(TAG, "inside onYelpData.");
        mVendorNameText = (TextView)getActivity().findViewById(R.id.vendor_name);
        mVendorNameText.setText(business.getName());

        mVendorNameText = (TextView)getActivity().findViewById(R.id.category);
        catListIterator(business);
        mVendorNameText.setText(mCategories);

        mVendorPicImage = (ImageView)getActivity().findViewById(R.id.vendor_pic);
        String businessImgUrl = (business.getImageUrl());
        Picasso.with(getActivity()).load(businessImgUrl).centerCrop().resize(350, 350).noFade().into(mVendorPicImage);

        mVendorRatingImage = (ImageView)getActivity().findViewById(R.id.vendor_rating);
        String vendorRatingUrlLarge = business.getRatingImgUrlLarge();
        Picasso.with(getActivity()).load(vendorRatingUrlLarge).into(mVendorRatingImage);

//        mVendorRatingNum = (TextView)getActivity().findViewById(R.id.rating_num);
//        mVendorRatingNum.setText(String.valueOf(business.getRating()));

        addressGenerator(business);
        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.address);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for( int i = 0; i < addList.size(); i++ )
        {
            TextView textView = new TextView(getActivity());
            textView.setText(addList.get(i));
            linearLayout.addView(textView);
        }

        mPhoneText = (TextView)getActivity().findViewById(R.id.phone);
        mPhoneText.setText(business.getDisplayPhone());

        mSnippetText = (TextView)getActivity().findViewById(R.id.snippet_text);
        mPhoneText.setText(business.getSnippetText());

        mId = business.getId();



    }

    public static String catListIterator (Business business){
        List<List<String>> catListOfLists = business.getCategories();
        int i = 0;
        List<String> catList = null;
        mCategories = "";

        while (i < catListOfLists.size()) {
            catList = catListOfLists.get(i);
            mCategories= mCategories + " " + (catList.get(0));
            i++;
        }

        return mCategories;
    }

    public static List<String> addressGenerator(Business business){

        Location bizLocation = business.getLocation();
        addList = bizLocation.getDisplayAddress();

        return addList;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "finished onYelpData.");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser user = ParseUser.getCurrentUser();
                final ParseRelation<ParseObject> relation = user.getRelation("favorite");

                //check if the yelpID is already in parse.com or not

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
                query.whereStartsWith("yelpId", mId);

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (e == null) {
                            //object exists
                            selectedVendor = object;
                            relation.add(selectedVendor);

                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        ParsePush.subscribeInBackground(object.getObjectId());
                                        Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                //object doesn't exist

                                //add yelpID as new vendor in parse.com
                                selectedVendor = new ParseObject("Vendor");
                                selectedVendor.put("yelpId", mId);
                                selectedVendor.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        relation.add(selectedVendor);

                                        user.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    ParsePush.subscribeInBackground(selectedVendor.getObjectId());
                                                    Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                //unknown error, debug
                            }
                        }
                    }
                });
            }
        });
    }
}
