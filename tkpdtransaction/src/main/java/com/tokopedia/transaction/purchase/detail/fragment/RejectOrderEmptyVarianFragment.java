package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.RejectOrderEmptyVarianAdapter;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailItemData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.listener.ToolbarChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderEmptyVarianFragment extends RejectOrderBaseFragment{

    private RejectOrderEmptyVarianFragmentListener listener;

    private ToolbarChangeListener toolbarListener;

    private RejectOrderEmptyVarianAdapter adapter;

    public static final int FRAGMENT_EDIT_EMPTY_VARIAN_REQUEST_CODE = 22;

    public static RejectOrderEmptyVarianFragment createFragment(OrderDetailData data) {
        RejectOrderEmptyVarianFragment fragment = new RejectOrderEmptyVarianFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_DETAIL_DATA_KEY, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RejectOrderEmptyVarianFragmentListener) activity;
        toolbarListener = (ToolbarChangeListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RejectOrderEmptyVarianFragmentListener) context;
        toolbarListener = (ToolbarChangeListener) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected String defineTitle() {
        return "Perbarui keterangan varian pada diskusi produk";
    }

    @Override
    protected RecyclerView.Adapter initAdapter(OrderDetailData data) {
        adapter = new RejectOrderEmptyVarianAdapter(
                generateEmptyVarianProductList(
                        data.getOrderId(),
                        data.getShopId(),
                        data.getItemList()),
                adapterListener()
        );
        return adapter;
    }

    private List<EmptyVarianProductEditable> generateEmptyVarianProductList(
            String orderId,
            String shopId,
            List<OrderDetailItemData> itemDataList
    ) {
        List<EmptyVarianProductEditable> emptyVarianProductEditables = new ArrayList<>();
        for(int i =0; i < itemDataList.size(); i++) {
            EmptyVarianProductEditable emptyVarianProductEditable = new EmptyVarianProductEditable();
            emptyVarianProductEditable.setOrderId(orderId);
            emptyVarianProductEditable.setShopId(shopId);
            emptyVarianProductEditable.setProductId(itemDataList.get(i).getProductId());
            emptyVarianProductEditable.setProductImage(itemDataList.get(i).getImageUrl());
            emptyVarianProductEditable.setProductName(itemDataList.get(i).getItemName());
            emptyVarianProductEditable.setProductPrice(itemDataList.get(i).getPrice());
            emptyVarianProductEditable.setProductDescription(itemDataList.get(i).getDescription());
            emptyVarianProductEditables.add(emptyVarianProductEditable);
        }
        return emptyVarianProductEditables;
    }

    private RejectOrderEmptyVarianAdapter.RejectOrderEmptyVarianAdapterListener adapterListener() {
        return new RejectOrderEmptyVarianAdapter.RejectOrderEmptyVarianAdapterListener() {
            @Override
            public void onItemSelected(EmptyVarianProductEditable emptyVarianProductEditable) {
                RejectOrderEmptyVarianEditFragment editFragment = RejectOrderEmptyVarianEditFragment
                        .createFragment(emptyVarianProductEditable);
                if(getFragmentManager()
                        .findFragmentByTag(FRAGMENT_REJECT_ORDER_SUB_MENU_TAG) == null) {
                    editFragment.setTargetFragment(
                            RejectOrderEmptyVarianFragment.this,
                            FRAGMENT_EDIT_EMPTY_VARIAN_REQUEST_CODE
                    );
                    toolbarListener
                            .onChangeTitle(getActivity()
                                    .getString(R.string.label_change_varian_description));
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

    @Override
    protected View.OnClickListener initConfirmButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRejectEmptyVarian(adapter.getEmptyVarianProductEditables());
            }
        };
    }

    public interface RejectOrderEmptyVarianFragmentListener {

        void onRejectEmptyVarian(List<EmptyVarianProductEditable> editableList);

    }
}
