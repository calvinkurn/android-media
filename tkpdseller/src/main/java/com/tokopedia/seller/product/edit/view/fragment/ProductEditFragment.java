package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.base.di.component.AppComponent;
//import com.tokopedia.seller.product.edit.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.edit.di.module.ProductEditModule;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditView;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditFragment extends ProductDraftEditFragment implements ProductEditView {

    public static final String EDIT_PRODUCT_ID = "EDIT_PRODUCT_ID";

    @Inject
    public ProductEditPresenter presenter;

    public static ProductEditFragment createInstance(String productId) {
        ProductEditFragment fragment = new ProductEditFragment();
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
    protected void fetchInputData() {
        showLoading();
        presenter.attachView(this);
        String productId = getArguments().getString(EDIT_PRODUCT_ID);
        presenter.fetchEditProductData(productId);
    }

}