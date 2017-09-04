package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerItemCacheList;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerCacheFragment extends BaseCacheListFragment<ProductVariantViewModel> implements ProductVariantPickerCacheListAdapter.RemoveCallback<ProductVariantViewModel>, ProductVariantPickerItemCacheList<ProductVariantViewModel> {

    private int startTempId;
    private long currentTempId;

    /**
     * Use temporary id to avoid lost value if regenerate (check and uncheck) temporary id
     */
    private HashMap<String, Long> temporaryIdMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTempId = getActivity().getIntent().getIntExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_START_TEMP_ID, 0);
        currentTempId = startTempId;
        temporaryIdMap = new HashMap<>();
    }

    @Override
    protected BaseListAdapter<ProductVariantViewModel> getNewAdapter() {
        ProductVariantPickerCacheListAdapter productVariantPickerCacheListAdapter = new ProductVariantPickerCacheListAdapter();
        productVariantPickerCacheListAdapter.setRemoveCallback(this);
        return productVariantPickerCacheListAdapter;
    }

    @Override
    public void addItem(ProductVariantViewModel productVariantViewModel) {
        if (TextUtils.isEmpty(productVariantViewModel.getId()) || productVariantViewModel.getTemporaryId() == 0) {
            // Set id as temp id
            productVariantViewModel.setTemporaryId(getTemporaryId(productVariantViewModel.getTitle()));
        } else {
            // Existing id, put no map
            temporaryIdMap.put(productVariantViewModel.getTitle(), productVariantViewModel.getTemporaryId());
        }
        itemList.add(productVariantViewModel);
        resetPageAndSearch();
    }

    @Override
    public void removeItem(ProductVariantViewModel productVariantViewModel) {
        ProductVariantViewModel productVariantViewModelTemp = getVariantByUnitValueId(productVariantViewModel.getUnitValueId());
        itemList.remove(productVariantViewModelTemp);
        resetPageAndSearch();
    }

    @Override
    public void onRemove(ProductVariantViewModel productVariantViewModel) {
        productVariantViewModel.setTemporaryId(0);
        itemList.remove(productVariantViewModel);
        pickerMultipleItem.removeItemFromCache(productVariantViewModel);
        resetPageAndSearch();
    }

    @Override
    public void removeAllItem() {
        itemList = new ArrayList<>();
        resetPageAndSearch();
    }

    /**
     * Get temporary id from map or generate new temp id
     *
     * @param name
     * @return
     */
    private long getTemporaryId(String name) {
        Long temporaryId = temporaryIdMap.get(name);
        if (temporaryId != null) {
            // Return existing temp id
            return temporaryId;
        }
        // Get available temporary id
        boolean temporaryIdAvailable = false;
        while (!temporaryIdAvailable) {
            currentTempId++;
            temporaryIdAvailable = isTemporaryIdAvailable(currentTempId);
        }
        // Put on map
        temporaryIdMap.put(name, currentTempId);
        return currentTempId;
    }

    /**
     * Check if temporary id available
     *
     * @param tempId
     * @return
     */
    private boolean isTemporaryIdAvailable(long tempId) {
        for (Long tempIdFromMap : temporaryIdMap.values()) {
            if (tempIdFromMap == tempId) {
                return false;
            }
        }
        return true;
    }

    private ProductVariantViewModel getVariantByUnitValueId(long unitValueId) {
        for (ProductVariantViewModel productVariantViewModel : itemList) {
            if (productVariantViewModel.getUnitValueId() == unitValueId) {
                return productVariantViewModel;
            }
        }
        return null;
    }

    public List<ProductVariantOptionSubmit> getVariantSubmitOptionList() {
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
        for (ProductVariantViewModel productVariantViewModel : itemList) {
            ProductVariantOptionSubmit productVariantOptionSubmit = new ProductVariantOptionSubmit();
            productVariantOptionSubmit.setTemporaryId(productVariantViewModel.getTemporaryId());
            if (productVariantViewModel.getUnitValueId() > 0) {
                productVariantOptionSubmit.setVariantUnitValueId(productVariantViewModel.getUnitValueId());
            } else {
                productVariantOptionSubmit.setCustomText(productVariantViewModel.getTitle());
            }
            productVariantOptionSubmitList.add(productVariantOptionSubmit);
        }
        return productVariantOptionSubmitList;
    }
}