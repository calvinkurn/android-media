package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 11/29/17. Tokopedia
 */

public class CancelSearchFragment extends TkpdFragment{

    private CancelSearchReplacementListener listener;

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    private RadioButton reasonRadioButton1;
    private RadioButton reasonRadioButton2;
    private RadioButton reasonRadioButton3;
    private RadioButton otherReasonRadioButton;
    private TextView reason1;
    private TextView reason2;
    private TextView reason3;
    private EditText otherReasonField;

    @Override
    protected String getScreenName() {
        return null;
    }

    public CancelSearchFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CancelSearchReplacementListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CancelSearchReplacementListener) activity;
    }

    public static CancelSearchFragment createFragment(String orderId) {
        CancelSearchFragment fragment = new CancelSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cancel_search_replacement_fragment, container, false);
        reason1 = view.findViewById(R.id.reason_1);
        reasonRadioButton1 = view.findViewById(R.id.radio_button_1);
        reason2 = view.findViewById(R.id.reason_2);
        reasonRadioButton2 = view.findViewById(R.id.radio_button_2);
        reason3 = view.findViewById(R.id.reason_3);
        reasonRadioButton3 = view.findViewById(R.id.radio_button_3);
        otherReasonField = view.findViewById(R.id.other_reason_field);
        otherReasonRadioButton = view.findViewById(R.id.radio_button_4);

        reasonRadioButton1.setOnCheckedChangeListener(onRadioButtonClickedListener(
                reasonRadioButton2,
                reasonRadioButton3
        ));
        reason1.setOnClickListener(onReasonTextSelected(
                reasonRadioButton1,
                reasonRadioButton2,
                reasonRadioButton3));
        reasonRadioButton2.setOnCheckedChangeListener(onRadioButtonClickedListener(
                reasonRadioButton1,
                reasonRadioButton3
        ));
        reason2.setOnClickListener(onReasonTextSelected(
                reasonRadioButton2,
                reasonRadioButton1,
                reasonRadioButton3));
        reasonRadioButton3.setOnCheckedChangeListener(onRadioButtonClickedListener(
                reasonRadioButton1,
                reasonRadioButton2
        ));
        reason3.setOnClickListener(onReasonTextSelected(reasonRadioButton3,
                reasonRadioButton1,
                reasonRadioButton3));
        otherReasonRadioButton.setOnCheckedChangeListener(onOtherReasonClickedListener());
        otherReasonField.setOnClickListener(onOtherReasonTextSelected());
        TextView submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(onSubmitButtonClickedListener());
        return view;
    }

    private CompoundButton.OnCheckedChangeListener onRadioButtonClickedListener(
            final RadioButton otherRadioButton1,
            final RadioButton otherRadioButton2
    ) {
        return new CompoundButton.OnCheckedChangeListener(
        ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {
                    otherReasonRadioButton.setChecked(false);
                    otherRadioButton1.setChecked(false);
                    otherRadioButton2.setChecked(false);
                }
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener onOtherReasonClickedListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {
                    reasonRadioButton1.setChecked(false);
                    reasonRadioButton2.setChecked(false);
                    reasonRadioButton3.setChecked(false);
                }
                otherReasonField.requestFocus();
                otherReasonField.setSelection(otherReasonField.getText().length());
            }
        };
    }

    private View.OnClickListener onReasonTextSelected(
            final RadioButton selectedRadioButton,
            final RadioButton otherRadioButton1,
            final RadioButton otherRadioButton2
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedRadioButton.isChecked()) {
                    selectedRadioButton.setChecked(true);
                    otherRadioButton1.setChecked(false);
                    otherRadioButton2.setChecked(false);
                    otherReasonRadioButton.setChecked(false);
                }
            }
        };
    }

    private View.OnClickListener onOtherReasonTextSelected() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!otherReasonRadioButton.isChecked()) {
                    otherReasonRadioButton.setChecked(true);
                    reasonRadioButton1.setChecked(false);
                    reasonRadioButton2.setChecked(false);
                    reasonRadioButton3.setChecked(false);
                }

            }
        };
    }

    private View.OnClickListener onSubmitButtonClickedListener(
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reasonRadioButton1.isChecked()) {
                    listener.cancelSearch(
                            getArguments().getString(ORDER_ID_ARGUMENT),
                            11,
                            reason1.getText().toString()
                    );
                } else if(reasonRadioButton2.isChecked()) {
                    listener.cancelSearch(
                            getArguments().getString(ORDER_ID_ARGUMENT),
                            12,
                            reason2.getText().toString()
                    );
                } else if(reasonRadioButton3.isChecked()) {
                    listener.cancelSearch(
                            getArguments().getString(ORDER_ID_ARGUMENT),
                            13,
                            reason3.getText().toString()
                    );
                } else {
                    listener.cancelSearch(
                            getArguments().getString(ORDER_ID_ARGUMENT),
                            14,
                            otherReasonField.getText().toString()
                    );
                }
            }
        };
    }

    public interface CancelSearchReplacementListener {
        void cancelSearch(String orderId, int reasonId,String notes);
    }
}
