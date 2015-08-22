package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

public class MenuFragment extends Fragment {

    ParseObject vendor = new ParseObject("Vendor");
    ImageView menuImage;
    String menuPicUrlStr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        menuImage = (ImageView)rootView.findViewById(R.id.menu_pic);
        return rootView;


    }



    @Override
    public void onResume() {
        super.onResume();

        getLatestPosts();

    }

    protected void getLatestPosts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
        query.getInBackground(MapsActivity.businessId, new GetCallback<ParseObject>() {
            public void done(ParseObject vendor, ParseException e) {
                if (e == null) {
                    menuPicUrlStr = vendor.getString("menu_url");
                    Picasso.with(getActivity()).load(menuPicUrlStr).into(menuImage);

                } else {
                    // something went wrong
                }
            }
        });

    }
}
