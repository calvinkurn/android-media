package com.tokopedia.transaction.checkout.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tokopedia.transaction.checkout.view.CartFragment;

/**
 * @author Aghny A. Putra on 19/02/18.
 */

public class CartItemRemoveSingleDialog extends DialogFragment {

    private static final String MESSAGE = "message";
    private static final String POSITION = "position";

    public static CartItemRemoveSingleDialog newInstance(String message, int position) {
        CartItemRemoveSingleDialog frag = new CartItemRemoveSingleDialog();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        args.putInt(POSITION, position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString(MESSAGE);
        final int position = getArguments().getInt(POSITION);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Hapus Barang")
                .setMessage(message)
                .setPositiveButton("Tambah Wishlist", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((CartFragment)getTargetFragment()).addToWishList(position);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
    }

}
