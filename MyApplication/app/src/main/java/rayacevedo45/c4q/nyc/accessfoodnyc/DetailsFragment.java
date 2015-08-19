package rayacevedo45.c4q.nyc.accessfoodnyc;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailsFragment extends Fragment {
    ParseObject vendor = new ParseObject("Vendor");
    String category;
    static String vendorName;
    URL vendorPicUrl;
    String vendorPicUrlStr;
    TextView categoryText;
    TextView vendorNameText;
    ImageView vendorPicImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        vendorNameText = (TextView)rootView.findViewById(R.id.vendor_name);
        categoryText = (TextView)rootView.findViewById(R.id.category);
        vendorPicImage = (ImageView)rootView.findViewById(R.id.vendor_pic);
        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();
        getLatestPosts();

    }

    protected void getLatestPosts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
        query.getInBackground(MapsActivity.objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject vendor, ParseException e) {
                if (e == null) {
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
