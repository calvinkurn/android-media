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

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.imageeditor.GalleryCropWatermarkActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkActivity;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.instoped.InstopedSellerCropWatermarkActivity;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.edit.view.activity.ProductAddCatalogPickerActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddDescriptionPickerActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddVideoActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductScoringDetailActivity;
import com.tokopedia.seller.product.edit.view.dialog.ImageAddDialogFragment;
import com.tokopedia.seller.product.edit.view.dialog.ImageDescriptionDialog;
import com.tokopedia.seller.product.edit.view.dialog.ImageEditProductDialogFragment;
import com.tokopedia.seller.product.edit.view.holder.ProductDeliveryInfoViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductDescriptionViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductImageViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductInfoViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductManageViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductPriceViewHolder;
import com.tokopedia.seller.product.edit.view.holder.ProductScoreViewHolder;
import com.tokopedia.seller.product.edit.view.listener.ProductAddView;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.edit.view.mapper.AnalyticsMapper;
import com.tokopedia.seller.product.edit.view.model.ImageSelectModel;
import com.tokopedia.seller.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.edit.view.widget.ImagesSelectView;
import com.tokopedia.seller.product.etalase.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
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

@RuntimePermissions
public abstract class BaseProductAddEditFragment<T extends ProductAddPresenter>
        extends BaseDaggerFragment
        implements ProductAddView,
        ProductScoreViewHolder.Listener, ProductDeliveryInfoViewHolder.Listener,
        ProductImageViewHolder.Listener, ProductInfoViewHolder.Listener,
        ProductManageViewHolder.Listener, ProductPriceViewHolder.Listener, ProductDescriptionViewHolder.Listener {

    @Inject
    protected T presenter;

    public static final String SAVED_PRODUCT_VIEW_MODEL = "svd_prd_model";

    protected ProductScoreViewHolder productScoreViewHolder;
    protected ProductInfoViewHolder productInfoViewHolder;
    protected ProductImageViewHolder productImageViewHolder;
    protected ProductPriceViewHolder productPriceViewHolder;
    protected ProductManageViewHolder productManageViewHolder;
    protected ProductDescriptionViewHolder productDescriptionViewHolder;
    protected ProductDeliveryInfoViewHolder productDeliveryInfoViewHolder;
    private LabelSwitch shareLabelSwitch;

    protected ValueIndicatorScoreModel valueIndicatorScoreModel;

    // view model to be compare later when we want to save as draft
    protected ProductViewModel firstTimeViewModel;

    protected ProductViewModel currentProductViewModel;

    private Listener listener;

    public interface Listener {
        void startUploadProduct(long productId);

        void startUploadProductWithShare(long productId);

        void startAddWholeSaleDialog(@CurrencyTypeDef int currencyType, WholesaleModel previousWholesalePrice, boolean officialStore);

        void startUploadProductAndAddWithShare(Long productId);

        void startUploadProductAndAdd(Long productId);

        void successSaveDraftToDBWhenBackpressed();
    }

    protected abstract @ProductStatus
    int getStatusUpload();

    public abstract boolean isNeedGetCategoryRecommendation();

    @Override
    public abstract void initInjector();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valueIndicatorScoreModel = new ValueIndicatorScoreModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_add, container, false);

        productScoreViewHolder = new ProductScoreViewHolder(view.findViewById(R.id.relative_layout_product_scoring), this);
        productInfoViewHolder = new ProductInfoViewHolder(view.findViewById(R.id.view_group_product_info), this);
        productImageViewHolder = new ProductImageViewHolder(view.findViewById(R.id.view_group_product_image), this);
        productPriceViewHolder = new ProductPriceViewHolder(view.findViewById(R.id.view_group_product_price), this);
        productManageViewHolder = new ProductManageViewHolder(view.findViewById(R.id.view_group_product_manage), this);
        productDescriptionViewHolder = new ProductDescriptionViewHolder(view.findViewById(R.id.view_group_product_description), this);
        productDeliveryInfoViewHolder = new ProductDeliveryInfoViewHolder(view.findViewById(R.id.view_group_product_delivery), this);
        shareLabelSwitch = view.findViewById(R.id.label_switch_share);

        presenter.attachView(this);
        presenter.getShopInfo();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_VIEW_MODEL)) {
                currentProductViewModel = savedInstanceState.getParcelable(SAVED_PRODUCT_VIEW_MODEL);
                onSuccessLoadProduct(currentProductViewModel);
            }
            productInfoViewHolder.onViewStateRestored(savedInstanceState);
            productImageViewHolder.onViewStateRestored(savedInstanceState);
            productPriceViewHolder.onViewStateRestored(savedInstanceState);
            productManageViewHolder.onViewStateRestored(savedInstanceState);
            productDescriptionViewHolder.onViewStateRestored(savedInstanceState);
            productDeliveryInfoViewHolder.onViewStateRestored(savedInstanceState);
        }
        if (currentProductViewModel == null) {
            currentProductViewModel = new ProductViewModel();
        }

        View btnSave = view.findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid()) {
                    saveDraft(true);
                }
            }
        });
        View btnSaveAndAdd = view.findViewById(R.id.button_save_and_add);
        btnSaveAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataValid()) {
                    saveAndAddDraft();
                }
            }
        });
        view.requestFocus();
        return view;
    }

    protected void saveAndAddDraft() {
        ProductViewModel viewModel = collectDataFromView();
        sendAnalyticsAddMore(viewModel);
        if (viewModel.getProductVariant()!= null) {
            viewModel.getProductVariant().generateTid();
        }
        presenter.saveDraftAndAdd(viewModel);
    }

    @Override
    public long getProductDraftId() {
        return 0;
    }

    @CallSuper
    protected boolean isDataValid() {
        return (productInfoViewHolder.isDataValid() &&
                productPriceViewHolder.isDataValid() &&
                productManageViewHolder.isDataValid() &&
                (!productManageViewHolder.isStockAvailable() || productImageViewHolder.isDataValid()) &&
                productDeliveryInfoViewHolder.isDataValid());
    }

    private void getCategoryRecommendation(String productName) {
        if (isNeedGetCategoryRecommendation()) {
            presenter.getCategoryRecommendation(productName);
        }
    }

    @Override
    public void startAddWholeSaleDialog(@CurrencyTypeDef int currencyType, WholesaleModel previousWholesalePrice, boolean officialStore) {
        listener.startAddWholeSaleDialog(currencyType, previousWholesalePrice, officialStore);
    }


    // Clicked Part
    @Override
    public void onDetailProductScoringClicked() {
        Intent intent = ProductScoringDetailActivity.createIntent(getActivity(), valueIndicatorScoreModel);
        startActivity(intent);
    }

    @Override
    public void onAddImagePickerClicked(final int imagePosition) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ImageAddDialogFragment dialogFragment = ImageAddDialogFragment.newInstance(imagePosition);
        dialogFragment.show(fm, ImageAddDialogFragment.FRAGMENT_TAG);
        dialogFragment.setOnImageAddListener(new ImageAddDialogFragment.OnImageAddListener() {
            @Override
            public void clickAddProductFromCamera(int position) {
                BaseProductAddEditFragmentPermissionsDispatcher.goToCameraWithCheck(BaseProductAddEditFragment.this, imagePosition);
            }

            @Override
            public void clickAddProductFromGallery(int position) {
                BaseProductAddEditFragmentPermissionsDispatcher.goToGalleryWithCheck(BaseProductAddEditFragment.this, imagePosition);
            }

            @Override
            public void clickAddProductFromInstagram(int position) {
                int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), BaseProductAddEditFragment.this,
                        INSTAGRAM_SELECT_REQUEST_CODE, remainingEmptySlot);
            }
        });
    }

    @Override
    public void onImagePickerItemClicked(int position, boolean isPrimary) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialogFragment = ImageEditProductDialogFragment.newInstance(position, isPrimary);
        dialogFragment.show(fm, ImageEditProductDialogFragment.FRAGMENT_TAG);
        ((ImageEditProductDialogFragment) dialogFragment).setOnImageEditListener(new ImageEditProductDialogFragment.OnImageEditListener() {

            @Override
            public void clickEditImagePathFromCamera(int position) {
                GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), BaseProductAddEditFragment.this, position,
                        true, 1, true);
            }

            @Override
            public void clickEditImagePathFromGallery(int position) {
                GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), BaseProductAddEditFragment.this, position, 1, true);
            }

            @Override
            public void clickEditImagePathFromInstagram(int position) {
                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), BaseProductAddEditFragment.this,
                        INSTAGRAM_SELECT_REQUEST_CODE, 1);
            }

            @Override
            public void clickImageEditor(int position) {
                if (BaseProductAddEditFragment.this.isAddStatus()) {
                    UnifyTracking.eventClickImageInAddProduct(AppEventTracking.AddProduct.EVENT_ACTION_EDIT);
                } else {
                    UnifyTracking.eventClickImageInEditProduct(AppEventTracking.AddProduct.EVENT_ACTION_EDIT);
                }
                String uriOrPath = productImageViewHolder.getImagesSelectView().getImageAt(position).getUriOrPath();
                if (!TextUtils.isEmpty(uriOrPath)) {
                    onImageEditor(uriOrPath);
                }
            }

            @Override
            public void clickEditImageDesc(int position) {
                String currentDescription = productImageViewHolder.getImagesSelectView().getImageAt(position).getDescription();
                ImageDescriptionDialog fragment = ImageDescriptionDialog.newInstance(currentDescription);
                fragment.show(getActivity().getSupportFragmentManager(), ImageDescriptionDialog.TAG);
                fragment.setListener(new ImageDescriptionDialog.OnImageDescDialogListener() {
                    @Override
                    public void onImageDescDialogOK(String newDescription) {
                        productImageViewHolder.getImagesSelectView().changeImageDesc(newDescription);
                    }

                    @Override
                    public void onDismiss() {
                        BaseProductAddEditFragment.this.clearFocus();
                    }
                });
            }

            @Override
            public void clickEditImagePrimary(int position) {
                productImageViewHolder.getImagesSelectView().changeImagePrimary(true, position);
            }

            @Override
            public void clickRemoveImage(int positions) {
                ImagesSelectView imagesSelectView = productImageViewHolder.getImagesSelectView();
                ImageSelectModel imageSelectModel = imagesSelectView.getSelectedImage();
                if (imageSelectModel != null) {
                    String path = imageSelectModel.getUriOrPath();
                    if (!TextUtils.isEmpty(path) && !isEdittingDraft()) {
                        FileUtils.deleteAllCacheTkpdFile(path);
                    }
                }
                imagesSelectView.removeImage();
            }
        });
    }

    private boolean isEdittingDraft() {
        return isEditStatus() && getProductDraftId() > 0;
    }

    public boolean isAddStatus() {
        return getStatusUpload() == ProductStatus.ADD;
    }

    public boolean isEditStatus() {
        return getStatusUpload() == ProductStatus.EDIT;
    }

    @Override
    public void onImageEditor(String uriOrPath) {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(uriOrPath);
        ImageEditorWatermarkActivity.start(getContext(),
                BaseProductAddEditFragment.this, imageUrls,
                !isEdittingDraft());
    }

    @Override
    public void onRemovePreviousPath(String uri) {
        if (!TextUtils.isEmpty(uri) && !isEdittingDraft()) {
            FileUtils.deleteAllCacheTkpdFile(uri);
        }
    }

    private void clearFocus() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    CommonUtils.hideSoftKeyboard(view);
                    view.clearFocus();
                }
            }
        });
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToGallery(int imagePosition) {
        int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
        GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), this, imagePosition, remainingEmptySlot, true);
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToCamera(int imagePosition) {
        int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
        GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), this, imagePosition,
                true, remainingEmptySlot, true);

    }

    @Override
    public void goToProductDescriptionPicker(String description) {
        ProductAddDescriptionPickerActivity.start(this, ProductDescriptionViewHolder.REQUEST_CODE_GET_DESCRIPTION, description);
    }

    @Override
    public final void startYoutubeVideoActivity(ArrayList<String> videoIds) {
        Intent intent = new Intent(getActivity(), ProductAddVideoActivity.class);
        if (CommonUtils.checkCollectionNotNull(videoIds)) {
            intent.putStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK, videoIds);
        }
        startActivityForResult(intent, ProductDescriptionViewHolder.REQUEST_CODE_GET_VIDEO);
    }

    @Override
    public final void startProductVariantActivity(ArrayList<ProductVariantByCatModel> productVariantByCatModelList) {
        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.fetchProductVariantByCat(productInfoViewHolder.getCategoryId());
                }
            }).showRetrySnackbar();
            return;
        }
        Intent intent = ProductVariantDashboardActivity.getIntent(getActivity(),
                productVariantByCatModelList,
                currentProductViewModel.getProductVariant(),
                productPriceViewHolder.getCurrencyType(),
                productPriceViewHolder.getPriceValue(),
                productManageViewHolder.getViewStatusStock(),
                productPriceViewHolder.isOfficialStore(),
                productManageViewHolder.getSkuText(),
                isEdittingDraft(),
                currentProductViewModel.getProductSizeChart());
        startActivityForResult(intent, ProductManageViewHolder.REQUEST_CODE_VARIANT);
    }

    @Override
    public final void onCategoryPickerClicked(long categoryId) {
        CategoryPickerActivity.start(this, getActivity(), ProductInfoViewHolder.REQUEST_CODE_CATEGORY, categoryId);
    }

    @Override
    public final void onCatalogPickerClicked(String keyword, long depId, long selectedCatalogId) {
        ProductAddCatalogPickerActivity.start(this, getActivity(), ProductInfoViewHolder.REQUEST_CODE_CATALOG, keyword, depId, selectedCatalogId);
    }

    @Override
    public final void onEtalaseViewClicked(long etalaseId) {
        Intent intent = EtalasePickerActivity.createInstance(getContext(), etalaseId);
        startActivityForResult(intent, ProductInfoViewHolder.REQUEST_CODE_ETALASE);
    }

    @CallSuper
    public void onSuccessLoadProduct(ProductViewModel model) {
        if (model == null) {
            currentProductViewModel = new ProductViewModel();
        } else {
            currentProductViewModel = model;
        }

        productScoreViewHolder.renderData(currentProductViewModel, valueIndicatorScoreModel);

        productInfoViewHolder.renderData(currentProductViewModel);
        productImageViewHolder.renderData(currentProductViewModel);
        productPriceViewHolder.renderData(currentProductViewModel);
        productManageViewHolder.renderData(currentProductViewModel);
        productDescriptionViewHolder.renderData(currentProductViewModel);
        productDeliveryInfoViewHolder.renderData(currentProductViewModel);

        onCategoryLoaded(currentProductViewModel.getProductCategory().getCategoryId());
    }

    // Presenter listener part
    @Override
    public void onSuccessLoadScoringProduct(DataScoringProductView dataScoringProductView) {
        productScoreViewHolder.setValueProductScoreToView(dataScoringProductView);
    }

    @Override
    public void onErrorLoadScoringProduct(String errorMessage) {
    }


    @Override
    public void onSuccessStoreProductToDraft(long productId, boolean isUploading) {
        if (isUploading) {
            CommonUtils.UniversalToast(getActivity(), getString(R.string.upload_product_waiting));
            if (isShare()) {
                listener.startUploadProductWithShare(productId);
            } else {
                listener.startUploadProduct(productId);
            }
        } else {
            if (listener != null) {
                listener.successSaveDraftToDBWhenBackpressed();
            }
        }
    }

    @Override
    public void onErrorStoreProductToDraftWhenUpload(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), getString(R.string.try_again), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (isDataValid()) {
                    saveDraft(true);
                }
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onErrorStoreProductToDraftWhenBackPressed(String errorMessage) {
        CommonUtils.UniversalToast(getActivity(), errorMessage);
        getActivity().finish();
    }

    @Override
    public void onErrorStoreProductAndAddToDraft(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), getString(R.string.try_again), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (isDataValid()) {
                    saveAndAddDraft();
                }
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onSuccessLoadCatalog(List<Catalog> catalogViewModelList) {
        productInfoViewHolder.successFetchCatalogData(catalogViewModelList);
    }

    @Override
    public void onErrorLoadCatalog(String errorMessage) {
        productInfoViewHolder.hideAndClearCatalog();
    }

    @Override
    public void onSuccessLoadRecommendationCategory(List<ProductCategoryPredictionViewModel> categoryPredictionList) {
        productInfoViewHolder.successGetCategoryRecommData(categoryPredictionList);
    }

    @Override
    public void onErrorLoadRecommendationCategory(String errorMessage) {

    }

    @Override
    public void onSuccessLoadShopInfo(boolean isGoldMerchant, boolean isFreeReturn, boolean officialStore) {
        valueIndicatorScoreModel.setFreeReturnActive(isFreeReturn);

        productDescriptionViewHolder.updateViewGoldMerchant(isGoldMerchant);
        productPriceViewHolder.setGoldMerchant(isGoldMerchant);
        productPriceViewHolder.setOfficialStore(officialStore);
        productDeliveryInfoViewHolder.showViewFreeReturn(isFreeReturn);
    }

    @Override
    public void onErrorGetProductVariantByCat(Throwable throwable) {
        onSuccessGetProductVariantCat(null);
    }

    @Override
    public void onSuccessGetProductVariantCat(List<ProductVariantByCatModel> productVariantByCatModelList) {
        productManageViewHolder.onSuccessGetProductVariantCat(productVariantByCatModelList);
    }

    @Override
    public void onErrorLoadShopInfo(String errorMessage) {

    }

    @Override
    public void onSuccessStoreProductAndAddToDraft(Long productId) {
        CommonUtils.UniversalToast(getActivity(), getString(R.string.upload_product_waiting));
        if (isShare()) {
            listener.startUploadProductAndAddWithShare(productId);
        } else {
            listener.startUploadProductAndAdd(productId);
        }
    }

    // Others
    @Override
    public final void onResolutionImageCheckFailed(String uri) {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.error_image_resolution));
        FileUtils.deleteAllCacheTkpdFile(uri);
    }

    @Override
    public final void onCategoryChanged(long categoryId) {
        // as default, the variant inputted will be deleted (wihtout notice)
        currentProductViewModel.setProductVariant(null);
        productManageViewHolder.onSuccessGetProductVariantCat(null);
        // check catalog by categoryID
        onCategoryLoaded(categoryId);
    }

    /**
     * when category changed, or category has been fetched
     *
     * @param categoryId
     */
    public final void onCategoryLoaded(long categoryId) {
        // fetch product variant by category
        presenter.fetchProductVariantByCat(categoryId);
        // check catalog by categoryId
        checkIfCatalogExist(productInfoViewHolder.getName(), categoryId);
        // fetch category strings
        fetchCategory(categoryId);
    }

    @Override
    public final void fetchCategory(long categoryId) {
        presenter.fetchCategory(categoryId);
    }

    protected final void checkIfCatalogExist(String productName, long categoryId) {
        presenter.fetchCatalogData(productName, categoryId, 0, 1);
    }

    public final void addWholesaleItem(WholesaleModel wholesaleModel) {
        productPriceViewHolder.addWholesaleItem(wholesaleModel);
    }

    @Override
    public final void populateCategory(List<String> strings) {
        String[] stringArray = new String[strings.size()];
        stringArray = strings.toArray(stringArray);
        productInfoViewHolder.processCategory(stringArray);
    }

    public void deleteNotUsedTkpdCacheImage() {
        ArrayList<ImageSelectModel> imageSelectModelArrayList = productImageViewHolder.getImagesSelectView().getImageList();
        if (imageSelectModelArrayList == null || imageSelectModelArrayList.size() == 0) {
            return;
        }
        ArrayList<String> uriArrayList = new ArrayList<>();
        for (int i = 0, sizei = imageSelectModelArrayList.size(); i < sizei; i++) {
            uriArrayList.add(imageSelectModelArrayList.get(i).getUriOrPath());
        }
        FileUtils.deleteAllCacheTkpdFiles(uriArrayList);
    }

    public void saveDraft(boolean isUploading) {
        ProductViewModel viewModel = collectDataFromView();
        if (isUploading) {
            sendAnalyticsAdd(viewModel);
        }
        if (viewModel.getProductVariant()!= null) {
            viewModel.getProductVariant().generateTid();
        }
        presenter.saveDraft(viewModel, isUploading);
    }

    public boolean isShare() {
        return shareLabelSwitch.isChecked();
    }

    @CallSuper
    protected ProductViewModel collectDataFromView() {
        productInfoViewHolder.updateModel(currentProductViewModel);
        productImageViewHolder.updateModel(currentProductViewModel);
        productPriceViewHolder.updateModel(currentProductViewModel);
        productManageViewHolder.updateModel(currentProductViewModel);
        productDescriptionViewHolder.updateModel(currentProductViewModel);
        productDeliveryInfoViewHolder.updateModel(currentProductViewModel);

        return currentProductViewModel;
    }

    private void sendAnalyticsAdd(ProductViewModel viewModel) {
        List<String> listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
                Integer.parseInt(getString(R.string.product_free_return_values_active)),
                isShare()
        );
        for (String labelAnalytics : listLabelAnalytics) {
            if (isAddStatus()) {
                UnifyTracking.eventAddProductAdd(labelAnalytics);
            } else if (isEditStatus()) {
                UnifyTracking.eventAddProductEdit(labelAnalytics);
            }
        }
    }

    private void sendAnalyticsAddMore(ProductViewModel viewModel) {
        List<String> listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
                Integer.parseInt(getString(R.string.product_free_return_values_active)),
                isShare()
        );
        for (String labelAnalytics : listLabelAnalytics) {
            if (isAddStatus()) {
                UnifyTracking.eventAddProductAddMore(labelAnalytics);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        productInfoViewHolder.onActivityResult(requestCode, resultCode, data);
        productImageViewHolder.onActivityResult(requestCode, resultCode, data);
        productManageViewHolder.onActivityResult(requestCode, resultCode, data);
        productDescriptionViewHolder.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Permission part
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        BaseProductAddEditFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @TargetApi(16)
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForExternalStorage(final PermissionRequest request) {
        request.proceed();
    }

    // View holder listener part
    @Override
    public void onProductNameChanged(String productName) {
        getCategoryRecommendation(productName);
        productInfoViewHolder.hideAndClearCatalog();
        checkIfCatalogExist(productInfoViewHolder.getName(), productInfoViewHolder.getCategoryId());
        valueIndicatorScoreModel.setLengthProductName(productName.length());
        presenter.getProductScoreDebounce(valueIndicatorScoreModel);
    }

    @Override
    public final void showDialogMoveToGM(@StringRes int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.add_product_title_alert_dialog_dollar);
        if (showDialogSaveDraftOnBack()) {
            builder.setMessage(getString(R.string.add_product_label_alert_save_as_draft_dollar_and_video, getString(message)));
        } else {
            builder.setMessage(message);
        }
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UnifyTracking.eventClickYesGoldMerchantAddProduct();
                if (showDialogSaveDraftOnBack()) {
                    saveDraft(false);
                }
                goToGoldMerchantPage();
                getActivity().finish();
            }
        });
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean showDialogSaveDraftOnBack() {
        return true;
    }

    @Override
    public void onHasVideoChange(boolean hasVideo) {
        valueIndicatorScoreModel.setHasVideo(hasVideo);
        updateProductScoring();
    }

    @Override
    public void onVariantCountChange(boolean hasActiveVariant) {
        valueIndicatorScoreModel.setVariantActive(hasActiveVariant);
        updateProductScoring();
    }

    @Override
    public void onTotalImageUpdated(int total) {
        valueIndicatorScoreModel.setImageCount(total);
        updateProductScoring();
    }

    @Override
    public void onImageResolutionChanged(long maxSize) {
        valueIndicatorScoreModel.setImageResolution((int) maxSize);
        updateProductScoring();
    }

    @Override
    public void onFreeReturnChecked(boolean checked) {
        valueIndicatorScoreModel.setFreeReturnStatus(checked);
        updateProductScoring();
    }

    @Override
    public void onTotalStockUpdated(int total) {
        valueIndicatorScoreModel.setStockStatus(total > 0);
        updateProductScoring();
    }

    @Override
    public void onDescriptionTextChanged(String text) {
        valueIndicatorScoreModel.setLengthDescProduct(text.length());
        updateProductScoring();
    }

    public void goToGoldMerchantPage() {
        if (getActivity().getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) getActivity().getApplication()).goToGMSubscribe(getActivity());
        }
    }

    @Override
    public void updateProductScoring() {
        presenter.getProductScoring(valueIndicatorScoreModel);
    }

    @Override
    public ProductVariantViewModel getCurrentVariantModel() {
        return currentProductViewModel.getProductVariant();
    }

    @Override
    public void updateVariantModel(ProductVariantViewModel productVariantViewModel) {
        currentProductViewModel.setProductVariant(productVariantViewModel);
        productPriceViewHolder.renderData(currentProductViewModel);
    }

    @Override
    public void updateVariantSizeChartModel(ProductPictureViewModel productPictureViewModel) {
        currentProductViewModel.setProductSizeChart(productPictureViewModel);
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentProductViewModel = collectDataFromView();
        outState.putParcelable(SAVED_PRODUCT_VIEW_MODEL, currentProductViewModel);
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    private void onAttachToContext(Context context) {
        this.listener = (Listener) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

}
