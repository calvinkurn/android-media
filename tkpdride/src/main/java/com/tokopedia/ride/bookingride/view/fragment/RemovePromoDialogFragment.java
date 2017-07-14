package com.tokopedia.ride.bookingride.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.ride.R;

/**
 * Created by alvarisi on 3/29/17.
 */

public class RemovePromoDialogFragment extends DialogFragment {
    private static final String TAG = "RemovePromoDialogFragment";


    private TextView mBtnYes;
    private TextView mBtnNo;

    private boolean isProgramaticallyDismissed = false;

    public static RemovePromoDialogFragment newInstance() {
        RemovePromoDialogFragment fragment = new RemovePromoDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_remove_promo_confirmation, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnYes = (TextView) view.findViewById(R.id.dialog_yes);
        mBtnNo = (TextView) view.findViewById(R.id.dialog_no);

        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        null
                );

                dismiss();
            }
        });

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    @Override
    public void dismiss() {
        if (isProgramaticallyDismissed) {
            super.dismiss();
        } else {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_CANCELED,
                    null);

            super.dismiss();
        }
    }
}
