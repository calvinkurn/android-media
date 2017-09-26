package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.apiservices.tome.FavoriteCheckResult;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ShopFavoritedDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/26/17.
 */

public class ShopFavoritedMapper implements Func1<Response<FavoriteCheckResult>, ShopFavoritedDomain> {

    @Override
    public ShopFavoritedDomain call(Response<FavoriteCheckResult> response) {
        if (response.isSuccessful()) {
            if (response.body().getShopIds() != null) {
                return mappingToDomain(!response.body().getShopIds().isEmpty());
            } else {
                throw new ErrorMessageException(MainApplication.getAppContext().getString
                        (R.string.default_request_error_unknown));
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

    private ShopFavoritedDomain mappingToDomain(boolean isFavorited) {
        return new ShopFavoritedDomain(isFavorited);
    }
}
