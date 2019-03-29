package com.tokopedia.discovery.newdiscovery.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.helper.OfficialStoreQueryHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;
import com.tokopedia.discovery.util.AutoCompleteTracking;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;

/**
 * Created by hangnadi on 10/3/17.
 */
public class DiscoveryActivity extends BaseDiscoveryActivity implements
        DiscoverySearchView.SearchViewListener,
        DiscoverySearchView.ImageSearchClickListener,
        DiscoverySearchView.OnQueryTextListener,
        BottomNavigationListener {

    private static final int REQUEST_CODE_IMAGE = 2390;
    private static final double MIN_SCORE = 10.0;
    private static final String FAILURE = "no matching result found";
    private static final String NO_RESPONSE = "no response";
    private static final String SUCCESS = "success match found";
    private static final String SEARCH_RESULT_TRACE = "search_result_trace";
    private Toolbar toolbar;
    private FrameLayout container;
    private AHBottomNavigation bottomNavigation;
    protected DiscoverySearchView searchView;
    protected ProgressBar loadingView;

    public MenuItem searchItem;
    private boolean isLastRequestForceSearch;

    private TkpdProgressDialog tkpdProgressDialog;
    private boolean fromCamera;
    private String imagePath;
    private UserSessionInterface userSession;
    private PerformanceMonitoring performanceMonitoring;
    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        userSession = new UserSession(this);
        proceed();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        proceed();
    }

    private void proceed() {
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
        root = findViewById(R.id.root);
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

    protected void onSearchingStart(String keyword) {
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
        hideBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(false);
        CommonUtils.forceShowKeyboard(this);
    }

    @Override
    public void onSearchViewClosed() {
        showBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query, boolean isOfficial) {
        AutoCompleteTracking.eventClickSubmit(this, query);
        if (OfficialStoreQueryHelper.isOfficialStoreSearchQuery(query)) {
            onHandleOfficialStorePage();
            sendSearchProductGTM(query);
            return true;
        } else {
            switch (searchView.getSuggestionFragment().getCurrentTab()) {
                case SearchMainFragment.PAGER_POSITION_PRODUCT:
                    onProductQuerySubmit(query, isOfficial);
                    sendSearchProductGTM(query);
                    return false;
                case SearchMainFragment.PAGER_POSITION_SHOP:
                    onShopQuerySubmit(query, isOfficial);
                    sendSearchShopGTM(query);
                    return false;
                default:
                    throw new RuntimeException("Please handle this function if you have new tab of suggestion search view.");
            }
        }
    }

    protected void onProductQuerySubmit(String query, boolean isOfficial) {
        setForceSwipeToShop(false);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(query, isOfficial);
    }

    private void onShopQuerySubmit(String query, boolean isOfficial) {
        setForceSwipeToShop(true);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(query, isOfficial);
    }

    private void sendSearchProductGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearch(this, keyword);
        }
    }

    private void sendSearchShopGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearchShop(this, keyword);
        }
    }

    private void sendVoiceSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoveryVoiceSearch(this, keyword);
        }
    }

    private void sendCameraImageSearchProductGTM() {
        UnifyTracking.eventDiscoveryCameraImageSearch(this);
    }

    private void sendGalleryImageSearchProductGTM() {
        UnifyTracking.eventDiscoveryGalleryImageSearch(this);
    }

    private void sendGalleryImageSearchResultGTM(String label) {
        UnifyTracking.eventDiscoveryGalleryImageSearchResult(this, label);
    }

    private void sendCameraImageSearchResultGTM(String label) {
        UnifyTracking.eventDiscoveryCameraImageSearchResult(this, label);
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

    public void onSuggestionProductClick(String keyword, String categoryID, boolean isOfficial) {
        SearchParameter parameter = new SearchParameter();
        parameter.setQueryKey(keyword);
        parameter.setUniqueID(
                userSession.isLoggedIn() ?
                        AuthUtil.md5(userSession.getUserId()) :
                        AuthUtil.md5(gcmHandler.getRegistrationId())
        );
        parameter.setUserID(
                userSession.isLoggedIn() ?
                        userSession.getUserId() :
                        null
        );
        parameter.setDepartmentId(categoryID);
        parameter.setOfficial(isOfficial);
        onSearchingStart(keyword);
        setForceSearch(false);
        getPresenter().requestProduct(parameter, isForceSearch(), isRequestOfficialStoreBanner());
    }

    public void onSuggestionProductClick(String keyword, boolean isOfficial) {
        setForceSwipeToShop(false);
        setForceSearch(false);
        setRequestOfficialStoreBanner(true);
        performRequestProduct(keyword, isOfficial);
    }

    protected void performRequestProduct(String keyword) {
        performRequestProduct(keyword, false);
    }

    protected void performRequestProduct(String keyword, boolean isOfficial) {
        SearchParameter parameter = new SearchParameter();
        parameter.setQueryKey(keyword);
        parameter.setUniqueID(
                userSession.isLoggedIn() ?
                        AuthUtil.md5(userSession.getUserId()) :
                        AuthUtil.md5(gcmHandler.getRegistrationId())
        );
        parameter.setUserID(
                userSession.isLoggedIn() ?
                        userSession.getUserId() :
                        null
        );
        parameter.setOfficial(isOfficial);
        onSearchingStart(keyword);
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_RESULT_TRACE);
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
    public void enableAutoShowBottomNav() {
        if (bottomNavigation != null) {
            bottomNavigation.setBehaviorTranslationEnabled(true);
        }
    }

    @Override
    public void disableAutoShowBottomNav() {
        if (bottomNavigation != null) {
            bottomNavigation.setBehaviorTranslationEnabled(false);
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
                performRequestProduct(searchView.getLastQuery(), searchView.getIsOfficial());
            }
        });
    }

    @Override
    public void onImageSearchClicked() {


        ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList = new ArrayList<>();

        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.ORIGINAL);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_1_1);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_3_4);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_4_3);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_16_9);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_9_16);

        ImagePickerEditorBuilder imagePickerEditorBuilder = new ImagePickerEditorBuilder
                (new int[]{ACTION_CROP, ACTION_BRIGHTNESS, ACTION_CONTRAST},
                        false,
                        imageRatioTypeDefArrayList);

        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.IMAGE_SEARCH_MIN_RESOLUTION, null, true,
                imagePickerEditorBuilder, null);
        Intent intent = ImagePickerActivity.getIntent(this, builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imagePathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS);
            if (imagePathList == null || imagePathList.size() <= 0) {
                return;
            }
            String imagePath = imagePathList.get(0);
            if (!TextUtils.isEmpty(imagePath)) {
                onImagePickedSuccess(imagePath);
            } else {
                showSnackBarView(getString(com.tokopedia.core2.R.string.error_gallery_valid));
            }
            if (searchView != null) {
                searchView.clearFocus();
            }
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

    public void onImagePickedSuccess(String imagePath) {
        setImagePath(imagePath);
        tkpdProgressDialog = new TkpdProgressDialog(this, 1);
        tkpdProgressDialog.showDialog();
        getPresenter().requestImageSearch(imagePath);
    }

    @Override
    public void onHandleImageSearchResponseError() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }

        if (fromCamera) {
            sendCameraImageSearchResultGTM(FAILURE);
        } else {
            sendGalleryImageSearchResultGTM(FAILURE);
        }
        NetworkErrorHelper.showSnackbar(this, getResources().getString(R.string.no_result_found));
    }

    @Override
    public void showErrorNetwork(String message) {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }

        if (fromCamera) {
            sendCameraImageSearchResultGTM(NO_RESPONSE);
        } else {
            sendGalleryImageSearchResultGTM(NO_RESPONSE);
        }

        if (TextUtils.isEmpty(getImagePath())) {
            NetworkErrorHelper.showSnackbar(this, message);
        } else {
            NetworkErrorHelper.createSnackbarWithAction(this, message, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    onImagePickedSuccess(getImagePath());
                }
            }).showRetrySnackbar();
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
            NetworkErrorHelper.createSnackbarWithAction(this, message, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    onImagePickedSuccess(getImagePath());
                }
            }).showRetrySnackbar();
        }
    }

    @Override
    public void onHandleInvalidImageSearchResponse() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }

        if (fromCamera) {
            sendCameraImageSearchResultGTM(NO_RESPONSE);
        } else {
            sendGalleryImageSearchResultGTM(NO_RESPONSE);
        }

        NetworkErrorHelper.showSnackbar(this, getResources().getString(R.string.invalid_image_search_response));
    }

    @Override
    public void onHandleImageSearchResponseSuccess() {

        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
        if (fromCamera) {
            sendCameraImageSearchResultGTM(SUCCESS);
        } else {
            sendGalleryImageSearchResultGTM(SUCCESS);
        }
    }

    @Override
    public void showImageNotSupportedError() {
        super.showImageNotSupportedError();
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }

        NetworkErrorHelper.showSnackbar(this, getResources().getString(R.string.image_not_supported));
    }

    public void showSnackBarView(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(this);
        } else {
            NetworkErrorHelper.showSnackbar(this, message);
        }
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void onHandleResponseSearch(ProductViewModel productViewModel) {
        super.onHandleResponseSearch(productViewModel);
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
        }
    }
}
