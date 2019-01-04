package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.common.data.order.OrderDetailData;

/**
 * Created by kris on 1/2/18. Tokopedia
 */

public class RejectOrderFragment extends TkpdFragment {

    private static final String ORDER_DATA_ARGUMENT = "ORDER_ID_ARGUMENT";

    public static final String REJECT_ORDER_MENU_FRAGMENT_TAG = "reject_order_fragment";

    public static RejectOrderFragment createFragment(OrderDetailData orderDetailData) {
        RejectOrderFragment fragment = new RejectOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_DATA_ARGUMENT, orderDetailData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reject_order_fragment, container, false);
        TextView reason1 = view.findViewById(R.id.reason_1);
        TextView reason3 = view.findViewById(R.id.reason_3);
        TextView reason4 = view.findViewById(R.id.reason_4);
        TextView reason5 = view.findViewById(R.id.reason_5);
        TextView reason6 = view.findViewById(R.id.reason_6);
        reason1.setOnClickListener(onStockEmpty());
        reason3.setOnClickListener(onWrongPriceWeight());
        reason4.setOnClickListener(onStoreClosed());
        reason5.setOnClickListener(onCourierProblem());
        reason6.setOnClickListener(onBuyerRequest());
        return view;
    }

    private View.OnClickListener onStockEmpty() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(RejectOrderEmptyProductFragment.createFragment(
                        (OrderDetailData) getArguments().getParcelable(ORDER_DATA_ARGUMENT))
                );
            }
        };
    }

    private View.OnClickListener onVarianEmpty() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(RejectOrderEmptyVarianFragment.createFragment(
                        (OrderDetailData) getArguments().getParcelable(ORDER_DATA_ARGUMENT))
                );
            }
        };
    }

    private View.OnClickListener onWrongPriceWeight() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(RejectOrderWeightPriceFragment.createFragment(
                        (OrderDetailData) getArguments().getParcelable(ORDER_DATA_ARGUMENT))
                );
            }
        };
    }

    private View.OnClickListener onStoreClosed() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(RejectOrderShopClosedFragment.createFragment(
                        (OrderDetailData) getArguments().getParcelable(ORDER_DATA_ARGUMENT))
                );
            }
        };
    }

    private View.OnClickListener onCourierProblem() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(RejectOrderCourierProblemFragment.createFragment(
                        ((OrderDetailData) getArguments().getParcelable(ORDER_DATA_ARGUMENT))
                                .getOrderId())
                );
            }
        };
    }

    private View.OnClickListener onBuyerRequest() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(RejectOrderBuyerRequest.createFragment(
                        ((OrderDetailData) getArguments().getParcelable(ORDER_DATA_ARGUMENT))
                                .getOrderId())
                );
            }
        };
    }

    private void openFragment(Fragment fragmentToOpen) {
        if (getFragmentManager().findFragmentByTag(REJECT_ORDER_MENU_FRAGMENT_TAG) == null) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, fragmentToOpen, REJECT_ORDER_MENU_FRAGMENT_TAG)
                    .commit();
        }
    }

}
