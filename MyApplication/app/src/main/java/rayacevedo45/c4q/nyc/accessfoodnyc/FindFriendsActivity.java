package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FindFriendsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FindFriendsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_find_friends);

        AccessToken currentToken = AccessToken.getCurrentAccessToken();



        GraphRequest request = GraphRequest.newMyFriendsRequest(currentToken, new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject friend = jsonArray.getJSONObject(i);
                        String id = friend.getString("id");
                        String name = friend.getString("name");
                        Log.i("SHOW ME!!!!!", friend.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("SHOW ME!!!!!", jsonArray.toString());
                Log.i("SHOW ME!!!!!!!!!", graphResponse.toString());

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();

        GraphRequest.executeBatchAsync(request);
    }
}
