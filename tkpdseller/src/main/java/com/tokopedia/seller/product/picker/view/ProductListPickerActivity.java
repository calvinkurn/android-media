package com.tokopedia.seller.product.picker.view;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.picker.common.ProductListPickerConstant;
import com.tokopedia.seller.product.picker.view.listener.ProductListPickerMultipleItem;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;
import com.tokopedia.seller.product.common.di.component.ProductComponent;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerActivity extends BasePickerMultipleItemActivity<ProductListPickerViewModel> implements ProductListPickerMultipleItem<ProductListPickerViewModel>, HasComponent<ProductComponent> {

    public static final String EXTRA_PRODUCT_LIST_SUBMIT = "extra_product_list_submit";

    @Override
    protected Fragment getInitialSearchListFragment() {
        return new ProductListPickerSearchFragment();
    }

    @Override
    protected Fragment getInitialCacheListFragment() {
        return new ProductListPickerCacheFragment();
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRODUCT_LIST_SUBMIT, "");
        return intent;
    }

    @Override
    public void validateFooterAndInfoView() {
        if (getCacheListSize() < 1) {
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

    private boolean isMaxVariantReached(){
        return ((ProductListPickerCacheFragment) getCacheListFragment()).getItemList().size()
                >= ProductListPickerConstant.MAX_LIMIT_VARIANT;
    }

    private void showMaxVariantReachedMessage(){
        NetworkErrorHelper.showCloseSnackbar(this,getString(R.string.product_list_picker_max_has_been_reached));
    }


    private int getCacheListSize() {
        Fragment fragment = getCacheListFragment();
        int selectedItemSize = 0;
        if ((fragment) != null) {
            selectedItemSize = ((ProductListPickerCacheFragment) fragment).getItemList().size();
        }
        return selectedItemSize;
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter)getApplication()).getProductComponent();
    }
}
