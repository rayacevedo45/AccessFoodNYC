package c4q.nyc.take2.accessfoodnyc;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    /*TODO to use the settingsActivity to check if you can send notifications check the status of boolean
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    Boolean notificationsPref=sharedPref.getBoolean("notifications_switch",true);

    TODO: Add button that will link to settings activity?
     */
}
