package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReviewsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton mButtonReview;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mAdapter;
    private String objectId;
    private boolean isYelp;

    private ImageView mImageViewUserFace;
    private TextView mTextViewName;
    private TextView mTextViewNoReview;

//    private ArrayList<Integer> ratingNums;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        mButtonReview = (FloatingActionButton) rootView.findViewById(R.id.button_review);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_review);
        mImageViewUserFace = (ImageView) rootView.findViewById(R.id.review_profile);
        mTextViewName = (TextView) rootView.findViewById(R.id.review_user_name);
        mTextViewNoReview = (TextView) rootView.findViewById(R.id.textView_no_review);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

        ParseUser user = ParseUser.getCurrentUser();

        Picasso.with(getActivity()).load(user.getString("profile_url")).resize(200,200).centerCrop().into(mImageViewUserFace);
        String name = user.getString("first_name") + " " + user.getString("last_name");
        mTextViewName.setText(name);

        objectId = getArguments().getString(Constants.EXTRA_KEY_OBJECT_ID);
        isYelp = getArguments().getBoolean(Constants.EXTRA_KEY_IS_YELP);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
        if (isYelp) {

            query.whereEqualTo("yelpId", objectId).findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (list.size() != 0) {
                        ParseObject vendor = list.get(0);
                        ParseQuery<ParseObject> reviewQuery = ParseQuery.getQuery("Review");
                        reviewQuery.include("writer");
                        reviewQuery.whereEqualTo("vendor", vendor).findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (list.size() == 0) {
                                    mTextViewNoReview.setText("No reviews yet :( \nWhy don't you write one?");
                                    mTextViewNoReview.setVisibility(View.VISIBLE);
                                } else {
                                    mAdapter = new ReviewAdapter(getActivity(), list);
                                    mRecyclerView.setAdapter(mAdapter);
                                }
                            }
                        });
                    }
                }
            });
        } else {
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject parseObject, ParseException e) {
                    ParseQuery<ParseObject> reviewQuery = ParseQuery.getQuery("Review");
                    reviewQuery.include("writer");
                    reviewQuery.whereEqualTo("vendor", parseObject).findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (list.size() == 0) {
                                mTextViewNoReview.setText("No reviews yet :( \nWhy don't you write one?");
                                mTextViewNoReview.setVisibility(View.VISIBLE);
                            } else {
                                mAdapter = new ReviewAdapter(getActivity(), list);
                                mRecyclerView.setAdapter(mAdapter);
                                double ratingsum = 0;
                                for (final ParseObject review : list) {
                                    ratingsum += (Integer) review.get("rating");
                                }
//                                Toast.makeText(getActivity(), String.valueOf(ratingsum), Toast.LENGTH_SHORT).show();
                                double averageRating = Math.round((ratingsum / (list.size())) * 10.0) / 10.0;

                                //Toast.makeText(getActivity(), String.valueOf(averageRating), Toast.LENGTH_SHORT).show();
                                parseObject.put("rating", averageRating);
                                parseObject.put("ratingCount", list.size());
                                parseObject.saveInBackground();
                            }
                        }
                    });


                }

            });


        }

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
                    ReviewDialogFragment dialog = new ReviewDialogFragment();
                    Bundle argument = new Bundle();
                    if (list.size() == 0) {
                        argument.putBoolean(Constants.EXTRA_KEY_IS_YELP, true);
                    } else {
                        objectId = list.get(0).getObjectId();
                        argument.putBoolean(Constants.EXTRA_KEY_IS_YELP, false);
                    }
                    argument.putString(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                    dialog.setArguments(argument);
                    dialog.show(manager, "Review");
                }
            });
        } else {
            ReviewDialogFragment dialog = new ReviewDialogFragment();
            Bundle argument = new Bundle();
            argument.putString(Constants.EXTRA_KEY_OBJECT_ID, objectId);
            argument.putBoolean(Constants.EXTRA_KEY_IS_YELP, false);
            dialog.setArguments(argument);
            dialog.show(manager, "Review");

//            ParseQuery<ParseObject> reviewquery = ParseQuery.getQuery("Review");
//            reviewquery.whereEqualTo("vendor", objectId);
//            reviewquery.countInBackground(new CountCallback() {
//                public void done(int count, ParseException e) {
//                    if (e == null) {
//                        final int rating = count;
//                        ParseQuery<ParseObject> vendorquery = ParseQuery.getQuery("Vendor");
//                        vendorquery.whereEqualTo("objectId", objectId);
//                        vendorquery.getInBackground(objectId, new GetCallback<ParseObject>(){
//                            public void done(ParseObject object, ParseException e) {
//                                if (e == null) {
//                                   object.put("ratingCount", rating);
//                                }
//                            }
//                        });
//
//                    }
//                }
//            });
        }
    }

}
