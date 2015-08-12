package rayacevedo45.c4q.nyc.accessfoodnyc.vendor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rayacevedo45.c4q.nyc.accessfoodnyc.R;

public class DetailsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_details, container, false);

        return result;
    }
}
