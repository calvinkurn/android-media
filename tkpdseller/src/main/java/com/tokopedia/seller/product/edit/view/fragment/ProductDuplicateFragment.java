package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.edit.di.module.ProductEditModule;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditView;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDuplicateFragment extends ProductDraftAddFragment implements ProductEditView {

    public static final String EDIT_PRODUCT_ID = "EDIT_PRODUCT_ID";

    @Inject
    public ProductEditPresenter presenter;
    private String productNameBeforeCopy;

    private String productId;

    public static ProductDuplicateFragment createInstance(String productId) {
        ProductDuplicateFragment fragment = new ProductDuplicateFragment();
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
    public void onSuccessLoadDraftProduct(UploadProductInputViewModel model) {
        super.onSuccessLoadDraftProduct(model);
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
        });
    }

}
