package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

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
        return new AlertDialog.Builder(getActivity())
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
    }

    public interface ResetShipmentFormCallbackAction {

        void onResetCartShipmentForm();

        void onCancelResetCartShipmentForm();

    }

}
