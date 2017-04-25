package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.di.module.ProductEditModule;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.presenter.ProductEditPresenter;
import com.tokopedia.seller.product.view.presenter.ProductEditView;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditFragment extends ProductAddFragment implements ProductEditView {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter.attachView(this);
        String productId = getArguments().getString(EDIT_PRODUCT_ID);
        presenter.fetchEditProductData(productId);
        return view;
    }

    @Override
    public void onSuccessLoadProduct(UploadProductInputViewModel model) {
        productInfoViewHolder.setName(model.getProductName());
        productInfoViewHolder.setCategoryId(model.getProductDepartmentId());
        if (model.getProductCatalogId() > 0) {
            productInfoViewHolder.setCatalog(model.getProductCatalogId(), model.getProductCatalogName());
        }
        productImageViewHolder.setProductPhotos(model.getProductPhotos());

        productDetailViewHolder.setPriceUnit(model.getProductPriceCurrency());
        productDetailViewHolder.setPriceValue((float) model.getProductPrice());
        productDetailViewHolder.setWeightUnit(model.getProductWeightUnit());
        productDetailViewHolder.setWeightValue((float) model.getProductWeight());
        productDetailViewHolder.setMinimumOrder(model.getProductMinOrder());
        productDetailViewHolder.setWholesalePrice(model.getProductWholesaleList());
//        productDetailViewHolder.setStockStatus(model.get);
//        productDetailViewHolder.setTotalStock();
        if (model.getProductEtalaseId() > 0) {
            productDetailViewHolder.setEtalaseId(model.getProductEtalaseId());
            productDetailViewHolder.setEtalaseName(model.getProductEtalaseName());
        }
        productDetailViewHolder.setCondition(model.getProductCondition());
        productDetailViewHolder.setInsurance(model.getProductMustInsurance());
        productDetailViewHolder.setFreeReturn(model.getProductReturnable());

        productAdditionalInfoViewHolder.setDescription(model.getProductDescription());
        if (model.getProductVideos() != null) {
            productAdditionalInfoViewHolder.setVideoIdList(model.getProductVideos());
        }
        if (model.getPoProcessValue() > 0) {
            productAdditionalInfoViewHolder.expandPreOrder(true);
            productAdditionalInfoViewHolder.setPreOrderUnit(model.getPoProcessType());
            productAdditionalInfoViewHolder.setPreOrderValue((float) model.getPoProcessValue());
        }
    }

    @Override
    public void onErrorLoadProduct(String errorMessage) {

    }

    @Override
    protected void getCategoryRecommendation(String productName) {
        // Do nothing
    }
}