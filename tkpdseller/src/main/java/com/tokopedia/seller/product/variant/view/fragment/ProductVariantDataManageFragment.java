package com.tokopedia.seller.product.variant.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDataManageActivity;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDataManageFragment extends BasePresenterFragment implements ProductVariantDataAdapter.OnProductVariantDataAdapterListener {

    private OnProductVariantDataManageFragmentListener listener;
    private LabelSwitch labelSwitchStatus;
    private View buttonSave;
    private ProductVariantDataAdapter productVariantDataAdapter;

    public interface OnProductVariantDataManageFragmentListener {
        void onSubmitVariant(List<Long> selectedVariantValueIds);
    }

    public static ProductVariantDataManageFragment newInstance() {
        return new ProductVariantDataManageFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityIntent = getActivity().getIntent();
        // TODO hendry just for test
        ArrayList<Long> variantValueIdList;
        ArrayList<ProductVariantValue> productVariantValueArrayList;
        if (!activityIntent.hasExtra(ProductVariantDataManageActivity.EXTRA_VARIANT_VALUE_LIST)) {
            productVariantValueArrayList = new ArrayList<>();
            productVariantValueArrayList.add(new ProductVariantValue(1,"Cak Lontong"));
            productVariantValueArrayList.add(new ProductVariantValue(2,"Cak Norman"));
            productVariantValueArrayList.add(new ProductVariantValue(3,"Cak Norman tong"));
            productVariantValueArrayList.add(new ProductVariantValue(4,"Cak tong norman"));
            variantValueIdList = new ArrayList<>();
            variantValueIdList.add(1L);
            variantValueIdList.add(3L);
        }
        else {
            productVariantValueArrayList = activityIntent.getParcelableArrayListExtra(ProductVariantDataManageActivity.EXTRA_VARIANT_VALUE_LIST);

            if (savedInstanceState == null) {
                variantValueIdList = activityIntent.getParcelableExtra(ProductVariantDataManageActivity.EXTRA_SELECTED_VARIANT_ID_LIST);
            } else {
                variantValueIdList = (ArrayList<Long>)
                        savedInstanceState.getSerializable(ProductVariantDataManageActivity.EXTRA_SELECTED_VARIANT_ID_LIST);
            }
        }

        productVariantDataAdapter = new ProductVariantDataAdapter(getContext(), productVariantValueArrayList, variantValueIdList);
        productVariantDataAdapter.setOnProductVariantDataAdapterListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_variant_data, container, false);
        labelSwitchStatus = (LabelSwitch) view.findViewById(R.id.label_switch_product_status);
        buttonSave = view.findViewById(R.id.button_save);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(productVariantDataAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        labelSwitchStatus.setListenerValue(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    setStockLabelAvailable();
                    productVariantDataAdapter.checkAllItems();
                } else {
                    setStockLabelEmpty();
                    productVariantDataAdapter.unCheckAllItems();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSubmitVariant(productVariantDataAdapter.getVariantValueIdList());
            }
        });
        // default value: if all is checked, set label switch summary to tersedia else to kosong
        if (productVariantDataAdapter.isCheckAll()) {
            setStockLabelAvailable();
        } else {
            setStockLabelEmpty();
        }
    }

    private void setStockLabelAvailable(){
        labelSwitchStatus.setSummary(getString(R.string.product_variant_status_available));
    }

    private void setStockLabelEmpty(){
        labelSwitchStatus.setSummary(getString(R.string.product_variant_status_not_available));
    }

    @Override
    public void onCheckAll() {
        labelSwitchStatus.setChecked(true);
    }

    @Override
    public void onUnCheckAll() {
        labelSwitchStatus.setChecked(false);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        listener = (OnProductVariantDataManageFragmentListener) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ProductVariantDataManageActivity.EXTRA_SELECTED_VARIANT_ID_LIST,
                productVariantDataAdapter.getVariantValueIdList());
    }
}