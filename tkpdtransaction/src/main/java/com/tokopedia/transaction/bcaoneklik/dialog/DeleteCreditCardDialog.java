package com.tokopedia.transaction.bcaoneklik.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.tokopedia.transaction.R;

/**
 * Created by kris on 8/23/17. Tokopedia
 * Modified by aghny on 12/1/18
 */

public class DeleteCreditCardDialog extends DialogFragment {

    private static final String TOKEN_ID = "TOKEN_ID";
    private static final String CARD_ID = "CARD_ID";

    private DeleteCreditCardDialogListener mDeleteCreditCardDialogListener;

    public static DeleteCreditCardDialog newInstance(String tokenId, String cardId) {
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN_ID, tokenId);
        bundle.putString(CARD_ID, cardId);

        DeleteCreditCardDialog cardDialog = new DeleteCreditCardDialog();
        cardDialog.setArguments(bundle);

        return cardDialog;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mDeleteCreditCardDialogListener = (DeleteCreditCardDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_credit_card)
                .setMessage(R.string.forever_delete_credit_card)
                .setPositiveButton(R.string.label_title_button_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDeleteCreditCardDialogListener.onConfirmDelete(getArguments().getString(TOKEN_ID));
                    }
                })
                .setNegativeButton(R.string.label_title_button_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDeleteCreditCardDialogListener = (DeleteCreditCardDialogListener) context;
    }

    public interface DeleteCreditCardDialogListener {
        void onConfirmDelete(String tokenId);
    }

}
