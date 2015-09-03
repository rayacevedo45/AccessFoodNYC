package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;
import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Coordinate;


public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.VendorViewHolder> {

    private Context mContext;
    private List<Object> mList;
    private ParseGeoPoint mPoint;

    public VendorListAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public VendorListAdapter(Context context, ParseGeoPoint point, List<ParseObject> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
        mPoint = point;
    }

    public VendorListAdapter(Context context, List<ParseObject> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }

    public VendorListAdapter(Context context, List<Business> yelpList, List<ParseObject> ourList) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(ourList);
        mList.addAll(yelpList);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void addYelpList(List<Business> list) {
        mList.addAll(list);
        notifyItemRangeInserted(mList.size() - 1, list.size());
        notifyItemRangeChanged(mList.size()-1, list.size());
    }

    public void addList(List<ParseObject> list) {
        mList.addAll(list);
        notifyItemRangeInserted(mList.size()-1, list.size());
        notifyItemRangeChanged(mList.size()-1, list.size());
    }

    public void addYelpItem(Business business) {
        mList.add(business);
        notifyItemInserted(mList.size()-1);
        notifyItemChanged(mList.size()-1);
    }

    public Object getItem(int position) {
        return mList.get(position);
    }


    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.list_item_vendor, parent, false);
        return new VendorViewHolder(row);
    }

    @Override
    public void onBindViewHolder(VendorViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.0");
        Object object = mList.get(position);

        if (object instanceof Business) {
            holder.icon.setVisibility(View.GONE);
            holder.hour.setVisibility(View.GONE);
            Business business = (Business) object;

            Coordinate coordinate = business.getLocation().getCoordinate();
            ParseGeoPoint point = new ParseGeoPoint(coordinate.getLatitude(), coordinate.getLongitude());
            double distance = mPoint.distanceInMilesTo(point);
            holder.distance.setText(df.format(distance) + " miles away");

            String businessImgUrl = (business.getImageUrl());
            Picasso.with(mContext).load(businessImgUrl).centerCrop().resize(250, 250).into(holder.thumbnail);

            holder.ratingImage.setVisibility(View.VISIBLE);
            String ratingImgUrl = (business.getRatingImgUrl());
            Picasso.with(mContext).load(ratingImgUrl).into(holder.ratingImage);

            List<String> address = DetailsFragment.addressGenerator(business);
            holder.name.setText(business.getName());
            holder.address.setText(address.get(0) + ", " + address.get(1));
            holder.yelpLogo.setVisibility(View.VISIBLE);
        } else {
            ParseObject vendor = (ParseObject) object;

            holder.yelpLogo.setVisibility(View.GONE);
            holder.ratingImage.setVisibility(View.GONE);
            holder.hour.setVisibility(View.VISIBLE);

            ParseGeoPoint point = vendor.getParseGeoPoint("location");
            double distance = mPoint.distanceInMilesTo(point);
            holder.distance.setText(df.format(distance) + " miles away");

            Number rate = vendor.getNumber("rating");
            double r = rate.doubleValue();
            String result = df.format(r);
            holder.rating.setText(result);
            holder.address.setText(vendor.getString("address"));

            if (vendor.get("profile_url") != null) {
                Picasso.with(mContext).load(vendor.getString("profile_url")).centerCrop().resize(250, 250).into(holder.thumbnail);
            }
            holder.name.setText(vendor.getString("name"));

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            String today = "day" + Integer.toString(day);
            String json = vendor.getString(today);

            if (!json.equals("closed")) {
                try {
                    JSONObject info = new JSONObject(json);

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
                    String minutes = close.getMinutes() + "";
                    if (now.after(open) && now.before(close)) {
                        if (close.getMinutes() == 0) {
                            minutes = "00";
                        }
                        if (close.getHours() > 12) {
                            holder.hour.setText("Open until " + (close.getHours() - 12) + ":" + minutes + " PM");
                            holder.icon.setVisibility(View.VISIBLE);
                        } else {
                            holder.hour.setText("Open until " + close.getHours() + ":" + minutes + " AM");
                            holder.icon.setVisibility(View.VISIBLE);
                        }

                    } else {
                        holder.hour.setText("Closed now");
                        holder.icon.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else {
                holder.hour.setText("Closed today");
                holder.icon.setVisibility(View.VISIBLE);
            }


        }


    }

    public static class VendorViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thumbnail;
        protected TextView name;
        protected ImageView ratingImage;
        protected TextView address;
        protected ImageView yelpLogo;
        protected TextView hour;
        protected TextView rating;
        protected ImageView icon;
        protected TextView distance;

        public VendorViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView_vendor);
            name = (TextView) itemView.findViewById(R.id.vendor_name);
            ratingImage = (ImageView) itemView.findViewById(R.id.vendor_rating_img);
            address = (TextView) itemView.findViewById(R.id.textView_address);
            yelpLogo = (ImageView) itemView.findViewById(R.id.yelp_logo);
            hour = (TextView) itemView.findViewById(R.id.textView_hour);
            rating = (TextView) itemView.findViewById(R.id.maps_vendor_rating);
            icon = (ImageView) itemView.findViewById(R.id.schedule_icon);
            distance = (TextView) itemView.findViewById(R.id.maps_distance);
        }


    }
}
