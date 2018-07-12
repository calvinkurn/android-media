//package com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber;
//
//import com.tokopedia.core.network.retrofit.response.ErrorHandler;
//import com.tokopedia.discovery.R;
//import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
//import com.tokopedia.discovery.newdiscovery.wishlist.model.AddWishListResponse;
//import com.tokopedia.graphql.data.model.GraphqlResponse;
//
//import rx.Subscriber;
//
///**
// * Created by henrypriyono on 10/19/17.
// */
//
//public class AddWishlistActionSubscriber extends Subscriber<GraphqlResponse> {
//    private final WishlistActionListener viewListener;
//    private String productId;
//
//    public AddWishlistActionSubscriber(WishlistActionListener viewListener, String productId) {
//        this.viewListener = viewListener;
//        this.productId = productId;
//    }
//
//    @Override
//    public void onCompleted() {
//
//    }
//
//    @Override
//    public void onError(Throwable e) {
//        viewListener.onErrorAddWishList(
//                ErrorHandler.getErrorMessage(e), productId);
//    }
//
//    @Override
//    public void onNext(GraphqlResponse graphqlResponse) {
//
//        if (graphqlResponse != null) {
//            AddWishListResponse addWishListResponse = graphqlResponse.getData(AddWishListResponse.class);
//            if (addWishListResponse.getWishlist_add().getSuccess())
//                viewListener.onSuccessAddWishlist(productId);
//            else
//                viewListener.onErrorAddWishList(
//                        viewListener.getString(R.string.msg_error_add_wishlist),
//                        productId);
//        } else {
//            viewListener.onErrorAddWishList(
//                    viewListener.getString(R.string.default_request_error_unknown),
//                    productId);
//        }
//    }
//}