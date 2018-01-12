package com.tokopedia.transaction.purchase.detail.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.transaction.purchase.detail.adapter.RejectOrderPriceWeightChangeAdapter;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderWeightPriceFragment extends RejectOrderBaseFragment{

    public static RejectOrderWeightPriceFragment createFragment(OrderDetailData data) {
        RejectOrderWeightPriceFragment fragment = new RejectOrderWeightPriceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_DETAIL_DATA_KEY, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected String defineTitle() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter initAdapter(OrderDetailData data) {
        //return new RejectOrderPriceWeightChangeAdapter(data.getItemList());
        return null;
    }

    @Override
    protected View.OnClickListener initConfirmButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

}
