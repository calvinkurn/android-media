package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

/**
 * Created by hangnadi on 10/3/17.
 */

@SuppressWarnings("WeakerAccess")
public class BaseDiscoveryPresenter<T extends CustomerView, D extends BaseDiscoveryContract.View>
        extends BaseDaggerPresenter<T>
        implements BaseDiscoveryContract.Presenter<D> {

    private D discoveryView;

    public BaseDiscoveryPresenter() {

    }

    @Override
    public void setDiscoveryView(D discoveryView) {
        this.discoveryView = discoveryView;
    }

    @Override
    public void detachView() {
        this.discoveryView = null;
        super.detachView();
    }

    public D getBaseDiscoveryView() {
        return discoveryView;
    }

    @Override
    public void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStoreBanner) {
        checkDiscoveryViewAttached();
    }

    @Override
    public void requestImageSearch(String imageByteArray) {
        checkDiscoveryViewAttached();
    }

    private void checkDiscoveryViewAttached() {
        if (!isDiscoveryViewAttached()) {
            throw new BaseDiscoveryPresenter.DiscoveryViewNotAttachedException();
        }
    }

    private static class DiscoveryViewNotAttachedException extends RuntimeException {
        DiscoveryViewNotAttachedException() {
            super("Please call Presenter.setDiscoveryView(Activity) before " +
                    "requesting data to the presenter");

        }
    }

    private boolean isDiscoveryViewAttached() {
        return discoveryView != null;
    }

}
