package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Hoshiko on 8/22/15.
 */
public class ClusterRendring extends DefaultClusterRenderer<MarkerCluster> {

    public ClusterRendring(Context context, GoogleMap map,
                           ClusterManager<MarkerCluster> clusterManager) {
        super(context, map, clusterManager);
    }


    protected void onBeforeClusterItemRendered(MarkerCluster item, MarkerOptions markerOptions) {

            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.food_truck));
//            markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
