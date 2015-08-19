package rayacevedo45.c4q.nyc.accessfoodnyc;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsFragment extends Fragment {
    ParseObject vendor = new ParseObject("Vendor");
    String category;
    static String vendorName;
    URL vendorPicUrl;
    String vendorPicUrlStr;
    TextView categoryText;
    TextView vendorNameText;
    ImageView vendorPicImage;

    private Button add;
    private ParseObject selectedVendor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        vendorNameText = (TextView)rootView.findViewById(R.id.vendor_name);
        categoryText = (TextView)rootView.findViewById(R.id.category);
        vendorPicImage = (ImageView)rootView.findViewById(R.id.vendor_pic);
        add = (Button) rootView.findViewById(R.id.button_add);
        return rootView;
    }




    @Override
    public void onResume() {
        super.onResume();

        getLatestPosts();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject userObject, ParseException e) {
                        if (e == null) {
//                            List<ParseObject> list = new ArrayList<ParseObject>();
//                            list.add(selectedVendor);
//                            userObject.put("favorites", list);
                            userObject.addUnique("favorites", Arrays.asList(selectedVendor));
                            //userObject.add("favorites", selectedVendor);
                            userObject.saveInBackground();
//                            userObject.saveInBackground(new SaveCallback() {
//                                @Override
//                                public void done(ParseException e) {
//
//                                    if (e == null) {
//                                        Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                                        startActivity(intent);
//                                    } else {
//                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
//
//                                    }
//
//                                }
//                            });
                        }
                    }
                });
            }
        });

    }

    protected void getLatestPosts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
        query.getInBackground(MapsActivity.objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject vendor, ParseException e) {
                if (e == null) {
                    selectedVendor = vendor;
                    vendorName = vendor.getString("vendor_name");
                    vendorNameText.setText("Name: "+vendorName);
                    category = vendor.getString("category");
                    categoryText.setText("Category: "+category);
                    vendorPicUrlStr = vendor.getString("picture_url");
                    Picasso.with(getActivity()).load(vendorPicUrlStr).centerCrop().resize(1100, 700).into(vendorPicImage);

                } else {
                    // something went wrong
                }
            }
        });

    }

}
