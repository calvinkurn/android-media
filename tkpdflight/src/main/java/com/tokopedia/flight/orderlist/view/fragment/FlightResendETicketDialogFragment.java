package com.tokopedia.flight.orderlist.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.contract.FlightResendETicketContract;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.presenter.FlightResendETicketPresenter;

import javax.inject.Inject;

/**
 * @author by furqan on 08/02/18.
 */

public class FlightResendETicketDialogFragment extends DialogFragment implements FlightResendETicketContract.View {

    private static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    private static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private boolean isProgramaticallyDismissed = false;

    private AppCompatTextView txtSend;
    private AppCompatTextView txtCancel;
    private AppCompatEditText edtEmail;
    private TkpdTextInputLayout containerEmail;

    @Inject
    FlightResendETicketPresenter flightResendETicketPresenter;

    private String userId, invoiceId;

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

        getComponent(FlightOrderComponent.class)
                .inject(this);
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
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
                flightResendETicketPresenter.onSendButtonClicked();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flightResendETicketPresenter.attachView(this);
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

    @Override
    public String getInvoiceId() {
        return invoiceId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return edtEmail.getText().toString();
    }

    @Override
    public void showEmailEmptyError(int resId) {
        showError(resId);
    }

    @Override
    public void showEmailInvalidError(int resId) {
        showError(resId);
    }

    @Override
    public void showEmailInvalidSymbolError(int resId) {
        showError(resId);
    }

    @Override
    public void onResendETicketSuccess() {
        isProgramaticallyDismissed = true;
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                null
        );
        dismiss();
    }

    @Override
    public void onResendETicketError(String errorMsg) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), errorMsg);
        dismiss();
    }

    private void showError(int resId) {
        containerEmail.setError(getString(resId));
    }
}
