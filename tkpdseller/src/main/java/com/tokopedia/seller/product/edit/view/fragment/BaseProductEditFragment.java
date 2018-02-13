package com.tokopedia.seller.product.edit.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.imageeditor.GalleryCropWatermarkActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkActivity;
import com.tokopedia.seller.instoped.InstopedSellerCropWatermarkActivity;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.edit.view.activity.CatalogPickerActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddInfoActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductScoringDetailActivity;
import com.tokopedia.seller.product.edit.view.activity.YoutubeAddVideoActivity;
import com.tokopedia.seller.product.edit.view.dialog.ImageAddDialogFragment;
import com.tokopedia.seller.product.edit.view.dialog.ImageDescriptionDialog;
import com.tokopedia.seller.product.edit.view.dialog.ImageEditProductDialogFragment;
import com.tokopedia.seller.product.edit.view.holder.ProductAdditionalInfoViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductImageViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductInfoViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductScoreViewHolder;
import com.tokopedia.seller.product.edit.view.listener.ProductAddView;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.edit.view.mapper.AnalyticsMapper;
import com.tokopedia.seller.product.edit.view.model.ImageSelectModel;
import com.tokopedia.seller.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditView;
import com.tokopedia.seller.product.edit.view.widget.ImagesSelectView;
import com.tokopedia.seller.product.etalase.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;

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
