package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.adapter.RejectOrderEmptyProductsAdapter;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.common.data.order.OrderDetailItemData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyProductEditable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderEmptyProductFragment extends RejectOrderBaseFragment {

    private static final String FRAGMENT_TITLE = "Produk yang Habis";
    private static final String NO_ITEM_SELECTED_ERROR = "Pilih Barang yang akan ditolak";
    private RejectOrderEmptyStockListener listener;

    private RejectOrderEmptyProductsAdapter adapter;

    public static RejectOrderEmptyProductFragment createFragment(OrderDetailData data) {
        RejectOrderEmptyProductFragment fragment = new RejectOrderEmptyProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_DETAIL_DATA_KEY, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RejectOrderEmptyStockListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RejectOrderEmptyStockListener) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected String defineTitle() {
        return FRAGMENT_TITLE;
    }

    @Override
    protected RecyclerView.Adapter initAdapter(OrderDetailData data) {
        adapter = new RejectOrderEmptyProductsAdapter(
                data.getOrderId(),
                emptyProductEditablesMapper(data.getItemList()));
        return adapter;
    }

    private List<EmptyProductEditable> emptyProductEditablesMapper(List<OrderDetailItemData> productList) {
        List<EmptyProductEditable> productEditables = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            EmptyProductEditable productEditable = new EmptyProductEditable();
            productEditable.setProductId(productList.get(i).getProductId());
            productEditable.setProductName(productList.get(i).getItemName());
            productEditable.setProductPrice(productList.get(i).getPrice());
            productEditable.setProductImage(productList.get(i).getImageUrl() );
            productEditables.add(productEditable);
         }
         return productEditables;
    }

    @Override
    protected View.OnClickListener initConfirmButtonClickedListener() {
        return onConfirmButtonClickedListener();
    }

    private View.OnClickListener onConfirmButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TKPDMapParam<String, String> productparam = adapter.getMappedProductParameter();
                if(productparam.isEmpty()) {
                    NetworkErrorHelper.showSnackbar(getActivity(),
                            NO_ITEM_SELECTED_ERROR);
                } else listener.onRejectEmptyStock(productparam);
            }
        };
    }

    public interface RejectOrderEmptyStockListener {

        void onRejectEmptyStock(TKPDMapParam<String, String> param);

    }

}
