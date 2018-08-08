package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogDomainModel;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogFragmentContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/13/17.
 */

public class GetBrowseCatalogLoadMoreSubscriber extends rx.Subscriber<CatalogDomainModel>  {

    protected final CatalogFragmentContract.View view;

    public GetBrowseCatalogLoadMoreSubscriber(CatalogFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof MessageErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof RuntimeHttpErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof IOException) {
            view.renderRetryInit();
        } else {
            view.renderUnknown();
            e.printStackTrace();
        }
        view.hideRefreshLayout();
    }

    @Override
    public void onNext(CatalogDomainModel domainModel) {
        view.renderNextListView(mappingCatalogViewModel(domainModel));
        view.setHasNextPage(isHasNextPage(domainModel.getUriNext()));
        if (!isHasNextPage(domainModel.getUriNext())) {
            view.unSetTopAdsEndlessListener();
        }
    }

    protected boolean isHasNextPage(String uriNext) {
        return uriNext != null && !uriNext.isEmpty();
    }

    protected List<Visitable> mappingCatalogViewModel(CatalogDomainModel domain) {
        List<Visitable> list = new ArrayList<>();
        for (CatalogItem item : domain.getCatalogList()) {
            CatalogViewModel model = new CatalogViewModel();
            model.setID(item.getCatalogID());
            model.setName(item.getCatalogName());
            model.setDesc(item.getCatalogDesc());
            model.setImage(item.getCatalogImage());
            model.setImage300(item.getCatalogImage300());
            model.setPrice(item.getCatalogPrice());
            model.setProductCounter(item.getCatalogProductCounter());
            model.setURL(item.getCatalogURL());
            list.add(model);
        }
        return list;
    }
}
