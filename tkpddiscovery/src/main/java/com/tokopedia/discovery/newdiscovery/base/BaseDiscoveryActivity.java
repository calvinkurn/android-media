package com.tokopedia.discovery.newdiscovery.base;

import android.content.Intent;
import android.os.Bundle;

import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.discovery.imagesearch.search.ImageSearchActivity;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.discovery.newdiscovery.search.SearchActivity;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by hangnadi on 9/26/17.
 */

public class BaseDiscoveryActivity
        extends BaseActivity
        implements BaseDiscoveryContract.View, HasComponent {

    private static final String KEY_FORCE_SWIPE_TO_SHOP = "KEY_FORCE_SWIPE_TO_SHOP";
    private static final String KEY_TAB_POSITION = "KEY_TAB_POSITION";
    private static final String KEY_FORCE_SEARCH = "KEY_FORCE_SEARCH";
    private static final String KEY_REQUEST_OS = "KEY_REQUEST_OS";

    private BaseDiscoveryContract.Presenter presenter;
    private boolean forceSwipeToShop;
    private boolean forceSearch;
    private boolean requestOfficialStoreBanner;
    private int activeTabPosition;

    private Boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setActiveTabPosition(savedInstanceState.getInt(KEY_TAB_POSITION, 0));
            setForceSwipeToShop(savedInstanceState.getBoolean(KEY_FORCE_SWIPE_TO_SHOP, false));
            setForceSearch(savedInstanceState.getBoolean(KEY_FORCE_SEARCH, false));
            setRequestOfficialStoreBanner(savedInstanceState.getBoolean(KEY_REQUEST_OS, false));
        }
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
    public boolean isForceSearch() {
        return forceSearch;
    }

    @Override
    public void setForceSearch(boolean forceSearch) {
        this.forceSearch = forceSearch;
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

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void onHandleResponseHotlist(String url, String query) {
        startActivity(HotlistActivity.createInstanceUsingURL(this, url, query, isPausing()));
        finish();
    }

    @Override
    public void onHandleResponseSearch(ProductViewModel productViewModel) {
        TrackingUtils.sendMoEngageSearchAttempt(this, productViewModel.getQuery(), !productViewModel.getProductList().isEmpty());
        JSONArray afProdIds = new JSONArray();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                } else {
                    break;
                }
            }
        }
        TrackingUtils.eventAppsFlyerViewListingSearch(this,afProdIds,productViewModel.getQuery(),prodIdArray);
        finish();
        SearchActivity.moveTo(this, productViewModel, isForceSwipeToShop(), isPausing());
    }

    @Override
    public void onHandleImageResponseSearch(ProductViewModel productViewModel) {
        TrackingUtils.sendMoEngageSearchAttempt(this,productViewModel.getQuery(), !productViewModel.getProductList().isEmpty());
        JSONArray afProdIds = new JSONArray();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                } else {
                    break;
                }
            }
        }
        TrackingUtils.eventAppsFlyerViewListingSearch(this, afProdIds,productViewModel.getQuery(),prodIdArray);
        ImageSearchActivity.moveTo(this, productViewModel);
        finish();
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
    public void onHandleOfficialStorePage() {
        startActivity(BrandsWebViewActivity.newInstance(this, TkpdBaseURL.OfficialStore.URL_WEBVIEW));
        finish();
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
        outState.putBoolean(KEY_FORCE_SEARCH, isForceSearch());
        outState.putBoolean(KEY_FORCE_SWIPE_TO_SHOP, isForceSwipeToShop());
        outState.putBoolean(KEY_REQUEST_OS, isRequestOfficialStoreBanner());
        outState.putInt(KEY_TAB_POSITION, getActiveTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setForceSearch(savedInstanceState.getBoolean(KEY_FORCE_SEARCH));
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
    }

    public Boolean isPausing() {
        return isPause;
    }
}
