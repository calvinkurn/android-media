package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.listener.WishlistActionListener;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoAuthApi;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.subscriber.AddWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.data.mapper.AddWishlistActionMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;
import com.tokopedia.discovery.newdiscovery.wishlist.model.AddWishListResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class AddWishlistActionUseCase{
    private static final String PARAM_USER_ID = "userID";
    private static final String PARAM_PRODUCT_ID = "productID";
    private final Context context;

    private AddWishlistActionMapper mapper;

    public AddWishlistActionUseCase(/*ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    MojitoAuthApi service,
                                    AddWishlistActionMapper mapper*/Context context) {
//        super(threadExecutor, postExecutionThread);
//        this.service = service;
        this.context = context;
        this.mapper = mapper;
    }

    public static RequestParams generateParam(String productId, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_PRODUCT_ID, productId);
        return params;
    }


    public void createObservable(String productId, String userId, WishlistActionListener wishlistActionListener, int adapterPosition) {

        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_PRODUCT_ID, Integer.parseInt(productId));
        variables.put(PARAM_USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.query_add_wishlist),
                AddWishListResponse.class,
                variables, "addWishlist");

        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(new AddWishlistActionSubscriber(wishlistActionListener, adapterPosition));

        /*return service
                .addWishlist(
                        requestParams.getString(PARAM_PRODUCT_ID, ""),
                        requestParams.getString(PARAM_USER_ID, ""))
                .map(mapper);*/
    }

}
