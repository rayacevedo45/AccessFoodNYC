package rayacevedo45.c4q.nyc.accessfoodnyc;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoScrollAdapter<T> {

    private Context mContext;
    private LayoutInflater mInflater;
    private LinearLayout mParentLayout;
    private List<T> mList;

    public NoScrollAdapter(Context mContext, LinearLayout parentLayout) {
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mParentLayout = parentLayout;

    }

    public void addReviews(List<T> list) {
        mList = list;

        for (int i = 0; i < mList.size(); i++) {
            final View row = mInflater.inflate(R.layout.list_item_review, null);

            ParseObject review = (ParseObject) mList.get(i);
            ParseUser writer = review.getParseUser("writer");

            ImageView picture = (ImageView) row.findViewById(R.id.imageView_review_profile);
            TextView title = (TextView) row.findViewById(R.id.textView_review_title);
            TextView description = (TextView) row.findViewById(R.id.textView_review_description);
            TextView reviewer = (TextView) row.findViewById(R.id.textView_review_name);
            TextView date = (TextView) row.findViewById(R.id.textView_review_date);

            try {
                Picasso.with(mContext).load(writer.getString("profile_url")).into(picture);
            } catch (Exception e) {
                Picasso.with(mContext).load(R.drawable.default_profile).into(picture);
            }

            reviewer.setText(writer.getString("first_name") + " " + writer.getString("last_name"));

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dates = dateFormat.format(review.getCreatedAt());
            date.setText(dates);

            title.setText(review.getString("title"));
            description.setText(review.getString("description"));

            mParentLayout.addView(row, i);
        }
    }
}
