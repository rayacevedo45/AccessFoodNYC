package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.accounts.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mImageViewProfile;
    private TextView first;
    private TextView last;
    private TextView yf;

    //private Button maps;
    private Button mButtonLogOut;
    private Button mButtonFindFriends;

    private ListView mListView;
    private FavoriteAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String objectId = getIntent().getStringExtra(Constants.EXTRA_KEY_OBJECT_ID);

        if (objectId != null) {
            final ParseUser me = ParseUser.getCurrentUser();
            final ParseRelation<ParseUser> relation = me.getRelation("friends");

            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereEqualTo(Constants.EXTRA_KEY_OBJECT_ID, objectId);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    final ParseUser friend = list.get(0);
                    relation.add(friend);
                    me.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Accepted!", Toast.LENGTH_SHORT).show();


                    ParseUser user = ParseUser.getCurrentUser();
                    String name = user.get("first_name") + " " + user.get("last_name");
                    try {
                        JSONObject data = new JSONObject("{\"alert\": \"" + name + " accepted your friend request!" + "\"," +
                                "\"accepted\": \"" + user.getObjectId() + "\"}");

                        ParseQuery query = ParseInstallation.getQuery();
                        query.whereEqualTo("user", friend);
                        ParsePush push = new ParsePush();
                        push.setQuery(query);
                        push.setData(data);
                        push.sendInBackground();

                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }



//                    final ParseRelation<ParseUser> relation1 = friend.getRelation("friends");
//                    ParseQuery<ParseUser> query1 = ParseQuery.getQuery("_User");
//                    query1.whereEqualTo(Constants.EXTRA_KEY_OBJECT_ID, me.getObjectId());
//                    query1.findInBackground(new FindCallback<ParseUser>() {
//                        @Override
//                        public void done(List<ParseUser> list, ParseException e) {
//                            ParseUser me2 = list.get(0);
//                            relation1.add(me2);
//                            friend.saveInBackground();
//                        }
//                    });
                }
            });
        }

        mImageViewProfile = (ImageView) findViewById(R.id.imageView_profile);
        first = (TextView) findViewById(R.id.first_name);
        last = (TextView) findViewById(R.id.last_name);
        //maps = (Button) findViewById(R.id.button_maps);
        mButtonFindFriends = (Button) findViewById(R.id.find_friends);
        mButtonLogOut = (Button) findViewById(R.id.log_out);
        mListView = (ListView) findViewById(R.id.listView_favorites);
        yf = (TextView) findViewById(R.id.yfID);

        yf.setPaintFlags(yf.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = user.getRelation("favorite");
        relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null) {
                    mAdapter = new FavoriteAdapter(getApplicationContext(), list);
                    mListView.setAdapter(mAdapter);

                }
            }
        });

        first.setText(user.getString("first_name"));
        last.setText(user.getString("last_name"));

        Picasso.with(getApplicationContext()).load(user.getString("profile_url")).centerCrop().resize(400, 400).into(mImageViewProfile);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListeners(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListeners(false);
    }

    private void setUpListeners(boolean isResumed) {
        if (isResumed) {
            mButtonFindFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), FindFriendsActivity.class);
                    startActivity(intent);
                }
            });
            mButtonLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOut();
                }
            });
        } else {
            mButtonFindFriends.setOnClickListener(null);
            mButtonLogOut.setOnClickListener(null);
        }
    }

    private class FavoriteAdapter extends BaseAdapter {

        private Context mContext;
        private List<ParseObject> mList;


        public FavoriteAdapter(Context context, List<ParseObject> list) {
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

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_favorite, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.favorite_name);

            ParseObject vendor = getItem(position);



            name.setText(vendor.getString("vendor_name"));
            name.setTextColor(Color.BLACK);

            return convertView;
        }
    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        ParseUser.logOut();
        Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
