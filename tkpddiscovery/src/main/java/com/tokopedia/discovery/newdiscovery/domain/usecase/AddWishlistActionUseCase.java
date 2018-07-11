package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber.AddWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.wishlist.model.AddWishListResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class AddWishlistActionUseCase {
    private final String PARAM_USER_ID = "userID";
    private final String PARAM_PRODUCT_ID = "productID";
    private final String OPERATION_NAME = "addWishlist";
    private final Context context;
    private GraphqlUseCase graphqlUseCase;

    public AddWishlistActionUseCase(Context context) {
        this.context = context;
    }

    public void createObservable(String productId, String userId, WishlistActionListener wishlistActionListener) {

        graphqlUseCase = new GraphqlUseCase();

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_PRODUCT_ID, Integer.parseInt(productId));
        variables.put(PARAM_USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.query_add_wishlist),
                AddWishListResponse.class,
                variables, OPERATION_NAME);

        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(new AddWishlistActionSubscriber(wishlistActionListener, productId));

    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
