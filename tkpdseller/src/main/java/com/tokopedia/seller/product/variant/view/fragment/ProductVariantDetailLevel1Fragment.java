package com.tokopedia.seller.product.variant.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.DividerItemDecoration;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailActivity;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDetailAdapter;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDetailLevel1Fragment extends Fragment implements ProductVariantDetailAdapter.OnProductVariantDataAdapterListener {

    public static final String SAVED_HAS_STOCK = "has_stk";

    private OnProductVariantDataManageFragmentListener listener;
    private LabelSwitch labelSwitchStatus;
    private View buttonSave;
    private ProductVariantDetailAdapter productVariantDetailAdapter;
    private boolean variantHasStock;

    public interface OnProductVariantDataManageFragmentListener {
        void onSubmitVariant(boolean isVariantHasStock, List<Long> selectedVariantValueIds);
    }

    public static ProductVariantDetailLevel1Fragment newInstance() {
        return new ProductVariantDetailLevel1Fragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityIntent = getActivity().getIntent();

        ArrayList<ProductVariantDetailViewModel> productVariantValueArrayList = activityIntent.getParcelableArrayListExtra(ProductVariantDetailActivity.EXTRA_VARIANT_VALUE_LIST);

        ArrayList<Long> variantValueIdList;
        if (savedInstanceState == null) {
            variantValueIdList = (ArrayList<Long>) activityIntent.getSerializableExtra(ProductVariantDetailActivity.EXTRA_SELECTED_VARIANT_ID_LIST);
            variantHasStock = activityIntent.getBooleanExtra(ProductVariantDetailActivity.EXTRA_VARIANT_HAS_STOCK, false);
        } else {
            variantValueIdList = (ArrayList<Long>)
                    savedInstanceState.getSerializable(ProductVariantDetailActivity.EXTRA_SELECTED_VARIANT_ID_LIST);
            variantHasStock = savedInstanceState.getBoolean(ProductVariantDetailActivity.EXTRA_VARIANT_HAS_STOCK, false);
        }
        if (variantValueIdList == null) {
            variantValueIdList = new ArrayList<>();
        }
        if (productVariantValueArrayList == null) {
            productVariantValueArrayList = new ArrayList<>();
        }

        productVariantDetailAdapter = new ProductVariantDetailAdapter(getContext(), productVariantValueArrayList, variantValueIdList);
        productVariantDetailAdapter.setOnProductVariantDataAdapterListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_variant_data, container, false);
        labelSwitchStatus = (LabelSwitch) view.findViewById(R.id.label_switch_product_status);
        buttonSave = view.findViewById(R.id.button_save);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(productVariantDetailAdapter);
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
                    productVariantDetailAdapter.checkAllItems();
                } else {
                    setStockLabelEmpty();
                    productVariantDetailAdapter.unCheckAllItems();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClicked();
            }
        });
        // default value: if all is checked, set label switch summary to tersedia else to kosong
        if (variantHasStock) {
            setStockLabelAvailable();
        } else {
            setStockLabelEmpty();
        }
    }

    public void onSubmitClicked(){
        listener.onSubmitVariant(labelSwitchStatus.isChecked(),
                productVariantDetailAdapter.getVariantValueIdListSorted());
    }

    private void setStockLabelAvailable() {
        labelSwitchStatus.setSummary(getString(R.string.product_variant_status_available));
        if (!labelSwitchStatus.isChecked()) {
            labelSwitchStatus.setCheckedNoListener(true);
        }
    }

    private void setStockLabelEmpty() {
        labelSwitchStatus.setSummary(getString(R.string.product_variant_status_not_available));
        if (labelSwitchStatus.isChecked()) {
            labelSwitchStatus.setCheckedNoListener(false);
        }
    }

    @Override
    public void onCheckAny() {
        labelSwitchStatus.setCheckedNoListener(true);
        setStockLabelAvailable();
    }

    @Override
    public void onUnCheckAll() {
        labelSwitchStatus.setCheckedNoListener(false);
        setStockLabelEmpty();
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onAttachListener(Context context) {
        listener = (OnProductVariantDataManageFragmentListener) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ProductVariantDetailActivity.EXTRA_SELECTED_VARIANT_ID_LIST,
                productVariantDetailAdapter.getVariantValueIdList());
        outState.putBoolean(SAVED_HAS_STOCK, labelSwitchStatus.isChecked());
    }
}