package com.tokopedia.transaction.bcaoneklik.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kris on 8/23/17. Tokopedia
 */

public class DeleteCreditCardDialog extends DialogFragment {

    private static final String TOKEN_ID = "TOKEN_ID";
    private static final String CARD_ID = "CARD_ID";

    private DeleteCreditCardDialogListener listener;

    @BindView(R2.id.delete_button) Button deleteButton;
    @BindView(R2.id.cancel_button) Button cancelButton;

    public static DeleteCreditCardDialog createDialog(String tokenId, String cardId) {
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN_ID, tokenId);
        bundle.putString(CARD_ID, cardId);

        DeleteCreditCardDialog cardDialog = new DeleteCreditCardDialog();
        cardDialog.setArguments(bundle);

        return cardDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.credit_card_delete_dialog, container, false);
        ButterKnife.bind(this, view);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initWindowDialog(getDialog().getWindow());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmDelete(getArguments().getString(TOKEN_ID));
                dismissDialog();
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

    private void initWindowDialog(Window window) {
        window.setGravity(Gravity.TOP | Gravity.START);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.y = -200;
        window.setAttributes(params);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void dismissDialog() {
        getActivity().onBackPressed();
    }

    public interface DeleteCreditCardDialogListener {
        void onConfirmDelete(String tokenId);
    }

}
