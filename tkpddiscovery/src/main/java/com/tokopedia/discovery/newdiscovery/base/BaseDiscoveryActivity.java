package com.tokopedia.discovery.newdiscovery.base;

import android.os.Bundle;

import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.discovery.newdiscovery.search.SearchActivity;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;

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
        startActivity(HotlistActivity.createInstanceUsingURL(this, url, query));
        finish();
    }

    @Override
    public void onHandleResponseSearch(ProductViewModel productViewModel) {
        SearchActivity.moveTo(this, productViewModel, isForceSwipeToShop());
        finish();
    }

    @Override
    public void onHandleResponseIntermediary(String departmentId) {
        IntermediaryActivity.moveTo(this, departmentId);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onHandleResponseCatalog(String url) {
        URLParser urlParser = new URLParser(url);
        startActivity(DetailProductRouter.getCatalogDetailActivity(this, urlParser.getHotAlias()));
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

}
