package com.tokopedia.discovery.newdiscovery.base;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.manage.people.profile.customdialog.UploadImageDialog;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.helper.OfficialStoreQueryHelper;
import com.tokopedia.discovery.imagesearch.domain.usecase.NewImageSearchResponse;
import com.tokopedia.discovery.newdiscovery.search.SearchActivity;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by hangnadi on 10/3/17.
 */

@RuntimePermissions
public class DiscoveryActivity extends BaseDiscoveryActivity implements
        DiscoverySearchView.SearchViewListener,
        DiscoverySearchView.ImageSearchClickListener,
        DiscoverySearchView.OnQueryTextListener,
        BottomNavigationListener {

    private static final double MIN_SCORE = 10.0;
    private Toolbar toolbar;
    private FrameLayout container;
    private AHBottomNavigation bottomNavigation;
    protected DiscoverySearchView searchView;
    protected ProgressBar loadingView;

    private MenuItem searchItem;
    private boolean isLastRequestForceSearch;

    private boolean fromCamera;
    private UploadImageDialog uploadDialog;
    private TkpdProgressDialog tkpdProgressDialog;
    private final int MAX_WIDTH = 400;
    private final int MAX_HEIGHT = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initView();
        prepareView();
    }

    protected int getLayoutRes() {
        return R.layout.activity_base_discovery;
    }

    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (FrameLayout) findViewById(R.id.container);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        searchView = (DiscoverySearchView) findViewById(R.id.search);
        loadingView = findViewById(R.id.progressBar);
    }

    protected void prepareView() {
        initToolbar();
        initSearchView();
        showLoadingView(false);
    }

    protected void showLoadingView(boolean visible) {
        loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void showContainer(boolean visible) {
        container.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public AHBottomNavigation getBottomNavigation() {
        return bottomNavigation;
    }

    private void onSearchingStart(String keyword) {
        searchView.closeSearch();
        showLoadingView(true);
        showContainer(false);
        setToolbarTitle(keyword);
        setLastQuerySearchView(keyword);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.showSearch(false, true);
            }
        });
    }

    protected void setToolbarTitle(String query) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(query);
        }
    }

    private void initSearchView() {
        searchView.setActivity(this);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchViewListener(this);
        searchView.setOnImageSearchClickListener(this);
    }

    protected void setLastQuerySearchView(String lastQuerySearchView) {
        searchView.setLastQuery(lastQuerySearchView);
    }

    @Override
    public void onSearchViewShown() {
        bottomNavigation.hideBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(false);
        CommonUtils.forceShowKeyboard(this);
    }

    @Override
    public void onSearchViewClosed() {
        bottomNavigation.restoreBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (OfficialStoreQueryHelper.isOfficialStoreSearchQuery(query)) {
            onHandleOfficialStorePage();
            sendSearchProductGTM(query);
            return true;
        } else {
            switch (searchView.getSuggestionFragment().getCurrentTab()) {
                case SearchMainFragment.PAGER_POSITION_PRODUCT:
                    onProductQuerySubmit(query);
                    sendSearchProductGTM(query);
                    return false;
                case SearchMainFragment.PAGER_POSITION_SHOP:
                    onShopQuerySubmit(query);
                    sendSearchShopGTM(query);
                    return false;
                default:
                    throw new RuntimeException("Please handle this function if you have new tab of suggestion search view.");
            }
        }
    }

    protected void onProductQuerySubmit(String query) {
        setForceSwipeToShop(false);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(query);
    }

    private void onShopQuerySubmit(String query) {
        setForceSwipeToShop(true);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(query);
    }

    private void sendSearchProductGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearch(keyword);
        }
    }

    private void sendSearchShopGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearchShop(keyword);
        }
    }

    private void sendVoiceSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoveryVoiceSearch(keyword);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            KeyboardHandler.DropKeyboard(this, findViewById(android.R.id.content));
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            if (searchView.isFinishOnClose()) {
                finish();
            } else {
                searchView.closeSearch();
            }
        } else {
            finish();
        }
    }

    public void onSuggestionProductClick(String keyword, String categoryID) {
        SearchParameter parameter = new SearchParameter();
        parameter.setQueryKey(keyword);
        parameter.setUniqueID(
                sessionHandler.isV4Login() ?
                        AuthUtil.md5(sessionHandler.getLoginID()) :
                        AuthUtil.md5(gcmHandler.getRegistrationId())
        );
        parameter.setUserID(
                sessionHandler.isV4Login() ?
                        sessionHandler.getLoginID() :
                        null
        );
        parameter.setDepartmentId(categoryID);
        onSearchingStart(keyword);
        setForceSearch(false);
        getPresenter().requestProduct(parameter, isForceSearch(), isRequestOfficialStoreBanner());
    }

    public void onSuggestionProductClick(String keyword) {
        setForceSwipeToShop(false);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(keyword);
    }

    protected void performRequestProduct(String keyword) {
        SearchParameter parameter = new SearchParameter();
        parameter.setQueryKey(keyword);
        parameter.setUniqueID(
                sessionHandler.isV4Login() ?
                        AuthUtil.md5(sessionHandler.getLoginID()) :
                        AuthUtil.md5(gcmHandler.getRegistrationId())
        );
        parameter.setUserID(
                sessionHandler.isV4Login() ?
                        sessionHandler.getLoginID() :
                        null
        );
        onSearchingStart(keyword);
        getPresenter().requestProduct(parameter, isForceSearch(), isRequestOfficialStoreBanner());
    }

    public void deleteAllRecentSearch() {
        searchView.getSuggestionFragment().deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword) {
        searchView.getSuggestionFragment().deleteRecentSearch(keyword);
    }

    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(this, findViewById(android.R.id.content));
    }

    public void setSearchQuery(String keyword) {
        searchView.setQuery(keyword, false, true);
    }

    public void adjustViewContainer(int y) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) container.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, y);
        container.setLayoutParams(layoutParams);
    }

    @Override
    public void setupBottomNavigation(List<AHBottomNavigationItem> items,
                                      AHBottomNavigation.OnTabSelectedListener tabSelectedListener) {

        bottomNavigation.setBackgroundResource(R.drawable.bottomtab_background);
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.tkpd_dark_green));
        bottomNavigation.setOnTabSelectedListener(tabSelectedListener);
        bottomNavigation.setUseElevation(true, getResources().getDimension(R.dimen.bottom_navigation_elevation));
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                adjustViewContainer(y);
            }
        });
    }

    @Override
    public void showBottomNavigation() {
        bottomNavigation.restoreBottomNavigation(true);
    }

    @Override
    public void hideBottomNavigation() {
        if (bottomNavigation != null) {
            bottomNavigation.hideBottomNavigation();
        }
    }

    @Override
    public void refreshBottomNavigationIcon(List<AHBottomNavigationItem> items) {
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
    }

    @Override
    public void onHandleResponseError() {

        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
        showLoadingView(false);
        showContainer(true);
        NetworkErrorHelper.showEmptyState(this, container, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                performRequestProduct(searchView.getLastQuery());
            }
        });
        hideBottomNavigation();
    }

    @Override
    public void onImageSearchClicked() {
        uploadDialog = new UploadImageDialog(DiscoveryActivity.this);
        showUploadDialog();
    }

    public void showUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(com.tokopedia.core.R.string.dialog_upload_option));
        builder.setPositiveButton(getString(com.tokopedia.core.R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DiscoveryActivityPermissionsDispatcher.actionImagePickerWithCheck(DiscoveryActivity.this);

            }
        }).setNegativeButton(getString(com.tokopedia.core.R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DiscoveryActivityPermissionsDispatcher.actionCameraWithCheck(DiscoveryActivity.this);
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UploadImageDialog.REQUEST_CAMERA || requestCode == UploadImageDialog.REQUEST_GALLERY) {

            fromCamera = requestCode == UploadImageDialog.REQUEST_CAMERA;

            uploadDialog.onResult(
                    requestCode,
                    resultCode,
                    data,
                    new UploadImageDialog.UploadImageDialogListener() {
                        @Override
                        public void onSuccess(String imagePath) {
                            onImagePickedSuccess(imagePath);
                        }

                        @Override
                        public void onFailed() {
                            showSnackBarView(getString(com.tokopedia.core.R.string.error_gallery_valid));
                        }
                    });

        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DiscoverySearchView.REQUEST_VOICE:
                    List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && results.size() > 0) {
                        searchView.setQuery(results.get(0), false);
                        sendVoiceSearchGTM(results.get(0));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void onImagePickedSuccess(String imagePath) {

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            myBitmap = resize(myBitmap);

            if (fromCamera) {
                addImageToGallery(myBitmap);
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

//            SearchImageAsyncTask searchImageAsyncTask = new SearchImageAsyncTask(this);
//            searchImageAsyncTask.execute(byteArray);

//            showLoadingView(true);
//
            tkpdProgressDialog = new TkpdProgressDialog(this, 1);
            tkpdProgressDialog.showDialog();
            getPresenter().requestImageSearch(byteArray);
        }
    }

    List<String> productIDList = new ArrayList<>();

    @Override
    public void onHandleImageSearchResponse(NewImageSearchResponse imageSearchResponse) {
        if (imageSearchResponse == null || imageSearchResponse.getAuctionsArrayList() == null) {
            if (tkpdProgressDialog != null) {
                tkpdProgressDialog.dismiss();
            }

            // TODO: 3/13/18 show appropriate error
            Toast.makeText(this, "Invalid Response", Toast.LENGTH_SHORT).show();
            return;
        }

        int productCount = imageSearchResponse.getAuctionsArrayList().size();
        StringBuilder productIDs = new StringBuilder();

        productIDList.clear();

        for (int i = 0; i < productCount; i++) {
            String itemId = imageSearchResponse.getAuctionsArrayList().get(i).getItemId();
            productIDList.add(itemId);
            productIDs.append(itemId);
            if (i != productCount - 1) {
                productIDs.append(",");
            }
        }

        if (StringUtils.isNotBlank(productIDs.toString())) {

            SearchParameter imageSearchProductParameter = new SearchParameter();
            imageSearchProductParameter.setStartRow(productCount);
            imageSearchProductParameter.setQueryKey(String.valueOf(productIDs));

            // TODO: 3/13/18 need to confirm the value of source
            imageSearchProductParameter.setSource("toppicks");
            getPresenter().requestImageSearchProduct(imageSearchProductParameter);

        } else {
            if (tkpdProgressDialog != null) {
                tkpdProgressDialog.dismiss();
            }
            Toast.makeText(this, "No Results Found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onHandleResponseSearch(ProductViewModel productViewModel) {

        if (tkpdProgressDialog != null)
            tkpdProgressDialog.dismiss();
        HashMap<String, ProductItem> productItemHashMap = new HashMap<>();
        for (ProductItem productItem : productViewModel.getProductList()) {
            productItemHashMap.put(productItem.getProductID(), productItem);
        }
        List<ProductItem> productItemList = new ArrayList<>();
        for (String productId : productIDList) {
            if (productItemHashMap.get(productId) != null)
                productItemList.add(productItemHashMap.get(productId));
        }
        productViewModel.setProductList(productItemList);
        SearchActivity.moveTo(this, productViewModel, isForceSwipeToShop());
        finish();
    }

    private Bitmap resize(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) MAX_WIDTH / (float) MAX_HEIGHT;

        int finalWidth = MAX_WIDTH;
        int finalHeight = MAX_HEIGHT;
        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float) MAX_HEIGHT * ratioBitmap);
        } else {
            finalHeight = (int) ((float) MAX_WIDTH / ratioBitmap);
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;
    }

    public void addImageToGallery(final Bitmap filePath) {

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n;
        MediaStore.Images.Media.insertImage(getContentResolver(), filePath, fname, "");


        /*ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
    }

    private void saveImage(Bitmap bitmap) {

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";

        File file = new File(dir, fname);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImageToGallery(Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSnackBarView(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(this);
        } else {
            NetworkErrorHelper.showSnackbar(this, message);
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        uploadDialog.openImagePicker();

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        uploadDialog.openCamera();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DiscoveryActivityPermissionsDispatcher.onRequestPermissionsResult(
                DiscoveryActivity.this, requestCode, grantResults);

    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(this, request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(this, listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(this, listPermission);
    }
}
