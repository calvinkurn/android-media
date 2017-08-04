package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

/**
 * @author by nisie on 7/4/17.
 */

public interface WishlistListener {

    void onErrorAddWishList(String errorMessage, int adapterPosition);

    void onSuccessAddWishlist(int productId);

    void onErrorRemoveWishlist(String errorMessage, int productId);

    void onSuccessRemoveWishlist(int productId);

    String getString(int resId);
}
