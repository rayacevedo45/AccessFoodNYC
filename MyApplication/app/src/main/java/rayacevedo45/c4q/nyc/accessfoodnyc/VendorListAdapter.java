package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.api.yelp.models.Business;


public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.VendorViewHolder> {

    private Context mContext;
//    private List<ParseObject> mList;
    private List<Business> mList;

    public VendorListAdapter(Context context, List<Business> list) {
        mContext = context;
        mList = list;

//        for (Business business : list) {
//            String yelpId = business.getId();
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Vendor");
//            query.whereContainsAll("")
//            query.whereEqualTo("yelpId", yelpId);
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> list, ParseException e) {
//                    if (list)
//                }
//            });
//            ParseObject newVendor = new ParseObject("Vendor");
//        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    public ParseObject getItem(int position) {
//        return mList.get(position);
//    }

    public Business getItem(int position) {
        return mList.get(position);
    }



    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.list_item_vendor, parent, false);
        return new VendorViewHolder(row);
    }

//    @Override
//    public void onBindViewHolder(VendorViewHolder vendorViewHolder, int position) {
//        ParseObject item = mList.get(position);
//        Picasso.with(mContext).load((String) item.get("picture_url")).centerCrop().resize(250, 250).into(vendorViewHolder.thumbnail);
//        vendorViewHolder.name.setText(Integer.toString(position + 1) + ". " + (String) item.get("vendor_name"));
//        vendorViewHolder.rating.setText(Integer.toString((Integer) item.get("rating")));
//
//    }

    @Override
    public void onBindViewHolder(VendorViewHolder holder, int position) {
        Business business = mList.get(position);
        String businessImgUrl = (business.getImageUrl());
        Picasso.with(mContext).load(businessImgUrl).centerCrop().resize(250, 250).into(holder.thumbnail);

        String ratingImgUrl = (business.getRatingImgUrl());
        Picasso.with(mContext).load(ratingImgUrl).into(holder.ratingImage);

        List<String> address = DetailsFragment.addressGenerator(business);
        holder.name.setText(Integer.toString(position + 1) + ". " + business.getName());
        holder.address.setText(address.get(0) + ", " + address.get(1));
    }

    public static class VendorViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thumbnail;
        protected TextView name;
        protected ImageView ratingImage;
        protected TextView address;

        public VendorViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView_vendor);
            name = (TextView) itemView.findViewById(R.id.vendor_name);
            ratingImage = (ImageView) itemView.findViewById(R.id.vendor_rating_img);
            address = (TextView) itemView.findViewById(R.id.textView_address);
        }


    }
}
