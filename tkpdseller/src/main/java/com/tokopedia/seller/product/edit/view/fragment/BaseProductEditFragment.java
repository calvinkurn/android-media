package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseProductEditFragment<T extends ProductAddPresenter>
        extends BaseProductAddEditFragment<T>
        implements ProductEditView{

    private TkpdProgressDialog tkpdProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState == null) {
            fetchInputData();
        }
        return view;
    }

    public abstract void fetchInputData();

    @CallSuper
    public void onSuccessLoadProduct(ProductViewModel model) {
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

        //TODO hendry set variant view
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

    protected void showLoading() {
        if (tkpdProgressDialog==null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(),
                    TkpdProgressDialog.NORMAL_PROGRESS, getString(R.string.edit_product));
        }
        tkpdProgressDialog.showDialog();
    }

    protected void hideLoading() {
        if (tkpdProgressDialog!= null) {
            tkpdProgressDialog.dismiss();
        }
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
    public void onErrorLoadProduct(Throwable throwable) {
        hideLoading();
        CommonUtils.UniversalToast(getActivity(), getErrorLoadProductString());
        getActivity().finish();
    }

    public abstract String getErrorLoadProductString();

}
