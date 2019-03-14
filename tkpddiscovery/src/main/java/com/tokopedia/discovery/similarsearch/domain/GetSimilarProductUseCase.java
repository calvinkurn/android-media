package com.tokopedia.discovery.similarsearch.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.similarsearch.model.ProductsItem;
import com.tokopedia.discovery.similarsearch.model.SearchProductList;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetSimilarProductUseCase extends UseCase<List<ProductsItem>> {

    Context context;

    @Inject
    public GetSimilarProductUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<ProductsItem>> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.similar_product_search), SearchProductList.class, requestParams.getParameters(), false);

        return ObservableFactory.create(Arrays.asList(graphqlRequest), null).map(graphqlResponse -> {
            SearchProductList searchProductList = graphqlResponse.getData(SearchProductList.class);
            return searchProductList.getSimilarProductsImageSearch().getData().getProducts();
        });
    }

}
