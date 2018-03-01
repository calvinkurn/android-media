package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.tokopedia.transaction.R;

/**
 * @author Aghny A. Putra on 28/02/18.
 */

public class ResetShipmentFormDialog extends DialogFragment {

    private ResetShipmentFormCallbackAction mCallbackAction;

    public static ResetShipmentFormDialog newInstance(ResetShipmentFormCallbackAction callbackAction) {
        Bundle args = new Bundle();

        ResetShipmentFormDialog frag = new ResetShipmentFormDialog();
        frag.setArguments(args);
        frag.setCallbackAction(callbackAction);

        return frag;
    }

    private void setCallbackAction(ResetShipmentFormCallbackAction callbackAction) {
        mCallbackAction = callbackAction;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog dialog =  new AlertDialog.Builder(getActivity())
                .setTitle("Kembali Ke Keranjang")
                .setMessage("Perubahan yang Anda lakukan di halaman ini tidak akan disimpan")
                .setPositiveButton("Kembali & Hapus Perubahan",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallbackAction.onResetCartShipmentForm();
                                dismiss();
                            }
                })
                .setNegativeButton("Tetap di Halaman Ini",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallbackAction.onCancelResetCartShipmentForm();
                                dismiss();
                            }
                        })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextColor(ContextCompat.getColor(getActivity(), R.color.medium_green));
                btnPositive.setAllCaps(false);

                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_54));
                btnNegative.setAllCaps(false);
            }
        });

        return dialog;
    }

    public interface ResetShipmentFormCallbackAction {

        void onResetCartShipmentForm();

        void onCancelResetCartShipmentForm();

    }

}
