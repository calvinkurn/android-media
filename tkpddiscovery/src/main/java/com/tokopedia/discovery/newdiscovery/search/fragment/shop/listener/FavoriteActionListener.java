package com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener;

/**
 * Created by henrypriyono on 10/23/17.
 */

public interface FavoriteActionListener {
    void onErrorToggleFavorite(String errorMessage, int adapterPosition);

    void onSuccessToggleFavorite(int adapterPosition, boolean targetFavoritedStatus);

    String getString(int resId);
}
