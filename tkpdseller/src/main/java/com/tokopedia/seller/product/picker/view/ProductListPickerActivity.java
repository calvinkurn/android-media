package com.tokopedia.seller.product.picker.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.picker.common.ProductListPickerConstant;
import com.tokopedia.seller.product.picker.view.listener.ProductListPickerMultipleItem;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerActivity extends BasePickerMultipleItemActivity<ProductListPickerViewModel> implements ProductListPickerMultipleItem<ProductListPickerViewModel>, HasComponent<ProductComponent> {

    public static Intent createIntent(Context context, List<ProductListPickerViewModel> productListPickerViewModels){
        Intent intent = new Intent(context, ProductListPickerActivity.class);
        intent.putParcelableArrayListExtra(ProductListPickerConstant.PRODUCT_LIST_PICKER_MODEL_EXTRA, new ArrayList<Parcelable>(productListPickerViewModels));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateBottomSheetInfo();
    }

    @Override
    protected Fragment getInitialSearchListFragment() {
        return new ProductListPickerSearchFragment();
    }

    @Override
    protected Fragment getInitialCacheListFragment() {
        return new ProductListPickerCacheFragment();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        bottomSheetContentTextView.setText(getString(R.string.product_list_featured_max_limit, ProductListPickerConstant.MAX_LIMIT_PRODUCT_LIST_FEATURED_PICKER));
        bottomSheetContentTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ProductListPickerConstant.EXTRA_PRODUCT_LIST_SUBMIT, new ArrayList<Parcelable>(getItemListCache()));
        return intent;
    }

    @Override
    public void validateFooterAndInfoView() {
        if (getSearchListSize() <1 && getCacheListSize() < 1) {
            showFooterAndInfo(false);
        } else {
            showFooterAndInfo(true);
        }
    }

    @Override
    public boolean allowAddItem() {
        if (isMaxVariantReached()) {
            showMaxVariantReachedMessage();
            return false;
        }
        return true;
    }

    @Override
    public void removeItemFromCache(ProductListPickerViewModel productListPickerViewModel) {
        super.removeItemFromCache(productListPickerViewModel);
        updateBottomSheetInfo();
    }

    @Override
    public void addItemFromSearch(ProductListPickerViewModel productListPickerViewModel) {
        super.addItemFromSearch(productListPickerViewModel);
        updateBottomSheetInfo();
    }

    @Override
    public void removeItemFromSearch(ProductListPickerViewModel productListPickerViewModel) {
        super.removeItemFromSearch(productListPickerViewModel);
        updateBottomSheetInfo();
    }

    private boolean isMaxVariantReached(){
        return ((ProductListPickerCacheFragment) getCacheListFragment()).getItemList().size()
                >= ProductListPickerConstant.MAX_LIMIT_PRODUCT_LIST_FEATURED_PICKER;
    }

    private void showMaxVariantReachedMessage(){
        NetworkErrorHelper.showCloseSnackbar(this,getString(R.string.product_list_picker_max_has_been_reached));
    }

    private void updateBottomSheetInfo() {
        bottomSheetTitleTextView.setText(getString(R.string.product_list_picker_variant_count,
                getCacheListSize()));
        if (getCacheListSize() > 0) {
            showBottomSheetInfo(true);
        } else {
            showBottomSheetInfo(false);
        }
    }

    private int getSearchListSize() {
        Fragment fragment = getSearchListFragment();
        int selectedItemSize = 0;
        if ((fragment) != null) {
            selectedItemSize = ((ProductListPickerSearchFragment) fragment).getItemList().size();
        }
        return selectedItemSize;
    }

    private int getCacheListSize() {
        return getItemListCache().size();
    }

    private List<ProductListPickerViewModel> getItemListCache(){
        Fragment fragment = getCacheListFragment();
        List<ProductListPickerViewModel> productListPickerViewModels = new ArrayList<>();
        if ((fragment) != null) {
            productListPickerViewModels = ((ProductListPickerCacheFragment) fragment).getItemList();
        }
        return productListPickerViewModels;
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter)getApplication()).getProductComponent();
    }
}
