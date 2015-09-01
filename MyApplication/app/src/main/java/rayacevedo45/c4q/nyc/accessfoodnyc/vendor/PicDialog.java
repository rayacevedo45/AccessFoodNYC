package rayacevedo45.c4q.nyc.accessfoodnyc.vendor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import rayacevedo45.c4q.nyc.accessfoodnyc.Constants;
import rayacevedo45.c4q.nyc.accessfoodnyc.PicActivity;

/**
 * Created by c4q-raynaldie on 9/1/15.
 */
public class PicDialog extends DialogFragment {
    static final int EXTERNAL_CONTENT_URI = 0;
    Uri targetUri;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pics")
                .setPositiveButton("Take Pic", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Intent intent = new Intent(getActivity(), PicActivity.class);
                        intent.putExtra(Constants.EXTRA_PICTIRE, Constants.FLAG_CAMERA);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Camera Roll", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), PicActivity.class);
                        intent.putExtra(Constants.EXTRA_PICTIRE, Constants.FLAG_GALLERY);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
