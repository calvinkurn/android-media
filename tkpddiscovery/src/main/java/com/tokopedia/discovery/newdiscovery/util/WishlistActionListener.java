package com.tokopedia.discovery.newdiscovery.util;

/**
 * Created by hangnadi on 10/24/17.
 */

public interface WishlistActionListener {

    void onErrorAddWishList(String errorMessage, String productID);

    void onSuccessAddWishlist(String productID);

    void onErrorRemoveWishlist(String errorMessage, String productID);

    void onSuccessRemoveWishlist(String productID);
}
