package c4q.nyc.take2.accessfoodnyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class NavigationDrawerActivity extends Activity{

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        listView = (ListView) findViewById(R.id.listView);
        setListenerOnListView();

    }

    public void setListenerOnListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent= new Intent(NavigationDrawerActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent1= new Intent(NavigationDrawerActivity.this, FriendsActivity.class);
                        startActivity(intent1);
                        break;

                    case 3:
                        Intent intent2= new Intent(NavigationDrawerActivity.this, UserReviewActivity.class);
                        startActivity(intent2);
                        break;

                    case 4:
                        Intent intent3= new Intent(NavigationDrawerActivity.this, ProfileActivity.class);
                        startActivity(intent3);
                        break;
                }
            }
        });

    }
}
