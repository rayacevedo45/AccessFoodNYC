package rayacevedo45.c4q.nyc.accessfoodnyc;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

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

    private TextView mVendorAddText;
    private static String mAddress = "";

    private TextView mNeighborhoodText;
    private static String mNeighborhood;

    private TextView mCategoriesText;
    private static String mCategories;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        add = (Button) rootView.findViewById(R.id.button_add);

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
        Picasso.with(getActivity()).load(businessImgUrl).resize(1100, 700).into(mVendorPicImage);

        mVendorRatingImage = (ImageView)getActivity().findViewById(R.id.vendor_rating);
        String vendorRatingUrlLarge = business.getRatingImgUrlLarge();
        Picasso.with(getActivity()).load(vendorRatingUrlLarge).into(mVendorRatingImage);

        addressGenerator(business);
        mVendorAddText = (TextView)getActivity().findViewById(R.id.address);
        mVendorAddText.setText(mAddress);
        mNeighborhoodText = (TextView)getActivity().findViewById(R.id.neighborhood);
        mNeighborhoodText.setText(mNeighborhood);
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

    public static String addressGenerator (Business business){

        Location bizLocation = business.getLocation();
        List <String> addList = bizLocation.getDisplayAddress();
        int i = 0;
        while (i < addList.size()) {
            mAddress = mAddress + ", " + (addList.get(i));
            i++;
        }
        mNeighborhood = addList.get(2);
        return mAddress;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "finished onYelpData.");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<ParseObject> relation = user.getRelation("favorite");
                relation.add(selectedVendor);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }



}
