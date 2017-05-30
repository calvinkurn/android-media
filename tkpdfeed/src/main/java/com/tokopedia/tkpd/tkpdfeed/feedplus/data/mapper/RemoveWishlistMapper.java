package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.wishlist.RemoveWishlistDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 5/30/17.
 */

public class RemoveWishlistMapper implements Func1<Response<TkpdResponse>, RemoveWishlistDomain> {
    @Override
    public RemoveWishlistDomain call(Response<TkpdResponse> responseResponse) {
        return null;
    }
}
