package com.tokopedia.transaction.purchase.detail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.common.data.order.OrderDetailData;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public abstract class RejectOrderBaseFragment extends TkpdFragment {

    protected static final String ORDER_DETAIL_DATA_KEY = "ORDER_DETAIL_DATA_KEY";

    public static final String FRAGMENT_REJECT_ORDER_SUB_MENU_TAG =
            "FRAGMENT_REJECT_ORDER_SUB_MENU_TAG";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_reject_generic_layout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.main_recycler_view);
        Button confirmButton = view.findViewById(R.id.reject_order_confirm_button);
        TextView titleTextView = view.findViewById(R.id.title_text_view);
        confirmButton.setOnClickListener(initConfirmButtonClickedListener());
        recyclerView. setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(initAdapter((OrderDetailData) getArguments()
                .getParcelable(ORDER_DETAIL_DATA_KEY)));
        titleTextView.setText(defineTitle());
        return view;
    }

    protected abstract String defineTitle();

    protected abstract RecyclerView.Adapter initAdapter(OrderDetailData data);

    protected abstract View.OnClickListener initConfirmButtonClickedListener();

}
