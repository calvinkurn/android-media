package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/9/18. Tokopedia
 */

public class ChangeAwbFragment extends TkpdFragment {

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";
    private static final String REF_NUMBER_ARGUMENT = "REF_NUMBER_ARGUMENT";

    private ChangeAwbListener listener;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static ChangeAwbFragment createFragment(String orderId, String refNumber) {
        ChangeAwbFragment fragment = new ChangeAwbFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        bundle.putString(REF_NUMBER_ARGUMENT, refNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ChangeAwbListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ChangeAwbListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_awb_fragment, container, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.notes_text_input_layout);
        EditText refNumberField = view.findViewById(R.id.cancel_order_notes_field);
        Button changeRefConfirmButton = view.findViewById(R.id.cancel_order_confirm_button);
        refNumberField.setText(getArguments().getString(REF_NUMBER_ARGUMENT));
        changeRefConfirmButton
                .setOnClickListener(onChangeAwbButtonClicked(refNumberField, textInputLayout));
        return view;
    }

    private View.OnClickListener onChangeAwbButtonClicked(
            final EditText refNumberField,
            final TextInputLayout RefNumberInputLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refNumberField.getText().toString().isEmpty()) {
                    RefNumberInputLayout.setError(
                            getActivity()
                                    .getString(com.tokopedia.core.R.string.error_note_empty)
                    );
                } else {
                    if (getArguments() != null) {
                        listener.changeAwb(getArguments().getString(ORDER_ID_ARGUMENT),
                                refNumberField.getText().toString());
                    }
                }
            }
        };
    }

    public interface ChangeAwbListener {
        void changeAwb(String orderId, String refNumber);
    }

}
