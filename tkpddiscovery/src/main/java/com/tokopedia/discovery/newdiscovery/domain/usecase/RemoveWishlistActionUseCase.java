package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber.RemoveWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.wishlist.model.RemoveWishListResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class RemoveWishlistActionUseCase {

    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_PRODUCT_ID = "productID";
    private static final String OPERATION_NAME = "removeWishlist";
    private Context context;

    public RemoveWishlistActionUseCase(Context context) {
        this.context = context;
    }

    public void createObservable(String productId, String userId, WishlistActionListener wishlistActionListener) {

        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_PRODUCT_ID, Integer.parseInt(productId));
        variables.put(PARAM_USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_remove_wishlist),
                RemoveWishListResponse.class,
                variables, OPERATION_NAME);

        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(new RemoveWishlistActionSubscriber(wishlistActionListener, productId));

    }
}
