package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by stevenfredian on 5/26/17.
 */

public interface FavoriteShopRepository {

    Observable<String> doFavoriteShop(RequestParams requestParams);
}
