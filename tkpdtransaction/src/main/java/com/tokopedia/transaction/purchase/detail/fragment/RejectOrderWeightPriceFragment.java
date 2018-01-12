package com.tokopedia.transaction.purchase.detail.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.RejectOrderPriceWeightChangeAdapter;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailItemData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.ArrayList;
import java.util.List;

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
        return "Ubah Harga dan Berat Produk";
    }

    @Override
    protected RecyclerView.Adapter initAdapter(OrderDetailData data) {
        return null;
        /*return new RejectOrderPriceWeightChangeAdapter(
                generateProductPriceWeight(
                        data.getOrderId(),
                        data.getShopId(),
                        data.getItemList()), adapterListener());*/
    }

    /*private RejectOrderPriceWeightChangeAdapter
            .RejectOrderPriceWeightAdapterListener adapterListener() {
        return new RejectOrderPriceWeightChangeAdapter.RejectOrderPriceWeightAdapterListener() {
            @Override
            public void onProductChoosen(WrongProductPriceWeightEditable model) {
                RejectOrderProductPriceWeightEditFragment editFragment = RejectOrderProductPriceWeightEditFragment.
                if(getFragmentManager()
                        .findFragmentByTag(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG) == null) {
                    editFragment.setTargetFragment(
                            RejectOrderEmptyVarianFragment.this,
                            FRAGMENT_EDIT_EMPTY_VARIAN_REQUEST_CODE
                    );
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                            .add(R.id.main_view,
                                    editFragment,
                                    FRAGMENT_REJECT_ORDER_SUB_MENU_TAG
                            ).commit();
                }
            }
        };
    }*/

    @Override
    protected View.OnClickListener initConfirmButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private List<WrongProductPriceWeightEditable> generateProductPriceWeight(
            String orderId,
            String shopId,
            List<OrderDetailItemData> itemDataList
    ) {
        List<WrongProductPriceWeightEditable> productWeightPriceModels = new ArrayList<>();
        for(int i =0; i < itemDataList.size(); i++) {
            WrongProductPriceWeightEditable productWeightPriceModel =
                    new WrongProductPriceWeightEditable();
            productWeightPriceModel.setOrderId(orderId);
            productWeightPriceModel.setShopId(shopId);
            productWeightPriceModel.setProductId(itemDataList.get(i).getProductId());
            productWeightPriceModel.setProductImage(itemDataList.get(i).getImageUrl());
            productWeightPriceModel.setProductName(itemDataList.get(i).getItemName());
            productWeightPriceModel.setProductPrice(itemDataList.get(i).getPrice());
            productWeightPriceModels.add(productWeightPriceModel);
        }
        return productWeightPriceModels;
    }

}
