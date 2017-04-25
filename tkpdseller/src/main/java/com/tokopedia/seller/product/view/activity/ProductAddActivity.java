package com.tokopedia.seller.product.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.dialog.AddWholeSaleDialog;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.dialog.TextPickerDialogListener;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.service.AddProductService;

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
import static com.tokopedia.core.newgallery.GalleryActivity.DEF_QLTY_COMPRESS;
import static com.tokopedia.core.newgallery.GalleryActivity.DEF_WIDTH_CMPR;

/**
 * Created by nathan on 4/3/17.
 */

@RuntimePermissions
public class ProductAddActivity extends TActivity implements HasComponent<AppComponent>,
        TextPickerDialogListener, AddWholeSaleDialog.WholeSaleDialogListener, ProductAddFragment.Listener {

    public static final String EXTRA_IMAGE_URLS = "img_urls";
    public static final String IMAGE = "image/";
    public static final String CONTENT_GMAIL_LS = "content://gmail-ls/";
    public static final int MAX_IMAGES = 5;
    // url got from gallery or camera
    private ArrayList<String> imageUrls;

    TkpdProgressDialog tkpdProgressDialog;

    public static void start(Context context) {
        Intent intent = new Intent(context, ProductAddActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, ArrayList<String> imageUrls) {
        Intent intent = new Intent(context, ProductAddActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGE_URLS, imageUrls);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragment();
    }

    protected void setupFragment() {
        inflateView(R.layout.activity_product_add);
        checkIntentImageUrls();
    }

    private void createProductAddFragment() {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, ProductAddFragment.createInstance(imageUrls), ProductAddFragment.class.getSimpleName())
                .commit();
    }

    private void checkIntentImageUrls() {
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

    private boolean validateHasLoginAndShop() {
        if (SessionHandler.isV4Login(this)) {
            if (SessionHandler.getShopID(this).equals("0")) {
                finish();
                CommonUtils.UniversalToast(getBaseContext(),
                        getString(R.string.title_no_shop));
                return false;
            }
        } else {
            Intent intentLogin = SessionRouter.getLoginActivityIntent(this);
            intentLogin.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            startActivity(intentLogin);
            finish();
            return false;
        }
        return true;
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
                imageUrls.add(FileUtils.getRealPathFromURI(this, imageUri));
            }
        }
        dismissDialog();
        createProductAddFragment();
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void handleImageUrlFromExternal() {
        showProgressDialog();
        List<String> oriImageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        int imagescount = (oriImageUrls.size() > MAX_IMAGES) ? MAX_IMAGES : oriImageUrls.size();
        imageUrls = new ArrayList<>();
        for (int i = 0; i < imagescount; i++) {
            String imageUrl = oriImageUrls.get(i);
            String fileNameToMove = FileUtils.generateUniqueFileName(imageUrl);
            File photo = FileUtils.writeImageToTkpdPath(
                    FileUtils.compressImage(imageUrl, DEF_WIDTH_CMPR,
                            DEF_WIDTH_CMPR, DEF_QLTY_COMPRESS),
                    fileNameToMove);
            if (photo != null) {
                imageUrls.add(photo.getAbsolutePath());
            }
        }
        dismissDialog();
        createProductAddFragment();
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void handleImageUrlImplicitSingle() {
        Uri imageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        ArrayList<Uri> imageUris = new ArrayList<>();
        imageUris.add(imageUri);
        processMultipleImage(imageUris);
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void handleImageUrlImplicitMultiple() {
        ArrayList<Uri> imageUris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (checkCollectionNotNull(imageUris)) {
            processMultipleImage(imageUris);
        } else {
            handleImageUrlImplicitSingle();
        }
    }

    @TargetApi(16)
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        createProductAddFragment();
    }

    @TargetApi(16)
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        createProductAddFragment();
    }

    @TargetApi(16)
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForExternalStorage(final PermissionRequest request) {
        request.proceed();
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
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
    public void onTextPickerSubmitted(String newEtalaseName) {

    }

    @Override
    public void addWholesaleItem(WholesaleModel item) {
        if (getProductAddFragment() != null && getProductAddFragment().isVisible()) {
            getProductAddFragment().addWholesaleItem(item);
        }
    }

    public ProductAddFragment getProductAddFragment() {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(ProductAddFragment.class.getSimpleName());
        if (fragmentByTag != null && fragmentByTag instanceof ProductAddFragment) {
            return (ProductAddFragment) fragmentByTag;
        }
        return null;
    }

    @Override
    public void startAddWholeSaleDialog(WholesaleModel baseValue) {
        AddWholeSaleDialog addWholeSaleDialog = AddWholeSaleDialog.newInstance(baseValue);
        addWholeSaleDialog.show(getSupportFragmentManager(), AddWholeSaleDialog.TAG);
    }

    public void startUploadProduct(long productId) {
        startService(AddProductService.getIntent(this, productId));
        finish();
    }

    public void startUploadProductWithShare(long productId) {
        startService(AddProductService.getIntent(this, productId));
        startActivity(ProductInfoActivity.createInstance(this));
        finish();
    }
}