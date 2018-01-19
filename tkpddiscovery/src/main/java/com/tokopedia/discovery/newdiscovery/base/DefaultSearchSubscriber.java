package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/5/17.
 */

@SuppressWarnings("WeakerAccess")
public class DefaultSearchSubscriber<D2 extends BaseDiscoveryContract.View>
        extends Subscriber<SearchResultModel> {

    private static final int DISCOVERY_URL_HOTLIST = 1;
    private static final int DISCOVERY_URL_CATEGORY = 2;
    private static final int DISCOVERY_URL_SEARCH = 3;
    private static final int DISCOVERY_URL_CATALOG = 4;
    private static final int DISCOVERY_URL_UNKNOWN = -1;

    private final SearchParameter searchParameter;

    private boolean forceSearch;
    private D2 discoveryView;

    public DefaultSearchSubscriber(SearchParameter searchParameter, boolean forceSearch, D2 discoveryView) {
        this.searchParameter = searchParameter;
        this.forceSearch = forceSearch;
        this.discoveryView = discoveryView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        discoveryView.onHandleResponseError();
        e.printStackTrace();
    }

    @Override
    public void onNext(SearchResultModel searchResult) {
        switch (defineResponse(searchResult)) {
            case DISCOVERY_URL_HOTLIST:
                discoveryView.onHandleResponseHotlist(searchResult.getRedirectUrl(), searchResult.getQuery());
                break;
            case DISCOVERY_URL_CATEGORY:
                discoveryView.onHandleResponseIntermediary(searchResult.getDepartmentId());
                break;
            case DISCOVERY_URL_SEARCH:
                ProductViewModel model = ProductViewModelHelper.convertToProductViewModelFirstPage(searchResult);
                model.setSearchParameter(searchParameter);
                model.setForceSearch(forceSearch);
                discoveryView.onHandleResponseSearch(model);
                break;
            case DISCOVERY_URL_CATALOG:
                discoveryView.onHandleResponseCatalog(searchResult.getRedirectUrl());
                break;
            default:
                discoveryView.onHandleResponseUnknown();
                break;
        }
    }

    private int defineResponse(SearchResultModel data) {
        if (data.getRedirectUrl() != null && !data.getRedirectUrl().isEmpty()) {
            return defineRedirectUrl(data.getRedirectUrl());
        } else {
            return DISCOVERY_URL_SEARCH;
        }
    }

    private int defineRedirectUrl(String redirectUrl) {
        if (redirectUrl.contains("/p/")) {
            return DISCOVERY_URL_CATEGORY;
        } else if (redirectUrl.contains("/hot/")) {
            return DISCOVERY_URL_HOTLIST;
        } else if (redirectUrl.contains("/catalog/")) {
            return DISCOVERY_URL_CATALOG;
        } else {
            return DISCOVERY_URL_UNKNOWN;
        }
    }

}
