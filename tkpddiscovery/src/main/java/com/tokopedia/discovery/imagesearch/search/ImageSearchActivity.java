package com.tokopedia.discovery.imagesearch.search;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.RequestPermissionUtil;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.search.fragment.ImageSearchProductListFragment;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.constant.SearchEventTracking;
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

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

public class ImageSearchActivity extends DiscoveryActivity
        implements ImageSearchContract.View, RedirectionListener {

    private static final int REQUEST_CODE_IMAGE = 2390;
    private static final String NO_RESPONSE = "no response";
    private static final String SUCCESS = "success match found";

    private String imagePath;
    private boolean isImageAlreadyPicked = false;

    @Inject
    ImageSearchPresenter searchPresenter;

    @Inject
    PermissionCheckerHelper permissionCheckerHelper;

    public static Intent newInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ImageSearchActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImageSearch();

        askForPermission();
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

    private void askForPermission() {
        String[] imageSearchPermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        permissionCheckerHelper.checkPermissions(
                this,
                imageSearchPermissions,
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        permissionCheckerHelper.onPermissionDenied(ImageSearchActivity.this, permissionText);
                        finish();
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        permissionCheckerHelper.onNeverAskAgain(ImageSearchActivity.this, permissionText);
                        finish();
                    }

                    @Override
                    public void onPermissionGranted() {
                        handleImageUri(getIntent());

                        if (!isImageAlreadyPicked) {
                            openImagePickerActivity();
                        }
                    }
                },
                ""
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void handleImageUri(Intent intent) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);

        if (remoteConfig.getBoolean(RemoteConfigKey.SHOW_IMAGE_SEARCH, false)
                && intent != null) {

            if (intent.getClipData() != null
                    && intent.getClipData().getItemCount() > 0) {
                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM();
                ClipData clipData = intent.getClipData();
                Uri uri = clipData.getItemAt(0).getUri();
                isImageAlreadyPicked = true;
                onImagePickedSuccess(uri.toString());
            }
            else if (intent.getData() != null
                    && !TextUtils.isEmpty(intent.getData().toString())
                    && isValidMimeType(intent.getData().toString())) {
                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM();
                isImageAlreadyPicked = true;
                onImagePickedSuccess(intent.getData().toString());
            }
        }
    }

    private void sendImageSearchFromGalleryGTM() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                SearchEventTracking.Category.IMAGE_SEARCH,
                SearchEventTracking.Action.EXTERNAL_IMAGE_SEARCH,
                "");
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

    @Override
    public void onHandleImageSearchResponseSuccess() {

        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
        if (isFromCamera) {
            sendCameraImageSearchResultGTM(SUCCESS);
        } else {
            sendGalleryImageSearchResultGTM(SUCCESS);
        }
    }

    private void sendGalleryImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                SearchEventTracking.Category.IMAGE_SEARCH,
                SearchEventTracking.Action.GALLERY_SEARCH_RESULT,
                label);
    }


    private void sendCameraImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                SearchEventTracking.Category.IMAGE_SEARCH,
                SearchEventTracking.Action.CAMERA_SEARCH_RESULT,
                label);
    }

    @Override
    public void onHandleImageResponseSearch(ProductViewModel productViewModel) {
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
        sendMoEngageSearchAttempt(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);

        proceed(productViewModel);
    }

    public void sendMoEngageSearchAttempt(String keyword, boolean isResultFound, HashMap<String, String> category) {
        Map<String, Object> value = DataLayer.mapOf(
                SearchEventTracking.MOENGAGE.KEYWORD, keyword,
                SearchEventTracking.MOENGAGE.IS_RESULT_FOUND, isResultFound
        );
        if (category != null) {
            value.put(SearchEventTracking.MOENGAGE.CATEGORY_ID_MAPPING, new JSONArray(Arrays.asList(category.keySet().toArray())));
            value.put(SearchEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING, new JSONArray((category.values())));
        }
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, SearchEventTracking.EventMoEngage.SEARCH_ATTEMPT);
    }

    private void proceed(ProductViewModel productViewModel) {
        initView();

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
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }

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
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }

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
                    () -> onImagePickedSuccess(getImagePath())).showRetrySnackbar();
        }
    }

    @Override
    public void showTimeoutErrorNetwork(String message) {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
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

    public void onImagePickedSuccess(String imagePath) {
        setImagePath(imagePath);
        tkpdProgressDialog = new TkpdProgressDialog(this, 1);
        tkpdProgressDialog.showDialog();
        getPresenter().requestImageSearch(imagePath);
    }

    @Override
    public void showImageNotSupportedError() {
        super.showImageNotSupportedError();
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }

        NetworkErrorHelper.showSnackbar(this, getResources().getString(R.string.image_not_supported));
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
    protected void onDestroy() {
        if(searchPresenter != null) {
            searchPresenter.detachView();
        }

        super.onDestroy();
    }
}
