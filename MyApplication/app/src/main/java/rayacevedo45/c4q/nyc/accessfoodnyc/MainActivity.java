package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView accessTV,foodTV,NYCtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accessTV = (TextView) findViewById(R.id.accessID);
        foodTV = (TextView) findViewById(R.id.foodID);
        NYCtv = (TextView) findViewById(R.id.NYCID);

        accessTV.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.lr));
        foodTV.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rl));
        NYCtv.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.lr));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
