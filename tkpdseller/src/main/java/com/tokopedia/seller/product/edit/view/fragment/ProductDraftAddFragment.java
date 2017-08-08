package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.di.component.DaggerProductDraftComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftModule;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftView;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddFragment extends ProductAddFragment implements ProductDraftView {

    @Inject
    public ProductDraftPresenter presenter;

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";

    private TkpdProgressDialog tkpdProgressDialog;
    private View coordinatorLayout;

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
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        presenter.attachView(this);
        fetchInputData();
        return view;
    }

    protected void fetchInputData() {
        showLoading();
        draftId = getArguments().getString(DRAFT_PRODUCT_ID);
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
                .appComponent(getComponent(AppComponent.class))
                .productDraftModule(new ProductDraftModule())
                .build()
                .inject(this);
    }

    @Override
    public void onSuccessLoadDraftProduct(UploadProductInputViewModel model) {
        hideLoading();
        productInfoViewHolder.setName(model.getProductName());
        productInfoViewHolder.setCategoryId(model.getProductDepartmentId());
        fetchCategory(model.getProductDepartmentId());
        if (model.getProductCatalogId() > 0) {
            productInfoViewHolder.setCatalog(model.getProductCatalogId(), model.getProductCatalogName());
        }
        productImageViewHolder.setProductPhotos(model.getProductPhotos(), getStatusUpload() == ProductStatus.EDIT);

        productDetailViewHolder.setPriceUnit(model.getProductPriceCurrency());
        productDetailViewHolder.setPriceValue(model.getProductPrice());
        if (model.getProductWholesaleList().size() > 0) {
            productDetailViewHolder.expandWholesale(true);
            productDetailViewHolder.setWholesalePrice(model.getProductWholesaleList());
        }
        productDetailViewHolder.setWeightUnit(model.getProductWeightUnit());
        productDetailViewHolder.setWeightValue(model.getProductWeight());
        productDetailViewHolder.setMinimumOrder(model.getProductMinOrder());
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

        productAdditionalInfoViewHolder.setDescription(model.getProductDescription());
        if (model.getProductVideos() != null) {
            productAdditionalInfoViewHolder.setVideoIdList(model.getProductVideos());
        }
        if (model.getPoProcessValue() > 0) {
            productAdditionalInfoViewHolder.expandPreOrder(true);
            productAdditionalInfoViewHolder.setPreOrderUnit(model.getPoProcessType());
            productAdditionalInfoViewHolder.setPreOrderValue(model.getPoProcessValue());
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

}
