package com.tokopedia.seller.product.edit.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.dialog.AddWholeSaleDialog;
import com.tokopedia.seller.base.view.dialog.BaseTextPickerDialogFragment;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.edit.view.service.UploadProductService;

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
public class ProductAddActivity extends BaseSimpleActivity implements HasComponent<ProductComponent>,
        BaseTextPickerDialogFragment.Listener, AddWholeSaleDialog.WholeSaleDialogListener, ProductAddFragment.Listener {

    public static final int PRODUCT_REQUEST_CODE = 8293;
    public static final String EXTRA_IMAGE_URLS = "img_urls";
    public static final String IMAGE = "image/";
    public static final String CONTENT_GMAIL_LS = "content://gmail-ls/";
    public static final int MAX_IMAGES = 5;

    TkpdProgressDialog tkpdProgressDialog;
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

    @DeepLink(Constants.Applinks.PRODUCT_ADD)
    public static Intent getCallingApplinkAddProductMainAppIntent(Context context, Bundle extras) {
        Intent intent = null;
        if (SessionHandler.isUserHasShop(context)) {
            intent = new Intent(context, ProductAddActivity.class);
        } else {
            if (GlobalConfig.isSellerApp()) {
                intent = SellerAppRouter.getSellerHomeActivity(context);
            } else {
                intent = HomeRouter.getHomeActivity(context);
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
            if(FileUtils.isInTkpdCache(new File(imageUrl))) {
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
                switch (getIntent().getAction()) {
                    case Intent.ACTION_SEND:
                        ProductAddActivityPermissionsDispatcher.handleImageUrlImplicitSingleWithCheck(this);
                        break;
                    case Intent.ACTION_SEND_MULTIPLE:
                        ProductAddActivityPermissionsDispatcher.handleImageUrlImplicitMultipleWithCheck(this);
                        break;
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
            if (SessionHandler.getShopID(this).equals("0")) {
                finish();
                CommonUtils.UniversalToast(getBaseContext(), getString(R.string.title_no_shop));
                return false;
            }
        } else {
            Intent intentLogin = OldSessionRouter.getLoginActivityIntent(this);
            intentLogin.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
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

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ProductAddActivity.class);
    }

    protected int getCancelMessageRes(){
        return R.string.product_draft_dialog_cancel_message;
    }

    @Override
    public void onBackPressed() {
        if (hasDataAdded()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(getCancelMessageRes()))
                    .setPositiveButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteNotUsedTkpdCacheImage();
                            backPressedHandleTaskRoot();
                        }
                    }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            alertDialogBuilder.setNeutralButton(getString(R.string.product_draft_save_as_draft), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean doSave = saveProductToDraft();
                    if (!doSave) {
                        UnifyTracking.eventClickAddProduct(AppEventTracking.Category.ADD_PRODUCT,
                                AppEventTracking.EventLabel.SAVE_DRAFT);
                        backPressedHandleTaskRoot();
                    }
                }
            });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        } else {
            backPressedHandleTaskRoot();
        }
    }

    private void backPressedHandleTaskRoot(){
        if (isTaskRoot()) {
            Intent homeIntent = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
            startActivity(homeIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasDataAdded() {
        ProductAddFragment fragment = getProductAddFragment();
        if (fragment != null) {
            return fragment.showDialogSaveDraftOnBack();
        }
        return false;
    }

    private void deleteNotUsedTkpdCacheImage() {
        if (needDeleteCacheOnBack()) {
            ProductAddFragment fragment = getProductAddFragment();
            if (fragment != null) {
                fragment.deleteNotUsedTkpdCacheImage();
            }
        }
    }

    protected boolean needDeleteCacheOnBack(){
        return true;
    }

    private boolean saveProductToDraft() {
        // save newly added product ToDraft
        Fragment fragment = getFragment();
        if (fragment != null && fragment instanceof ProductAddFragment) {
            ((ProductAddFragment) fragment).saveDraft(false);
            return true;
        }
        return false;
    }

    private void createProductAddFragment() {
        if (getFragment() != null) {
            return;
        }
        inflateFragment();
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductAddFragment.createInstance(imageUrls);
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

    public void showProgressDialog() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        }
        tkpdProgressDialog.showDialog();
    }

    public void dismissDialog() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    public void onTextPickerSubmitted(String text) {

    }

    @Override
    public void addWholesaleItem(WholesaleModel item) {
        if (getProductAddFragment() != null && getProductAddFragment().isVisible()) {
            getProductAddFragment().addWholesaleItem(item);
        }
    }

    public ProductAddFragment getProductAddFragment() {
        Fragment fragment = getFragment();
        if (fragment != null && fragment instanceof ProductAddFragment) {
            return (ProductAddFragment) fragment;
        }
        return null;
    }

    @Override
    public void startAddWholeSaleDialog(
            WholesaleModel fixedPrice,
            @CurrencyTypeDef int currencyType,
            WholesaleModel previousWholesalePrice, boolean officialStore) {
        AddWholeSaleDialog addWholeSaleDialog = AddWholeSaleDialog.newInstance(
                fixedPrice, currencyType, previousWholesalePrice, officialStore
        );
        addWholeSaleDialog.show(getSupportFragmentManager(), AddWholeSaleDialog.TAG);
        addWholeSaleDialog.setOnDismissListener(new AddWholeSaleDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        View view = getCurrentFocus();
                        if (view != null) {
                            CommonUtils.hideSoftKeyboard(view);
                            view.clearFocus();
                        }
                    }
                });
            }
        });
    }

    public void startUploadProduct(long productId) {
        startUploadProductService(productId);
        finish();
    }

    private void startUploadProductService(long productId){
        ProductAddFragment productAddFragment = getProductAddFragment();
        boolean isAdd = true;
        if (productAddFragment!= null) {
            isAdd = productAddFragment.getStatusUpload() == ProductStatus.ADD;
        }
        startService(UploadProductService.getIntent(this, productId, isAdd));
    }

    public void startUploadProductWithShare(long productId) {
        startUploadProductService(productId);
        startActivity(ProductDetailRouter.createAddProductDetailInfoActivity(this));
        finish();
    }

    @Override
    public void startUploadProductAndAdd(Long productId) {
        startUploadProductService(productId);
        start(this);
        finish();
    }

    @Override
    public void successSaveDraftToDBWhenBackpressed() {
        CommonUtils.UniversalToast(this, getString(R.string.product_draft_product_has_been_saved_as_draft));
        finish();
    }

    @Override
    public void startUploadProductAndAddWithShare(Long productId) {
        startUploadProductService(productId);
        start(this);
        startActivity(ProductDetailRouter.createAddProductDetailInfoActivity(this));
        finish();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_ADD_PRODUCT;
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

}