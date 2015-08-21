package nyc.c4q.take2.accessfood;


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

import nyc.c4q.take2.accessfood.yelp.Business;


public class DetailsFragment extends Fragment {
    private static final String TAG = DetailsFragment.class.getName();

    private TextView mVendorNameText;
    private ImageView mVendorPicImage;
    private ImageView mVendorRatingImage;
    private Button add;
    private ParseObject selectedVendor;


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

        mVendorPicImage = (ImageView)getActivity().findViewById(R.id.vendor_pic);
        String businessImgUrl = (business.getImageUrl());
        Picasso.with(getActivity()).load(businessImgUrl).resize(1100, 700).into(mVendorPicImage);

        mVendorRatingImage = (ImageView) getActivity().findViewById(R.id.vendor_ratings);
        String vendorRatingUrlLarge = business.getRatingImgUrlLarge();
        Picasso.with(getActivity()).load(vendorRatingUrlLarge).into(mVendorRatingImage);


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
