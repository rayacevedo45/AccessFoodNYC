package rayacevedo45.c4q.nyc.accessfoodnyc;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Hoshiko on 8/22/15.
 */


public class MarkerCluster implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;


    public MarkerCluster(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;

    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }




}