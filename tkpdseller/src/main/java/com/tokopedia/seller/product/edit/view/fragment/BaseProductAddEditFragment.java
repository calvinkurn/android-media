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
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.imageeditor.GalleryCropWatermarkActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkActivity;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.instoped.InstopedSellerCropWatermarkActivity;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.edit.view.activity.ProductAddCatalogPickerActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddDescriptionPickerActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddVideoActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddWholesaleActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductScoringDetailActivity;
import com.tokopedia.seller.product.edit.view.dialog.ProductAddImageDialogFragment;
import com.tokopedia.seller.product.edit.view.dialog.ProductAddImageDescriptionDialog;
import com.tokopedia.seller.product.edit.view.dialog.ProductAddImageEditProductDialogFragment;
import com.tokopedia.seller.product.edit.view.dialog.ProductChangeVariantPriceDialogFragment;
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
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.edit.view.widget.ImagesSelectView;
import com.tokopedia.seller.product.etalase.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
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
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

@RuntimePermissions
public abstract class BaseProductAddEditFragment<T extends ProductAddPresenter>
        extends BaseDaggerFragment
        implements ProductAddView,
        ProductScoreViewHolder.Listener, ProductDeliveryInfoViewHolder.Listener,
        ProductImageViewHolder.Listener, ProductInfoViewHolder.Listener,
        ProductManageViewHolder.Listener, ProductPriceViewHolder.Listener,
        ProductDescriptionViewHolder.Listener {

    public static final int DEFAULT_PARENT_STOCK_IF_VARIANT = 1;
    public static final int REQUEST_CODE_ADD_PRODUCT_IMAGE = 3912;
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

    protected ProductViewModel currentProductViewModel;

    private Listener listener;
    private boolean hasOriginalVariantLevel1;
    private boolean hasOriginalVariantLevel2;
    private boolean hasLoadShopInfo;
    private boolean officialStore;

    public interface Listener {
        void startUploadProduct(long productId);

        void startUploadProductWithShare(long productId);

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
        if (needHideShareAndAddMore()) {
            hideShareAndAddMore(view);
        }
        view.requestFocus();
        return view;
    }

    protected boolean needHideShareAndAddMore(){
        return false;
    }

    private void hideShareAndAddMore(View view){
        view.findViewById(R.id.button_save_and_add).setVisibility(View.GONE);
        view.findViewById(R.id.label_switch_share).setVisibility(View.GONE);
    }

    protected void saveAndAddDraft() {
        ProductViewModel viewModel = collectDataFromView();
        sendAnalyticsAddMore(viewModel);
        if (viewModel.getProductVariant() != null) {
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
                isImageValid() &&
                productDescriptionViewHolder.isDataValid() &&
                productDeliveryInfoViewHolder.isDataValid());
    }

    private boolean isImageValid() {
        // if it has catalog, image is valid (because no image needed)
        // if no catalog, check stock, if the stock is not empty, it should have picture.
        return productInfoViewHolder.getCatalogId() > 0 ||
                !currentProductViewModel.isProductStatusActive()||
                productImageViewHolder.isDataValid();
    }

    private void getCategoryRecommendation(String productName) {
        if (isNeedGetCategoryRecommendation() && !currentProductViewModel.hasVariant()) {
            presenter.getCategoryRecommendation(productName);
        }
    }

    // Clicked Part
    @Override
    public void onDetailProductScoringClicked() {
        Intent intent = ProductScoringDetailActivity.createIntent(getActivity(), valueIndicatorScoreModel);
        startActivity(intent);
    }

    @Override
    public void onAddImagePickerClicked(final int imagePosition) {
        ImagePickerBuilder builder = getImagePickerBuilder();
        Intent intent = ImagePickerActivity.getIntent(getContext(), builder);
        startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT_IMAGE);
    }

    private ImagePickerBuilder getImagePickerBuilder(){
        //TODO just for test
        ArrayList<String> initialPathList = new ArrayList<>();
        initialPathList.add("https://scontent-sit4-1.cdninstagram.com/vp/4d462c7e62452e54862602872a4f2f55/5B772ADA/t51.2885-15/e35/30603662_2044572549200360_6725615414816014336_n.jpg");
        initialPathList.add("https://scontent-sit4-1.cdninstagram.com/vp/4d462c7e62452e54862602872a4f2f55/5B772ADA/t51.2885-15/e35/30603662_2044572549200360_6725615414816014336_n.jpg");
        initialPathList.add("https://9to5google.files.wordpress.com/2016/10/google.jpg");

        return new ImagePickerBuilder(getString(R.string.choose_shop_picture),
                new int[]{TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, new int[]{1,1}, true,
                new ImagePickerEditorBuilder(new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false)
                ,new ImagePickerMultipleSelectionBuilder(
//                        productImageViewHolder.getImagesSelectView().getImageStringList(),
                initialPathList,
                null,
                0,
                ImagesSelectView.DEFAULT_LIMIT));
    }

    @Override
    public void onImagePickerItemClicked(int position, boolean isPrimary) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialogFragment = ProductAddImageEditProductDialogFragment.newInstance(position, isPrimary);
        dialogFragment.show(fm, ProductAddImageEditProductDialogFragment.FRAGMENT_TAG);
        ((ProductAddImageEditProductDialogFragment) dialogFragment).setOnImageEditListener(new ProductAddImageEditProductDialogFragment.OnImageEditListener() {

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
                ProductAddImageDescriptionDialog fragment = ProductAddImageDescriptionDialog.newInstance(currentDescription);
                fragment.show(getActivity().getSupportFragmentManager(), ProductAddImageDescriptionDialog.TAG);
                fragment.setListener(new ProductAddImageDescriptionDialog.OnImageDescDialogListener() {
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToGallery(int imagePosition) {
        int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
        GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), this, imagePosition, remainingEmptySlot, true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
        productPriceViewHolder.updateModel(currentProductViewModel);
        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.fetchProductVariantByCat(productInfoViewHolder.getCategoryId());
                }
            }).showRetrySnackbar();
            return;
        }

        boolean hasWholesale = currentProductViewModel.getProductWholesale()!=null && currentProductViewModel.getProductWholesale().size() > 0;

        Intent intent = ProductVariantDashboardActivity.getIntent(getActivity(),
                productVariantByCatModelList,
                currentProductViewModel.getProductVariant(),
                productPriceViewHolder.getCurrencyType(),
                productPriceViewHolder.getPriceValue(),
                productManageViewHolder.getViewStatusStock(),
                productPriceViewHolder.isOfficialStore(),
                productManageViewHolder.getSkuText(),
                isEdittingDraft(),
                currentProductViewModel.getProductSizeChart(),
                hasOriginalVariantLevel1,
                hasOriginalVariantLevel2,
                hasWholesale);
        startActivityForResult(intent, ProductManageViewHolder.REQUEST_CODE_VARIANT);
    }

    @Override
    public final void onCategoryPickerClicked(long categoryId) {
        if (hasVariant() && isEditStatus()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                    R.style.AppCompatAlertDialogStyle);
            builder.setTitle(R.string.product_category_locked);
            builder.setMessage(R.string.product_category_locked_description);
            builder.setCancelable(true);
            builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            CategoryPickerActivity.start(this, getActivity(), ProductInfoViewHolder.REQUEST_CODE_CATEGORY, categoryId);
        }
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

    @Override
    public boolean hasVariant() {
        productPriceViewHolder.updateModel(currentProductViewModel);
        return currentProductViewModel.hasVariant();
    }

    @Override
    public boolean hasWholesale() {
        productPriceViewHolder.updateModel(currentProductViewModel);
        return currentProductViewModel.getProductWholesale()!=null && currentProductViewModel.getProductWholesale().size() > 0;
    }

    @Override
    public void changeAllPriceVariant(int currencyType, double currencyValue) {
        currentProductViewModel.changePriceTo(currencyType, currencyValue);
    }

    @Override
    public void showDialogEditPriceVariant() {
        ProductChangeVariantPriceDialogFragment dialogFragment =
                ProductChangeVariantPriceDialogFragment.newInstance(productPriceViewHolder.getCurrencyType(),
                        productPriceViewHolder.isGoldMerchant(),
                        productPriceViewHolder.getPriceValue(),
                        productPriceViewHolder.isOfficialStore());
        dialogFragment.show(getActivity().getSupportFragmentManager(),
                ProductChangeVariantPriceDialogFragment.TAG);
        dialogFragment.setOnDismissListener(new ProductChangeVariantPriceDialogFragment.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isAdded() || getActivity() == null) {
                            return;
                        }
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            CommonUtils.hideSoftKeyboard(view);
                            view.clearFocus();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void startProductAddWholesaleActivity() {
        productPriceViewHolder.updateModel(currentProductViewModel);
        ArrayList<ProductWholesaleViewModel> productWholesaleViewModelList = (ArrayList<ProductWholesaleViewModel>) currentProductViewModel.getProductWholesale();
        Intent intent = ProductAddWholesaleActivity.getIntent(getActivity(),
                productWholesaleViewModelList,
                (int) currentProductViewModel.getProductPriceCurrency(),
                currentProductViewModel.getProductPrice(),
                officialStore,
                hasVariant());
        startActivityForResult(intent, ProductPriceViewHolder.REQUEST_CODE_GET_PRODUCT_WHOLESALE);
    }

    public void onChangeAllPriceVariantSubmit(int currencyType, double currencyValue) {
        currentProductViewModel.setProductPriceCurrency(productPriceViewHolder.getCurrencyType());
        currentProductViewModel.changePriceTo(currencyType, currencyValue);
        currentProductViewModel.setProductWholesale(new ArrayList<ProductWholesaleViewModel>());
        productPriceViewHolder.renderData(currentProductViewModel);
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

        if (currentProductViewModel.getProductCategory() != null) {
            onCategoryLoaded(currentProductViewModel.getProductCategory().getCategoryId());
        }

        getCategoryRecommendation(currentProductViewModel.getProductName());
        checkIfCatalogExist(productInfoViewHolder.getName(), productInfoViewHolder.getCategoryId());

        checkOriginalVariant(model);
    }

    private void checkOriginalVariant(ProductViewModel model) {
        if (isEditStatus() && model.hasVariant()) {
            ProductVariantViewModel productVariantViewModel = model.getProductVariant();
            hasOriginalVariantLevel1 = (productVariantViewModel.getVariantOptionParent(0) != null);
            hasOriginalVariantLevel2 = (productVariantViewModel.getVariantOptionParent(1) != null);
        }
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
    public void onSuccessLoadCatalog(String keyword, long departmentId, List<Catalog> catalogViewModelList) {
        productInfoViewHolder.successFetchCatalogData(keyword, departmentId, catalogViewModelList);
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

        productPriceViewHolder.setGoldMerchant(isGoldMerchant);
        productPriceViewHolder.setOfficialStore(officialStore);
        productDeliveryInfoViewHolder.showViewFreeReturn(isFreeReturn);

        hasLoadShopInfo = true;
        this.officialStore = officialStore;
    }

    public boolean isHasLoadShopInfo() {
        return hasLoadShopInfo;
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

    @Override
    public void onCatalogPicked(boolean isCatalogExist) {
        if (valueIndicatorScoreModel.isHasCatalog()!= isCatalogExist) {
            valueIndicatorScoreModel.setHasCatalog(isCatalogExist);
            updateProductScoring();
        }
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
        if (viewModel.getProductVariant() != null) {
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
        productPriceViewHolder.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Permission part
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        BaseProductAddEditFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForExternalStorage(final PermissionRequest request) {
        request.proceed();
    }

    // View holder listener part
    @Override
    public void onProductNameChanged(String productName) {
        // this is to prevent clear catalog when don't activity
        if (currentProductViewModel.getProductName().equalsIgnoreCase(productName)) {
            return;
        }
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
        if (valueIndicatorScoreModel.isHasVideo() != hasVideo) {
            valueIndicatorScoreModel.setHasVideo(hasVideo);
            updateProductScoring();
        }
    }

    @Override
    public void onVariantCountChange(boolean hasActiveVariant) {
        if (hasActiveVariant!= valueIndicatorScoreModel.isVariantActive()) {
            valueIndicatorScoreModel.setVariantActive(hasActiveVariant);
            updateProductScoring();
        }
    }

    @Override
    public void onTotalImageUpdated(int total) {
        if (valueIndicatorScoreModel.getImageCount() != total) {
            valueIndicatorScoreModel.setImageCount(total);
            updateProductScoring();
        }
    }

    @Override
    public void onImageResolutionChanged(long maxSize) {
        if (valueIndicatorScoreModel.getImageResolution()!= maxSize) {
            valueIndicatorScoreModel.setImageResolution((int) maxSize);
            updateProductScoring();
        }
    }

    @Override
    public void onFreeReturnChecked(boolean checked) {
        if (valueIndicatorScoreModel.isFreeReturnStatus() != checked) {
            valueIndicatorScoreModel.setFreeReturnStatus(checked);
            updateProductScoring();
        }
    }

    @Override
    public void onTotalStockUpdated(int total) {
        //we need to update this in "realtime" because stock and total will be needed for variant immediately
        if (currentProductViewModel!= null) {
            currentProductViewModel.setProductStock(total);
        }
        boolean stockStatus = total > 0;
        if (valueIndicatorScoreModel.isStockStatus() != stockStatus) {
            valueIndicatorScoreModel.setStockStatus(total > 0);
            updateProductScoring();
        }
    }

    @Override
    public void onStockStatusUpdated(boolean isActive) {
        if (currentProductViewModel!= null) {
            currentProductViewModel.setProductStatus(isActive);
        }
    }

    @Override
    public long getTotalStock() {
        if (currentProductViewModel == null) {
            return 0;
        }
        return currentProductViewModel.getProductStock();
    }

    @Override
    public void onDescriptionTextChanged(String text) {
        if (valueIndicatorScoreModel.getLengthDescProduct()!= text.length()) {
            valueIndicatorScoreModel.setLengthDescProduct(text.length());
            updateProductScoring();
        }
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
        // if has any variant, we want to get summary of stock, then update to the parent model.
        if (productVariantViewModel != null && productVariantViewModel.hasSelectedVariant()) {
            @StockTypeDef int stockType = productVariantViewModel.getCalculateProductStatus();
            if (stockType == StockTypeDef.TYPE_ACTIVE_LIMITED) {
                onStockStatusUpdated(true);
                onTotalStockUpdated(DEFAULT_PARENT_STOCK_IF_VARIANT);
            } else {
                onStockStatusUpdated(stockType == StockTypeDef.TYPE_ACTIVE);
                onTotalStockUpdated(0);
            }
        }
        // refresh the stock and variant label.
        productManageViewHolder.renderData(currentProductViewModel);

        // to disable or enable price/wholesale/etc
        productPriceViewHolder.renderData(currentProductViewModel);

        //to hide category recommendation
        productInfoViewHolder.renderByVariant(currentProductViewModel.hasVariant());
    }

    @Override
    public void updateVariantSizeChartModel(ProductPictureViewModel productPictureViewModel) {
        currentProductViewModel.setProductSizeChart(productPictureViewModel);
    }

    @Override
    public void showInstallSellerApp() {
        ((TkpdCoreRouter) getActivity().getApplication()).goToCreateMerchantRedirect(getActivity());
    }

    @Override
    public List<ProductVideoViewModel> getVideoIdList() {
        if (currentProductViewModel == null || currentProductViewModel.getProductVideo() == null ||
                currentProductViewModel.getProductVideo().size() == 0){
            return new ArrayList<>();
        }
        return currentProductViewModel.getProductVideo();
    }

    @Override
    public void updateVideoIdList(ArrayList<String> videoIdList) {
        List<ProductVideoViewModel> productVideoViewModelList = new ArrayList<>();
        for (String videoId : videoIdList) {
            ProductVideoViewModel productVideoViewModel = new ProductVideoViewModel(videoId);
            productVideoViewModelList.add(productVideoViewModel);
        }
        currentProductViewModel.setProductVideo(productVideoViewModelList);
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

    @TargetApi(Build.VERSION_CODES.M)
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
