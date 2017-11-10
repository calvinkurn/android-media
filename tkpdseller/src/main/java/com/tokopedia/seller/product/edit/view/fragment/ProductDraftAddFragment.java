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
import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.di.component.DaggerProductDraftComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftModule;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftView;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

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
    public void onSuccessLoadDraftProduct(UploadProductInputViewModel model) {
        hideLoading();
        productInfoViewHolder.setName(model.getProductName());
        productInfoViewHolder.setCategoryId(model.getProductDepartmentId());
        onCategoryLoaded(model.getProductDepartmentId());
        if (model.getProductCatalogId() > 0) {
            productInfoViewHolder.setCatalog(model.getProductCatalogId(), model.getProductCatalogName());
        }
        productImageViewHolder.setProductPhotos(model.getProductPhotos(), getStatusUpload() == ProductStatus.EDIT);

        productDetailViewHolder.setPriceUnit(model.getProductPriceCurrency());
        if (model.getProductPrice()>0) {
            productDetailViewHolder.setPriceValue(model.getProductPrice());
        }
        if (model.getProductWholesaleList().size() > 0) {
            productDetailViewHolder.expandWholesale(true);
            productDetailViewHolder.setWholesalePrice(model.getProductWholesaleList());
        }
        productDetailViewHolder.setWeightUnit(model.getProductWeightUnit());
        if (model.getProductWeight() > 0) {
            productDetailViewHolder.setWeightValue(model.getProductWeight());
        }
        if (model.getProductMinOrder() > 0) {
            productDetailViewHolder.setMinimumOrder(model.getProductMinOrder());
        }
        productDetailViewHolder.setStockStatus(model.getProductUploadTo());
        productDetailViewHolder.setStockManaged(model.getProductInvenageSwitch() == InvenageSwitchTypeDef.TYPE_ACTIVE);
        productDetailViewHolder.setTotalStock(model.getProductInvenageValue());
        if (model.getProductEtalaseId() > 0) {
            productDetailViewHolder.setEtalaseId(model.getProductEtalaseId());
            productDetailViewHolder.setEtalaseName(model.getProductEtalaseName());
        }
        productDetailViewHolder.setCondition(model.getProductCondition());
        productDetailViewHolder.setInsurance(model.getProductMustInsurance());
        productDetailViewHolder.setFreeReturn(model.getProductReturnable());

        if (!TextUtils.isEmpty(model.getProductDescription())) {
            productAdditionalInfoViewHolder.setDescription(model.getProductDescription());
        }
        if (model.getProductVideos() != null) {
            productAdditionalInfoViewHolder.setVideoIdList(model.getProductVideos());
        }
        if (model.getPoProcessValue() > 0) {
            productAdditionalInfoViewHolder.expandPreOrder(true);
            productAdditionalInfoViewHolder.setPreOrderUnit(model.getPoProcessType());
            productAdditionalInfoViewHolder.setPreOrderValue(model.getPoProcessValue());
        }
        if (model.getSwitchVariant() == ProductVariantConstant.SWITCH_VARIANT_EXIST) {
            productAdditionalInfoViewHolder.setProductVariantDataSubmit(model.getProductVariantDataSubmit(),
                    model.getVariantStringSelection());
            productAdditionalInfoViewHolder.setOptionSubmitLv1(model.getProductVariantDataSubmit());
        }
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
