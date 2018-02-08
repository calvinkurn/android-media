package com.tokopedia.flight.orderlist.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.flight.R;

/**
 * @author by furqan on 08/02/18.
 */

public class FlightResendETicketDialogFragment extends DialogFragment {

    private static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    private static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private boolean isProgramaticallyDismissed = false;

    private AppCompatTextView txtSend;
    private AppCompatTextView txtCancel;
    private AppCompatEditText edtEmail;
    private TkpdTextInputLayout containerEmail;

    private String email, userId, invoiceId;

    public FlightResendETicketDialogFragment() {
    }

    public static FlightResendETicketDialogFragment newInstace(String invoiceId, String userId) {
        FlightResendETicketDialogFragment fragment = new FlightResendETicketDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        bundle.putString(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoiceId = getArguments().getString(EXTRA_INVOICE_ID);
            userId = getArguments().getString(EXTRA_USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_resend_eticket_dialog, container, false);
        edtEmail = view.findViewById(R.id.et_resend_eticket_email);
        txtSend = view.findViewById(R.id.tv_resend_eticket_send);
        txtCancel = view.findViewById(R.id.tv_resend_eticket_cancel);
        containerEmail = view.findViewById(R.id.container_email);

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput(edtEmail.getText().toString())) {
                    isProgramaticallyDismissed = true;
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            null
                    );
                    dismiss();
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProgramaticallyDismissed = true;
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_CANCELED,
                        null
                );
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void dismiss() {
        if (isProgramaticallyDismissed) {
            super.dismiss();
        } else {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_CANCELED,
                    null
            );

            super.dismiss();
        }
    }

    private boolean validateInput(String email) {
        boolean isValid = true;

        if (email == null || email.isEmpty()) {
            isValid = false;
            containerEmail.setError(getString(R.string.flight_resend_eticket_dialog_email_empty_error));
        } else if (!isValidEmail(email)) {
            isValid = false;
            containerEmail.setError(getString(R.string.flight_resend_eticket_dialog_email_invalid_error));
        } else if (!isEmailWithoutProhibitSymbol(email)) {
            isValid = false;
            containerEmail.setError(getString(R.string.flight_resend_eticket_dialog_email_invalid_symbol_error));
        }

        return isValid;
    }

    private boolean isValidEmail(String contactEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.");
    }

    private boolean isEmailWithoutProhibitSymbol(String contactEmail) {
        return !contactEmail.contains("+");
    }
}
