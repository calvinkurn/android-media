package com.tokopedia.transaction.cart.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.transaction.cart.adapter.PaymentGatewayAdapter;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public class PaymentGatewayFragment extends DialogFragment implements
        PaymentGatewayAdapter.ActionListener {
    private static final String ARG_PARAM_EXTRA_GATEWAY_LIST = "ARG_PARAM_EXTRA_GATEWAY_LIST";

    @Bind(R2.id.rv_payment_gateway)
    RecyclerView rvPaymentGateway;
    private List<GatewayList> gatewayList;
    private ActionListener actionListener;

    public interface ActionListener {
        void onSelectedPaymentGateway(GatewayList gateway);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public static PaymentGatewayFragment newInstance(List<GatewayList> gatewayLists) {
        PaymentGatewayFragment dialogFragment = new PaymentGatewayFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_GATEWAY_LIST,
                new ArrayList<Parcelable>(gatewayLists));
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null)
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gatewayList = getArguments().getParcelableArrayList(ARG_PARAM_EXTRA_GATEWAY_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_payment_gateway, container);
        ButterKnife.bind(this, view);
        rvPaymentGateway.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPaymentGateway.setAdapter(new PaymentGatewayAdapter(this, gatewayList));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onSelectedPaymentGateway(GatewayList gateway) {
        actionListener.onSelectedPaymentGateway(gateway);
        dismiss();
    }
}
