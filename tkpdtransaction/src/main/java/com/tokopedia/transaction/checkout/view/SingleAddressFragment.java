package com.tokopedia.transaction.checkout.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressAdapter;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Aghny A. Putra on 24/1/18
 */

public class SingleAddressFragment extends TkpdFragment {

    private static final String SCREEN_NAME = "SingleAddressCartFragment";

    @BindView(R2.id.rv_cart_order_details) RecyclerView mRvCartOrderDetails;

    public static SingleAddressFragment newInstance() {
        return new SingleAddressFragment();
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_address_shipment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R2.id.btn_next_to_payment_option)
    protected void onClickToPaymentSection() {

    }

}
