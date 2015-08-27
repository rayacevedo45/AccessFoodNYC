package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsAdapter extends RecyclerView.Adapter<FindFriendsAdapter.FindFriendsViewHolder> {

    private Context mContext;
    private List<Friend> mList;
    private List<Friend> mFindFriendsList;
    private List<ParseUser> mCurrentFriendsList;


    public FindFriendsAdapter(Context mContext, List<Friend> friendslist, List<ParseUser> list) {
        this.mContext = mContext;
        this.mFindFriendsList = friendslist;
        this.mCurrentFriendsList = list;

        boolean isEqual = false;
        mList = new ArrayList<>();
        for (Friend friend : mFindFriendsList) {
            for (ParseUser user : mCurrentFriendsList) {
                if (friend.getId().equals(user.get("fbId"))) {
                    isEqual = true;
                    break;
                }
            }
            if (!isEqual) {
                mList.add(friend);
            }
            isEqual = false;
        }
    }

    @Override
    public FindFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.list_item_friends, parent, false);
        return new FindFriendsViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final FindFriendsViewHolder holder, int position) {

        final Friend friend = mList.get(position);

        if (friend.getThumbnailUrl().length() == 0) {
            Picasso.with(mContext).load(R.drawable.default_profile).resize(200, 200).centerCrop().into(holder.thumbnail);
        } else {
            Picasso.with(mContext).load(friend.getThumbnailUrl()).resize(200, 200).centerCrop().into(holder.thumbnail);
        }

        holder.name.setText(friend.getName());


        // TODO : check if this friend is in pending list.
        holder.request.setText("+ Add");



        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser user = ParseUser.getCurrentUser();
                String name = user.get("first_name") + " " + user.get("last_name");
                try {
                    JSONObject data = new JSONObject("{\"alert\": \"" + name + " wants to be your friend!" + "\"," +
                            "\"profile_url\": \"" + friend.getThumbnailUrl() + "\"," +
                            "\"objectId\": \"" + user.getObjectId() + "\"}");
                    Toast.makeText(mContext, "Friend request is sent to " + friend.getName(), Toast.LENGTH_SHORT).show();
                    holder.request.setText("sent");

                    ParseQuery query = ParseInstallation.getQuery();
                    query.whereEqualTo("fbId", friend.getId());
                    ParsePush push = new ParsePush();
                    push.setQuery(query);
                    push.setData(data);
                    push.sendInBackground();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

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
            request = (Button) itemView.findViewById(R.id.button_request);
        }


    }
}
