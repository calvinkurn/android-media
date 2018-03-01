package com.tokopedia.seller.product.edit.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;

/**
 * Created by nathan on 4/3/17.
 */

@RuntimePermissions
public class ProductAddActivity extends BaseProductAddEditActivity {

    public static final String EXTRA_IMAGE_URLS = "img_urls";
    public static final String IMAGE = "image/";
    public static final String CONTENT_GMAIL_LS = "content://gmail-ls/";

    // url got from gallery or camera
    private ArrayList<String> imageUrls;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ProductAddActivity.class);
        activity.startActivityForResult(intent, PRODUCT_REQUEST_CODE);
    }

    public static void start(Activity activity, ArrayList<String> imageUrls) {
        Intent intent = new Intent(activity, ProductAddActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGE_URLS, imageUrls);
        activity.startActivityForResult(intent, PRODUCT_REQUEST_CODE);
    }

    public static void start(Fragment fragment, Context context, ArrayList<String> imageUrls) {
        Intent intent = new Intent(context, ProductAddActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGE_URLS, imageUrls);
        fragment.startActivityForResult(intent, PRODUCT_REQUEST_CODE);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ProductAddActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductAddFragment.createInstance(imageUrls);
    }

    @Override
    protected int getCancelMessageRes() {
        return R.string.product_draft_dialog_cancel_message;
    }

    @Override
    protected boolean needDeleteCacheOnBack() {
        return true;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ADD_PRODUCT;
    }

    @DeepLink(Constants.Applinks.PRODUCT_ADD)
    public static Intent getCallingApplinkAddProductMainAppIntent(Context context, Bundle extras) {
        Intent intent = null;
        if (SessionHandler.isUserHasShop(context)) {
            intent = new Intent(context, ProductAddActivity.class);
        } else {
            if (GlobalConfig.isSellerApp()) {
                intent = SellerAppRouter.getSellerHomeActivity(context);
            } else {
                intent = HomeRouter.getHomeActivityInterfaceRouter(context);
            }
        }
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return intent
                .setData(uri.build())
                .putExtras(extras);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void handleImageUrlFromExternal() {
        showProgressDialog();
        List<String> oriImageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        int imagesCount = (oriImageUrls.size() > MAX_IMAGES) ? MAX_IMAGES : oriImageUrls.size();
        imageUrls = new ArrayList<>();
        for (int i = 0; i < imagesCount; i++) {
            String imageUrl = oriImageUrls.get(i);
            if (FileUtils.isInTkpdCache(new File(imageUrl))) {
                imageUrls.add(imageUrl);
            } else {
                File photo = FileUtils.writeImageToTkpdPath(imageUrl);
                if (photo != null) {
                    imageUrls.add(photo.getAbsolutePath());
                }
            }
        }
        dismissDialog();
        createProductAddFragment();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void handleImageUrlImplicitSingle() {
        Uri imageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        ArrayList<Uri> imageUris = new ArrayList<>();
        imageUris.add(imageUri);
        processMultipleImage(imageUris);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void handleImageUrlImplicitMultiple() {
        ArrayList<Uri> imageUris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (checkCollectionNotNull(imageUris)) {
            processMultipleImage(imageUris);
        } else {
            handleImageUrlImplicitSingle();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        createProductAddFragment();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        createProductAddFragment();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForExternalStorage(final PermissionRequest request) {
        request.proceed();
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        if (getFragment() != null) {
            return;
        }
        if (checkExplicitImageUrls()) {
            ProductAddActivityPermissionsDispatcher.handleImageUrlFromExternalWithCheck(this);
        } else if (checkImplicitImageUrls()) {
            // because it comes form implicit Uris, check if already login and has shop
            if (validateHasLoginAndShop()) {
                Intent intent = getIntent();
                if (intent != null && intent.getAction()!=null) {
                    switch (intent.getAction()) {
                        case Intent.ACTION_SEND:
                            ProductAddActivityPermissionsDispatcher.handleImageUrlImplicitSingleWithCheck(this);
                            break;
                        case Intent.ACTION_SEND_MULTIPLE:
                            ProductAddActivityPermissionsDispatcher.handleImageUrlImplicitMultipleWithCheck(this);
                            break;
                    }
                }
            }
        } else { // no image urls, create it directly
            createProductAddFragment();
        }
    }

    private boolean checkExplicitImageUrls() {
        Intent intent = getIntent();
        ArrayList<String> imageUrlsTemp = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS);
        return (imageUrlsTemp != null && imageUrlsTemp.size() > 0);
    }

    private boolean checkImplicitImageUrls() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (type == null) {
            return false;
        }
        if (!type.startsWith(IMAGE)) {
            return false;
        }
        if (Intent.ACTION_SEND.equals(action)) {
            return (intent.getParcelableExtra(Intent.EXTRA_STREAM) != null);
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
            return (intent.getParcelableExtra(Intent.EXTRA_STREAM) != null ||
                    checkCollectionNotNull(intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)));
        }
        return false;
    }

    private boolean validateHasLoginAndShop() {
        if (SessionHandler.isV4Login(this)) {
            if (!SessionHandler.isUserHasShop(this)) {
                finish();
                CommonUtils.UniversalToast(getBaseContext(), getString(R.string.title_no_shop));
                return false;
            }
        } else {
            Intent intentLogin = ((SellerModuleRouter)(getApplication())).getLoginIntent (this);
            startActivity(intentLogin);
            finish();
            return false;
        }
        return true;
    }

    @DeepLink(Constants.Applinks.SellerApp.PRODUCT_ADD)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            Intent intent = getCallingIntent(context);
            return intent
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
    }

    private void processMultipleImage(ArrayList<Uri> imageUris) {
        showProgressDialog();
        int imagescount = (imageUris.size() > MAX_IMAGES) ? MAX_IMAGES : imageUris.size();
        imageUrls = new ArrayList<>();
        for (int i = 0; i < imagescount; i++) {
            Uri imageUri = imageUris.get(i);
            String uriString = imageUri.toString();
            if (uriString.startsWith(CONTENT_GMAIL_LS)) {// get email attachment from gmail
                imageUrls.add(FileUtils.getPathFromGmail(this, imageUri));
            } else { // get extras for import from gallery
                String url = FileUtils.getTkpdPathFromURI(this, imageUri);
                if (!TextUtils.isEmpty(url)) {
                    imageUrls.add(url);
                }
            }
        }
        dismissDialog();
        createProductAddFragment();
    }



}