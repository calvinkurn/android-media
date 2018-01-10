package com.tokopedia.transaction.bcaoneklik.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.tokopedia.transaction.R;

/**
 * Created by kris on 8/23/17. Tokopedia
 */

public class DeleteCreditCardDialog extends DialogFragment{

    private static final String TOKEN_ID = "TOKEN_ID";
    private static final String CARD_ID = "CARD_ID";

    private DeleteCreditCardDialogListener listener;

    public static DeleteCreditCardDialog createDialog(String tokenId, String cardId) {
        DeleteCreditCardDialog cardDialog = new DeleteCreditCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN_ID, tokenId);
        bundle.putString(CARD_ID, cardId);
        cardDialog.setArguments(bundle);
        return cardDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= inflater.inflate(R.layout.credit_card_delete_dialog, container, false);
        Button deleteButton = view.findViewById(R.id.delete_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmDelete(getArguments().getString(TOKEN_ID));
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DeleteCreditCardDialogListener) context;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (DeleteCreditCardDialogListener) activity;
    }

    public interface DeleteCreditCardDialogListener {
        void onConfirmDelete(String tokenId);
    }
}
