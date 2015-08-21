package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;
import android.content.Intent;
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

import com.facebook.login.LoginManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import rayacevedo45.c4q.nyc.accessfoodnyc.accounts.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mImageViewProfile;
    private TextView first;
    private TextView last;

    private Button maps;
    private Button logout;

    private ListView mListView;
    private FavoriteAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageViewProfile = (ImageView) findViewById(R.id.imageView_profile);
        first = (TextView) findViewById(R.id.first_name);
        last = (TextView) findViewById(R.id.last_name);
        maps = (Button) findViewById(R.id.button_maps);
        logout = (Button) findViewById(R.id.log_out);
        mListView = (ListView) findViewById(R.id.listView_favorites);

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





        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                ParseUser.logOut();
                Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


        first.setText((String) user.get("first_name"));
        last.setText((String) user.get("last_name"));

        Picasso.with(getApplicationContext()).load(user.getString("profile_url")).centerCrop().resize(300, 300).into(mImageViewProfile);



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



            return convertView;
        }
    }
}
