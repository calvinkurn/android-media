package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.edit.di.module.ProductEditModule;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditPresenter;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditFragment extends BaseProductEditFragment<ProductEditPresenter> {

    protected static final String EDIT_PRODUCT_ID = "EDIT_PRODUCT_ID";

    private String productId;

    public static ProductEditFragment createInstance(String productId) {
        ProductEditFragment fragment = new ProductEditFragment();
        Bundle args = new Bundle();
        args.putString(EDIT_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        productId = getArguments().getString(EDIT_PRODUCT_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getStatusUpload() {
        return ProductStatus.EDIT;
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
    public void fetchInputData() {
        showLoading();
        presenter.attachView(this);
        presenter.fetchEditProductData(productId);
    }

    @Override
    public String getErrorLoadProductString() {
        return getString(R.string.msg_network_error);
    }
}