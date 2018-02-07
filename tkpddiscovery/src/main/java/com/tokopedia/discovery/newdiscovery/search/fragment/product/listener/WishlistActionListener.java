package com.tokopedia.discovery.newdiscovery.search.fragment.product.listener;

/**
 * Created by henrypriyono on 10/19/17.
 */

public interface WishlistActionListener {
    void onErrorAddWishList(String errorMessage, int adapterPosition);

    void onSuccessAddWishlist(int adapterPosition);

    void onErrorRemoveWishlist(String errorMessage, int adapterPosition);

    void onSuccessRemoveWishlist(int adapterPosition);

    String getString(int resId);
}
