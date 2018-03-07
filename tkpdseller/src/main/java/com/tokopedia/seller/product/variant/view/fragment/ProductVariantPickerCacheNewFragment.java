package com.tokopedia.seller.product.variant.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListNewAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerItemCacheList;

import java.util.ArrayList;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerCacheNewFragment extends BaseCacheListFragment<ProductVariantOption>
        implements ProductVariantPickerCacheListNewAdapter.RemoveCallback<ProductVariantOption>,
        ProductVariantPickerItemCacheList<ProductVariantOption> {

    //private int startTempId;
    //private long currentTempId;

    public OnProductVariantPickerCacheNewFragmentListener onProductVariantPickerCacheNewFragmentListener;
    public interface OnProductVariantPickerCacheNewFragmentListener{
        boolean isDataColorType();
    }

    /**
     * Use temporary id to avoid lost value if regenerate (check and uncheck) temporary id
     */
    //private HashMap<String, Long> temporaryIdMap;

    public static ProductVariantPickerCacheNewFragment newInstance() {

        Bundle args = new Bundle();
        ProductVariantPickerCacheNewFragment fragment = new ProductVariantPickerCacheNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startTempId = getActivity().getIntent().getIntExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_START_TEMP_ID, 0);
        //currentTempId = startTempId;
        //temporaryIdMap = new HashMap<>();
    }

    @Override
    protected BaseListAdapter<ProductVariantOption> getNewAdapter() {
        ProductVariantPickerCacheListNewAdapter productVariantPickerCacheListNewAdapter =
                new ProductVariantPickerCacheListNewAdapter(
                onProductVariantPickerCacheNewFragmentListener.isDataColorType()
        );
        productVariantPickerCacheListNewAdapter.setRemoveCallback(this);
        return productVariantPickerCacheListNewAdapter;
    }

    @Override
    public void addItem(ProductVariantOption productVariantOption) {
//        if (TextUtils.isEmpty(productVariantViewModel.getId()) || productVariantViewModel.getTemporaryId() == 0) {
//            // Set id as temp id
//            productVariantViewModel.setTemporaryId(getTemporaryId(productVariantViewModel.getTitle()));
//        } else {
//            // Existing id, put no map
//            temporaryIdMap.put(productVariantViewModel.getTitle(), productVariantViewModel.getTemporaryId());
//        }
        itemList.add(productVariantOption);
        resetPageAndSearch();
    }

    @Override
    public void removeItem(ProductVariantOption productVariantOption) {
        itemList.remove(productVariantOption);
        resetPageAndSearch();
    }

    @Override
    public void onRemove(ProductVariantOption productVariantOption) {
        itemList.remove(productVariantOption);
        pickerMultipleItem.removeItemFromCache(productVariantOption);
        resetPageAndSearch();
    }

    @Override
    public void removeAllItem() {
        itemList = new ArrayList<>();
        resetPageAndSearch();
    }

//    /**
//     * Get temporary id from map or generate new temp id
//     *
//     * @param name
//     * @return
//     */
//    private long getTemporaryId(String name) {
//        Long temporaryId = temporaryIdMap.get(name);
//        if (temporaryId != null) {
//            // Return existing temp id
//            return temporaryId;
//        }
//        // Get available temporary id
//        boolean temporaryIdAvailable = false;
//        while (!temporaryIdAvailable) {
//            currentTempId++;
//            temporaryIdAvailable = isTemporaryIdAvailable(currentTempId);
//        }
//        // Put on map
//        temporaryIdMap.put(name, currentTempId);
//        return currentTempId;
//    }

//    /**
//     * Check if temporary id available
//     *
//     * @param tempId
//     * @return
//     */
//    private boolean isTemporaryIdAvailable(long tempId) {
//        for (Long tempIdFromMap : temporaryIdMap.values()) {
//            if (tempIdFromMap == tempId) {
//                return false;
//            }
//        }
//        return true;
//    }

//    private ProductVariantViewModel getVariantByUnitValueId(long unitValueId) {
//        for (ProductVariantViewModel productVariantViewModel : itemList) {
//            if (productVariantViewModel.getUnitValueId() == unitValueId) {
//                return productVariantViewModel;
//            }
//        }
//        return null;
//    }
//
//    public List<ProductVariantOptionSubmit> getVariantSubmitOptionList() {
//        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
//        for (ProductVariantViewModel productVariantViewModel : itemList) {
//            ProductVariantOptionSubmit productVariantOptionSubmit = new ProductVariantOptionSubmit();
//            productVariantOptionSubmit.setTemporaryId(productVariantViewModel.getTemporaryId());
//            if (productVariantViewModel.getUnitValueId() > 0) {
//                productVariantOptionSubmit.setVariantUnitValueId(productVariantViewModel.getUnitValueId());
//            } else {
//                productVariantOptionSubmit.setCustomText(productVariantViewModel.getTitle());
//            }
//            productVariantOptionSubmitList.add(productVariantOptionSubmit);
//        }
//        return productVariantOptionSubmitList;
//    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onProductVariantPickerCacheNewFragmentListener = (OnProductVariantPickerCacheNewFragmentListener) context;
    }
}