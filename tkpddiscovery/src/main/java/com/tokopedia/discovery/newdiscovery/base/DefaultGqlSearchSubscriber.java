package com.tokopedia.discovery.newdiscovery.base;

import android.text.TextUtils;

import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

@SuppressWarnings("WeakerAccess")
public class DefaultGqlSearchSubscriber<D2 extends BaseDiscoveryContract.View>
        extends Subscriber<GraphqlResponse> {

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

    public DefaultGqlSearchSubscriber(SearchParameter searchParameter, boolean forceSearch, D2 discoveryView, boolean imageSearch) {
        this.searchParameter = searchParameter;
        this.forceSearch = forceSearch;
        this.discoveryView = discoveryView;
        this.imageSearch = imageSearch;
    }

    public DefaultGqlSearchSubscriber(D2 discoveryView) {
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
    public void onNext(GraphqlResponse graphqlResponse) {

        SearchProductGqlResponse gqlResponse = graphqlResponse.getData(SearchProductGqlResponse.class);

        switch (defineResponse(gqlResponse.getSearchProduct())) {
            case DISCOVERY_URL_HOTLIST:
                onHandleHotlist(gqlResponse);
                break;
            case DISCOVERY_URL_CATEGORY:
                onHandleIntermediary(gqlResponse);
                break;
            case DISCOVERY_URL_SEARCH:
                onHandleSearch(gqlResponse);
                break;
            case DISCOVERY_URL_CATALOG:
                onHandleCatalog(gqlResponse);
                break;
            default:
                discoveryView.onHandleResponseUnknown();
                break;
        }
    }

    protected void onHandleHotlist(SearchProductGqlResponse gqlResponse) {
        discoveryView.onHandleResponseHotlist(gqlResponse.getSearchProduct().getRedirection().getRedirectUrl(), gqlResponse.getSearchProduct().getQuery());
    }

    protected void onHandleIntermediary(SearchProductGqlResponse gqlResponse) {
        discoveryView.onHandleResponseIntermediary(gqlResponse.getSearchProduct().getRedirection().getDepartmentId());
    }

    protected void onHandleCatalog(SearchProductGqlResponse gqlResponse) {
        discoveryView.onHandleResponseCatalog(gqlResponse.getSearchProduct().getRedirection().getRedirectUrl());
    }

    protected void onHandleSearch(SearchProductGqlResponse gqlResponse) {
        ProductViewModel model = ProductViewModelHelper.convertToProductViewModelFirstPageGql(gqlResponse);
        model.setSearchParameter(searchParameter);
        model.setForceSearch(forceSearch);
        model.setImageSearch(imageSearch);
        discoveryView.onHandleResponseSearch(model);
    }

    private int defineResponse(SearchProductGqlResponse.SearchProduct searchProduct) {
        if (!TextUtils.isEmpty(searchProduct.getRedirection().getRedirectUrl())) {
            return defineRedirectUrl(searchProduct.getRedirection().getRedirectUrl());
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
