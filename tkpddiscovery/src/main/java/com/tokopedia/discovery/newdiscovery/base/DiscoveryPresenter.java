package com.tokopedia.discovery.newdiscovery.base;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
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
    public void initiateSearch(SearchParameter searchParameter, InitiateSearchListener initiateSearchListener) {
        super.initiateSearch(searchParameter, initiateSearchListener);

        com.tokopedia.usecase.RequestParams requestParams = createInitiateSearchRequestParams(searchParameter);

        GqlSearchHelper.initiateSearch(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_initiate_search),
                requestParams,
                graphqlUseCase,
                new InitiateSearchSubscriber(initiateSearchListener)
        );
    }

    private com.tokopedia.usecase.RequestParams createInitiateSearchRequestParams(SearchParameter searchParameter) {
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();

        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putBoolean(SearchApiConst.RELATED, true);

        requestParams.putAll(searchParameter.getSearchParameterMap());

        return requestParams;
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
