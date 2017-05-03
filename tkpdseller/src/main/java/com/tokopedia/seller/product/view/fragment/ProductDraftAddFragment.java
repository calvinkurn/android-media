package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.AppComponent;
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

    public static Fragment createInstance(String productDraftId) {
        ProductDraftAddFragment fragment = new ProductDraftAddFragment();
        Bundle args = new Bundle();
        args.putString(DRAFT_PRODUCT_ID, productDraftId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter.attachView(this);
        fetchInputData();
        return view;
    }

    protected void fetchInputData() {
        String draftId = getArguments().getString(DRAFT_PRODUCT_ID);
        presenter.fetchDraftData(draftId);
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
    public void onSuccessLoadProduct(UploadProductInputViewModel model) {
        productInfoViewHolder.setName(model.getProductName());
        productInfoViewHolder.setCategoryId(model.getProductDepartmentId());
        fetchCategory(model.getProductDepartmentId());
        if (model.getProductCatalogId() > 0) {
            productInfoViewHolder.setCatalog(model.getProductCatalogId(), model.getProductCatalogName());
        }
        productImageViewHolder.setProductPhotos(model.getProductPhotos());

        productDetailViewHolder.setPriceUnit(model.getProductPriceCurrency());
        productDetailViewHolder.setPriceValue((float) model.getProductPrice());
        if (model.getProductWholesaleList().size() > 0) {
            productDetailViewHolder.expandWholesale(true);
            productDetailViewHolder.setWholesalePrice(model.getProductWholesaleList());
        }
        productDetailViewHolder.setWeightUnit(model.getProductWeightUnit());
        productDetailViewHolder.setWeightValue((float) model.getProductWeight());
        productDetailViewHolder.setMinimumOrder(model.getProductMinOrder());
        productDetailViewHolder.setStockStatus(model.getProductUploadTo());

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
}
