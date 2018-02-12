package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.edit.di.module.ProductEditModule;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditView;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDuplicateFragment extends ProductEditFragment implements ProductEditView {

    private String productNameBeforeCopy;

    public static ProductDuplicateFragment createInstance(String productId) {
        ProductDuplicateFragment fragment = new ProductDuplicateFragment();
        Bundle args = new Bundle();
        args.putString(EDIT_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getStatusUpload() {
        return ProductStatus.ADD;
    }

    @Override
    public boolean isNeedGetCategoryRecommendation() {
        return false;
    }

    @Override
    public void initInjector() {
        DaggerProductEditComponent
                .builder()
                .productComponent(getComponent(ProductComponent.class))
                .productEditModule(new ProductEditModule())
                .build()
                .inject(this);
    }

    @Override
    public void onSuccessLoadProduct(ProductViewModel model) {
        super.onSuccessLoadProduct(model);
        productNameBeforeCopy = model.getProductName();
    }

    @Override
    protected boolean isDataValid() {
        boolean dataValid = super.isDataValid();
        if (!productInfoViewHolder.checkWithPreviousNameBeforeCopy(productNameBeforeCopy).first){
            UnifyTracking.eventAddProductError(productInfoViewHolder.isDataValid().second);
            return false;
        }
        return dataValid;
    }

}
