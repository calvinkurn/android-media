package com.tokopedia.discovery.newdiscovery.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.imagesearch.search.ImageSearchActivity;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.constant.SearchEventTracking;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.track.TrackApp;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.discovery.common.constants.SearchConstant.AUTO_COMPLETE_ACTIVITY_RESULT_CODE_START_ACTIVITY;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_FORCE_SWIPE_TO_SHOP;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_HAS_CATALOG;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL;

/**
 * Created by hangnadi on 9/26/17.
 */

public class BaseDiscoveryActivity
        extends BaseActivity
        implements BaseDiscoveryContract.View, HasComponent {

    private static final String KEY_FORCE_SWIPE_TO_SHOP = "KEY_FORCE_SWIPE_TO_SHOP";
    private static final String KEY_TAB_POSITION = "KEY_TAB_POSITION";
    private static final String KEY_REQUEST_OS = "KEY_REQUEST_OS";

    private BaseDiscoveryContract.Presenter presenter;
    private boolean forceSwipeToShop;
    private boolean requestOfficialStoreBanner;
    private int activeTabPosition;

    private Boolean isPause = false;
    private boolean isStartingSearchActivityWithProductViewModel = false;
    private ProductViewModel productViewModelForOnResume;

    protected GCMHandler gcmHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gcmHandler = new GCMHandler(this);

        if (savedInstanceState != null) {
            setActiveTabPosition(savedInstanceState.getInt(KEY_TAB_POSITION, 0));
            setForceSwipeToShop(savedInstanceState.getBoolean(KEY_FORCE_SWIPE_TO_SHOP, false));
            setRequestOfficialStoreBanner(savedInstanceState.getBoolean(KEY_REQUEST_OS, false));
        }
    }

    @Override
    protected void onDestroy() {
        gcmHandler = null;

        super.onDestroy();
    }

    public int getActiveTabPosition() {
        return activeTabPosition;
    }

    public void setActiveTabPosition(int activeTabPosition) {
        this.activeTabPosition = activeTabPosition;
    }

    public boolean isForceSwipeToShop() {
        return forceSwipeToShop;
    }

    public void setForceSwipeToShop(boolean forceSwipeToShop) {
        this.forceSwipeToShop = forceSwipeToShop;
    }

    @Override
    public boolean isRequestOfficialStoreBanner() {
        return requestOfficialStoreBanner;
    }

    @Override
    public void setRequestOfficialStoreBanner(boolean requestOfficialStoreBanner) {
        this.requestOfficialStoreBanner = requestOfficialStoreBanner;
    }

    public void setPresenter(BaseDiscoveryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public BaseDiscoveryContract.Presenter getPresenter() {
        return presenter;
    }

    protected void onProductQuerySubmit() {

    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    @Override
    public void onHandleResponseHotlist(String url, String query) {
        startActivity(HotlistActivity.createInstanceUsingURL(this, url, query, isPausing()));
        finish();
    }

    @Override
    public void onHandleApplink(String applink) {
        if (getApplicationContext() instanceof DiscoveryRouter
                && ((DiscoveryRouter) getApplicationContext()).isSupportApplink(applink)) {
            openApplink(applink);
        } else {
            openWebViewURL(applink, this);
        }
        finish();
    }

    public void openApplink(String applink) {
        if (!TextUtils.isEmpty(applink)) {
            ((DiscoveryRouter) getApplicationContext())
                    .goToApplinkActivity(this, applink);
        }
    }

    public void openWebViewURL(String url, Context context) {
        if (!TextUtils.isEmpty(url) && context != null) {
            ((DiscoveryRouter) getApplication())
                    .actionOpenGeneralWebView(
                            this,
                            url);
        }
    }

    @Override
    public void onHandleResponseSearch(ProductViewModel productViewModel) {
        handleMoveToSearchActivity(productViewModel);
    }

    protected void handleMoveToSearchActivity(ProductViewModel productViewModel) {
        if (!isPausing()) {
            finishAndMoveToSearchActivity(productViewModel);
        }
        else {
            prepareMoveToSearchActivityDuringOnResume(productViewModel);
        }
    }

    private void finishAndMoveToSearchActivity(ProductViewModel productViewModel) {
        isStartingSearchActivityWithProductViewModel = false;

        moveToSearchActivity(productViewModel);
        finish();
    }

    private void moveToSearchActivity(ProductViewModel productViewModel) {
        if(getApplication() instanceof DiscoveryRouter) {
            DiscoveryRouter router = (DiscoveryRouter)getApplication();

            if(router != null) {
                Intent searchActivityIntent = getSearchActivityIntent(router, productViewModel);
                if(isActivityCalledForResult()) {
                    setResult(AUTO_COMPLETE_ACTIVITY_RESULT_CODE_START_ACTIVITY, searchActivityIntent);
                }
                else {
                    startActivity(searchActivityIntent);
                }
            }
        }
    }

    private Intent getSearchActivityIntent(@NonNull DiscoveryRouter router, ProductViewModel productViewModel) {
        Intent searchActivityIntent = router.gotoSearchPage(this);
        searchActivityIntent.putExtra(EXTRA_SEARCH_PARAMETER_MODEL, productViewModel.getSearchParameter());
        searchActivityIntent.putExtra(EXTRA_HAS_CATALOG, productViewModel.isHasCatalog());
        searchActivityIntent.putExtra(EXTRA_FORCE_SWIPE_TO_SHOP, forceSwipeToShop);

        return searchActivityIntent;
    }

    private boolean isActivityCalledForResult() {
        return getCallingActivity() != null;
    }

    private void prepareMoveToSearchActivityDuringOnResume(ProductViewModel productViewModel) {
        isStartingSearchActivityWithProductViewModel = true;
        productViewModelForOnResume = productViewModel;
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
        sendMoEngageSearchAttempt(this, productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);
        ImageSearchActivity.moveTo(this, productViewModel);
        finish();
    }

    public void sendMoEngageSearchAttempt(Context context, String keyword, boolean isResultFound, HashMap<String, String> category) {
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

    @Override
    public void onHandleImageSearchResponseError() {
    }

    @Override
    public void onHandleResponseIntermediary(String departmentId) {
        IntermediaryActivity.moveTo(this, departmentId, isPausing());
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onHandleResponseCatalog(String url) {
        URLParser urlParser = new URLParser(url);
        startActivity(DetailProductRouter.getCatalogDetailActivity(this, urlParser.getHotAlias(), isPausing()));
        finish();
    }

    @Override
    public void onHandleResponseUnknown() {
        throw new RuntimeException("not yet handle unknown response");
    }

    @Override
    public void onHandleResponseError() {

    }

    @Override
    public void onHandleInvalidImageSearchResponse() {

    }

    @Override
    public void showErrorNetwork(String message) {

    }

    @Override
    public void showTimeoutErrorNetwork(String message) {

    }

    @Override
    public void onHandleImageSearchResponseSuccess() {

    }

    @Override
    public void showImageNotSupportedError() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_FORCE_SWIPE_TO_SHOP, isForceSwipeToShop());
        outState.putBoolean(KEY_REQUEST_OS, isRequestOfficialStoreBanner());
        outState.putInt(KEY_TAB_POSITION, getActiveTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setForceSwipeToShop(savedInstanceState.getBoolean(KEY_FORCE_SWIPE_TO_SHOP));
        setRequestOfficialStoreBanner(savedInstanceState.getBoolean(KEY_REQUEST_OS));
        setActiveTabPosition(savedInstanceState.getInt(KEY_TAB_POSITION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;

        handleMoveToSearchActivityOnResume();
    }

    private void handleMoveToSearchActivityOnResume() {
        if(isStartingSearchActivityWithProductViewModel) {
            if(productViewModelForOnResume != null) {
                finishAndMoveToSearchActivity(productViewModelForOnResume);
            }
            else {
                onProductQuerySubmit();
            }
        }
    }

    public Boolean isPausing() {
        return isPause;
    }
}
