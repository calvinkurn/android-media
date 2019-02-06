package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 11/29/17. Tokopedia
 */

public class CancelSearchFragment extends TkpdFragment {

    private CancelSearchReplacementListener listener;

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    private RadioButton reasonRadioButton1;
    private RadioButton reasonRadioButton2;
    private RadioButton reasonRadioButton3;
    private RadioButton otherReasonRadioButton;
    private TextView reason1;
    private TextView reason2;
    private TextView reason3;
    private TextView otherReason;
    private EditText otherReasonField;
    private RelativeLayout layoutCancelSearch;
    private TextView submitButton;

    @Override
    protected String getScreenName() {
        return null;
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
        listener.setToolbarCancelSearch(getString(R.string.cancel_search_replacement), R.drawable.ic_clear_24dp);
        View view = inflater.inflate(R.layout.cancel_search_replacement_fragment, container, false);
        reason1 = view.findViewById(R.id.reason_1);
        reasonRadioButton1 = view.findViewById(R.id.radio_button_1);
        reason2 = view.findViewById(R.id.reason_2);
        reasonRadioButton2 = view.findViewById(R.id.radio_button_2);
        reason3 = view.findViewById(R.id.reason_3);
        reasonRadioButton3 = view.findViewById(R.id.radio_button_3);
        otherReasonField = view.findViewById(R.id.other_reason_field);
        otherReasonRadioButton = view.findViewById(R.id.radio_button_4);
        layoutCancelSearch = view.findViewById(R.id.layout_cancel_search_replacement);
        otherReason = view.findViewById(R.id.other_reason_text);

        reasonRadioButton1.setOnCheckedChangeListener(onRadioButtonClickedListener(reason1,
                reasonRadioButton2,
                reasonRadioButton3
        ));
        reason1.setOnClickListener(onReasonTextSelected(
                reasonRadioButton1,
                reasonRadioButton2,
                reasonRadioButton3));
        reasonRadioButton2.setOnCheckedChangeListener(onRadioButtonClickedListener(reason2,
                reasonRadioButton1,
                reasonRadioButton3
        ));
        reason2.setOnClickListener(onReasonTextSelected(
                reasonRadioButton2,
                reasonRadioButton1,
                reasonRadioButton3));
        reasonRadioButton3.setOnCheckedChangeListener(onRadioButtonClickedListener(reason3,
                reasonRadioButton1,
                reasonRadioButton2
        ));
        reason3.setOnClickListener(onReasonTextSelected(reasonRadioButton3,
                reasonRadioButton1,
                reasonRadioButton2));
        otherReasonRadioButton.setOnCheckedChangeListener(onOtherReasonClickedListener(otherReason));
        otherReason.setOnClickListener(onOtherReasonTextSelected());
        layoutCancelSearch.setOnClickListener(onOtherClickTextListener());
        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(onSubmitButtonClickedListener());
        return view;
    }

    private CompoundButton.OnCheckedChangeListener onRadioButtonClickedListener(
            final TextView textChoice,
            final RadioButton otherRadioButton1,
            final RadioButton otherRadioButton2
    ) {
        return new CompoundButton.OnCheckedChangeListener(
        ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    textChoice.setTypeface(null, Typeface.BOLD);
                    setButtonCancelSearch(true);
                    KeyboardHandler.hideSoftKeyboard(getActivity());
                    otherReasonRadioButton.setChecked(false);
                    otherReasonField.setVisibility(View.GONE);
                    otherRadioButton1.setChecked(false);
                    otherRadioButton2.setChecked(false);
                } else {
                    textChoice.setTypeface(null, Typeface.NORMAL);
                }
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener onOtherReasonClickedListener(final TextView textChoice) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    textChoice.setTypeface(null, Typeface.BOLD);
                    setButtonCancelSearch(true);
                    KeyboardHandler.showSoftKeyboard(getActivity());
                    reasonRadioButton1.setChecked(false);
                    reasonRadioButton2.setChecked(false);
                    reasonRadioButton3.setChecked(false);
                } else {
                    textChoice.setTypeface(null, Typeface.NORMAL);
                }
                otherReasonField.setVisibility(View.VISIBLE);
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
                if (!selectedRadioButton.isChecked()) {
                    selectedRadioButton.setChecked(true);
                    KeyboardHandler.hideSoftKeyboard(getActivity());
                    setButtonCancelSearch(true);
                    otherRadioButton1.setChecked(false);
                    otherRadioButton2.setChecked(false);
                    otherReasonRadioButton.setChecked(false);
                    otherReasonField.setVisibility(View.GONE);
                }
            }
        };
    }

    private View.OnClickListener onOtherReasonTextSelected() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!otherReasonRadioButton.isChecked()) {
                    otherReasonRadioButton.setChecked(true);
                    KeyboardHandler.showSoftKeyboard(getActivity());
                    setButtonCancelSearch(true);
                    otherReasonField.setVisibility(View.VISIBLE);
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
                if (reasonRadioButton1.isChecked()) {
                    listener.cancelSearch(
                            getArguments().getString(ORDER_ID_ARGUMENT),
                            11,
                            reason1.getText().toString()
                    );
                } else if (reasonRadioButton2.isChecked()) {
                    listener.cancelSearch(
                            getArguments().getString(ORDER_ID_ARGUMENT),
                            12,
                            reason2.getText().toString()
                    );
                } else if (reasonRadioButton3.isChecked()) {
                    listener.cancelSearch(
                            getArguments().getString(ORDER_ID_ARGUMENT),
                            13,
                            reason3.getText().toString()
                    );
                } else if (otherReasonRadioButton.isChecked()) {
                    if (otherReasonField.getText().toString().isEmpty()) {
                        otherReasonField.setError(getActivity()
                                .getString(com.tokopedia.core2.R.string.error_note_empty));
                    } else {
                        listener.cancelSearch(
                                getArguments().getString(ORDER_ID_ARGUMENT),
                                14,
                                otherReasonField.getText().toString()
                        );
                    }
                } else {
                    setButtonCancelSearch(false);
                    Toast.makeText(
                            getActivity(),
                            "Mohon Pilih Salah Satu",
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void setButtonCancelSearch(boolean active) {
        submitButton.setEnabled(active);
        submitButton.setBackground(active ? ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_green) :
                ContextCompat.getDrawable(getActivity(), R.drawable.bg_grey_button_rounded));
        submitButton.setTextColor(active ? ContextCompat.getColor(getActivity(), R.color.white) :
                ContextCompat.getColor(getActivity(), R.color.grey_700));
    }

    private View.OnClickListener onOtherClickTextListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!otherReasonRadioButton.isChecked()) {
                    otherReasonRadioButton.setChecked(true);
                    otherReasonField.setVisibility(View.VISIBLE);
                    reasonRadioButton1.setChecked(false);
                    reasonRadioButton2.setChecked(false);
                    reasonRadioButton3.setChecked(false);
                }
            }
        };
    }

    public interface CancelSearchReplacementListener {
        void cancelSearch(String orderId, int reasonId, String notes);

        void setToolbarCancelSearch(String titleToolbar, int drawable);
    }
}
