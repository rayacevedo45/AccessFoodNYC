package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class UserReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ParseObject> mList;

    public UserReviewAdapter(Context context, List<ParseObject> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        ParseObject review = getItem(position);
        ParseObject vendor = review.getParseObject("vendor");
        if (vendor.getString("yelpId") == null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row;
        switch (viewType) {
            case 1:
                row = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_review, parent, false);
                return new UserReviewHolder(row);
            case 2:
                row = LayoutInflater.from(mContext).inflate(R.layout.list_item_yelp_review, parent, false);
                return new YelpReviewHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof YelpReviewHolder) {
            onBindYelpViewHolder((YelpReviewHolder) holder, position);
        } else {
            onBindUserViewHolder((UserReviewHolder) holder, position);
        }
    }

    public ParseObject getItem(int position) {
        return mList.get(position);
    }

    public void onBindYelpViewHolder(YelpReviewHolder holder, int position) {
        ParseObject review = getItem(position);
        ParseObject vendor = review.getParseObject("vendor");
        String yelpId = vendor.getString("yelpId");
        String yelpVendorName = yelpId.replaceAll("-", " ");
        yelpVendorName = Character.toString(yelpVendorName.charAt(0)).toUpperCase() + yelpVendorName.substring(1);
        holder.vendor.setText(yelpVendorName);

        Date uploadDate = review.getCreatedAt();
        holder.date.setText(uploadDate.getMonth() + "/" + uploadDate.getDay() + "/" + uploadDate.getYear());

        holder.title.setText(review.getString("title"));
        holder.description.setText(review.getString("description"));

        ParseUser user = review.getParseUser("writer");
        try {
            Picasso.with(mContext).load(user.getString("profile_url")).resize(200, 200).centerCrop().into(holder.picture);
        } catch (Exception e) {
            Picasso.with(mContext).load(R.drawable.default_profile).resize(200, 200).centerCrop().into(holder.picture);
        }

        int rating = review.getInt("rating");
        switch (rating) {
            case 1:
                holder.grade2.setVisibility(View.GONE);
            case 2:
                holder.grade3.setVisibility(View.GONE);
            case 3:
                holder.grade4.setVisibility(View.GONE);
            case 4:
                holder.grade5.setVisibility(View.GONE);
        }
    }

    public void onBindUserViewHolder(UserReviewHolder holder, int position) {
        ParseObject review = getItem(position);

        ParseObject vendor = review.getParseObject("vendor");

        holder.vendorName.setText(vendor.getString("name"));
        Picasso.with(mContext).load(vendor.getString("profile_url")).centerCrop().resize(200, 200).into(holder.vendorPicture);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String today = "day" + Integer.toString(day);
        String json = vendor.getString(today);
        try {
            JSONObject info = new JSONObject(json);
            holder.address.setText(info.getString("address"));
            if (info.getBoolean("isOpen")) {

                String opening = info.getString("openAt");
                String closing = info.getString("closeAt");

                long current = System.currentTimeMillis();
                Date open = new Date(current);
                Date close = new Date(current);
                Date now = new Date(current);
                open.setHours(Integer.valueOf(opening.substring(0,2)));
                open.setMinutes(Integer.valueOf(opening.substring(2)));
                close.setHours(Integer.valueOf(closing.substring(0,2)));
                close.setMinutes(Integer.valueOf(closing.substring(2)));

                if (now.after(open) && now.before(close)) {
                    holder.hour.setText("Open until " + close.getHours() + ":" + close.getMinutes());
                } else {
                    holder.hour.setText("Closed now");
                }

            } else {
                holder.hour.setText("Closed now");
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        holder.title.setText(review.getString("title"));
        holder.description.setText(review.getString("description"));

        Date uploadDate = review.getCreatedAt();
        holder.date.setText(uploadDate.getMonth() + "/" + uploadDate.getDay() + "/" + uploadDate.getYear());

        ParseUser user = review.getParseUser("writer");
        try {
            Picasso.with(mContext).load(user.getString("profile_url")).resize(100, 100).centerCrop().into(holder.writerPicture);
        } catch (Exception e) {
            Picasso.with(mContext).load(R.drawable.default_profile).resize(100, 100).centerCrop().into(holder.writerPicture);
        }

        int rating = review.getInt("rating");
        switch (rating) {
            case 1:
                holder.grade2.setVisibility(View.GONE);
            case 2:
                holder.grade3.setVisibility(View.GONE);
            case 3:
                holder.grade4.setVisibility(View.GONE);
            case 4:
                holder.grade5.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class YelpReviewHolder extends RecyclerView.ViewHolder {
        protected TextView vendor;

        protected ImageView picture;
        protected TextView writer;
        protected TextView date;
        protected TextView title;
        protected TextView description;

        protected ImageView grade1;
        protected ImageView grade2;
        protected ImageView grade3;
        protected ImageView grade4;
        protected ImageView grade5;

        public YelpReviewHolder(View itemView) {
            super(itemView);
            grade1 = (ImageView) itemView.findViewById(R.id.grade_1);
            grade2 = (ImageView) itemView.findViewById(R.id.grade_2);
            grade3 = (ImageView) itemView.findViewById(R.id.grade_3);
            grade4 = (ImageView) itemView.findViewById(R.id.grade_4);
            grade5 = (ImageView) itemView.findViewById(R.id.grade_5);

            picture = (ImageView) itemView.findViewById(R.id.yelp_review_user_picture);
            vendor = (TextView) itemView.findViewById(R.id.yelp_review_vendor);
            writer = (TextView) itemView.findViewById(R.id.yelp_review_writer);
            date = (TextView) itemView.findViewById(R.id.yelp_review_date);
            title = (TextView) itemView.findViewById(R.id.yelp_review_title);
            description = (TextView) itemView.findViewById(R.id.yelp_review_description);
        }
    }

    public static class UserReviewHolder extends RecyclerView.ViewHolder {

        protected ImageView vendorPicture;
        protected TextView vendorName;
        protected TextView address;
        protected TextView rating;
        protected TextView hour;

        protected ImageView writerPicture;
        protected TextView writer;
        protected TextView date;
        protected TextView title;
        protected TextView description;

        protected ImageView grade1;
        protected ImageView grade2;
        protected ImageView grade3;
        protected ImageView grade4;
        protected ImageView grade5;

        public UserReviewHolder(View itemView) {
            super(itemView);
            grade1 = (ImageView) itemView.findViewById(R.id.grade_1);
            grade2 = (ImageView) itemView.findViewById(R.id.grade_2);
            grade3 = (ImageView) itemView.findViewById(R.id.grade_3);
            grade4 = (ImageView) itemView.findViewById(R.id.grade_4);
            grade5 = (ImageView) itemView.findViewById(R.id.grade_5);

            writer = (TextView) itemView.findViewById(R.id.user_review_writer);
            date = (TextView) itemView.findViewById(R.id.user_review_date);
            title = (TextView) itemView.findViewById(R.id.user_review_title);
            description = (TextView) itemView.findViewById(R.id.user_review_description);

            writerPicture = (ImageView) itemView.findViewById(R.id.user_review_user_picture);
            vendorName = (TextView) itemView.findViewById(R.id.user_review_vendor);
            address = (TextView) itemView.findViewById(R.id.user_review_vendor_address);
            rating = (TextView) itemView.findViewById(R.id.user_review_rating);
            hour = (TextView) itemView.findViewById(R.id.user_review_vendor_hour);
            vendorPicture = (ImageView) itemView.findViewById(R.id.user_review_vendor_picture);
        }
    }
}
