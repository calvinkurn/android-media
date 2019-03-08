package com.tokopedia.discovery.newdiscovery.base;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchHelper;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.graphql.domain.GraphqlUseCase;

/**
 * Created by hangnadi on 9/28/17.
 */

@SuppressWarnings("unchecked")
public class DiscoveryPresenter<T1 extends CustomerView, D2 extends View>
        extends BaseDiscoveryPresenter<T1, D2> {

    public static final String HEADLINE = "headline";
    public static final String TEMPLATE_VALUE = "3,4";
    public static final String ITEM_VALUE = "1";
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
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false, false);
        enrichWithForceSearchParam(requestParams, forceSearch);
        enrichWithRelatedSearchParam(requestParams, true);

        GqlSearchHelper.requestProductFirstPage(context, requestParams, graphqlUseCase,
                new DefaultGqlSearchSubscriber(searchParameter, forceSearch, getBaseDiscoveryView(), false)
        );
    }

    private void enrichWithForceSearchParam(RequestParams requestParams, boolean isForceSearch) {
        requestParams.putBoolean(SearchApiConst.REFINED, isForceSearch);
    }

    private void enrichWithRelatedSearchParam(RequestParams requestParams, boolean relatedSearchEnabled) {
        requestParams.putBoolean(SearchApiConst.RELATED, relatedSearchEnabled);
    }

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
