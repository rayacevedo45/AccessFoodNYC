package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class ReviewsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton mButtonReview;
    private RecyclerView mRecyclerView;
    private String objectId;
    private boolean isYelp;

    private String b1Name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        mButtonReview = (FloatingActionButton) rootView.findViewById(R.id.button_review);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_review);
        objectId = getArguments().getString(Constants.EXTRA_KEY_OBJECT_ID);
        isYelp = getArguments().getBoolean(Constants.EXTRA_KEY_IS_YELP);

        return rootView;

    }


    private void setUpListener(boolean isResumed) {
        if (isResumed) {
            mButtonReview.setOnClickListener(this);
        } else {
            mButtonReview.setOnClickListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpListener(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setUpListener(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_review:
                writeReview();
                break;
        }
    }

    private void writeReview() {
        final FragmentManager manager = getActivity().getSupportFragmentManager();
        if (isYelp) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
            query.whereEqualTo("yelpId", objectId).findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (list.size() == 0) {
                        ParseObject newVendor = new ParseObject("Vendor");
                        newVendor.put("yelpId", objectId);
                        // parseobject when do they create object id?
                        newVendor.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {


                            }
                        });
                    } else {
                        objectId = list.get(0).getObjectId();
                        ReviewDialogFragment dialog = new ReviewDialogFragment();
                        Bundle argument = new Bundle();
                        argument.putString(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                        dialog.setArguments(argument);
                        dialog.show(manager, "Review");
                    }
                }
            });
        } else {
            ReviewDialogFragment dialog = new ReviewDialogFragment();
            Bundle argument = new Bundle();
            argument.putString(Constants.EXTRA_KEY_OBJECT_ID, objectId);
            dialog.setArguments(argument);
            dialog.show(manager, "Review");

        }

    }

}
