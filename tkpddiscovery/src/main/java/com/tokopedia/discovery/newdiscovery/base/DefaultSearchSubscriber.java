package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

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
    private static final int DISCOVERY_URL_IMAGE_SEARCH = 4;
    private static final int DISCOVERY_URL_UNKNOWN = -1;

    private final SearchParameter searchParameter;

    public boolean forceSearch;
    public D2 discoveryView;
    public boolean imageSearch;

    public DefaultSearchSubscriber(SearchParameter searchParameter, boolean forceSearch, D2 discoveryView, boolean imageSearch) {
        this.searchParameter = searchParameter;
        this.forceSearch = forceSearch;
        this.discoveryView = discoveryView;
        this.imageSearch = imageSearch;
    }

    public DefaultSearchSubscriber(D2 discoveryView) {
        this.searchParameter = null;
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
                onHandleHotlist(searchResult);
                break;
            case DISCOVERY_URL_CATEGORY:
                onHandleIntermediary(searchResult);
                break;
            case DISCOVERY_URL_SEARCH:
                onHandleSearch(searchResult);
                break;
            case DISCOVERY_URL_CATALOG:
                onHandleCatalog(searchResult);
                break;
            default:
                discoveryView.onHandleResponseUnknown();
                break;
        }
    }

    protected void onHandleHotlist(SearchResultModel searchResult) {
        discoveryView.onHandleResponseHotlist(searchResult.getRedirectUrl(), searchResult.getQuery());
    }

    protected void onHandleIntermediary(SearchResultModel searchResult) {
        discoveryView.onHandleResponseIntermediary(searchResult.getDepartmentId());
    }

    protected void onHandleCatalog(SearchResultModel searchResult) {
        discoveryView.onHandleResponseCatalog(searchResult.getRedirectUrl());
    }

    protected void onHandleSearch(SearchResultModel searchResult) {
        ProductViewModel model = ProductViewModelHelper.convertToProductViewModelFirstPage(searchResult);
        model.setSearchParameter(searchParameter);
        model.setForceSearch(forceSearch);
        model.setImageSearch(imageSearch);
        discoveryView.onHandleResponseSearch(model);
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
