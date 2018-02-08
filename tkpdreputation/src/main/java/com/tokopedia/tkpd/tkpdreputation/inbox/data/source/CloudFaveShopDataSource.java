package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.FaveShopMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;

import rx.Observable;

/**
 * @author by nisie on 9/26/17.
 */

public class CloudFaveShopDataSource {
    private final FaveShopActService faveShopActService;
    private final FaveShopMapper faveShopMapper;

    public CloudFaveShopDataSource(FaveShopActService faveShopActService, FaveShopMapper
            faveShopMapper) {
        this.faveShopActService = faveShopActService;
        this.faveShopMapper = faveShopMapper;
    }


    public Observable<FavoriteShopDomain> favoriteShop(RequestParams requestParams) {
        return faveShopActService.getApi()
                .faveShop(AuthUtil.generateParams(MainApplication.getAppContext(), requestParams
                        .getParamsAllValueInString()))
                .map(faveShopMapper);
    }
}
