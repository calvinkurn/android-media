package com.tokopedia.discovery.similarsearch.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.similarsearch.model.ProductsItem;
import com.tokopedia.discovery.similarsearch.model.SearchProductList;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class GetSimilarProductUseCase extends UseCase<List<ProductsItem>> {

    Context context;
    private final MojitoApi service;

    @Inject
    public GetSimilarProductUseCase(@ApplicationContext Context context, MojitoApi service) {
        this.context = context;
        this.service = service;
    }

    @Override
    public Observable<List<ProductsItem>> createObservable(RequestParams requestParams) {

        GraphqlClient.init(context);
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.similar_product_search), SearchProductList.class, requestParams.getParameters(), false);

        return ObservableFactory.create(Arrays.asList(graphqlRequest), null).map(new Func1<GraphqlResponse, List<ProductsItem>>() {
            @Override
            public List<ProductsItem> call(GraphqlResponse graphqlResponse) {
                SearchProductList searchProductList = graphqlResponse.getData(SearchProductList.class);
                List<ProductsItem> productsItems = searchProductList.getSimilarProductsImageSearch().getData().getProducts();
                return productsItems;
            }
        }).flatMap(new Func1<List<ProductsItem>, Observable<List<ProductsItem>>>() {
            @Override
            public Observable<List<ProductsItem>> call(List<ProductsItem> productsItems) {
                if(productsItems == null || productsItems.size() <= 0) {
                    return Observable.just(null);
                }
                List<String> productIdList = generateProductIdList(productsItems);
                return Observable.zip(Observable.just(productsItems), getWishList(requestParams.getString("userId", ""), productIdList), new Func2<List<ProductsItem>, Response<WishlistCheckResult>, List<ProductsItem>>() {
                    @Override
                    public List<ProductsItem> call(List<ProductsItem> productsItems, Response<WishlistCheckResult> wishlistCheckResultResponse) {
                        enrichWithWishlistData(productsItems, wishlistCheckResultResponse);
                        return productsItems;
                    }
                });
            }
        });
    }



    Observable<Response<WishlistCheckResult>>getWishList(String userId, List<String> productIds) {
        return service.checkWishlist(userId, TextUtils.join(",", productIds))
                .onErrorReturn(new Func1<Throwable, Response<WishlistCheckResult>>() {
                    @Override
                    public Response<WishlistCheckResult> call(Throwable throwable) {
                        WishlistCheckResult wishlistCheckResult = new WishlistCheckResult();
                        WishlistCheckResult.CheckResultIds ids = new WishlistCheckResult.CheckResultIds();
                        wishlistCheckResult.setCheckResultIds(ids);
                        return Response.success(wishlistCheckResult);
                    }
                });
    }

    private List<String> generateProductIdList(List<ProductsItem> producdsItemList) {
        List<String> productIdList = new ArrayList<>();
        for (ProductsItem productsItem : producdsItemList) {
            productIdList.add(productsItem.getId()+"");
        }
        return productIdList;
    }

    private void enrichWithWishlistData(List<ProductsItem> productsItems,
                                        Response<WishlistCheckResult> wishlistCheckResultResponse) {

        List<String> wishlistedIdList = wishlistCheckResultResponse.body().getCheckResultIds().getIds();

        for (ProductsItem productsItem : productsItems) {
            productsItem.setWishListed(wishlistedIdList.contains(String.valueOf(productsItem.getId())));
        }
    }
}
