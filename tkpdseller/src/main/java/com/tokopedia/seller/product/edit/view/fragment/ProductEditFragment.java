package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
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
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(EDIT_PRODUCT_ID);
    }

    @Override
    protected void initInjector() {
        DaggerProductEditComponent
                .builder()
                .productComponent(getComponent(ProductComponent.class))
                .productEditModule(new ProductEditModule())
                .build()
                .inject(this);
    }

    @Override
    protected void fetchInputData() {
        showLoading();
        presenter.attachView(this);
        fetchProductInfoData(productId);
    }

    private void fetchProductInfoData(String productId){
        presenter.fetchEditProductData(productId);
    }

    @Override
    public void onErrorFetchEditProduct(Throwable throwable) {
        hideLoading();
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                showLoading();
                fetchInputData();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}