package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ReviewDialogFragment extends DialogFragment {

    private String objectId;
    private boolean isYelp;
    private RatingBar mRatingBar;
    private TextView mTextViewTitle;
    private TextView mTextViewRating;
    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private TextView mTextViewCounter;
    private View mDialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objectId = getArguments().getString(Constants.EXTRA_KEY_OBJECT_ID);
        isYelp = getArguments().getBoolean(Constants.EXTRA_KEY_IS_YELP);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_review, null);
        mRatingBar = (RatingBar) mDialogView.findViewById(R.id.ratingBar);
        mTextViewTitle = (TextView) mDialogView.findViewById(R.id.dialog_review_title);
        mTextViewRating = (TextView) mDialogView.findViewById(R.id.dialog_review_rating);
        mEditTextTitle = (EditText) mDialogView.findViewById(R.id.editText_dialog_title);
        mEditTextDescription = (EditText) mDialogView.findViewById(R.id.editText_dialog_description);
        mTextViewCounter = (TextView) mDialogView.findViewById(R.id.textView_counter);

        mEditTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remaining = 140 - s.length();
                mTextViewCounter.setText(remaining + "");
                if (remaining <= 0) {
                    mTextViewCounter.setTextColor(getResources().getColor(R.color.red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRatingBar.setStepSize(1);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String ratingText = "";
                if (rating == 1) {
                    ratingText = "I hate it";
                } else if (rating == 2) {
                    ratingText = "I don't like it";
                } else if (rating == 3) {
                    ratingText = "It's okay";
                } else if (rating == 4) {
                    ratingText = "I like it";
                } else {
                    ratingText = "I love it";
                }
                mTextViewRating.setText(ratingText);
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mDialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        

                        final String title = mEditTextTitle.getText().toString();
                        final String description = mEditTextDescription.getText().toString();
                        final int rating = Math.round(mRatingBar.getRating());

                        if (isYelp) {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
                            query.whereEqualTo("yelpId", objectId).getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    ParseObject review = new ParseObject("Review");
                                    review.put("title", title);
                                    review.put("description", description);
                                    review.put("rating", rating);
                                    review.put("writer", ParseUser.getCurrentUser());
                                    review.put("vendor", parseObject);
                                    review.saveInBackground();
                                }
                            });
                        } else {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
                            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    ParseObject review = new ParseObject("Review");
                                    review.put("title", title);
                                    review.put("description", description);
                                    review.put("rating", rating);
                                    review.put("writer", ParseUser.getCurrentUser());
                                    review.put("vendor", parseObject);
                                    review.saveInBackground();
                                }
                            });
                        }
                    }
                });

        return builder.create();
    }


}
