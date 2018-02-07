package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogDomainModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogFragmentContract;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/18/17.
 */

public class RefreshCatalogSubscriber extends GetBrowseCatalogSubscriber {

    public RefreshCatalogSubscriber(CatalogFragmentContract.View view) {
        super(view);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof MessageErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof RuntimeHttpErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof IOException) {
            view.renderRetryRefresh();
        } else {
            view.renderUnknown();
            e.printStackTrace();
        }
        view.hideRefreshLayout();
    }

    @Override
    public void onNext(CatalogDomainModel domainModel) {
        view.successRefreshCatalog(mappingCatalogViewModel(domainModel));
        view.renderShareURL(domainModel.getShareURL());
        view.setHasNextPage(isHasNextPage(domainModel.getUriNext()));
        if (!isHasNextPage(domainModel.getUriNext())) {
            view.unSetTopAdsEndlessListener();
        }
    }
}
