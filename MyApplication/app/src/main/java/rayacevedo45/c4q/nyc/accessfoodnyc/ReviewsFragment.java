package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;


public class ReviewsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

//        APIStaticInfo api_keys = new APIStaticInfo();
//
//        Yelp yelp = new Yelp(api_keys.getYelpConsumerKey(), api_keys.getYelpConsumerSecret(),
//                api_keys.getYelpToken(), api_keys.getYelpTokenSecret());
//        String response = yelp.search("restaurants", 40.731889, -73.984369);
//
//        YelpParser yParser = new YelpParser();
//        yParser.setResponse(response);
//        try {
//            yParser.parseBusiness();
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            //Do whatever you want with the error, like throw a Toast error report
//        }
//
//        int i = 0;
//        String mobile_url = yParser.getBusinessMobileURL(i);
//        String rating_url = yParser.getRatingURL(i);
//        String b_name = yParser.getBusinessName(i);
//        Bundle tmpBundle = yParser.getYelpBundle();
//        ArrayList<String> tmpKeys = yParser.getBundleKeys();
//
//        Toast.makeText(getActivity(), b_name, Toast.LENGTH_SHORT).show();
//    }
}
