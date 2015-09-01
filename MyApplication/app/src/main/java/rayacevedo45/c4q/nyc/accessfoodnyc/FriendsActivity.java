package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView mPendingRecyclerView;

    private FriendsAdapter mAdapter;
    private PendingFriendsAdapter mPendingAdapter;
    private TextView mTextVIewPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ParseUser user = ParseUser.getCurrentUser();

        mPendingRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_pending_friends);
        mPendingRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm1 = new LinearLayoutManager(this);
        lm1.setOrientation(LinearLayoutManager.VERTICAL);
        mPendingRecyclerView.setLayoutManager(lm1);

        mTextVIewPending = (TextView) findViewById(R.id.textView_pending_friends);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_friends);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);


        ParseRelation<ParseUser> pending = user.getRelation("friend_requests");
        pending.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (list.size() != 0) {
                    mTextVIewPending.setText(list.size() + " friend request(s) pending");
                    mPendingAdapter = new PendingFriendsAdapter(getApplicationContext(), list);
                    mPendingRecyclerView.setAdapter(mPendingAdapter);
                }

            }
        });





        ParseRelation<ParseUser> relation = user.getRelation("friends");
        relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                mAdapter = new FriendsAdapter(getApplicationContext(), list);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String objectId = mAdapter.getItem(position).getObjectId();
                        Intent intent = new Intent(getApplicationContext(), FriendProfileActivity.class);
                        intent.putExtra(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                        startActivity(intent);
                    }
                }));
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        mRecyclerView.setOnClickListener(null);
    }
}
