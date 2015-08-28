package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendProfileActivity extends AppCompatActivity {

    private String objectId;
    private ImageView mImageView;
    private TextView mTextViewName;
    private Button mButtonRemove;
    private RecyclerView mRecyclerViewFavorites;
    private VendorListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        if (getIntent() != null) {
            objectId = getIntent().getStringExtra(Constants.EXTRA_KEY_OBJECT_ID);
        } else {
            objectId = "";
        }

        mImageView = (ImageView) findViewById(R.id.imageView_friend_profile);
        mTextViewName = (TextView) findViewById(R.id.friend_name);
        mButtonRemove = (Button) findViewById(R.id.button_friend_remove);
        mRecyclerViewFavorites = (RecyclerView) findViewById(R.id.recyclerView_friend_favorites);

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", objectId).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                ParseUser user = list.get(0);
                String first = user.getString("first_name");
                Picasso.with(getApplicationContext()).load(user.getString("profile_url")).resize(400, 400).centerCrop().into(mImageView);
                mTextViewName.setText(user.getString("first_name") + " " + user.getString("last_name"));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                RemoveDialogFragment dialog = new RemoveDialogFragment();
                Bundle argument = new Bundle();
                argument.putString(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                dialog.setArguments(argument);
                dialog.show(manager, "Remove Friend");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mButtonRemove.setOnClickListener(null);
    }
}
