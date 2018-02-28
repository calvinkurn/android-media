package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerProductDraftComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftModule;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftPresenter;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddFragment extends BaseProductDraftAddEditFragment<ProductDraftPresenter> {

    public static Fragment createInstance(long draftProductId) {
        ProductDraftAddFragment fragment = new ProductDraftAddFragment();
        Bundle args = new Bundle();
        args.putLong(DRAFT_PRODUCT_ID, draftProductId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getStatusUpload() {
        return ProductStatus.ADD;
    }

    @Override
    public void initInjector() {
        DaggerProductDraftComponent
                .builder()
                .productComponent(getComponent(ProductComponent.class))
                .productDraftModule(new ProductDraftModule())
                .build()
                .inject(this);
    }

}
