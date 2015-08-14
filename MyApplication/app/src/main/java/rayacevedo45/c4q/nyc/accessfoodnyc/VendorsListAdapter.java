package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VendorsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ParseObject> mList;

    public VendorsListAdapter(Context context, List<ParseObject> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ParseObject getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_vendor, parent, false);
        }

        ParseObject item = getItem(position);

        ImageView photo = (ImageView) convertView.findViewById(R.id.imageView_vendor);
        TextView name = (TextView) convertView.findViewById(R.id.vendor_name);
        TextView rating = (TextView) convertView.findViewById(R.id.vendor_rating);

        Picasso.with(mContext).load((String) item.get("picture_url")).centerCrop().resize(200, 200).into(photo);
        name.setText(Integer.toString(position + 1) + ". " + (String) item.get("vendor_name"));
        rating.setText(Integer.toString((Integer) item.get("rating")));


        return convertView;
    }
}
