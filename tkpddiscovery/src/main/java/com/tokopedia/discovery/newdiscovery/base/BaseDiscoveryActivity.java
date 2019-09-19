package com.tokopedia.discovery.newdiscovery.base;

import android.os.Bundle;

import com.tkpd.library.utils.URLParser;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;

/**
 * Created by hangnadi on 9/26/17.
 */

public class BaseDiscoveryActivity
        extends BaseActivity
        implements BaseDiscoveryContract.View, HasComponent {

    private static final String KEY_TAB_POSITION = "KEY_TAB_POSITION";

    private BaseDiscoveryContract.Presenter presenter;
    private int activeTabPosition;

    private Boolean isPause = false;

    protected GCMHandler gcmHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gcmHandler = new GCMHandler(this);

        if (savedInstanceState != null) {
            setActiveTabPosition(savedInstanceState.getInt(KEY_TAB_POSITION, 0));
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

    public AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    @Override
    public void onHandleResponseHotlist(String url, String query) {
        startActivity(HotlistActivity.createInstanceUsingURL(this, url, query, isPausing()));
        finish();
    }

    @Override
    public void onHandleImageResponseSearch(ProductViewModel productViewModel) {
    }

    @Override
    public void onHandleImageSearchResponseError() { }

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
    public void showImageNotSupportedError() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TAB_POSITION, getActiveTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
