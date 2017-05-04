package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.di.module.ProductEditModule;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.view.presenter.ProductEditPresenter;
import com.tokopedia.seller.product.view.presenter.ProductEditView;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDuplicateFragment extends ProductDraftAddFragment implements ProductEditView {

    public static final String EDIT_PRODUCT_ID = "EDIT_PRODUCT_ID";

    @Inject
    public ProductEditPresenter presenter;

    public static ProductDuplicateFragment createInstance(String productId) {
        ProductDuplicateFragment fragment = new ProductDuplicateFragment();
        Bundle args = new Bundle();
        args.putString(EDIT_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initInjector() {
        DaggerProductEditComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .productEditModule(new ProductEditModule())
                .build()
                .inject(this);
    }

    @Override
    protected void fetchInputData(Bundle savedInstanceState) {
        showLoading();
        if (savedInstanceState != null){
            super.fetchInputData(savedInstanceState);
        } else {
            presenter.attachView(this);
            String productId = getArguments().getString(EDIT_PRODUCT_ID);
            presenter.fetchEditProductData(productId);
        }
    }

}
