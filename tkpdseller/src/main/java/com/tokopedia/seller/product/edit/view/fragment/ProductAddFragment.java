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
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
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
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.edit.di.component.DaggerProductAddComponent;
import com.tokopedia.seller.product.edit.di.module.ProductAddModule;
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
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;
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

//import com.tokopedia.seller.product.edit.di.component.DaggerProductAddComponent;

/**
 * Created by nathan on 4/3/17.
 */

@RuntimePermissions
public class ProductAddFragment extends BaseDaggerFragment implements ProductAddView,
        ProductScoreViewHolder.Listener, ProductAdditionalInfoViewHolder.Listener,
        ProductImageViewHolder.Listener, ProductInfoViewHolder.Listener,
        ProductDetailViewHolder.Listener {

    public static final String TAG = ProductAddFragment.class.getSimpleName();

    @Inject
    public ProductAddPresenter presenter;
    protected ProductScoreViewHolder productScoreViewHolder;

    protected ProductImageViewHolder productImageViewHolder;
    protected ProductDetailViewHolder productDetailViewHolder;
    protected ProductAdditionalInfoViewHolder productAdditionalInfoViewHolder;
    protected ProductInfoViewHolder productInfoViewHolder;
    private ValueIndicatorScoreModel valueIndicatorScoreModel;

    // view model to be compare later when we want to save as draft
    private ProductViewModel firstTimeViewModel;

    /**
     * Url got from gallery or camera or other paths
     */
    private ArrayList<String> imageUrlList;
    private Listener listener;
    private View btnSave;
    private View btnSaveAndAdd;

    public static ProductAddFragment createInstance(ArrayList<String> tkpdImageUrls) {
        ProductAddFragment fragment = new ProductAddFragment();
        if (tkpdImageUrls != null && tkpdImageUrls.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ProductAddActivity.EXTRA_IMAGE_URLS, tkpdImageUrls);
            fragment.setArguments(bundle);
        }
        return fragment;
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
        if (context instanceof Listener) {
            this.listener = (Listener) context;
        } else {
            throw new RuntimeException("Activity must implement Listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        valueIndicatorScoreModel = new ValueIndicatorScoreModel();
        Bundle args = getArguments();
        if (args != null && args.containsKey(ProductAddActivity.EXTRA_IMAGE_URLS)) {
            imageUrlList = args.getStringArrayList(ProductAddActivity.EXTRA_IMAGE_URLS);
        }
    }

    @Override
    protected void initInjector() {
        DaggerProductAddComponent
                .builder()
                .productAddModule(new ProductAddModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_add, container, false);
        productInfoViewHolder = new ProductInfoViewHolder(view.findViewById(R.id.view_group_product_info));
        productInfoViewHolder.setListener(this);

        productImageViewHolder = new ProductImageViewHolder(view.findViewById(R.id.view_group_product_image));
        productImageViewHolder.setListener(this);
        if (CommonUtils.checkCollectionNotNull(imageUrlList)) {
            productImageViewHolder.setImages(imageUrlList);
        }
        productDetailViewHolder = new ProductDetailViewHolder(view);
        productDetailViewHolder.setListener(this);
        productAdditionalInfoViewHolder = new ProductAdditionalInfoViewHolder(view);
        productAdditionalInfoViewHolder.setListener(this);
        productScoreViewHolder = new ProductScoreViewHolder(view);
        productScoreViewHolder.setListener(this);
        presenter.attachView(this);
        presenter.getShopInfo();
        btnSave = view.findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid()) {
                    saveDraft(true);
                }
            }
        });
        btnSaveAndAdd = view.findViewById(R.id.button_save_and_add);
        btnSaveAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataValid()) {
                    saveAndAddDraft(true);
                }
            }
        });
        saveDefaultModel();
        view.requestFocus();
        return view;
    }

    private void saveAndAddDraft(boolean isUploading) {
        ProductViewModel viewModel = collectDataFromView();
        if (isUploading) {
            sendAnalyticsAddMore(viewModel);
        }
        presenter.saveDraftAndAdd(viewModel, isUploading);
    }

    protected void saveDefaultModel(){
        //save default value here, so we can compare when we want to save draft
        // will be overriden when not adding product
        firstTimeViewModel = collectDataFromView();
    }

    public boolean hasDataAdded(){
        // check if this fragment has any data
        // will compare will the default value and the current value
        // if there is the difference, then assume that the data has been added.
        // will be overriden when not adding product
        ProductViewModel model = collectDataFromView();
        return !model.equals(firstTimeViewModel);
    }

    public void deleteNotUsedTkpdCacheImage(){
        ArrayList<ImageSelectModel> imageSelectModelArrayList = productImageViewHolder.getImagesSelectView().getImageList();
        if (imageSelectModelArrayList == null || imageSelectModelArrayList.size() == 0) {
            return;
        }
        ArrayList<String> uriArrayList = new ArrayList<>();
        for (int i = 0, sizei = imageSelectModelArrayList.size(); i<sizei; i++) {
            uriArrayList.add(imageSelectModelArrayList.get(i).getUriOrPath());
        }
        FileUtils.deleteAllCacheTkpdFiles(uriArrayList);
    }

    public void saveDraft(boolean isUploading) {
        ProductViewModel viewModel = collectDataFromView();
        if (isUploading) {
            sendAnalyticsAdd(viewModel);
        }
        presenter.saveDraft(viewModel, isUploading);
    }

    private void sendAnalyticsAdd(ProductViewModel viewModel) {
        List<String>  listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
                Integer.parseInt(getString(R.string.product_free_return_values_active)),
                productAdditionalInfoViewHolder.isShare()
        );
        for (String labelAnalytics : listLabelAnalytics){
            if(getStatusUpload() == ProductStatus.ADD) {
                UnifyTracking.eventAddProductAdd(labelAnalytics);
            } else if(getStatusUpload() == ProductStatus.EDIT){
                UnifyTracking.eventAddProductEdit(labelAnalytics);
            }
        }
    }

    private void sendAnalyticsAddMore(ProductViewModel viewModel) {
        List<String>  listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
                Integer.parseInt(getString(R.string.product_free_return_values_active)),
                productAdditionalInfoViewHolder.isShare()
        );
        for (String labelAnalytics : listLabelAnalytics){
            if(getStatusUpload() == ProductStatus.ADD) {
                UnifyTracking.eventAddProductAddMore(labelAnalytics);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductInfoViewHolder.REQUEST_CODE_CATEGORY:
                productInfoViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ProductInfoViewHolder.REQUEST_CODE_CATALOG:
                productInfoViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ImageGallery.TOKOPEDIA_GALLERY:
                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case INSTAGRAM_SELECT_REQUEST_CODE:
                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ImageEditorActivity.REQUEST_CODE:
                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ProductDetailViewHolder.REQUEST_CODE_ETALASE:
                productDetailViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ProductAdditionalInfoViewHolder.REQUEST_CODE_GET_VIDEO:
                productAdditionalInfoViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ProductAdditionalInfoViewHolder.REQUEST_CODE_VARIANT:
                productAdditionalInfoViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Permission part
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ProductAddFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @TargetApi(16)
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    protected void getCategoryRecommendation(String productName) {
        presenter.getCategoryRecommendation(productName);
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
        valueIndicatorScoreModel.setImageResolution((int)maxSize);
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

    private void updateProductScoring() {
        presenter.getProductScoring(valueIndicatorScoreModel);
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
            if (productAdditionalInfoViewHolder.isShare()) {
                listener.startUploadProductWithShare(productId);
            } else {
                listener.startUploadProduct(productId);
            }
        } else {
            if (listener!= null) {
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
                    saveAndAddDraft(true);
                }
            }
        }).showRetrySnackbar();
    }

    @Override
    public long getProductDraftId() {
        return 0;
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
        productAdditionalInfoViewHolder.updateViewGoldMerchant(isGoldMerchant);
        productDetailViewHolder.setGoldMerchant(isGoldMerchant);
        productDetailViewHolder.setOfficialStore(officialStore);
        productDetailViewHolder.updateViewFreeReturn(isFreeReturn);
        valueIndicatorScoreModel.setFreeReturnActive(isFreeReturn);

        saveDefaultModel();
    }

    @Override
    public void onErrorGetProductVariantByCat(Throwable throwable) {
        onSuccessGetProductVariant(null);
    }

    @Override
    public void onSuccessGetProductVariant(List<ProductVariantByCatModel> productVariantByCatModelList) {
        productAdditionalInfoViewHolder.onSuccessGetProductVariantCat(productVariantByCatModelList);
    }

    @Override
    public void onErrorLoadShopInfo(String errorMessage) {

    }

    @CallSuper
    protected boolean isDataValid() {
        if (!productInfoViewHolder.isDataValid().first) {
            UnifyTracking.eventAddProductError(productInfoViewHolder.isDataValid().second);
            return false;
        }
        if (!productDetailViewHolder.isDataValid().first) {
            UnifyTracking.eventAddProductError(productDetailViewHolder.isDataValid().second);
            return false;
        }
        if (productDetailViewHolder.getStatusStock() == Integer.parseInt(getString(R.string.product_stock_available_value)) && !productImageViewHolder.isDataValid().first) {
            UnifyTracking.eventAddProductError(productImageViewHolder.isDataValid().second);
            return false;
        }
        if (!productAdditionalInfoViewHolder.isDataValid().first) {
            UnifyTracking.eventAddProductError(productAdditionalInfoViewHolder.isDataValid().second);
            return false;
        }
        return true;
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
                ProductAddFragmentPermissionsDispatcher.goToCameraWithCheck(ProductAddFragment.this, imagePosition);
            }

            @Override
            public void clickAddProductFromGallery(int position) {
                ProductAddFragmentPermissionsDispatcher.goToGalleryWithCheck(ProductAddFragment.this, imagePosition);
            }

            @Override
            public void clickAddProductFromInstagram(int position) {
                int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), ProductAddFragment.this,
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
                GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), ProductAddFragment.this, position,
                        true, 1,true);
            }

            @Override
            public void clickEditImagePathFromGallery(int position) {
                GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), ProductAddFragment.this, position, 1, true);
            }

            @Override
            public void clickEditImagePathFromInstagram(int position) {
                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), ProductAddFragment.this,
                        INSTAGRAM_SELECT_REQUEST_CODE, 1);
            }

            @Override
            public void clickImageEditor(int position) {
                if (ProductAddFragment.this.getStatusUpload() == ProductStatus.ADD) {
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
                        ProductAddFragment.this.clearFocus();
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
                if (imageSelectModel!= null) {
                    String path = imageSelectModel.getUriOrPath();
                    if (!TextUtils.isEmpty(path) && !isEdittingDraft()) {
                        FileUtils.deleteAllCacheTkpdFile(path);
                    }
                }
                imagesSelectView.removeImage();
            }
        });
    }

    private boolean isEdittingDraft(){
        return ((getStatusUpload()== ProductStatus.EDIT) && (getProductDraftId() > 0));
    }

    @Override
    public void onImageEditor(String uriOrPath) {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(uriOrPath);
        ImageEditorWatermarkActivity.start(getContext(), ProductAddFragment.this, imageUrls, !isEdittingDraft());
    }

    @Override
    public void onRemovePreviousPath(String uri) {
        if (!TextUtils.isEmpty(uri) && !isEdittingDraft()) {
            FileUtils.deleteAllCacheTkpdFile(uri);
        }
    }

    private void clearFocus(){
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
                true, remainingEmptySlot,true);

    }

    @Override
    public void startInfoAddProduct() {
        startActivity(new Intent(getActivity(), ProductAddInfoActivity.class));
    }

    @Override
    public void startYoutubeVideoActivity(ArrayList<String> videoIds) {
        Intent intent = new Intent(getActivity(), YoutubeAddVideoActivity.class);
        if (CommonUtils.checkCollectionNotNull(videoIds)) {
            intent.putStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK, videoIds);
        }
        startActivityForResult(intent, ProductAdditionalInfoViewHolder.REQUEST_CODE_GET_VIDEO);
    }

    @Override
    public void startProductVariantActivity(ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                            ProductVariantDataSubmit productVariantDataSubmit,
                                            ArrayList<ProductVariantOptionSubmit> productVariantOptionSubmitArrayList) {
        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.fetchProductVariantByCat(productInfoViewHolder.getCategoryId());
                }
            }).showRetrySnackbar();
            return;
        }
        Intent intent = new Intent(getActivity(), ProductVariantDashboardActivity.class);
        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, productVariantByCatModelList);
        if (productVariantDataSubmit != null && productVariantDataSubmit.getProductVariantUnitSubmitList()!= null &&
                productVariantByCatModelList.size() > 0) {
            intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION, productVariantDataSubmit);
        }
        intent.putExtra(ProductVariantConstant.EXTRA_OLD_PRODUCT_OPTION_SUBMIT_LV1_LIST, productVariantOptionSubmitArrayList);
        startActivityForResult(intent, ProductAdditionalInfoViewHolder.REQUEST_CODE_VARIANT);
    }

    @Override
    public void onCategoryPickerClicked(long categoryId) {
        CategoryPickerActivity.start(this, getActivity(), ProductInfoViewHolder.REQUEST_CODE_CATEGORY, categoryId);
    }

    @Override
    public void onCatalogPickerClicked(String keyword, long depId, long selectedCatalogId) {
        CatalogPickerActivity.start(this, getActivity(), ProductInfoViewHolder.REQUEST_CODE_CATALOG, keyword, depId, selectedCatalogId);
    }

    @Override
    public void onEtalaseViewClicked(long etalaseId) {
        Intent intent = EtalasePickerActivity.createInstance(getActivity(), etalaseId);
        startActivityForResult(intent, ProductDetailViewHolder.REQUEST_CODE_ETALASE);
    }

    // Others
    @Override
    public void onResolutionImageCheckFailed(String uri) {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.error_image_resolution));
        FileUtils.deleteAllCacheTkpdFile(uri);
    }

    @Override
    public void onCategoryChanged(long categoryId) {
        // when category change, check if catalog exists
        productAdditionalInfoViewHolder.setProductVariantDataSubmit(null, "");
        productAdditionalInfoViewHolder.onSuccessGetProductVariantCat(null);
        onCategoryLoaded(categoryId);
    }

    /**
     * when category changed, or category has been fetched
     * @param categoryId
     */
    public void onCategoryLoaded(long categoryId) {
        presenter.fetchProductVariantByCat(categoryId);
        checkIfCatalogExist(productInfoViewHolder.getName(), categoryId);
        fetchCategory(categoryId);
    }

    @Override
    public void fetchCategory(long categoryId) {
        presenter.fetchCategory(categoryId);
    }

    protected void checkIfCatalogExist(String productName, long categoryId) {
        presenter.fetchCatalogData(productName, categoryId, 0, 1);
    }

    public void addWholesaleItem(WholesaleModel wholesaleModel) {
        productDetailViewHolder.addWholesaleItem(wholesaleModel);
    }

    @Override
    public void startAddWholeSaleDialog(WholesaleModel fixedPrice,
                                        @CurrencyTypeDef int currencyType,
                                        WholesaleModel previousWholesalePrice, boolean officialStore) {
        listener.startAddWholeSaleDialog(fixedPrice, currencyType, previousWholesalePrice, officialStore);
    }

    @Override
    public void onSuccessStoreProductAndAddToDraft(Long productId) {
        CommonUtils.UniversalToast(getActivity(), getString(R.string.upload_product_waiting));
        if (productAdditionalInfoViewHolder.isShare()) {
            listener.startUploadProductAndAddWithShare(productId);
        } else {
            listener.startUploadProductAndAdd(productId);
        }
    }

    @Override
    public void populateCategory(List<String> strings) {
        String[] stringArray = new String[strings.size()];
        stringArray = strings.toArray(stringArray);
        productInfoViewHolder.processCategory(stringArray);
    }

    @CallSuper
    protected ProductViewModel collectDataFromView() {
        ProductViewModel viewModel = new ProductViewModel();
        viewModel.setProductName(productInfoViewHolder.getName());
        viewModel.setProductCategory(productInfoViewHolder.getProductCategory());
        viewModel.setProductCatalog(productInfoViewHolder.getProductCatalog());
        viewModel.setProductPicture(productImageViewHolder.getProductPhotos());

        viewModel.setProductPriceCurrency(productDetailViewHolder.getPriceUnit());
        viewModel.setProductPrice(productDetailViewHolder.getPriceValue());
        viewModel.setProductWeightUnit(productDetailViewHolder.getWeightUnit());
        viewModel.setProductWeight(productDetailViewHolder.getWeightValue());
        viewModel.setProductMinOrder(productDetailViewHolder.getMinimumOrder());

        viewModel.setProductWholesale(productDetailViewHolder.getProductWholesaleViewModels());

        viewModel.setProductStock(productDetailViewHolder.getTotalStock());
        viewModel.setProductStatus(productDetailViewHolder.getStatusStock());
        viewModel.setProductEtalase(productDetailViewHolder.getProductEtalase());
        viewModel.setProductCondition(productDetailViewHolder.getCondition());
        viewModel.setProductMustInsurance(productDetailViewHolder.isMustInsurance());
        viewModel.setProductDescription(productAdditionalInfoViewHolder.getDescription());

        viewModel.setProductFreeReturn(productDetailViewHolder.isFreeReturns());
        viewModel.setProductVideo(productAdditionalInfoViewHolder.getVideoList());
        viewModel.setProductPreorder(productAdditionalInfoViewHolder.getPreOrder());
//        viewModel.setProductVariant(productAdditionalInfoViewHolder.getProductVariant());
        viewModel.setProductNameEditable(productInfoViewHolder.isNameEditable());

//        viewModel.setVariantStringSelection(productAdditionalInfoViewHolder.getVariantStringSelection());
        return viewModel;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        productInfoViewHolder.onSaveInstanceState(outState);
        productImageViewHolder.onSaveInstanceState(outState);
        productDetailViewHolder.onSaveInstanceState(outState);
        productAdditionalInfoViewHolder.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        productInfoViewHolder.onViewStateRestored(savedInstanceState);
        productImageViewHolder.onViewStateRestored(savedInstanceState);
        productDetailViewHolder.onViewStateRestored(savedInstanceState);
        productAdditionalInfoViewHolder.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void showDialogMoveToGM(@StringRes int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.add_product_title_alert_dialog_dollar);
        if(hasDataAdded() ){
            builder.setMessage(getString(R.string.add_product_label_alert_save_as_draft_dollar_and_video, getString(message)));
        }else{
            builder.setMessage(message);
        }
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UnifyTracking.eventClickYesGoldMerchantAddProduct();
                if(hasDataAdded()){
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @ProductStatus
    public int getStatusUpload() {
        return ProductStatus.ADD;
    }

    public interface Listener {
        void startUploadProduct(long productId);

        void startUploadProductWithShare(long productId);

        void startAddWholeSaleDialog(WholesaleModel fixedPrice,
                                     @CurrencyTypeDef int currencyType,
                                     WholesaleModel previousWholesalePrice, boolean officialStore);

        void startUploadProductAndAddWithShare(Long productId);

        void startUploadProductAndAdd(Long productId);

        void successSaveDraftToDBWhenBackpressed();
    }
}