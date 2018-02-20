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
        productImageViewHolder.setProductPhotos(model.getProductPictureViewModelList());

        productPriceViewHolder.setPriceUnit((int)model.getProductPriceCurrency());
        if (model.getProductPrice()>0) {
            productPriceViewHolder.setPriceValue(model.getProductPrice());
        }
        if (model.getProductWholesale().size() > 0) {
            productPriceViewHolder.expandWholesale(true);
            productPriceViewHolder.setWholesalePrice(model.getProductWholesale());
        }
        productDeliveryInfoViewHolder.setWeightUnit((int)model.getProductWeightUnit());
        if (model.getProductWeight() > 0) {
            productDeliveryInfoViewHolder.setWeightValue((int)model.getProductWeight());
        }
        if (model.getProductMinOrder() > 0) {
            productPriceViewHolder.setMinimumOrder((int)model.getProductMinOrder());
        }
        productManageViewHolder.setStockStatus(model.getProductStatus());
        productManageViewHolder.setStockManaged(model.getProductStatus() == UploadToTypeDef.TYPE_ACTIVE && model.getProductStock() > 0);
        productManageViewHolder.setTotalStock((int)model.getProductStock());
        if (model.getProductEtalase().getEtalaseId() > 0) {
            productInfoViewHolder.setEtalaseId(model.getProductEtalase().getEtalaseId());
            productInfoViewHolder.setEtalaseName(model.getProductEtalase().getEtalaseName());
        }
        productDescriptionViewHolder.setCondition((int)model.getProductCondition());
        productDeliveryInfoViewHolder.setInsurance(model.isProductMustInsurance());
        productDeliveryInfoViewHolder.setFreeReturn(model.isProductFreeReturn());

        if (!TextUtils.isEmpty(model.getProductDescription())) {
            productDescriptionViewHolder.setDescription(model.getProductDescription());
        }
        if (model.getProductVideo() != null) {
            productDescriptionViewHolder.setVideoIdList(convertToListString(model.getProductVideo()));
        }
        if (model.getProductPreorder().getPreorderProcessTime() > 0) {
            productDeliveryInfoViewHolder.expandPreOrder(true);
            productDeliveryInfoViewHolder.setPreOrderUnit((int)model.getProductPreorder().getPreorderTimeUnit());
            productDeliveryInfoViewHolder.setPreOrderValue((int)model.getProductPreorder().getPreorderProcessTime());
        }

        //TODO hendry set variant view
//        if (model.getProductVariant()!= null) {
//            productDeliveryInfoViewHolder.setProductVariantDataSubmit(model.getProductVariant(),
//                    model.getVariantStringSelection());
//            productDeliveryInfoViewHolder.setOptionSubmitLv1(model.getProductVariantDataSubmit());
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
