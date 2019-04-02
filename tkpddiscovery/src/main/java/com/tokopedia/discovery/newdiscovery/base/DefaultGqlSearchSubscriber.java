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

    private static final int DISCOVERY_URL_SEARCH = 1;
    private static final int DISCOVERY_APPLINK = 2;

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

        switch (defineRedirectApplink(gqlResponse.getSearchProduct().getRedirection().getRedirectApplink())) {
            case DISCOVERY_URL_SEARCH:
                onHandleSearch(gqlResponse);
                break;
            case DISCOVERY_APPLINK:
                onHandleApplink(gqlResponse.getSearchProduct().getRedirection().getRedirectApplink());
                break;
            default:
                discoveryView.onHandleResponseUnknown();
                break;
        }
    }

    private void onHandleApplink(String applink){
        discoveryView.onHandleApplink(applink);
    }

    private void onHandleSearch(SearchProductGqlResponse gqlResponse) {
        ProductViewModel model = ProductViewModelHelper.convertToProductViewModelFirstPageGql(gqlResponse);
        model.setSearchParameter(searchParameter);
        model.setForceSearch(forceSearch);
        model.setImageSearch(imageSearch);
        discoveryView.onHandleResponseSearch(model);
    }

    private int defineRedirectApplink(String applink){
        if (TextUtils.isEmpty(applink)){
            return DISCOVERY_URL_SEARCH;
        } else {
            return DISCOVERY_APPLINK;
        }
    }
}
