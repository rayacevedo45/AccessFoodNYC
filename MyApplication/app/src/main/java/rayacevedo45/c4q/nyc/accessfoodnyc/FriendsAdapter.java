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

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private Context mContext;
    private List<ParseUser> mList;

    public FriendsAdapter(Context context, List<ParseUser> friendsList) {

        mContext = context;
        mList = friendsList;

    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.list_item_friend, parent, false);
        return new FriendsViewHolder(row);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {

        ParseObject item = mList.get(position);

        Picasso.with(mContext).load(item.getString("profile_url")).resize(200, 200).centerCrop().into(holder.picture);
        holder.friendName.setText(item.getString("first_name") + " " + item.getString("last_name"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        protected ImageView picture;
        protected TextView friendName;


        public FriendsViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.imageView_fr);
            friendName = (TextView) itemView.findViewById(R.id.textView_name_friend);
        }
    }
}

