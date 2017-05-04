package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.product.constant.SwitchTypeDef;
import com.tokopedia.seller.product.di.component.DaggerProductDraftComponent;
import com.tokopedia.seller.product.di.module.ProductDraftModule;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.presenter.ProductDraftPresenter;
import com.tokopedia.seller.product.view.presenter.ProductDraftView;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddFragment extends ProductAddFragment implements ProductDraftView {

    @Inject
    public ProductDraftPresenter presenter;

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";

    private String draftId;

    public static Fragment createInstance(String productDraftId) {
        ProductDraftAddFragment fragment = new ProductDraftAddFragment();
        Bundle args = new Bundle();
        args.putString(DRAFT_PRODUCT_ID, productDraftId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void fetchInputData(Bundle savedInstanceState) {
        showLoading();
        if (savedInstanceState != null){
            super.fetchInputData(savedInstanceState);
        } else {
            draftId = getArguments().getString(DRAFT_PRODUCT_ID);
            presenter.fetchDraftData(draftId);
        }
    }

    @Override
    protected void initInjector() {
        DaggerProductDraftComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .productDraftModule(new ProductDraftModule())
                .build()
                .inject(this);
    }

    @Override
    public void onErrorLoadProduct(String errorMessage) {
        hideLoading();
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                showLoading();
                presenter.fetchDraftData(draftId);
            }
        });
    }

}
