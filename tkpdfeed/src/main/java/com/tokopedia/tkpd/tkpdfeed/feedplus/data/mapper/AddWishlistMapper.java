package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.pojo.WishlistData;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.wishlist.AddWishlistDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 5/30/17.
 */

public class AddWishlistMapper implements Func1<Response<TkpdResponse>, AddWishlistDomain> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";

    @Override
    public AddWishlistDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private AddWishlistDomain mappingResponse(Response<TkpdResponse> response) {
        AddWishlistDomain model = new AddWishlistDomain();

        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                WishlistData data = response.body().convertDataObj(WishlistData.class);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(DEFAULT_ERROR);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
