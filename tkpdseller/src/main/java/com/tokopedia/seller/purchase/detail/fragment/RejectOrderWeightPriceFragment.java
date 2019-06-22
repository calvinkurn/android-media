package com.tokopedia.seller.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.purchase.detail.adapter.RejectOrderPriceWeightChangeAdapter;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.common.data.order.OrderDetailItemData;
import com.tokopedia.seller.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;
import com.tokopedia.transaction.common.listener.ToolbarChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderWeightPriceFragment extends RejectOrderBaseFragment {

    public static final int FRAGMENT_EDIT_WEIGHT_PRICE_REQUEST_CODE = 24;

    private static final int FIXED_KILOGRAM_MODE = 2;

    private static final int FIXED_GRAM_MODE = 1;

    private static final int FIXED_RUPIAH_MODE = 1;

    private RejectOrderChangeWeightPriceListener listener;

    private ToolbarChangeListener toolbarListener;

    private RejectOrderPriceWeightChangeAdapter adapter;

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
        return getString(R.string.title_reject_order_weight_price_fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RejectOrderChangeWeightPriceListener)  activity;
        toolbarListener = (ToolbarChangeListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RejectOrderChangeWeightPriceListener)  context;
        toolbarListener = (ToolbarChangeListener) context;
    }

    @Override
    protected RecyclerView.Adapter initAdapter(OrderDetailData data) {
        adapter = new RejectOrderPriceWeightChangeAdapter(
                generateProductPriceWeight(
                        data.getOrderId(),
                        data.getShopId(),
                        data.getItemList()), adapterListener());
        return adapter;
    }

    private RejectOrderPriceWeightChangeAdapter
            .RejectOrderPriceWeightAdapterListener adapterListener() {
        return new RejectOrderPriceWeightChangeAdapter.RejectOrderPriceWeightAdapterListener() {
            @Override
            public void onProductChoosen(WrongProductPriceWeightEditable model) {
                RejectOrderProductPriceWeightEditFragment editFragment =
                        RejectOrderProductPriceWeightEditFragment.createFragment(model);
                if(getFragmentManager()
                        .findFragmentByTag(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG) == null) {
                    editFragment.setTargetFragment(
                            RejectOrderWeightPriceFragment.this,
                            FRAGMENT_EDIT_WEIGHT_PRICE_REQUEST_CODE
                    );
                    toolbarListener.onChangeTitle(getActivity()
                            .getString(R.string.label_change_weight_price));
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                            .add(R.id.main_view,
                                    editFragment,
                                    FRAGMENT_REJECT_ORDER_SUB_MENU_TAG)
                            .commit();
                }
            }
        };
    }

    @Override
    protected View.OnClickListener initConfirmButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.isEdited())
                    listener.onConfirmWeightPrice(adapter.getProductPriceWeightData());
                else NetworkErrorHelper.showSnackbar(
                        getActivity(), getString(R.string.error_no_change_price_weight)
                );
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
            productWeightPriceModel.setProductWeight(itemDataList.get(i).getWeight());
            productWeightPriceModel.setCurrencyMode(FIXED_RUPIAH_MODE);
            productWeightPriceModel.setWeightMode(FIXED_GRAM_MODE);
            productWeightPriceModel
                    .setProductPriceUnformatted(itemDataList.get(i).getPriceUnformatted());
            int convertedWeightFormat = (int) Math.round(Double
                    .parseDouble(itemDataList.get(i).getWeightUnformatted()) * 1000);
            productWeightPriceModel
                    .setProductWeightUnformatted(String.valueOf(convertedWeightFormat));
            productWeightPriceModels.add(productWeightPriceModel);
        }
        return productWeightPriceModels;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.exit_bottom, R.animator.exit_bottom)
                .remove(getFragmentManager().findFragmentByTag(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG))
                .commit();
        toolbarListener.onRemoveTitle();
    }

    public interface RejectOrderChangeWeightPriceListener {

        void onConfirmWeightPrice(List<WrongProductPriceWeightEditable> listOfEditable);

    }

}
