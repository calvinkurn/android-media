package com.tokopedia.discovery.newdiscovery.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.discovery.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.helper.UrlParamHelper;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by hangnadi on 9/28/17.
 */

@SuppressWarnings("unchecked")
public class DiscoveryPresenter<T1 extends CustomerView, D2 extends View>
        extends BaseDiscoveryPresenter<T1, D2> {

    private GetProductUseCase getProductUseCase;
    private GetImageSearchUseCase getImageSearchUseCase;
    private GraphqlUseCase graphqlUseCase;
    private Context context;

    public DiscoveryPresenter(GetProductUseCase getProductUseCase) {
        this.getProductUseCase = getProductUseCase;
    }

    public DiscoveryPresenter(Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        this.getProductUseCase = getProductUseCase;
        this.getImageSearchUseCase = getImageSearchUseCase;
        this.graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    @Override
    public void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStore) {
        super.requestProduct(searchParameter, forceSearch, requestOfficialStore);

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("q", searchParameter.getQueryKey());
        paramsMap.put("start", "0");
        paramsMap.put("rows", "12");
        paramsMap.put("uniqueId", searchParameter.getUniqueID());
        paramsMap.put("source", searchParameter.getSource());
        paramsMap.put("userId", searchParameter.getUserID());

        Map<String, Object> variables = new HashMap<>();
        variables.put("query", searchParameter.getQueryKey());
        variables.put("params", UrlParamHelper.generateUrlParamString(paramsMap));
        variables.put("source", searchParameter.getSource());

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_search_product_first_page), SearchProductGqlResponse.class, variables);

        graphqlUseCase.setRequest(graphqlRequest);

        graphqlUseCase.execute(
                new DefaultGqlSearchSubscriber(searchParameter, forceSearch, getBaseDiscoveryView(), false)
        );
    }

    /*@Override
    public void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStore) {
        super.requestProduct(searchParameter, forceSearch, requestOfficialStore);
        getProductUseCase.execute(
                GetProductUseCase.createInitializeSearchParam(searchParameter, forceSearch, requestOfficialStore),
                new DefaultSearchSubscriber(searchParameter, forceSearch, getBaseDiscoveryView(), false)
        );
    }*/

    @Override
    public void requestImageSearch(String imagePath) {
        super.requestImageSearch(imagePath);
        getImageSearchUseCase.setImagePath(imagePath);
        getImageSearchUseCase.execute(
                RequestParams.EMPTY,
                new DefaultImageSearchSubscriber(getBaseDiscoveryView())
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductUseCase.unsubscribe();
    }

}
