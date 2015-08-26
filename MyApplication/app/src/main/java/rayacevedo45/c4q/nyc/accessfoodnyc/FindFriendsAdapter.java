package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FindFriendsAdapter extends RecyclerView.Adapter<FindFriendsAdapter.FindFriendsViewHolder> {

    private Context mContext;
    private List<Friend> mList;


    public FindFriendsAdapter(Context mContext, List<Friend> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public FindFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FindFriendsViewHolder holder, int position) {

        Friend friend = mList.get(position);

        Picasso.with(mContext).load(friend.getThumbnailUrl()).resize(200, 200).centerCrop().into(holder.thumbnail);
        holder.name.setText(friend.getName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView name;
        private Button request;

        public FindFriendsViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView_friend);
            name = (TextView) itemView.findViewById(R.id.name_friend);
            request = (Button) itemView.findViewById(R.id.button_add);
        }


    }
}
