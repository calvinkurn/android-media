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
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.listener.BcaOneClickDeleteListener;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickDeleteDialog extends DialogFragment{
    private static final String TOKEN_ID = "token_id";
    private static final String CARD_HOLDER = "card_holder";
    private static final String CARD_NUMBER = "card_number";
    private TextView cancelButton;

    private TextView deleteButton;

    private TextView cardHolderName;

    private TextView cardNumber;

    private BcaOneClickDeleteListener listener;

    public static BcaOneClickDeleteDialog createDialog(String tokenId, String cardHolder, String cardNumber) {
        BcaOneClickDeleteDialog bcaOneClickDeleteDialog = new BcaOneClickDeleteDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CARD_HOLDER, cardHolder);
        bundle.putString(CARD_NUMBER, cardNumber);
        bundle.putString(TOKEN_ID, tokenId);
        bcaOneClickDeleteDialog.setArguments(bundle);
        return bcaOneClickDeleteDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = ((BcaOneClickDeleteListener) context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = ((BcaOneClickDeleteListener) activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.bca_one_click_delete_dialog, container, false);
        cancelButton = (TextView) view.findViewById(R.id.cancel_button);
        deleteButton = (TextView) view.findViewById(R.id.delete_button);
        cardHolderName = (TextView) view.findViewById(R.id.card_holder_name);
        cardNumber = (TextView) view.findViewById(R.id.card_number);

        cardHolderName.setText(getArguments().getString(CARD_HOLDER));
        cardNumber.setText(getArguments().getString(CARD_NUMBER));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(getArguments().getString(TOKEN_ID));
                dismiss();
            }
        });
        return view;
    }
}
