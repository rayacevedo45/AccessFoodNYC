package c4q.nyc.take2.accessfoodnyc;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RedeemDialogFragment extends DialogFragment {

    private TextView mTextViewAmount;
    private TextView mTextViewExpiration;
    private Button mButtonRedeem;
    private TextView mTextViewAddress;
    private TextView mTextViewName;

    private View mDialogView;
    private String objectId;
    private DialogCallback mListener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objectId = getArguments().getString(Constants.EXTRA_KEY_OBJECT_ID);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_redeem, null);

        mTextViewAddress = (TextView) mDialogView.findViewById(R.id.redeem_address);
        mTextViewName = (TextView) mDialogView.findViewById(R.id.redeem_name);
        mTextViewAmount = (TextView) mDialogView.findViewById(R.id.redeem_amount);
        mTextViewExpiration = (TextView) mDialogView.findViewById(R.id.redeem_expiration);
        mButtonRedeem = (Button) mDialogView.findViewById(R.id.button_redeem);

        ParseQuery<ParseObject> coupons = ParseQuery.getQuery("Coupon");
        coupons.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject coupon, ParseException e) {
                if (e == null) {
                    ParseObject vendor = coupon.getParseObject("vendor");
                    vendor.fetchInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject vendor, ParseException e) {
                            mTextViewAddress.setText(vendor.getString("address"));
                            mTextViewName.setText(vendor.getString("name"));
                            mTextViewExpiration.setText(coupon.getDate("expiration").toString());
                            String type = coupon.getString("type");
                            String price = coupon.getString("amount");
                            if (type.equals("%")) {
                                mTextViewAmount.setText(price + type);
                            } else {
                                mTextViewAmount.setText(type + price);
                            }
                        }
                    });

                    mButtonRedeem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            coupon.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    mListener.dialogClicked(0);
                                    dismiss();
                                }
                            });
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (DialogCallback) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setView(mDialogView);
        return builder.create();
    }
}
