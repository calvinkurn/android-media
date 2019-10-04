package com.tokopedia.discovery.imagesearch.search;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.search.fragment.ImageSearchProductListFragment;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.constant.DiscoveryEventTracking;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

public class ImageSearchActivity extends DiscoveryActivity
        implements ImageSearchContract.View,
        RedirectionListener,
        PermissionCheckerHelper.PermissionCheckListener {

    private static final int REQUEST_CODE_IMAGE = 2390;
    private static final String NO_RESPONSE = "no response";
    private static final String SUCCESS = "success match found";
    private static final String KEY_IMAGE_PATH = "image_path";

    private String imagePath = "";
    private boolean isFromCamera = false;

    @Inject
    ImageSearchPresenter searchPresenter;

    @Inject
    PermissionCheckerHelper permissionCheckerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImageSearch();

        if (savedInstanceState == null) {
            checkPermissionToContinue();
        }
        else {
            restoreStateOnCreate(savedInstanceState);
        }
    }

    private void initImageSearch() {
        initInjector();
        setPresenter(searchPresenter);
        searchPresenter.attachView(this);
        searchPresenter.setDiscoveryView(this);
    }

    private void initInjector() {
        SearchComponent searchComponent = DaggerSearchComponent.builder()
                .appComponent(getApplicationComponent())
                .build();

        searchComponent.inject(this);
    }

    private void checkPermissionToContinue() {
        String[] imageSearchPermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        permissionCheckerHelper.checkPermissions(this, imageSearchPermissions, this, "");
    }

    private void restoreStateOnCreate(@NonNull Bundle savedInstanceState) {
        String imagePath = savedInstanceState.getString(KEY_IMAGE_PATH, "");

        if (!TextUtils.isEmpty(imagePath)) {
            onImagePickedSuccess(imagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(@NotNull String permissionText) {
        permissionCheckerHelper.onPermissionDenied(this, permissionText);
        finish();
    }

    @Override
    public void onNeverAskAgain(@NotNull String permissionText) {
        permissionCheckerHelper.onNeverAskAgain(this, permissionText);
        finish();
    }

    @Override
    public void onPermissionGranted() {
        boolean isImageAlreadyPicked = handleImageUri(getIntent());

        if (!isImageAlreadyPicked) {
            openImagePickerActivity();
        }
    }

    private boolean handleImageUri(Intent intent) {
        if (canHandleImageUri(intent)) {
            if (hasClipData(intent)) {
                String imagePath = getImagePathFromClipData(intent);
                processImageUri(imagePath);
                return true;
            }
            else if (hasDataWithValidMimeType(intent)) {
                String imagePath = getImagePathFromData(intent);
                processImageUri(imagePath);
                return true;
            }
        }

        return false;
    }

    private boolean canHandleImageUri(Intent intent) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);

        return remoteConfig.getBoolean(RemoteConfigKey.SHOW_IMAGE_SEARCH, false)
                && intent != null;
    }

    private boolean hasClipData(Intent intent) {
        return intent.getClipData() != null && intent.getClipData().getItemCount() > 0;
    }

    private String getImagePathFromClipData(Intent intent) {
        ClipData clipData = intent.getClipData();
        Uri uri = Objects.requireNonNull(clipData).getItemAt(0).getUri();
        return uri.toString();
    }

    private boolean hasDataWithValidMimeType(Intent intent) {
        return intent.getData() != null
                && !TextUtils.isEmpty(intent.getData().toString())
                && isValidMimeType(intent.getData().toString());
    }

    private boolean isValidMimeType(String url) {
        String mimeType = getMimeTypeUri(Uri.parse(url));

        return mimeType != null &&
                (mimeType.equalsIgnoreCase("image/jpg") ||
                        mimeType.equalsIgnoreCase("image/png") ||
                        mimeType.equalsIgnoreCase("image/jpeg"));

    }

    private String getMimeTypeUri(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private String getImagePathFromData(Intent intent) {
        return Objects.requireNonNull(intent.getData()).toString();
    }

    private void processImageUri(String imagePath) {
        searchView.hideShowCaseDialog(true);
        sendImageSearchFromGalleryGTM();
        onImagePickedSuccess(imagePath);
    }

    private void sendImageSearchFromGalleryGTM() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DiscoveryEventTracking.Event.IMAGE_SEARCH_CLICK,
                DiscoveryEventTracking.Category.IMAGE_SEARCH,
                DiscoveryEventTracking.Action.EXTERNAL_IMAGE_SEARCH,
                "");
    }

    private void openImagePickerActivity() {
        ImagePickerEditorBuilder imagePickerEditorBuilder = new ImagePickerEditorBuilder(
                createAllowedImageEditorActions(),
                false,
                createAllowedImageRatioList()
        );

        ImagePickerBuilder builder = new ImagePickerBuilder(
                getString(R.string.choose_image),
                createImagePickerTabTypes(),
                GalleryType.IMAGE_ONLY,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.IMAGE_SEARCH_MIN_RESOLUTION,
                null,
                true,
                imagePickerEditorBuilder,
                null
        );

        Intent intent = ImageSearchImagePickerActivity.getIntent(this, builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
        overridePendingTransition(0, 0);
    }

    @ImageEditActionTypeDef
    private int[] createAllowedImageEditorActions() {
        return new int[] {
                ACTION_CROP,
                ACTION_BRIGHTNESS,
                ACTION_CONTRAST
        };
    }

    private ArrayList<ImageRatioTypeDef> createAllowedImageRatioList() {
        ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList = new ArrayList<>();

        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.ORIGINAL);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_1_1);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_3_4);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_4_3);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_16_9);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_9_16);

        return imageRatioTypeDefArrayList;
    }

    @ImagePickerTabTypeDef
    private int[] createImagePickerTabTypes() {
        return new int[] {
                TYPE_GALLERY,
                TYPE_CAMERA
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE) {
            handleResultFromImagePicker(resultCode, data);
        }
    }

    private void handleResultFromImagePicker(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CANCELED:
                cancelImageSearch();
                break;
            case RESULT_OK:
                continueToImageSearch(data);
                break;
            default:
                errorHandlingImagePicker();
        }
    }

    private void cancelImageSearch() {
        finish();
    }

    private void continueToImageSearch(Intent data) {
        if (data == null) {
            errorHandlingImagePicker();
            return;
        }

        ArrayList<String> imagePathList = data.getStringArrayListExtra(ImageSearchImagePickerActivity.PICKER_RESULT_PATHS);
        if (imagePathList == null || imagePathList.size() <= 0) {
            errorHandlingImagePicker();
            return;
        }

        isFromCamera = data.getBooleanExtra(ImageSearchImagePickerActivity.RESULT_IS_FROM_CAMERA, false);

        String imagePath = imagePathList.get(0);

        if (!TextUtils.isEmpty(imagePath)) {
            onImagePickedSuccess(imagePath);
        } else {
            errorHandlingImagePicker();
        }

        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    private void errorHandlingImagePicker() {
        Toast.makeText(this, getString(com.tokopedia.core2.R.string.error_gallery_valid), Toast.LENGTH_LONG).show();
        finish();
    }

    private void sendGalleryImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DiscoveryEventTracking.Event.IMAGE_SEARCH_CLICK,
                DiscoveryEventTracking.Category.IMAGE_SEARCH,
                DiscoveryEventTracking.Action.GALLERY_SEARCH_RESULT,
                label);
    }


    private void sendCameraImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DiscoveryEventTracking.Event.IMAGE_SEARCH_CLICK,
                DiscoveryEventTracking.Category.IMAGE_SEARCH,
                DiscoveryEventTracking.Action.CAMERA_SEARCH_RESULT,
                label);
    }

    public void onImagePickedSuccess(String imagePath) {
        setImagePath(imagePath);
        showLoadingView(true);
        showContainer(false);
        getPresenter().requestImageSearch(imagePath);
    }

    @Override
    public void onHandleImageResponseSearch(ProductViewModel productViewModel) {
        trackEventOnSuccessImageSearch(productViewModel);

        showImageSearchResult(productViewModel);

        showLoadingView(false);
    }

    private void trackEventOnSuccessImageSearch(ProductViewModel productViewModel) {
        sendGTMEventSuccessImageSearch();
        sendAppsFlyerEventSuccessImageSearch(productViewModel);
    }

    private void sendGTMEventSuccessImageSearch() {
        if (isFromCamera) {
            sendCameraImageSearchResultGTM(SUCCESS);
        } else {
            sendGalleryImageSearchResultGTM(SUCCESS);
        }
    }

    private void sendAppsFlyerEventSuccessImageSearch(ProductViewModel productViewModel) {
        JSONArray afProdIds = new JSONArray();
        HashMap<String, String> category = new HashMap<String, String>();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                } else {
                    break;
                }
                category.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());
            }
        }

        TrackingUtils.eventAppsFlyerViewListingSearch(this, afProdIds,productViewModel.getQuery(),prodIdArray);
        sendMoEngageSearchAttemptSuccessImageSearch(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);
    }

    public void sendMoEngageSearchAttemptSuccessImageSearch(String keyword, boolean isResultFound, HashMap<String, String> category) {
        Map<String, Object> value = DataLayer.mapOf(
                DiscoveryEventTracking.MOENGAGE.KEYWORD, keyword,
                DiscoveryEventTracking.MOENGAGE.IS_RESULT_FOUND, isResultFound
        );
        if (category != null) {
            value.put(DiscoveryEventTracking.MOENGAGE.CATEGORY_ID_MAPPING, new JSONArray(Arrays.asList(category.keySet().toArray())));
            value.put(DiscoveryEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING, new JSONArray((category.values())));
        }
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, DiscoveryEventTracking.EventMoEngage.SEARCH_ATTEMPT);
    }

    private void showImageSearchResult(ProductViewModel productViewModel) {
        if (productViewModel != null) {
            setLastQuerySearchView(productViewModel.getQuery());
            loadSection(productViewModel);
            setToolbarTitle(getString(R.string.image_search_title));
        } else {
            searchView.showSearch(true, false);
        }
    }

    private void loadSection(ProductViewModel productViewModel) {
        addFragment(R.id.container, ImageSearchProductListFragment.newInstance(productViewModel));

        showContainer(true);
    }

    private void addFragment(int containerViewId, ImageSearchProductListFragment fragment) {
        if (!isFinishing() && !fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onHandleInvalidImageSearchResponse() {
        showLoadingView(false);

        if (isFromCamera) {
            sendCameraImageSearchResultGTM(NO_RESPONSE);
        } else {
            sendGalleryImageSearchResultGTM(NO_RESPONSE);
        }

        Toast.makeText(this, getString(R.string.invalid_image_search_response), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showErrorNetwork(String message) {
        showLoadingView(false);

        if (isFromCamera) {
            sendCameraImageSearchResultGTM(NO_RESPONSE);
        } else {
            sendGalleryImageSearchResultGTM(NO_RESPONSE);
        }

        if (TextUtils.isEmpty(getImagePath())) {
            NetworkErrorHelper.showSnackbar(this, message);
        } else {
            NetworkErrorHelper.createSnackbarWithAction(
                    this,
                    message,
                    () -> onImagePickedSuccess(getImagePath())
            ).showRetrySnackbar();
        }
    }

    @Override
    public void showTimeoutErrorNetwork(String message) {
        showLoadingView(false);

        if (TextUtils.isEmpty(getImagePath())) {
            NetworkErrorHelper.showSnackbar(this, message);
        } else {
            NetworkErrorHelper.createSnackbarWithAction(
                    this,
                    message,
                    () -> onImagePickedSuccess(getImagePath())
            ).showRetrySnackbar();
        }
    }

    @Override
    public void showImageNotSupportedError() {
        showLoadingView(false);

        Toast.makeText(this, getResources().getString(R.string.image_not_supported), Toast.LENGTH_LONG).show();
        finish();
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void showSearchInputView() {
        searchView.showSearch(true, DiscoverySearchView.TAB_DEFAULT_SUGGESTION);
        searchView.setFinishOnClose(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_IMAGE_PATH, imagePath);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if(searchPresenter != null) {
            searchPresenter.detachView();
        }

        super.onDestroy();
    }
}
