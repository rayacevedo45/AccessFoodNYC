package nyc.c4q.take2.accessfood;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;


public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.VendorViewHolder> {

    private Context mContext;
    private List<ParseObject> mList;

    public VendorListAdapter(Context context, List<ParseObject> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public ParseObject getItem(int position) {
        return mList.get(position);
    }

    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.list_item_vendor, parent, false);
        return new VendorViewHolder(row);
    }

    @Override
    public void onBindViewHolder(VendorViewHolder vendorViewHolder, int position) {
        ParseObject item = mList.get(position);
        Picasso.with(mContext).load((String) item.get("picture_url")).centerCrop().resize(250, 250).into(vendorViewHolder.thumbnail);
        vendorViewHolder.name.setText(Integer.toString(position + 1) + ". " + (String) item.get("vendor_name"));
        vendorViewHolder.rating.setText(Integer.toString((Integer) item.get("rating")));

    }

    public static class VendorViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thumbnail;
        protected TextView rating;
        protected TextView name;

        public VendorViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView_vendor);
            rating = (TextView) itemView.findViewById(R.id.vendor_rating);
            name = (TextView) itemView.findViewById(R.id.vendor_name);
        }

    }
}
