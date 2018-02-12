package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.di.component.DaggerProductDraftComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftModule;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddFragment extends ProductAddFragment implements ProductDraftView {

    @Inject
    public ProductDraftPresenter presenter;

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";

    private TkpdProgressDialog tkpdProgressDialog;

    private String draftId;

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

        draftId = getArguments().getString(DRAFT_PRODUCT_ID);

        presenter.attachView(this);
        if (savedInstanceState == null) {
            fetchInputData();
        }
        return view;
    }

    protected void fetchInputData() {
        showLoading();
        presenter.fetchDraftData(draftId);
    }

    protected void showLoading() {
        if (tkpdProgressDialog==null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS, getString(R.string.edit_product));
        }
        tkpdProgressDialog.showDialog();
    }

    protected void hideLoading() {
        if (tkpdProgressDialog!= null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    protected void initInjector() {
        DaggerProductDraftComponent
                .builder()
                .productComponent(getComponent(ProductComponent.class))
                .productDraftModule(new ProductDraftModule())
                .build()
                .inject(this);
    }

    @Override
    @CallSuper
    /*
     * for onSuccess Variant for edit product, @see ProductEditFragment
     * {@link #onSuccessFetchProductVariantByPrd(ProductVariantByPrdModel)}
     */
    public void onSuccessLoadDraftProduct(ProductViewModel model) {
        hideLoading();
        productInfoViewHolder.setName(model.getProductName());
        productInfoViewHolder.setCategoryId(model.getProductCategory().getCategoryId());
        onCategoryLoaded(model.getProductCategory().getCategoryId());
        if (model.getProductCatalog().getCatalogId() > 0) {
            productInfoViewHolder.setCatalog(model.getProductCatalog().getCatalogId(), model.getProductCatalog().getCatalogName());
        }
        productImageViewHolder.setProductPhotos(model.getProductPicture());

        productDetailViewHolder.setPriceUnit((int)model.getProductPriceCurrency());
        if (model.getProductPrice()>0) {
            productDetailViewHolder.setPriceValue(model.getProductPrice());
        }
        if (model.getProductWholesale().size() > 0) {
            productDetailViewHolder.expandWholesale(true);
            productDetailViewHolder.setWholesalePrice(model.getProductWholesale());
        }
        productDetailViewHolder.setWeightUnit((int)model.getProductWeightUnit());
        if (model.getProductWeight() > 0) {
            productDetailViewHolder.setWeightValue((int)model.getProductWeight());
        }
        if (model.getProductMinOrder() > 0) {
            productDetailViewHolder.setMinimumOrder((int)model.getProductMinOrder());
        }
        productDetailViewHolder.setStockStatus(model.getProductStatus());
        productDetailViewHolder.setStockManaged(model.getProductStatus() == UploadToTypeDef.TYPE_ACTIVE && model.getProductStock() > 0);
        productDetailViewHolder.setTotalStock((int)model.getProductStock());
        if (model.getProductEtalase().getEtalaseId() > 0) {
            productDetailViewHolder.setEtalaseId(model.getProductEtalase().getEtalaseId());
            productDetailViewHolder.setEtalaseName(model.getProductEtalase().getEtalaseName());
        }
        productDetailViewHolder.setCondition((int)model.getProductCondition());
        productDetailViewHolder.setInsurance(model.isProductMustInsurance());
        productDetailViewHolder.setFreeReturn(model.isProductFreeReturn());

        if (!TextUtils.isEmpty(model.getProductDescription())) {
            productAdditionalInfoViewHolder.setDescription(model.getProductDescription());
        }
        if (model.getProductVideo() != null) {
            productAdditionalInfoViewHolder.setVideoIdList(convertToListString(model.getProductVideo()));
        }
        if (model.getProductPreorder().getPreorderProcessTime() > 0) {
            productAdditionalInfoViewHolder.expandPreOrder(true);
            productAdditionalInfoViewHolder.setPreOrderUnit((int)model.getProductPreorder().getPreorderTimeUnit());
            productAdditionalInfoViewHolder.setPreOrderValue((int)model.getProductPreorder().getPreorderProcessTime());
        }
//        if (model.getSwitchVariant() == ProductVariantConstant.SWITCH_VARIANT_EXIST) {
//            productAdditionalInfoViewHolder.setProductVariantDataSubmit(model.getProductVariantDataSubmit(),
//                    model.getVariantStringSelection());
//            productAdditionalInfoViewHolder.setOptionSubmitLv1(model.getProductVariantDataSubmit());
//        }
    }

    private List<String> convertToListString(List<ProductVideoViewModel> productVideo) {
        List<String> productVideos = new ArrayList<>();
        for(ProductVideoViewModel productVideoViewModel : productVideo){
            productVideos.add(productVideoViewModel.getUrl());
        }
        return productVideos;
    }

    @Override
    public boolean hasDataAdded() {
        // this is to enable always save to draft
        return true;
    }

    protected void saveDefaultModel(){
        // in draft and edit mode, no need to save default model.
        // no op;
    }

    @Override
    public long getProductDraftId() {
        if (TextUtils.isEmpty(draftId)) {
            return 0;
        }
        try {
            return Long.valueOf(draftId);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void onErrorLoadDraftProduct(Throwable throwable) {
        hideLoading();
        CommonUtils.UniversalToast(getActivity(), getString(R.string.product_draft_error_cannot_load_draft));
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
