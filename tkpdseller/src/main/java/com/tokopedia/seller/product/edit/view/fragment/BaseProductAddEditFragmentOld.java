package com.tokopedia.seller.product.edit.view.fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;

//@RuntimePermissions
@Deprecated
public abstract class BaseProductAddEditFragmentOld<T extends ProductAddPresenter>
        extends BaseDaggerFragment
        /*implements ProductAddView,
        ProductScoreViewHolder.Listener, ProductDeliveryInfoViewHolder.Listener,
        ProductImageViewHolder.Listener, ProductInfoViewHolder.Listener,
        ProductManageViewHolder.Listener*/ {

//    @Inject
//    protected T presenter;
//
//    protected ProductScoreViewHolder productScoreViewHolder;
//
//    protected ProductImageViewHolder productImageViewHolder;
//    protected ProductManageViewHolder productManageViewHolder;
//    protected ProductDeliveryInfoViewHolder productDeliveryInfoViewHolder;
//    protected ProductInfoViewHolder productInfoViewHolder;
//    protected ValueIndicatorScoreModel valueIndicatorScoreModel;
//
//    // view model to be compare later when we want to save as draft
//    protected ProductViewModel firstTimeViewModel;
//
//    private Listener listener;
//
//    public interface Listener {
//        void startUploadProduct(long productId);
//
//        void startUploadProductWithShare(long productId);
//
//        void startAddWholeSaleDialog(WholesaleModel fixedPrice,
//                                     @CurrencyTypeDef int currencyType,
//                                     WholesaleModel previousWholesalePrice, boolean officialStore);
//
//        void startUploadProductAndAddWithShare(Long productId);
//
//        void startUploadProductAndAdd(Long productId);
//
//        void successSaveDraftToDBWhenBackpressed();
//    }
//
//    protected abstract @ProductStatus int getStatusUpload();
//
//    public abstract boolean isNeedGetCategoryRecommendation();
//
//    @Override
//    public abstract void initInjector();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//        valueIndicatorScoreModel = new ValueIndicatorScoreModel();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_product_add_old, container, false);
//        productInfoViewHolder = new ProductInfoViewHolder(view.findViewById(R.id.view_group_product_info));
//        productInfoViewHolder.setListener(this);
//
//        productImageViewHolder = new ProductImageViewHolder(view.findViewById(R.id.view_group_product_image));
//        productImageViewHolder.setListener(this);
//        productManageViewHolder = new ProductManageViewHolder(view);
//        productManageViewHolder.setListener(this);
//        productDeliveryInfoViewHolder = new ProductDeliveryInfoViewHolder(view);
//        productDeliveryInfoViewHolder.setListener(this);
//        productScoreViewHolder = new ProductScoreViewHolder(view);
//        productScoreViewHolder.setListener(this);
//        presenter.attachView(this);
//        presenter.getShopInfo();
//        View btnSave = view.findViewById(R.id.button_save);
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isDataValid()) {
//                    saveDraft(true);
//                }
//            }
//        });
//        View btnSaveAndAdd = view.findViewById(R.id.button_save_and_add);
//        btnSaveAndAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isDataValid()) {
//                    saveAndAddDraft();
//                }
//            }
//        });
//        view.requestFocus();
//        return view;
//    }
//
//    protected void saveAndAddDraft() {
//        ProductViewModel viewModel = collectDataFromView();
//        sendAnalyticsAddMore(viewModel);
//        presenter.saveDraftAndAdd(viewModel);
//    }
//
//    @Override
//    public long getProductDraftId() {
//        return 0;
//    }
//
//    @CallSuper
//    protected boolean isDataValid() {
//        if (!productInfoViewHolder.isDataValid().first) {
//            UnifyTracking.eventAddProductError(productInfoViewHolder.isDataValid().second);
//            return false;
//        }
//        if (!productManageViewHolder.isDataValid().first) {
//            UnifyTracking.eventAddProductError(productManageViewHolder.isDataValid().second);
//            return false;
//        }
//        if (productManageViewHolder.getStatusStock() == Integer.parseInt(getString(R.string.product_stock_available_value)) && !productImageViewHolder.isDataValid().first) {
//            UnifyTracking.eventAddProductError(productImageViewHolder.isDataValid().second);
//            return false;
//        }
//        if (!productDeliveryInfoViewHolder.isDataValid().first) {
//            UnifyTracking.eventAddProductError(productDeliveryInfoViewHolder.isDataValid().second);
//            return false;
//        }
//        return true;
//    }
//
//    private void getCategoryRecommendation(String productName) {
//        if (isNeedGetCategoryRecommendation()) {
//            presenter.getCategoryRecommendation(productName);
//        }
//    }
//
//    @Override
//    public void startAddWholeSaleDialog(WholesaleModel fixedPrice,
//                                        @CurrencyTypeDef int currencyType,
//                                        WholesaleModel previousWholesalePrice, boolean officialStore) {
//        listener.startAddWholeSaleDialog(fixedPrice, currencyType, previousWholesalePrice, officialStore);
//    }
//
//
//
//    // Clicked Part
//    @Override
//    public void onDetailProductScoringClicked() {
//        Intent intent = ProductScoringDetailActivity.createIntent(getActivity(), valueIndicatorScoreModel);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onAddImagePickerClicked(final int imagePosition) {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        ImageAddDialogFragment dialogFragment = ImageAddDialogFragment.newInstance(imagePosition);
//        dialogFragment.show(fm, ImageAddDialogFragment.FRAGMENT_TAG);
//        dialogFragment.setOnImageAddListener(new ImageAddDialogFragment.OnImageAddListener() {
//            @Override
//            public void clickAddProductFromCamera(int position) {
//                BaseProductAddEditFragmentOldPermissionsDispatcher.goToCameraWithCheck(BaseProductAddEditFragmentOld.this, imagePosition);
//            }
//
//            @Override
//            public void clickAddProductFromGallery(int position) {
//                BaseProductAddEditFragmentOldPermissionsDispatcher.goToGalleryWithCheck(BaseProductAddEditFragmentOld.this, imagePosition);
//            }
//
//            @Override
//            public void clickAddProductFromInstagram(int position) {
//                int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
//                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), BaseProductAddEditFragmentOld.this,
//                        INSTAGRAM_SELECT_REQUEST_CODE, remainingEmptySlot);
//            }
//        });
//    }
//
//    @Override
//    public void onImagePickerItemClicked(int position, boolean isPrimary) {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        DialogFragment dialogFragment = ImageEditProductDialogFragment.newInstance(position, isPrimary);
//        dialogFragment.show(fm, ImageEditProductDialogFragment.FRAGMENT_TAG);
//        ((ImageEditProductDialogFragment) dialogFragment).setOnImageEditListener(new ImageEditProductDialogFragment.OnImageEditListener() {
//
//            @Override
//            public void clickEditImagePathFromCamera(int position) {
//                GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), BaseProductAddEditFragmentOld.this, position,
//                        true, 1,true);
//            }
//
//            @Override
//            public void clickEditImagePathFromGallery(int position) {
//                GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), BaseProductAddEditFragmentOld.this, position, 1, true);
//            }
//
//            @Override
//            public void clickEditImagePathFromInstagram(int position) {
//                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), BaseProductAddEditFragmentOld.this,
//                        INSTAGRAM_SELECT_REQUEST_CODE, 1);
//            }
//
//            @Override
//            public void clickImageEditor(int position) {
//                if (BaseProductAddEditFragmentOld.this.isAddStatus()) {
//                    UnifyTracking.eventClickImageInAddProduct(AppEventTracking.AddProduct.EVENT_ACTION_EDIT);
//                } else {
//                    UnifyTracking.eventClickImageInEditProduct(AppEventTracking.AddProduct.EVENT_ACTION_EDIT);
//                }
//                String uriOrPath = productImageViewHolder.getImagesSelectView().getImageAt(position).getUriOrPath();
//                if (!TextUtils.isEmpty(uriOrPath)) {
//                    onImageEditor(uriOrPath);
//                }
//            }
//
//            @Override
//            public void clickEditImageDesc(int position) {
//                String currentDescription = productImageViewHolder.getImagesSelectView().getImageAt(position).getDescription();
//                ImageDescriptionDialog fragment = ImageDescriptionDialog.newInstance(currentDescription);
//                fragment.show(getActivity().getSupportFragmentManager(), ImageDescriptionDialog.TAG);
//                fragment.setListener(new ImageDescriptionDialog.OnImageDescDialogListener() {
//                    @Override
//                    public void onImageDescDialogOK(String newDescription) {
//                        productImageViewHolder.getImagesSelectView().changeImageDesc(newDescription);
//                    }
//
//                    @Override
//                    public void onDismiss() {
//                        BaseProductAddEditFragmentOld.this.clearFocus();
//                    }
//                });
//            }
//
//            @Override
//            public void clickEditImagePrimary(int position) {
//                productImageViewHolder.getImagesSelectView().changeImagePrimary(true, position);
//            }
//
//            @Override
//            public void clickRemoveImage(int positions) {
//                ImagesSelectView imagesSelectView = productImageViewHolder.getImagesSelectView();
//                ImageSelectModel imageSelectModel = imagesSelectView.getSelectedImage();
//                if (imageSelectModel!= null) {
//                    String path = imageSelectModel.getUriOrPath();
//                    if (!TextUtils.isEmpty(path) && !isEdittingDraft()) {
//                        FileUtils.deleteAllCacheTkpdFile(path);
//                    }
//                }
//                imagesSelectView.removeImage();
//            }
//        });
//    }
//
//    private boolean isEdittingDraft(){
//        return isEditStatus() && getProductDraftId() > 0;
//    }
//
//    public boolean isAddStatus(){
//        return getStatusUpload() == ProductStatus.ADD;
//    }
//
//    public boolean isEditStatus(){
//        return getStatusUpload() == ProductStatus.EDIT;
//    }
//
//    @Override
//    public void onImageEditor(String uriOrPath) {
//        ArrayList<String> imageUrls = new ArrayList<>();
//        imageUrls.add(uriOrPath);
//        ImageEditorWatermarkActivity.start(getContext(),
//                BaseProductAddEditFragmentOld.this, imageUrls,
//                !isEdittingDraft());
//    }
//
//    @Override
//    public void onRemovePreviousPath(String uri) {
//        if (!TextUtils.isEmpty(uri) && !isEdittingDraft()) {
//            FileUtils.deleteAllCacheTkpdFile(uri);
//        }
//    }
//
//    private void clearFocus(){
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                View view = getActivity().getCurrentFocus();
//                if (view != null) {
//                    CommonUtils.hideSoftKeyboard(view);
//                    view.clearFocus();
//                }
//            }
//        });
//    }
//
//    @TargetApi(16)
//    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//    public void goToGallery(int imagePosition) {
//        int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
//        GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), this, imagePosition, remainingEmptySlot, true);
//    }
//
//    @TargetApi(16)
//    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//    public void goToCamera(int imagePosition) {
//        int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
//        GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), this, imagePosition,
//                true, remainingEmptySlot,true);
//
//    }
//
//    @Override
//    public final void startInfoAddProduct() {
//        startActivity(new Intent(getActivity(), ProductAddInfoActivity.class));
//    }
//
//    @Override
//    public final void startYoutubeVideoActivity(ArrayList<String> videoIds) {
//        Intent intent = new Intent(getActivity(), YoutubeAddVideoActivity.class);
//        if (CommonUtils.checkCollectionNotNull(videoIds)) {
//            intent.putStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK, videoIds);
//        }
//        startActivityForResult(intent, ProductDeliveryInfoViewHolder.REQUEST_CODE_GET_VIDEO);
//    }
//
//    @Override
//    public final void startProductVariantActivity(ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
//                                                  ProductVariantDataSubmit productVariantDataSubmit,
//                                                  ArrayList<ProductVariantOptionSubmit> productVariantOptionSubmitArrayList) {
//        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
//            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
//                @Override
//                public void onRetryClicked() {
//                    presenter.fetchProductVariantByCat(productInfoViewHolder.getCategoryId());
//                }
//            }).showRetrySnackbar();
//            return;
//        }
//        Intent intent = new Intent(getActivity(), ProductVariantDashboardActivity.class);
//        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, productVariantByCatModelList);
//        if (productVariantDataSubmit != null && productVariantDataSubmit.getProductVariantUnitSubmitList()!= null &&
//                productVariantByCatModelList.size() > 0) {
//            intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION, productVariantDataSubmit);
//        }
//        intent.putExtra(ProductVariantConstant.EXTRA_OLD_PRODUCT_OPTION_SUBMIT_LV1_LIST, productVariantOptionSubmitArrayList);
//        startActivityForResult(intent, ProductDeliveryInfoViewHolder.REQUEST_CODE_VARIANT);
//    }
//
//    @Override
//    public final void onCategoryPickerClicked(long categoryId) {
//        CategoryPickerActivity.start(this, getActivity(), ProductInfoViewHolder.REQUEST_CODE_CATEGORY, categoryId);
//    }
//
//    @Override
//    public final void onCatalogPickerClicked(String keyword, long depId, long selectedCatalogId) {
//        CatalogPickerActivity.start(this, getActivity(), ProductInfoViewHolder.REQUEST_CODE_CATALOG, keyword, depId, selectedCatalogId);
//    }
//
//    @Override
//    public final void onEtalaseViewClicked(long etalaseId) {
//        Intent intent = EtalasePickerActivity.getIntent(getActivity(), etalaseId);
//        startActivityForResult(intent, ProductManageViewHolder.REQUEST_CODE_ETALASE);
//    }
//
//
//
//    // Presenter listener part
//    @Override
//    public void onSuccessLoadScoringProduct(DataScoringProductView dataScoringProductView) {
//        productScoreViewHolder.setValueProductScoreToView(dataScoringProductView);
//    }
//
//    @Override
//    public void onErrorLoadScoringProduct(String errorMessage) {
//    }
//
//
//    @Override
//    public void onSuccessStoreProductToDraft(long productId, boolean isUploading) {
//        if (isUploading) {
//            CommonUtils.UniversalToast(getActivity(), getString(R.string.upload_product_waiting));
//            if (productDeliveryInfoViewHolder.isShare()) {
//                listener.startUploadProductWithShare(productId);
//            } else {
//                listener.startUploadProduct(productId);
//            }
//        } else {
//            if (listener!= null) {
//                listener.successSaveDraftToDBWhenBackpressed();
//            }
//        }
//    }
//
//    @Override
//    public void onErrorStoreProductToDraftWhenUpload(String errorMessage) {
//        NetworkErrorHelper.createSnackbarWithAction(getActivity(), getString(R.string.try_again), new NetworkErrorHelper.RetryClickedListener() {
//            @Override
//            public void onRetryClicked() {
//                if (isDataValid()) {
//                    saveDraft(true);
//                }
//            }
//        }).showRetrySnackbar();
//    }
//
//    @Override
//    public void onErrorStoreProductToDraftWhenBackPressed(String errorMessage) {
//        CommonUtils.UniversalToast(getActivity(), errorMessage);
//        getActivity().finish();
//    }
//
//    @Override
//    public void onErrorStoreProductAndAddToDraft(String errorMessage) {
//        NetworkErrorHelper.createSnackbarWithAction(getActivity(), getString(R.string.try_again), new NetworkErrorHelper.RetryClickedListener() {
//            @Override
//            public void onRetryClicked() {
//                if (isDataValid()) {
//                    saveAndAddDraft();
//                }
//            }
//        }).showRetrySnackbar();
//    }
//
//    @Override
//    public void onSuccessLoadCatalog(List<Catalog> catalogViewModelList) {
//        productInfoViewHolder.successFetchCatalogData(catalogViewModelList);
//    }
//
//    @Override
//    public void onErrorLoadCatalog(String errorMessage) {
//        productInfoViewHolder.hideAndClearCatalog();
//    }
//
//    @Override
//    public void onSuccessLoadRecommendationCategory(List<ProductCategoryPredictionViewModel> categoryPredictionList) {
//        productInfoViewHolder.successGetCategoryRecommData(categoryPredictionList);
//    }
//
//    @Override
//    public void onErrorLoadRecommendationCategory(String errorMessage) {
//
//    }
//
//    @Override
//    public void onSuccessLoadShopInfo(boolean isGoldMerchant, boolean isFreeReturn, boolean officialStore) {
//        productDeliveryInfoViewHolder.updateViewGoldMerchant(isGoldMerchant);
//        productManageViewHolder.setGoldMerchant(isGoldMerchant);
//        productManageViewHolder.setOfficialStore(officialStore);
//        productManageViewHolder.updateViewFreeReturn(isFreeReturn);
//        valueIndicatorScoreModel.setFreeReturnActive(isFreeReturn);
//    }
//
//    @Override
//    public void onErrorGetProductVariantByCat(Throwable throwable) {
//        onSuccessGetProductVariantCat(null);
//    }
//
//    @Override
//    public void onSuccessGetProductVariantCat(List<ProductVariantByCatModel> productVariantByCatModelList) {
//        productDeliveryInfoViewHolder.onSuccessGetProductVariantCat(productVariantByCatModelList);
//    }
//
//    @Override
//    public void onErrorLoadShopInfo(String errorMessage) {
//
//    }
//
//    @Override
//    public void onSuccessStoreProductAndAddToDraft(Long productId) {
//        CommonUtils.UniversalToast(getActivity(), getString(R.string.upload_product_waiting));
//        if (productDeliveryInfoViewHolder.isShare()) {
//            listener.startUploadProductAndAddWithShare(productId);
//        } else {
//            listener.startUploadProductAndAdd(productId);
//        }
//    }
//
//    // Others
//    @Override
//    public final void onResolutionImageCheckFailed(String uri) {
//        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.error_image_resolution));
//        FileUtils.deleteAllCacheTkpdFile(uri);
//    }
//
//    @Override
//    public final void onCategoryChanged(long categoryId) {
//        // as default, the variant inputted will be deleted (wihtout notice)
//        productDeliveryInfoViewHolder.setProductVariantDataSubmit(null, "");
//        productDeliveryInfoViewHolder.onSuccessGetProductVariantCat(null);
//        // check catalog by categoryID
//        onCategoryLoaded(categoryId);
//    }
//
//    /**
//     * when category changed, or category has been fetched
//     * @param categoryId
//     */
//    public final void onCategoryLoaded(long categoryId) {
//        // fetch product variant by category
//        presenter.fetchProductVariantByCat(categoryId);
//        // check catalog by categoryId
//        checkIfCatalogExist(productInfoViewHolder.getName(), categoryId);
//        // fetch category strings
//        fetchCategory(categoryId);
//    }
//
//    @Override
//    public final void fetchCategory(long categoryId) {
//        presenter.fetchCategory(categoryId);
//    }
//
//    protected final void checkIfCatalogExist(String productName, long categoryId) {
//        presenter.fetchCatalogData(productName, categoryId, 0, 1);
//    }
//
//    public final void addWholesaleItem(WholesaleModel wholesaleModel) {
//        productManageViewHolder.addWholesaleItem(wholesaleModel);
//    }
//
//    @Override
//    public final void populateCategory(List<String> strings) {
//        String[] stringArray = new String[strings.size()];
//        stringArray = strings.toArray(stringArray);
//        productInfoViewHolder.processCategory(stringArray);
//    }
//
//    public void deleteNotUsedTkpdCacheImage(){
//        ArrayList<ImageSelectModel> imageSelectModelArrayList = productImageViewHolder.getImagesSelectView().getImageList();
//        if (imageSelectModelArrayList == null || imageSelectModelArrayList.size() == 0) {
//            return;
//        }
//        ArrayList<String> uriArrayList = new ArrayList<>();
//        for (int i = 0, sizei = imageSelectModelArrayList.size(); i<sizei; i++) {
//            uriArrayList.add(imageSelectModelArrayList.get(i).getUriOrPath());
//        }
//        FileUtils.deleteAllCacheTkpdFiles(uriArrayList);
//    }
//
//    public void saveDraft(boolean isUploading) {
//        ProductViewModel viewModel = collectDataFromView();
//        if (isUploading) {
//            sendAnalyticsAdd(viewModel);
//        }
//        presenter.saveDraft(viewModel, isUploading);
//    }
//
//    @CallSuper
//    protected ProductViewModel collectDataFromView() {
//        ProductViewModel viewModel = new ProductViewModel();
//        viewModel.setProductName(productInfoViewHolder.getName());
//        viewModel.setProductCategory(productInfoViewHolder.getProductCategory());
//        viewModel.setProductCatalog(productInfoViewHolder.getProductCatalog());
//        viewModel.setProductPictureViewModelList(productImageViewHolder.getProductPhotos());
//
//        viewModel.setProductPriceCurrency(productManageViewHolder.getPriceUnit());
//        viewModel.setProductPrice(productManageViewHolder.getPriceValue());
//        viewModel.setProductWeightUnit(productManageViewHolder.getWeightUnit());
//        viewModel.setProductWeight(productManageViewHolder.getWeightValue());
//        viewModel.setProductMinOrder(productManageViewHolder.getMinimumOrder());
//
//        viewModel.setProductWholesale(productManageViewHolder.getProductWholesaleViewModels());
//
//        viewModel.setProductStock(productManageViewHolder.getTotalStock());
//        viewModel.setProductStatus(productManageViewHolder.getStatusStock());
//        viewModel.setProductEtalase(productManageViewHolder.getProductEtalase());
//        viewModel.setProductCondition(productManageViewHolder.getCondition());
//        viewModel.setProductMustInsurance(productManageViewHolder.isMustInsurance());
//        viewModel.setProductDescription(productDeliveryInfoViewHolder.getDescription());
//
//        viewModel.setProductFreeReturn(productManageViewHolder.isFreeReturns());
//        viewModel.setProductVideo(productDeliveryInfoViewHolder.getVideoList());
//        viewModel.setProductPreorder(productDeliveryInfoViewHolder.getPreOrder());
//        //to do hendry map old draft model to new draft model variant
////        viewModel.setProductVariant(productDeliveryInfoViewHolder.getProductVariant());
//        viewModel.setProductNameEditable(productInfoViewHolder.isNameEditable());
//
////        viewModel.setVariantStringSelection(productDeliveryInfoViewHolder.getVariantStringSelection());
//        return viewModel;
//    }
//
//    private void sendAnalyticsAdd(ProductViewModel viewModel) {
//        List<String>  listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
//                Integer.parseInt(getString(R.string.product_free_return_values_active)),
//                productDeliveryInfoViewHolder.isShare()
//        );
//        for (String labelAnalytics : listLabelAnalytics){
//            if(isAddStatus()) {
//                UnifyTracking.eventAddProductAdd(labelAnalytics);
//            } else if(isEditStatus()){
//                UnifyTracking.eventAddProductEdit(labelAnalytics);
//            }
//        }
//    }
//
//    private void sendAnalyticsAddMore(ProductViewModel viewModel) {
//        List<String>  listLabelAnalytics = AnalyticsMapper.mapViewToAnalytic(viewModel,
//                Integer.parseInt(getString(R.string.product_free_return_values_active)),
//                productDeliveryInfoViewHolder.isShare()
//        );
//        for (String labelAnalytics : listLabelAnalytics){
//            if(isAddStatus()) {
//                UnifyTracking.eventAddProductAddMore(labelAnalytics);
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case ProductInfoViewHolder.REQUEST_CODE_CATEGORY:
//                productInfoViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            case ProductInfoViewHolder.REQUEST_CODE_CATALOG:
//                productInfoViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            case ImageGallery.TOKOPEDIA_GALLERY:
//                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            case INSTAGRAM_SELECT_REQUEST_CODE:
//                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            case ImageEditorActivity.REQUEST_CODE:
//                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            case ProductManageViewHolder.REQUEST_CODE_ETALASE:
//                productManageViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            case ProductDeliveryInfoViewHolder.REQUEST_CODE_GET_VIDEO:
//                productDeliveryInfoViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            case ProductDeliveryInfoViewHolder.REQUEST_CODE_VARIANT:
//                productDeliveryInfoViewHolder.onActivityResult(requestCode, resultCode, data);
//                break;
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    // Permission part
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // NOTE: delegate the permission handling to generated method
//        BaseProductAddEditFragmentOldPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }
//
//    @TargetApi(16)
//    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    void showDeniedForExternalStorage() {
//        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//    }
//
//    @TargetApi(16)
//    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    void showNeverAskForExternalStorage() {
//        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//    }
//
//    @TargetApi(16)
//    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    void showRationaleForExternalStorage(final PermissionRequest request) {
//        request.proceed();
//    }
//
//    // View holder listener part
//    @Override
//    public void onProductNameChanged(String productName) {
//        getCategoryRecommendation(productName);
//        productInfoViewHolder.hideAndClearCatalog();
//        checkIfCatalogExist(productInfoViewHolder.getName(), productInfoViewHolder.getCategoryId());
//        valueIndicatorScoreModel.setLengthProductName(productName.length());
//        presenter.getProductScoreDebounce(valueIndicatorScoreModel);
//    }
//
//    @Override
//    public final void showDialogMoveToGM(@StringRes int message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
//                R.style.AppCompatAlertDialogStyle);
//        builder.setTitle(R.string.add_product_title_alert_dialog_dollar);
//        if(showDialogSaveDraftOnBack() ){
//            builder.setMessage(getString(R.string.add_product_label_alert_save_as_draft_dollar_and_video, getString(message)));
//        }else{
//            builder.setMessage(message);
//        }
//        builder.setCancelable(true);
//        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                UnifyTracking.eventClickYesGoldMerchantAddProduct();
//                if(showDialogSaveDraftOnBack()){
//                    saveDraft(false);
//                }
//                goToGoldMerchantPage();
//                getActivity().finish();
//            }
//        });
//        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    public boolean showDialogSaveDraftOnBack(){
//        return true;
//    }
//
//    @Override
//    public void onHasVideoChange(boolean hasVideo) {
//        valueIndicatorScoreModel.setHasVideo(hasVideo);
//        updateProductScoring();
//    }
//
//    @Override
//    public void onVariantCountChange(boolean hasActiveVariant) {
//        valueIndicatorScoreModel.setVariantActive(hasActiveVariant);
//        updateProductScoring();
//    }
//
//    @Override
//    public void onTotalImageUpdated(int total) {
//        valueIndicatorScoreModel.setImageCount(total);
//        updateProductScoring();
//    }
//
//    @Override
//    public void onImageResolutionChanged(long maxSize) {
//        valueIndicatorScoreModel.setImageResolution((int)maxSize);
//        updateProductScoring();
//    }
//
//    @Override
//    public void onFreeReturnChecked(boolean checked) {
//        valueIndicatorScoreModel.setFreeReturnStatus(checked);
//        updateProductScoring();
//    }
//
//    @Override
//    public void onTotalStockUpdated(int total) {
//        valueIndicatorScoreModel.setStockStatus(total > 0);
//        updateProductScoring();
//    }
//
//    @Override
//    public void onDescriptionTextChanged(String text) {
//        valueIndicatorScoreModel.setLengthDescProduct(text.length());
//        updateProductScoring();
//    }
//
//    public void goToGoldMerchantPage() {
//        if (getActivity().getApplication() instanceof SellerModuleRouter) {
//            ((SellerModuleRouter) getActivity().getApplication()).goToGMSubscribe(getActivity());
//        }
//    }
//
//    private void updateProductScoring() {
//        presenter.getProductScoring(valueIndicatorScoreModel);
//    }
//
//    @Override
//    public final void onDestroy() {
//        super.onDestroy();
//        presenter.detachView();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        productInfoViewHolder.onSaveInstanceState(outState);
//        productImageViewHolder.onSaveInstanceState(outState);
//        productManageViewHolder.onSaveInstanceState(outState);
//        productDeliveryInfoViewHolder.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState == null) {
//            return;
//        }
//        productInfoViewHolder.onViewStateRestored(savedInstanceState);
//        productImageViewHolder.onViewStateRestored(savedInstanceState);
//        productManageViewHolder.onViewStateRestored(savedInstanceState);
//        productDeliveryInfoViewHolder.onViewStateRestored(savedInstanceState);
//    }
//
//    @TargetApi(23)
//    @Override
//    public final void onAttach(Context context) {
//        super.onAttach(context);
//        onAttachToContext(context);
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public final void onAttach(Activity activity) {
//        super.onAttach(activity);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            onAttachToContext(activity);
//        }
//    }
//
//    private void onAttachToContext(Context context) {
//        this.listener = (Listener) context;
//    }
//
//    @Override
//    protected String getScreenName() {
//        return null;
//    }

}
