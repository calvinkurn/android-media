package com.tokopedia.seller.product.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.ProductCategoryPrediction;
import com.tokopedia.seller.product.di.component.DaggerProductAddComponent;
import com.tokopedia.seller.product.di.module.ProductAddModule;
import com.tokopedia.seller.product.view.activity.CatalogPickerActivity;
import com.tokopedia.seller.product.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.view.activity.ProductScoringDetailActivity;
import com.tokopedia.seller.product.view.activity.YoutubeAddVideoActivity;
import com.tokopedia.seller.product.view.dialog.ImageDescriptionDialog;
import com.tokopedia.seller.product.view.dialog.ImageEditDialogFragment;
import com.tokopedia.seller.product.view.holder.ProductAdditionalInfoViewHolder;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.holder.ProductImageViewHolder;
import com.tokopedia.seller.product.view.holder.ProductInfoViewHolder;
import com.tokopedia.seller.product.view.holder.ProductScoreViewHolder;
import com.tokopedia.seller.product.view.listener.ProductAddView;
import com.tokopedia.seller.product.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

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
    /**
     * Url got from gallery or camera or other paths
     */
    private ArrayList<String> imageUrlList;
    private Listener listener;

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
                .appComponent(getComponent(AppComponent.class))
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
        presenter.getShopInfo(SessionHandler.getShopID(getContext()));
        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid()) {
                    UploadProductInputViewModel viewModel = collectDataFromView();
                    presenter.saveDraft(viewModel);
                }
            }

            private boolean isDataValid() {
                if (!productInfoViewHolder.isDataValid()) {
                    return false;
                }
                if (!productDetailViewHolder.isDataValid()) {
                    return false;
                }
                if (productDetailViewHolder.getStatusStock() == Integer.parseInt(getString(R.string.product_stock_available_value)) && !productImageViewHolder.isDataValid()) {
                    return false;
                }
                if (!productAdditionalInfoViewHolder.isDataValid()) {
                    return false;
                }
                return true;
            }
        });
        return view;
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
            case com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY:
                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ProductDetailViewHolder.REQUEST_CODE_ETALASE:
                productDetailViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case YoutubeAddVideoView.REQUEST_CODE_GET_VIDEO:
                productAdditionalInfoViewHolder.onActivityResult(requestCode, resultCode, data);
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
        checkIfCatalogExist(productInfoViewHolder.getName(), productInfoViewHolder.getCategoryId());
        valueIndicatorScoreModel.setLengthProductName(productName.length());
        updateProductScoring();
    }

    protected void getCategoryRecommendation(String productName) {
        presenter.getCategoryRecommendation(productName);
    }

    @Override
    public void onTotalImageUpdated(int total) {
        valueIndicatorScoreModel.setImageCount(total);
        updateProductScoring();
    }

    @Override
    public void onImageResolutionChanged(int maxSize) {
        valueIndicatorScoreModel.setImageResolution(maxSize);
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
    public void onSuccessStoreProductToDraft(long productId) {
        if (productAdditionalInfoViewHolder.isShare()) {
            listener.startUploadProductWithShare(productId);
        } else {
            listener.startUploadProduct(productId);
        }
    }

    @Override
    public void onErrorStoreProductToDraft(String errorMessage) {

    }

    @Override
    public void onSuccessLoadCatalog(List<Catalog> catalogViewModelList, int maxRows) {
        productInfoViewHolder.successFetchCatalogData(catalogViewModelList, maxRows);
    }

    @Override
    public void onErrorLoadCatalog(String errorMessage) {
        productInfoViewHolder.hideAndClearCatalog();
    }

    @Override
    public void onSuccessLoadRecommendationCategory(List<ProductCategoryPrediction> categoryPredictionList) {
        productInfoViewHolder.successGetCategoryRecommData(categoryPredictionList);
    }

    @Override
    public void onErrorLoadRecommendationCategory(String errorMessage) {

    }

    @Override
    public void onSuccessLoadShopInfo(boolean isGoldMerchant, boolean isFreeReturn) {
        productAdditionalInfoViewHolder.updateViewGoldMerchant(isGoldMerchant);
        productDetailViewHolder.setGoldMerchant(isGoldMerchant);
        productDetailViewHolder.updateViewFreeReturn(isFreeReturn);
    }

    @Override
    public void onErrorLoadShopInfo(String errorMessage) {

    }

    // Clicked Part
    @Override
    public void onDetailProductScoringClicked() {
        Intent intent = ProductScoringDetailActivity.createIntent(getActivity(), valueIndicatorScoreModel);
        startActivity(intent);
    }

    @Override
    public void onAddImagePickerClicked(int imagePosition) {
        ProductAddFragmentPermissionsDispatcher.goToGalleryWithCheck(this, imagePosition);
    }

    @Override
    public void onImagePickerItemClicked(int position, boolean isPrimary) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialogFragment = ImageEditDialogFragment.newInstance(position, isPrimary);
        dialogFragment.show(fm, ImageEditDialogFragment.FRAGMENT_TAG);
        ((ImageEditDialogFragment) dialogFragment).setOnImageEditListener(new ImageEditDialogFragment.OnImageEditListener() {
            @Override
            public void clickEditImagePath(int position) {
                GalleryActivity.moveToImageGallery(getActivity(), ProductAddFragment.this, position, 1, true);
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
                });
            }

            @Override
            public void clickEditImagePrimary(int position) {
                productImageViewHolder.getImagesSelectView().changeImagePrimary(true, position);
            }

            @Override
            public void clickRemoveImage(int positions) {
                productImageViewHolder.getImagesSelectView().removeImage();
            }
        });
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToGallery(int imagePosition) {
        int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
        GalleryActivity.moveToImageGallery(getActivity(), this, imagePosition, remainingEmptySlot, true);
    }

    @Override
    public void startYoutubeVideoActivity(ArrayList<String> videoIds) {
        Intent intent = new Intent(getActivity(), YoutubeAddVideoActivity.class);
        if (CommonUtils.checkCollectionNotNull(videoIds)) {
            intent.putStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK, videoIds);
        }
        startActivityForResult(intent, YoutubeAddVideoView.REQUEST_CODE_GET_VIDEO);
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
    public void onEtalaseViewClicked() {
        Intent intent = new Intent(getActivity(), EtalasePickerActivity.class);
        this.startActivityForResult(intent, ProductDetailViewHolder.REQUEST_CODE_ETALASE);
    }

    // Others
    @Override
    public void onResolutionImageCheckFailed(String uri) {
        Snackbar.make(getView(), getString(R.string.error_image_resolution), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCategoryChanged(long categoryId) {
        // when category change, check if catalog exists
        checkIfCatalogExist(productInfoViewHolder.getName(), categoryId);
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
                                        WholesaleModel previousWholesalePrice) {
        listener.startAddWholeSaleDialog(fixedPrice, currencyType, previousWholesalePrice);
    }

    @Override
    public void onUSDClickedNotAllowed() {
        Snackbar.make(getView(), getString(R.string.error_must_be_gold_merchant), Snackbar.LENGTH_LONG).show();
    }

    private UploadProductInputViewModel collectDataFromView() {
        UploadProductInputViewModel viewModel = new UploadProductInputViewModel();
        viewModel.setProductName(productInfoViewHolder.getName());
        viewModel.setProductDepartmentId(productInfoViewHolder.getCategoryId());
        viewModel.setProductCatalogId(productInfoViewHolder.getCatalogId());
        viewModel.setProductPhotos(productImageViewHolder.getProductPhotos());
        viewModel.setProductPriceCurrency(productDetailViewHolder.getPriceUnit());
        viewModel.setProductPrice(productDetailViewHolder.getPriceValue());
        viewModel.setProductWholesaleList(productDetailViewHolder.getProductWholesaleViewModels());
        viewModel.setProductWeightUnit(productDetailViewHolder.getWeightUnit());
        viewModel.setProductWeight(productDetailViewHolder.getWeightValue());
        viewModel.setProductMinOrder(productDetailViewHolder.getMinimumOrder());
        viewModel.setProductUploadTo(productDetailViewHolder.getStatusStock());
        viewModel.setProductEtalaseId(productDetailViewHolder.getEtalaseId());
        viewModel.setProductCondition(productDetailViewHolder.getCondition());
        viewModel.setProductMustInsurance(productDetailViewHolder.getInsurance());
        viewModel.setProductReturnable(productDetailViewHolder.getFreeReturns());
        viewModel.setProductDescription(productAdditionalInfoViewHolder.getDescription());
        viewModel.setProductVideos(productAdditionalInfoViewHolder.getVideoIdList());
        if (productAdditionalInfoViewHolder.getPreOrderValue() > 0) {
            viewModel.setPoProcessType(productAdditionalInfoViewHolder.getPreOrderUnit());
            viewModel.setPoProcessValue(productAdditionalInfoViewHolder.getPreOrderValue());
        }
        return viewModel;
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

    public interface Listener {
        void startUploadProduct(long productId);

        void startUploadProductWithShare(long productId);

        void startAddWholeSaleDialog(WholesaleModel fixedPrice,
                                     @CurrencyTypeDef int currencyType,
                                     WholesaleModel previousWholesalePrice);
    }
}