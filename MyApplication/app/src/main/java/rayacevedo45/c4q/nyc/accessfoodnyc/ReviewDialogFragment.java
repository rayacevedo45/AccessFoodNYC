package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ReviewDialogFragment extends DialogFragment {

    private String objectId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //objectId = getArguments().getString(Constants.EXTRA_KEY_OBJECT_ID);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Reviews")
                .setMessage("Write Review For this Vendor")
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null).create();
    }
}
